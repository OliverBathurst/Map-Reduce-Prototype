import javafx.util.Pair;
import java.io.File;
import java.io.FileWriter;

class OutputWriter {
    private final Logger logger = new Logger();
    private String filepath;
    private Context c;

    OutputWriter(){}

    void setFilepath(String path){
        this.filepath = path;
    }
    void setContext(Context context){
        this.c = context;
    }

    void write(){
        try {
            logger.log("Writing to file...");
            FileWriter fw = new FileWriter(new File(filepath)); //get the stored output path
            logger.log("Reduced set size: " + c.getMap().size());
            for (Pair<Object, Object> objectEntry : c.getMap()) {
                fw.write("Key: " + objectEntry.getKey() + " Value: " + objectEntry.getValue() + "\n");
            }
            fw.flush();
            fw.close();
        }catch(Exception e){
            logger.logCritical("Cause: " + e.getCause() + " Message: " + e.getMessage());
        }
    }
}