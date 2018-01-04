import javafx.util.Pair;
import java.util.List;

/**
 * The reducer calls reduce() for a single (key (list)values) pair
 */
class Reducer {
    private final Pair<Object, List<Object>> keyListValue;//key and list of values for that key
    private final Context context = new Context();//context to write final key-value pairs to
    private final Logger logger = new Logger();
    private final Class reducerClass; //the reducer class to call (set by 'setReducerClass()')

    /**
     * The reducer is initialised by the pair and the reducer class to call
     */
    Reducer(Pair<Object, List<Object>> pair, Class reducerClass){
        this.keyListValue = pair;
        this.reducerClass = reducerClass;
    }

    /**
     * This method creates a new instance of the reduce() class (and subsequently invokes the constructor)
     * with the key, list of values for that key, and a context to write to.
     */
    @SuppressWarnings("unchecked")
    void reduce(){
        if (reducerClass != null) {//if the class has been set
            try{
                reducerClass.getDeclaredConstructor(Object.class, Iterable.class, Context.class)
                        .newInstance(keyListValue.getKey(), keyListValue.getValue(), context);//get the reduce() constructor and create new instance of class with the required parameters
            }catch(Exception e){
                logger.logCritical("Error: " + e.getMessage() + " cause: " + e.getCause());
            }
        } else {
            logger.logCritical("Reducer method 'reduce' not defined\n" +
                    "use config.setReducer(class);"); //print informative message
        }
    }

    /**
     * Return the context from the reducer
     */
    Context getFinalKeyPairs(){
        return context;
    }
}