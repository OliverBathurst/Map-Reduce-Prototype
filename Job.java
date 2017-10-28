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
    private final Config config;
    private Parser parse;

    Job(Config conf){
        this.config = conf;
    }

    private void parse(){
        System.out.println(getTime() + " Running Parser...");
        parse = new Parser(config.getInputPath(), config.getChunkSize());
        parse.run();
    }
    @SuppressWarnings("unchecked")
    private void map(){
        try {
            if(config.getMapperContext() != null) {
                if (config.getMapper() != null) {
                    System.out.println(getTime() + " Running Mapper...");
                    Constructor<?> cons = config.getMapper().getConstructor(String.class, Context.class);
                    System.out.println(getTime() + " Found mapper method...");

                    for(int a = 0; a < parse.returnMap().size(); a++){
                        System.out.println(getTime()+ " Creating new Context for chunk ID: " + a);
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
            }else{
                System.out.println(getTime() + " Mapper context not defined\n" +
                        "use config.setMapperContext(context);");//no context found
            }
        }catch(Exception e) {
            System.out.println(getTime() + " Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }

    private void shuffle(){
        Collections.shuffle(mapperInstances);
    } //shuffle before being sent to reducer(s)

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
    @SuppressWarnings({"unchecked", "unused"})
    private void merge(){
        try{
            if(config.getReducerContext() != null) {
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
                        cons.newInstance(toReduce.getKey(), toReduce.getValue(), config.getReducerContext());
                    }
                } else {
                    System.out.println(getTime() + " Reducer method 'reduce' not defined\n" +
                            "use config.setReducer(class);");//no reduce method found
                }
            }else{
                System.out.println(getTime() + " Reducer context not defined\n" +
                        "use config.setReducerContext(context);");//no context found
            }
        }catch(Exception e){
            System.out.println(getTime() + " Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }
    @SuppressWarnings("unchecked")
    private void output(){
        try {
            System.out.println(getTime() + " Writing to file...");
            FileWriter fw = new FileWriter(new File(config.getOutputPath()));
            System.out.println(getTime() + " Reduced set size: " + config.getReducerContext().getMap().size());
            for (Pair<Object, Object> objectEntry : config.getReducerContext().getMap()) {
                fw.write("Key: " + objectEntry.getKey() + " Value: " + objectEntry.getValue() + "\n");
            }
            fw.flush();
            fw.close();
        }catch(Exception e){
            System.out.println(getTime() + " Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
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
    private String getTime(){
        return LOG_TIME.format(new Date());
    }
}