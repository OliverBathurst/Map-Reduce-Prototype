import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

class OutputWriter {
    private final Logger logger = new Logger();
    private BufferedWriter bw;
    private String filepath;
    private Context c;

    OutputWriter(){}

    void setFilepath(String path){
        this.filepath = path;
    }

    void setContext(Context context){
        this.c = context;
    }
    void prepare(){
        try {
            File file = new File(filepath);
            if(file.exists()){
                logger.log("Deleting existing file...");
                if(!file.delete()){
                    logger.logCritical("Error deleting existing file");
                }
            }
            bw = new BufferedWriter(new FileWriter(new File(filepath)));
        }catch(Exception e){
            logger.logCritical("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
    void closeOutput(){
        try {
            bw.close();
        }catch(Exception e){
            logger.log("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
    void write(){
        try {
            for (Pair<Object, Object> objectEntry : c.getMap()) {
                bw.write("Key: " + String.valueOf(objectEntry.getKey()) + " Value: " + String.valueOf(objectEntry.getValue()));
                bw.newLine();
            }
            bw.flush();
        }catch(Exception e){
            logger.logCritical("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
}