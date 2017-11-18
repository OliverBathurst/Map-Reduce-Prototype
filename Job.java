import javafx.util.Pair;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Job {
    private final SimpleDateFormat LOG_TIME = new SimpleDateFormat("HH:mm:ss");
    private final ArrayList<Mapper> mappers = new ArrayList<>();
    private final ArrayList<Reducer> reducers = new ArrayList<>();
    private final ArrayList<Parser> parsers = new ArrayList<>();
    private final int threadCount = Runtime.getRuntime().availableProcessors();
    private final Context finalContext = new Context();
    private final Config config;
    private ExecutorService service = Executors.newFixedThreadPool(threadCount);

    Job(Config conf){
        this.config = conf;
    }

    /**
     * Takes the input path from the config and runs the parser, with a chunk size (defaults to 128)
     */
    private void parse(){
        System.out.println(getTime() + " Running Parser...");
        for(String str: config.getInputPaths()) { //get input files
            parsers.add(new Parser(str, config.getChunkSize()));
        }
        for(Parser p: parsers){
            p.run(); //run the parsers
        }
    }
    @SuppressWarnings("unchecked")
    private void assignChunksToMappers(){
        try {
            if (config.getMapper() != null) {
                Constructor<?> cons = config.getMapper().getConstructor(String.class, Context.class);
                System.out.println(getTime() + " Found map method...");
                for(Parser p: parsers) {
                    for (ArrayList<String> chunk : p.returnMap()) {
                        mappers.add(new Mapper(chunk, new Context(), cons)); //give each chunk to a different mapper with a new context
                    }
                }
                System.out.println(getTime() + " Mappers: " + mappers.size());
            } else {
                System.out.println(getTime() + " Mapper method 'mapper' not defined\n" + "use config.setMapper(class);");//no map method found
            }
        }catch(Exception e) {
            System.out.println(getTime() + " Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }
    /**
     * runs each map in order
     */
    private void runMappers(){
        if(config.getMultiThreaded()) {
            System.out.println(getTime() + " Thread count: " + threadCount);
            for (Mapper map : mappers) {
                service.execute(map::run);
            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            for(Mapper map: mappers){
                map.run();
            }
        }
    }
    /**
     * shuffle the mappers around before reducing them
     */
    private void shuffle(){
        if(config.getShuffle()) {
            Collections.shuffle(mappers);
        }
    }

    /**
     * creates a new reducer for each mapper (1-1 mapping) and adds each to an array of reducers
     */
    private void assignMappersToReducers(){
        for(Mapper map: mappers){
            reducers.add(new Reducer(map.returnMap().getMap()));
        }
    }
    /**
     * runs each reducer in order
     */
    private void runReducers(){
        if(config.getMultiThreaded()) {
            if(service.isShutdown()){
                service = Executors.newFixedThreadPool(threadCount);
            }
            System.out.println(getTime() + " Thread count: " + threadCount);
            for (Reducer r : reducers) {
                service.execute(r::run);
            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            for(Reducer r: reducers){
                r.run();
            }
        }
    }
    @SuppressWarnings({"unchecked", "unused"})
    private void merge(){
        try{
            if (config.getReducer() != null) {
                Constructor cons = config.getReducer().getConstructor(Object.class, Iterable.class, Context.class);
                for(Pair<Object, ArrayList<Object>> toReduce : new Merger(reducers).returnFinalPairs()){ //merge the reducers into a single key/value(list) and iterate over them
                    cons.newInstance(toReduce.getKey(), toReduce.getValue(), finalContext);
                }
            } else {
                System.out.println(getTime() + " Reducer method 'reduce' not defined\n" +
                        "use config.setReducer(class);");//no reduce method found
            }
        }catch(Exception e){
            System.out.println(getTime() + " Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }
    /**
     * This is the output, writes every key/value pair to file, this is the final step
     */
    @SuppressWarnings("unchecked")
    private void output(){
        try {
            System.out.println(getTime() + " Writing to file...");
            FileWriter fw = new FileWriter(new File(config.getOutputPath())); //get the stored output path
            System.out.println(getTime() + " Reduced set size: " + finalContext.getMap().size());
            for (Pair<Object, Object> objectEntry : finalContext.getMap()) {
                fw.write("Key: " + objectEntry.getKey() + " Value: " + objectEntry.getValue() + "\n");
            }
            fw.flush();
            fw.close();
        }catch(Exception e){
            System.out.println(getTime() + " Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
    /**
     * This is the driver, performs all actions in order
     */
    void runJob(){
        long now = System.currentTimeMillis();
        parse(); //parse input data into chunks
        assignChunksToMappers(); //push everything into mapper array
        runMappers(); //run all mappers in mapper array
        shuffle();//shuffle mappers around, will produce different ordering
        assignMappersToReducers();
        runReducers();
        merge();
        output();
        System.out.println(getTime() + " Completed Job: " + "'" + config.getJobName() + "'" + " to "
                + config.getOutputPath() + " in " + (System.currentTimeMillis() - now)
                + "ms");
    }
    /**
     * Get current time for logging
     */
    private String getTime(){
        return LOG_TIME.format(new Date());
    }
}