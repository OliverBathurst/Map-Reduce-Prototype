import java.io.*;
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
            scanner.useDelimiter(",");
            readRow = new ArrayList<>();
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                readRow.add(line);
                System.out.println("Read: " + line);
            }
            scanner.close();
        }catch(Exception e){
            e.printStackTrace();
           // System.out.println("EXCEPTION: " + e.getMessage());
        }
    }
    void parse(){

    }
    void runJob(){
        readIn();
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
