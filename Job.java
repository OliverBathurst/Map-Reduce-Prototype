import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Scanner;

class Job {
    private Config config;
    private ArrayList<String> readRow;

    Job(Config conf){
        config = conf;
    }

    private void read(){
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
            //System.out.println("EXCEPTION: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @SuppressWarnings("all")
    private void parse(){
        try {
            if (config.getMapper() != null) {
                String regex = (config.getRegex() != null ? config.getRegex() : ","); //default to comma if no regex provided
                Constructor<?> cons = ((Class) config.getMapper()).getConstructor(String[].class);
                for (String s : readRow) { //for each row/string in the list
                    cons.newInstance(new Object[]{s.split(regex)});//split using regex to get string array and invoke the map method with that parameter
                }
            }else{
                System.out.println("Map method 'map' not defined");//no map method found
            }
        }catch(Exception e){
            e.printStackTrace();
            //System.out.println("Exception: " + e.getCause() + "\n"
                   // + e.getMessage());
        }
    }
    void runJob(){
        read();
        parse();
    }
    void output(){
        try {
            FileWriter fw = new FileWriter(new File(config.getOutputPath()));

        }catch(Exception e){
            System.out.println("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
}
