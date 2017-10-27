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
    private final ArrayList<ArrayList<Pair<Object,Object>>> mapperNodes = new ArrayList<>(); //stores a list of array lists, these are the mapper instances of size 128 each
    private final SimpleDateFormat LOG_TIME = new SimpleDateFormat("HH:mm:ss");
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

                    int a = 0;
                    for(ArrayList<String> b: parse.returnMap()){
                        a++;
                    }
                    System.out.println(getTime() + " Chunks: " + a);

                    for(ArrayList<String> chunk : parse.returnMap()) {
                        for (String str : chunk) { //for each row/string in the list
                            cons.newInstance(str, config.getMapperContext());//Invoke the map method with each string (line) and the context
                        }
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
        Collections.shuffle(mapperNodes);
    } //shuffle before being sent to reducer(s)

    @SuppressWarnings({"unchecked", "unused"})
    private void partitioner(){
        int size = config.getMapperContext().getMap().size();
        int block_size = config.getBlockSize();
        for (int i = 0 ; i < size; i += block_size) {
            mapperNodes.add(new ArrayList<>(config.getMapperContext().getMap().subList(i , i + block_size >= size ? size : i + block_size)));
        }
    }
    @SuppressWarnings({"unchecked", "unused"})
    private void reduce(){
        try{
            if(config.getReducerContext() != null) {
                if (config.getReducer() != null) {
                    System.out.println(getTime() + " Running Reducer...");
                    Constructor cons = config.getReducer().getConstructor(Object.class, Iterable.class, Context.class);
                    System.out.println(getTime() + " Found reduce method...");
                    System.out.println(getTime() + " Map count: " + mapperNodes.size());
                    System.out.println(getTime() + " Items: " + config.getMapperContext().getMap().size());

                    ArrayList<Pair<Object, ArrayList<Object>>> pairs = new ArrayList<>(); //stores for "key, Iterable<values>" in reducer

                    for(ArrayList<Pair<Object,Object>> mapperSet: mapperNodes){ //loop through mapper instances
                        for (Pair<Object, Object> objectEntry : mapperSet) { //loop through entries on the mapper node
                            boolean contains = false;
                            for(Pair<Object, ArrayList<Object>> pair : pairs){
                                if(pair.getKey().equals(objectEntry.getKey())){
                                    pair.getValue().add(objectEntry.getValue());//if it already exists add it's value to its list
                                    contains = true; //if it contains it
                                    break;
                                }
                            }
                            if(!contains){
                                System.out.println(getTime() + " Creating new pair....");
                                pairs.add(new Pair(objectEntry.getKey(), new ArrayList(Collections.singletonList(objectEntry.getValue()))));
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
            for (Pair<Object, Object> objectEntry : (ArrayList<Pair<Object, Object>>) config.getReducerContext().getMap()) {
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
        partitioner();
        shuffle();
        reduce();
        output();
        System.out.println(getTime() + " Completed Job: " + "'" + config.getJobName() + "'" + " to "
                + config.getOutputPath() + " in " + (System.currentTimeMillis() - now)
                + "ms");
    }
    private String getTime(){
        return LOG_TIME.format(new Date());
    }
}