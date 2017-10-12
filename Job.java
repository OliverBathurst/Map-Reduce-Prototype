import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Scanner;

class Job extends Mapper{
    private Config config;
    private ArrayList<String> readRow;

    Job(){}

    void setJobConfig(Config conf){
        config = conf;
    }

    private void read(){
        try {
            readRow = new ArrayList<>();
            Scanner scanner = new Scanner(new File(config.getInputPath()));
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                readRow.add(line);
                System.out.println("Read: " + line);
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
                    String regex = (config.getRegex() != null ? config.getRegex() : ","); //default to comma if no regex provided
                    Constructor<?> cons = config.getMapper().getConstructor(String[].class);//(Class)
                    for (String s : readRow) { //for each row/string in the list
                        cons.newInstance(new Object[]{s.split(regex)});//split using regex to get string array and invoke the map method with that parameter
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
        read();
        map();
        //reduce();
        //output();
    }
    @SuppressWarnings("unchecked")
    private void reduce(){
        try{
            if(config.getReducer() != null){
                Constructor<?> cons = config.getReducer().getConstructor(String[].class);
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
            System.out.println("Completed Job: " + config.getJobName() + " To: " + config.getOutputPath());
        }catch(Exception e){
            System.out.println("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
}