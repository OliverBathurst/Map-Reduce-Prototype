import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

class Job {
    private Config config;
    private ArrayList<String> readRow;

    Job(Config conf){
        config = conf;
    }

    private void readIn(){
        try {
            Scanner scanner = new Scanner(new File(config.getInputPath()));
            readRow = new ArrayList<>();
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                readRow.add(line);
                System.out.println("Read: " + line);
            }
            scanner.close();
        }catch(Exception e){
            System.out.println("EXCEPTION: " + e.getMessage());
        }
    }
    void parse(){
        try {
            if (config.getMapper() != null) {
                String regex = (config.getRegex() != null ? config.getRegex() : ","); //default to comma if no regex provided
                Class<?> mapClass = config.getMapper(); //get class set by the config
                Method method = mapClass.getMethod("map"); //get the required map method
                for (String s : readRow) { //for each row/string in the list
                    method.invoke(s.split(regex));//split using regex to get string array and invoke the map method with that parameter
                }
            }else{
                System.out.println("Map method 'map' not defined");
            }
        }catch(Exception e){
            System.out.println("Exception: " + e.getCause() + "\n"
                    + e.getMessage());
        }
    }
    void runJob(){
        readIn();
        //parse();

    }
    void output(){
        try {
            FileWriter fw = new FileWriter(new File(config.getOutputPath()));

        }catch(Exception e){
            System.out.println("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
}
