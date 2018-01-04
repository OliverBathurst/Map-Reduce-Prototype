import javafx.util.Pair;
import java.util.List;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Reducer {
    private final Pair<Object, List<Object>> keyListValue;
    private final Context finalContext = new Context();
    private final Logger logger = new Logger();
    private final Class reducerClass;

    Reducer(Pair<Object, List<Object>> pair, Class reducerClass){
        this.keyListValue = pair;
        this.reducerClass = reducerClass;
    }
    @SuppressWarnings("unchecked")
    void reduce(){
        if (reducerClass != null) {
            try{
                reducerClass.getDeclaredConstructor(Object.class, Iterable.class, Context.class)
                        .newInstance(keyListValue.getKey(), keyListValue.getValue(), finalContext);
            }catch(Exception e){
                logger.logCritical("Error: " + e.getMessage() + " cause: " + e.getCause());
            }
        } else {
            logger.logCritical("Reducer method 'reduce' not defined\n" +
                    "use config.setReducer(class);");
        }
    }
    Context getFinalKeyPairs(){
        return finalContext;
    }
}