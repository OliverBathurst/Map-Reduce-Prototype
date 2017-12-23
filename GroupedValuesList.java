import javafx.util.Pair;
import java.util.ArrayList;

/**
 * Each GroupedValuesList object stores a list of key-value pairs based on a key,
 * such that any given key 'k' will only exist in one GroupedValuesList,
 * this checking is done in the Combiner class.
 */
class GroupedValuesList {
    private final ArrayList<Pair<Object,Object>> groupedKeys = new ArrayList<>();

    GroupedValuesList(){}

    void add(Pair<Object,Object> pair){
        groupedKeys.add(pair);
    }

    ArrayList<Pair<Object,Object>> getBuffer(){
        return groupedKeys;
    }
}
