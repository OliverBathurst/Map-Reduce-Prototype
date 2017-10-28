import javafx.util.Pair;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.*;

class Job {
    private final SimpleDateFormat LOG_TIME = new SimpleDateFormat("HH:mm:ss");
    private final ArrayList<Mapper> mappers = new ArrayList<>();
    private final ArrayList<Reducer> reducers = new ArrayList<>();
    private final Context finalContext = new Context();
    private final Config config;
    private Parser parse;

    Job(Config conf){
        this.config = conf;
    }

    /**
     * Takes the input path from the config and runs the parser, with a chunk size (defaults to 128)
     */
    private void parse(){
        System.out.println(getTime() + " Running Parser...");
        parse = new Parser(config.getInputPath(), config.getChunkSize());
        parse.run();
    }
    @SuppressWarnings("unchecked")
    private void map(){
        try {
            if (config.getMapper() != null) {
                System.out.println(getTime() + " Running Mapper...");
                Constructor<?> cons = config.getMapper().getConstructor(String.class, Context.class);
                System.out.println(getTime() + " Found map method...");
                System.out.println(getTime() + " Chunks: " + parse.returnMap().size());

                for(ArrayList<String> chunk : parse.returnMap()) {
                    mappers.add(new Mapper(chunk, new Context(), cons));
                }
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
    private void runMap(){
        for(Mapper map: mappers){
            map.run();
        }
    }
    /**
     * shuffle the mappers around before reducing them
     */
    private void shuffle(){
        Collections.shuffle(mappers);
    }

    /**
     * creates a new reducer for each mapper (1-1 mapping) and adds each to an array of reducers
     */
    private void reduce(){
        for(Mapper map: mappers){
            reducers.add(new Reducer(map));
        }
    }
    /**
     * runs each reducer in order
     */
    private void runReduce(){
        for(Reducer r: reducers){
            r.run();
        }
    }
    @SuppressWarnings({"unchecked", "unused"})
    private void merge(){
        try{
            if (config.getReducer() != null) {
                Constructor cons = config.getReducer().getConstructor(Object.class, Iterable.class, Context.class);
                for(Pair<Object, ArrayList<Object>> toReduce : new Merger(reducers).returnFinalPairs()){ //finally send the key and iterable values to reducer
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
        parse();

        map();
        runMap(); //use thread here maybe

        shuffle(); //shuffle mappers around

        reduce();
        runReduce(); //use thread here maybe

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