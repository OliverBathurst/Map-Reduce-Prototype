import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Oliver Bathurst on 13/10/2017.
 * All Rights Reserved
 * Unauthorized copying of this file via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Job {
    private final ArrayList<ArrayList<Pair<Object, ArrayList<Object>>>> reducedMappers = new ArrayList<>();
    private final SimpleDateFormat LOG_TIME = new SimpleDateFormat("HH:mm:ss");
    private final ArrayList<Context> mapperInstances = new ArrayList<>();
    private final Context finalContext = new Context();
    private final Config config;
    private Parser parse;

    Job(Config conf){
        this.config = conf;
    }

    /**
     * Takes the input path from the config and runs the parser
     */
    private void parse(){
        System.out.println(getTime() + " Running Parser...");
        parse = new Parser(config.getInputPath(), config.getChunkSize());
        parse.run();
    }

    /**
     * 1. Creates a context for each chunk made by the parser
     * 2. mapperInstances stores the contexts the user writes to (1 chunk = 1 context)
     * 3. For each line in each chunk, pass the line and the context for that chunk
     *      to the mapper method in main
     * 4. mapperInstances is now full of contexts that contain key/value pairs, mapperInstances can
     *      be thought of as a list of mappers
     */
    @SuppressWarnings("unchecked")
    private void map(){
        try {
            if (config.getMapper() != null) {
                System.out.println(getTime() + " Running Mapper...");
                Constructor<?> cons = config.getMapper().getConstructor(String.class, Context.class);
                System.out.println(getTime() + " Found mapper method...");

                for(int a = 0; a < parse.returnMap().size(); a++){
                    System.out.println(getTime()+ " Creating new Mapper for chunk ID: " + a);
                    mapperInstances.add(new Context()); //add a mapper instance for each chunk (1-1 mapping)
                }
                System.out.println(getTime() + " Chunks: " + mapperInstances.size());

                int contextNum = 0; //one context to one chunk
                for(ArrayList<String> chunk : parse.returnMap()) {
                    for (String aChunk : chunk) { //for each row/string in the list
                        cons.newInstance(aChunk, mapperInstances.get(contextNum));//Invoke the map method with each string (line) and the context
                    }
                    contextNum++;
                }
            } else {
                System.out.println(getTime() + " Mapper method 'mapper' not defined\n" +
                        "use config.setMapper(class);");//no map method found
            }
        }catch(Exception e) {
            System.out.println(getTime() + " Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }

    /**
     * shuffle the mappers around before reducing them
     */
    private void shuffle(){
        Collections.shuffle(mapperInstances);
    } //shuffle before being sent to reducer(s)

    /**
     * 1. Create a pairs array that stores the key and list(values) of that key
     * 2. reduce method loops through the mappers
     * 3. For each pair in the mapper check if the key already exists in the pairs array
     * 4. If it does, add the key's accompanying value to the existing list of values for that key
     * 5. If it doesn't, create a new key, add the initial value in and add it to the pairs array
     * 6. At the end of searching each context, add the reduced pairs for that context to reducedMappers
     *      and clear the pairs array, ready to reduce the next mapper
     */
    @SuppressWarnings({"unchecked", "unused"})
    private void reduce(){
        try {
            ArrayList<Pair<Object, ArrayList<Object>>> pairs = new ArrayList<>(); //stores for "key, Iterable<values>" in reducer

            for(Context c: mapperInstances){
                for(Pair<Object, Object> pairsFromContext : c.getMap()){
                    boolean contains = false;
                    for (Pair<Object, ArrayList<Object>> pair : pairs) {
                        if (pair.getKey().equals(pairsFromContext.getKey())) {
                            pair.getValue().add(pairsFromContext.getValue());//if it already exists add it's value to its list
                            contains = true; //if it contains it
                            break;
                        }
                    }
                    if (!contains) {
                        pairs.add(new Pair(pairsFromContext.getKey(), new ArrayList(Collections.singletonList(pairsFromContext.getValue()))));
                    }
                }
                reducedMappers.add(new ArrayList<>(pairs));
                pairs.clear();
            }
        }catch(Exception e){
            System.out.println(getTime() + " Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }

    /**
     * 1. Create a pairs array that stores the key and list(values) of that key
     * 2. Looped through the reduced mappers
     * 3. For each pair in the reduced mapper check if the key already exists in the pairs array
     * 4. If it does, add the key's accompanying value to the existing list of values for that key
     * 5. If it doesn't, create a new key, add the initial value in and add it to the pairs array
     * 6. At the end we now have our final reduced list of (key, list(values))
     *      to send to Main's reduce method
     * 7. for each (key, list(values)) in our pairs array, send the key, value (which is an iterable array list),
     *      and the final context we are going to write to.
     */
    @SuppressWarnings({"unchecked", "unused"})
    private void merge(){
        try{
            if (config.getReducer() != null) {
                Constructor cons = config.getReducer().getConstructor(Object.class, Iterable.class, Context.class);

                ArrayList<Pair<Object, ArrayList<Object>>> pairs = new ArrayList<>(); //stores for "key, Iterable<values>" in reducer

                for(ArrayList<Pair<Object, ArrayList<Object>>> reduced: reducedMappers){ //loop through mapper instances
                    for (Pair<Object, ArrayList<Object>> objectEntry : reduced) { //loop through entries on the mapper node
                        boolean contains = false;
                        for(Pair<Object, ArrayList<Object>> pair : pairs){
                            if(pair.getKey().equals(objectEntry.getKey())){
                                pair.getValue().add(objectEntry.getValue());//if it already exists add it's value to its list
                                contains = true; //if it contains it
                                break;
                            }
                        }
                        if(!contains){
                            pairs.add(new Pair(objectEntry.getKey(), new ArrayList(objectEntry.getValue())));
                        }
                    }
                }
                for(Pair<Object, ArrayList<Object>> toReduce : pairs){ //finally send the key and iterable values to reducer
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
        shuffle();
        reduce();
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