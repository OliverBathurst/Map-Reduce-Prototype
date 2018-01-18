import javafx.util.Pair;
import java.io.*;
import java.util.ArrayList;

/**
 * The OutputWriter class writes all final key-value pairs to file
 */
class OutputWriter {
    private final ArrayList<Context> contexts = new ArrayList<>();//store a list of contexts from the reducers
    private final Logger logger = new Logger();
    private String filepath;//output path

    OutputWriter(){}

    /**
     * Sets output path
     */
    void setFilepath(String path){
        this.filepath = path;
    }

    /**
     * Adds a context to the list
     */
    void addContext(Context context){
        contexts.add(context);
    }

    /**
     * write() iterates over all given contexts, writing the key-value pairs from each to file
     */
    void write(){
        try {
            File file = new File(filepath);//create new file object
            if(file.exists()){//if file exists
                logger.log("Deleting existing file...");
                if(!file.delete()){//if deletion has failed
                    logger.logCritical("Error deleting existing file");//exit
                }
            }
            if(!file.exists()) {//if the file does not exist
                if(!file.createNewFile()){//try creating new file
                    logger.logCritical("Error creating file");//critical error
                }
            }
            FileWriter fw = new FileWriter(file);//create new file writer
            for(Context c: contexts) {//iterate over all contexts
                for (Pair<Object, Object> keyValuePairs : c.getMap()) {//iterate over all key-value pairs within the context
                    fw.write("Key: " + String.valueOf(keyValuePairs.getKey()) + " Value: " + String.valueOf(keyValuePairs.getValue()) + '\n');//write the key and value
                    fw.flush();//flush to file
                }
            }
            fw.close();//close and finish
        }catch(Exception e){
            logger.logCritical("Error while writing to file: " + e.getMessage());
        }
    }
}