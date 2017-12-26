import javafx.util.Pair;
import java.io.*;
import java.util.ArrayList;

class OutputWriter {
    private final ArrayList<Context> contexts = new ArrayList<>();
    private final Logger logger = new Logger();
    private String filepath;

    OutputWriter(){}

    void setFilepath(String path){
        this.filepath = path;
    }

    void addContext(Context context){
        contexts.add(context);
    }

    void write(){
        try {
            File file = new File(filepath);
            if(file.exists()){
                logger.log("Deleting existing file...");
                if(!file.delete()){
                    logger.logCritical("Error deleting existing file");
                }
            }
            if(!file.exists()) {
                file = new File(filepath);
                if(!file.createNewFile()){
                    logger.logCritical("Error creating file");
                }
            }
            FileWriter fw = new FileWriter(file);
            for(Context c: contexts) {
                for (Pair<Object, Object> objectEntry : c.getMap()) {
                    fw.write("Key: " + String.valueOf(objectEntry.getKey()) + " Value: " + String.valueOf(objectEntry.getValue()) + '\n');
                    fw.flush();
                }
            }
            fw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}