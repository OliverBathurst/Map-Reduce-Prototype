import javafx.util.Pair;
import java.util.ArrayList;

/**
 * Each GroupedValuesList object stores a list of key-value pairs based on a key,
 * such that any given key 'k' will only exist in one GroupedValuesList,
 * this checking is done in the Combiner class.
 */
class GroupedValuesList {
    private final Pair<Object, ArrayList<Object>> groupedValues;

    GroupedValuesList(Object key){
        groupedValues = new Pair<>(key, new ArrayList<>());
    }

    void add(Object pairValue){
        groupedValues.getValue().add(pairValue);
    }

    Pair<Object, ArrayList<Object>> getList(){
        return groupedValues;
    }
}
