import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Job {
    private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final ArrayList<GroupedValuesList> groupedValues = new ArrayList<>();
    private final ArrayList<Mapper> mappers = new ArrayList<>();
    private final ArrayList<Reducer> reducers = new ArrayList<>();
    private final ArrayList<InputReader> inputReaders = new ArrayList<>();
    private final Logger logger = new Logger();
    private final Config config;
    private long startTime;

    Job(Config conf){
        this.config = conf;
    }

    /**
     * Takes the input path from the config and runs the parser, with a chunk size (defaults to 128)
     */
    private void reader(){
        logger.log("Running InputReader...");
        for(String filepath: config.getInputPaths()) { //get input files
            inputReaders.add(new InputReader(filepath, config.getChunkSize()));
        }
        if(config.getMultiThreaded()){
            setupThreadPool();
            for (InputReader p : inputReaders) {
                service.execute(p::run); //run the inputReaders
            }
            shutdownThreadPool();
        }else{
            for (InputReader p : inputReaders) {
                p.run(); //run the inputReaders
            }
        }
    }
    @SuppressWarnings("unchecked")
    private void assignChunksToMappers(){
        if (config.getMapper() != null) {
            try {
                Constructor<?> cons = config.getMapper().getConstructor(String.class, Context.class);
                logger.log("Found map method...");
                for(InputReader inputReader : inputReaders) {
                    for (ArrayList<String> chunk : inputReader.returnChunks()) {
                        mappers.add(new Mapper(chunk, cons)); //give each chunk to a different mapper with a new context
                    }
                }
                logger.log("Mappers: " + mappers.size());
            }catch(Exception e) {
                logger.logCritical("Other error: " + e.getMessage() + " cause: " + e.getCause());
            }
        } else {
            logger.log("Mapper method 'mapper' not defined\n" + "use config.setMapper(class);");
        }
    }
    /**
     * runs each map in order
     */
    private void runMappers(){
        if(config.getMultiThreaded()) {
            setupThreadPool();
            for (Mapper map : mappers) {
                service.execute(map::run);
            }
            shutdownThreadPool();
        }else{
            for(Mapper map: mappers){
                map.run();
            }
        }
    }

    /**
     * Takes the buffer from each Mapper and groups values for the same key.
     */
    private void combine(){
        for (Mapper map : mappers) {
            new Combiner(map.getIntermediateOutput(), groupedValues).combine();//pass intermediate keys and grouped key storage
        }
    }
    /**
     * Shuffling ensures that all of the values associated with a specific key go to the same reducer
     * To do this, each (key, list(values)) pair produced from the combiner phase is given a unique reducer.
     * Each reducer is initialised with a unique (key, list(values)) pair
     * from the grouped values ArrayList, this ArrayList is populated in the Combiner phase,
     * where values are grouped for the same key.
     */
    private void shuffle(){
        for (GroupedValuesList groupedValuesList : groupedValues) {
            reducers.add(new Reducer(groupedValuesList.getPair(), config)); //give grouped intermediate key value pairs to reducer
        }
    }
    /**
     * runs each reducer in order
     */
    private void runReducers(){
        if(config.getMultiThreaded()) {
            setupThreadPool();
            for (Reducer r : reducers) {
                service.execute(r::reduce);
            }
            shutdownThreadPool();
        }else{
            for(Reducer r: reducers){
                r.reduce();
            }
        }
    }
    /**
     * This is the output, writes every key/value pair to file, this is the final step
     */
    @SuppressWarnings("unchecked")
    private void output(){
        OutputWriter out = new OutputWriter();
        out.setFilepath(config.getOutputPath());
        out.prepare();
        for(Reducer r: reducers){
            out.setContext(r.getReducedKeyPairs());
            out.write();
        }
        out.closeOutput();
    }
    /**
     * This is the driver, performs all actions in order
     */
    void runJob(){
        setStartTime(System.currentTimeMillis());

        reader(); //parse input data into chunks
        assignChunksToMappers(); //push everything into mapper array
        runMappers(); //run all mappers in mapper array
        combine();
        shuffle();//assign mappers IO to reducers
        runReducers();
        output();

        logger.log("Completed Job: " + "'" + config.getJobName() + "'" + " to "
                + config.getOutputPath() + " in " + getElapsed()
                + "ms");
    }
    private void setupThreadPool(){
        if(service.isShutdown()){
            service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
    }
    private void shutdownThreadPool() {
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            logger.log(e.getMessage());
        }
    }
    private void setStartTime(long l){
        startTime = l;
    }
    private long getElapsed(){
        return System.currentTimeMillis() - startTime;
    }
}