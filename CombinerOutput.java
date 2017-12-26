import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Each CombinerOutput object stores a list of key-value pairs based on a key,
 * such that any given key 'k' will only exist in one CombinerOutput,
 * this checking is done in the Combiner class.
 */
class CombinerOutput {
    private final Pair<Object, List<Object>> groupedValues;

    CombinerOutput(Object key){
        groupedValues = new Pair<>(key, Collections.synchronizedList(new ArrayList<>()));
    }

    synchronized void add(Object pairValue){
        groupedValues.getValue().add(pairValue);
    }

    Pair<Object, List<Object>> getKeyAndValuesPair(){
        return groupedValues;
    }
}
