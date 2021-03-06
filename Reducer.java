import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * The reducer calls reduce() for a single (key (list)values) pair
 */
class Reducer {
    private final List<Pair<Object, List<Object>>> keyListValues = new ArrayList<>();//key and list of values for that key
    private final Context context = new Context();//context to write final key-value pairs to
    private final Logger logger = new Logger();
    private final Class reducerClass; //the reducer class to call (set by 'setReducerClass()')

    /**
     * The reducer is initialised by the pair and the reducer class to call
     */
    Reducer(Class reducerClass){
        this.reducerClass = reducerClass;
    }

    void addKeyListValues(Pair<Object, List<Object>> pair){
        this.keyListValues.add(pair);
    }

    /**
     * This method creates a new instance of the reduce() class (and subsequently invokes the constructor)
     * with a key, list of values for that key, and the context to write they key-value pairs to.
     */
    @SuppressWarnings("unchecked")
    void reduce(){
        if (reducerClass != null) {//if the class has been set
            try{
                for(Pair<Object, List<Object>> pair: keyListValues) {
                    reducerClass.getDeclaredConstructor(Object.class, Iterable.class, Context.class)
                            .newInstance(pair.getKey(), pair.getValue(), context);//get the reduce() constructor and create new instance of class with the required parameters
                }
            }catch(Exception e){
                logger.logCritical("Error: " + e.getMessage() + " cause: " + e.getCause());//log error and quit
            }
        } else {
            logger.logCritical("Reducer method 'reduce' not defined\n" +
                    "use config.setReducer(class);"); //print informative message, reducer class not set
        }
    }

    /**
     * Return the context from the reducer
     */
    Context getFinalKeyPairs(){
        return context;
    }
}