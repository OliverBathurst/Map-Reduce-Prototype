import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Created by Oliver Bathurst on 13/10/2017.
 * All Rights Reserved
 * Unauthorized copying of this file via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Job {
    private final Config config;
    private Parser parse;

    Job(Config conf){
        this.config = conf;
    }

    private void parse(){
        System.out.println("Running Parser...");
        parse = new Parser(config.getInputPath());
        parse.run();
    }

    @SuppressWarnings("unchecked")
    private void map(){
        try {
            if(config.getMapperContext() != null) {
                if (config.getMapper() != null) {
                    System.out.println("Running Mapper...");
                    Constructor<?> cons = config.getMapper().getConstructor(String.class, Context.class);
                    System.out.println("Running map...");
                    for (String str : parse.returnMap()) { //for each row/string in the list
                        System.out.println("Passing: " + str);
                        cons.newInstance(str, config.getMapperContext());//Invoke the map method with each string (line) and the context
                    }
                } else {
                    System.out.println("Mapper method 'mapper' not defined\n" +
                            "use config.setMapper(class);");//no map method found
                }
            }else{
                System.out.println("Mapper context not defined\n" +
                        "use config.setMapperContext(context);");//no context found
            }
        }catch(Exception e) {
            System.out.println("Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }
    @SuppressWarnings({"unchecked", "unused"})
    private void reduce(){
        try{
            if(config.getReducerContext() != null) {
                if (config.getReducer() != null) {
                    System.out.println("Running Reducer...");
                    Constructor cons = config.getReducer().getConstructor(String.class, Iterable.class, Context.class);
                    System.out.println("Running reduce...");
                    System.out.println("Map size: " + config.getMapperContext().getMap().size());
                    for (Map.Entry<Object, Object> objectEntry : (Iterable<Map.Entry<Object, Object>>) config.getMapperContext().getMap().entrySet()) {
                        System.out.println("Key: " + objectEntry.getKey().toString() + " Value: " + objectEntry.getValue().toString());
                        cons.newInstance(objectEntry.getKey().toString(), config.getMapperContext().getMap().values(), config.getReducerContext());
                    }
                } else {
                    System.out.println("Reducer method 'reduce' not defined\n" +
                            "use config.setReducer(class);");//no reduce method found
                }
            }else{
                System.out.println("Reducer context not defined\n" +
                        "use config.setReducerContext(context);");//no context found
            }
        }catch(Exception e){
            System.out.println("Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }
    @SuppressWarnings("unchecked")
    private void output(){
        try {
            System.out.println("Writing to file...");
            FileWriter fw = new FileWriter(new File(config.getOutputPath()));
            for (Map.Entry<Object, Object> objectEntry : (Iterable<Map.Entry<Object, Object>>) config.getReducerContext().getMap().entrySet()) {
                fw.write("Key: " + objectEntry.getKey() + " Value: " + objectEntry.getValue());
            }
            fw.flush();
            fw.close();
        }catch(Exception e){
            System.out.println("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
    void runJob(){
        long now = System.currentTimeMillis();
        parse();
        map();
        reduce();
        output();
        System.out.println("Completed Job: " + "'" + config.getJobName() + "'" + " to "
                + config.getOutputPath() + " in " + (System.currentTimeMillis() - now)
                + "ms");
    }
}