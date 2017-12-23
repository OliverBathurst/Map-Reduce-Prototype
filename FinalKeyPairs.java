import javafx.util.Pair;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

class FinalKeyPairs {
    private ArrayList<Pair<Object, ArrayList<Object>>> keyListValues;
    private Logger logger = new Logger();
    private Context c = new Context();
    private Config config;

    FinalKeyPairs(Config c, ArrayList<Pair<Object, ArrayList<Object>>> mergedFinal){
        this.config = c;
        this.keyListValues = mergedFinal;
    }
    @SuppressWarnings("unchecked")
    void run(){
        if (config.getReducer() != null) {
            try{
                Constructor cons = config.getReducer().getConstructor(Object.class, Iterable.class, Context.class);
                for(Pair<Object, ArrayList<Object>> toReduce : keyListValues){ //merge the reducers into a single key/value(list) and iterate over them
                    cons.newInstance(toReduce.getKey(), toReduce.getValue(), c);
                }
            }catch(Exception e){
                logger.logCritical("Error: " + e.getMessage() + " cause: " + e.getCause());
            }
        } else {
            logger.log("Reducer method 'reduce' not defined\n" +
                    "use config.setReducer(class);");
        }
    }

    ArrayList<Pair<Object, Object>> getFinalOutput(){
        return c.getMap();
    }
}