import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * Each CombinerOutput object stores a list of values for a particular key,
 * such that any given key 'k' will only exist in one CombinerOutput object,
 * this checking is done in the Combiner class.
 */
class CombinerOutput {
    private final Pair<Object, List<Object>> kayAndListOfValues;//pair stores key and list of values

    /**
     * Initialise object with the key and a new container for the associated values
     */
    CombinerOutput(Object key){
        kayAndListOfValues = new Pair<>(key, new ArrayList<>());
    }

    /**
     * Add a value to the list for that key
     */
    synchronized void add(Object pairValue){
        kayAndListOfValues.getValue().add(pairValue);
    }

    /**
     * Return the key of the (key (list)values) pair
     */
    Object getKey(){
        return kayAndListOfValues.getKey();//return key for checking
    }

    /**
     * Return key and list of values in the pair data structure
     */
    Pair<Object, List<Object>> getKeyAndValuesPair(){
        return kayAndListOfValues;
    }
}
