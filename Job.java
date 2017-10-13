import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Oliver Bathurst on 13/10/2017.
 * All Rights Reserved
 * Unauthorized copying of this file via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Job {
    private Config config;
    private final ArrayList<String> readRow = new ArrayList<>();

    Job(){}

    void setJobConfig(Config conf){
        config = conf;
    }

    private void read(){
        try {
            Scanner scanner = new Scanner(new File(config.getInputPath()));
            while(scanner.hasNextLine()){
                readRow.add(scanner.nextLine());
            }
            scanner.close();
        }catch(FileNotFoundException e){
            System.out.println("EXCEPTION: File not Found Exception\n"
                    + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    private void map(){
        try {
            if(config.getContext() != null) {
                if (config.getMapper() != null) {
                    //String regex = (config.getRegex() != null ? config.getRegex() : ","); //default to comma if no regex provided
                    Constructor cons = config.getMapper().getConstructor(String.class, Context.class);
                    for (String s : readRow) { //for each row/string in the list
                        cons.newInstance(s, config.getContext());//split using regex to get string array and invoke the map method with that parameter , config.getContext()
                    }
                } else {
                    System.out.println("Mapper method 'mapper' not defined\n" +
                            "use config.setMapper(class);");//no map method found
                }
            }else{
                System.out.println("Context not defined\n" +
                        "use config.setContext(context);");//no context found
            }
        }catch(NoSuchMethodException e){ //catch exception to give feedback
            System.out.println("EXCEPTION: No Such Method Exception\n"
                            + e.getMessage() + "\npublic map(String[] args) constructor not found in map class");
        }catch(Exception e) {
            e.printStackTrace(); //print other errors
        }
    }
    void runJob(){
        long now = System.currentTimeMillis();
        read();
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
            if(config.getReducer() != null){
                Constructor cons = config.getReducer().getConstructor(String[].class);
            }else{
                System.out.println("Reducer method 'reduce' not defined\n" +
                        "use config.setReducer(class);");//no reduce method found
            }
        }catch(NoSuchMethodException e){
            System.out.println("EXCEPTION: No Such Method Exception\n"
                    + e.getMessage() + "\npublic reduce(String[] args) constructor not found in reduce class");
        }
    }
    private void output(){
        try {
            //FileWriter fw = new FileWriter(new File(config.getOutputPath()));
        }catch(Exception e){
            System.out.println("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
}