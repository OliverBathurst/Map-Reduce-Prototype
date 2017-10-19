import java.lang.reflect.Constructor;
import java.util.ArrayList;

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
        parse = new Parser(config.getInputPath());
        parse.run();
    }

    @SuppressWarnings("unchecked")
    private void map(){
        try {
            if(config.getContext() != null) {
                if (config.getMapper() != null) {
                    Constructor<?> cons = config.getMapper().getConstructor(String.class, Context.class);
                    for (String str : parse.returnMap()) { //for each row/string in the list
                        cons.newInstance(str, config.getContext());//Invoke the map method with each string (line) and the context
                    }
                } else {
                    System.out.println("Mapper method 'mapper' not defined\n" +
                            "use config.setMapper(class);");//no map method found
                }
            }else{
                System.out.println("Context not defined\n" +
                        "use config.setContext(context);");//no context found
            }
        }catch(Exception e) {
            System.out.println("Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }
    void runJob(){
        long now = System.currentTimeMillis();
        parse();
        map();
        //reduce();
        //output();
        System.out.println("Completed Job: " + "'" + config.getJobName() + "'" + " to "
                + config.getOutputPath() + " in " + (System.currentTimeMillis() - now)
                + "ms");
    }
    @SuppressWarnings({"unchecked", "unused"})
    private void reduce(){
        try{
            if(config.getContext() != null) {
                if (config.getReducer() != null) {
                    Constructor cons = config.getReducer().getConstructor(String.class, Iterable.class, Context.class);
                    /////TEST
                    cons.newInstance("key", new ArrayList<Integer>(), config.getContext());
                    System.out.println("Success");
                    /////TEST
                } else {
                    System.out.println("Reducer method 'reduce' not defined\n" +
                            "use config.setReducer(class);");//no reduce method found
                }
            }else{
                System.out.println("Context not defined\n" +
                        "use config.setContext(context);");//no context found
            }
        }catch(Exception e){
            System.out.println("Other error: " + e.getMessage() + " cause: " + e.getCause());
        }
    }
    @SuppressWarnings("EmptyTryBlock")
    private void output(){
        try {
            //FileWriter fw = new FileWriter(new File(config.getOutputPath()));
        }catch(Exception e){
            System.out.println("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
}