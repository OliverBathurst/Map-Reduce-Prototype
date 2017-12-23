import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Reducer {
    private final ArrayList<Pair<Object, ArrayList<Object>>> reducedKeyValuePairs = new ArrayList<>();
    private final ArrayList<Pair<Object, Object>> intermediateKeys;

    Reducer(ArrayList<Pair<Object, Object>> map){
        this.intermediateKeys = map;
    }

    ArrayList<Pair<Object, ArrayList<Object>>> returnReduced(){
        return reducedKeyValuePairs;
    }

    @SuppressWarnings("unchecked")
    void reduce(){
        for(Pair<Object, Object> pairsFromContext : intermediateKeys){
            boolean contains = false;
            for (Pair<Object, ArrayList<Object>> pair : reducedKeyValuePairs) {
                if (pair.getKey().equals(pairsFromContext.getKey())) {
                    pair.getValue().add(pairsFromContext.getValue());//if it already exists add its value to the list
                    contains = true; //set flag that it has been found (the key)
                    break; //the key has been found in the reduced list, no need for further processing
                }
            }
            if (!contains) {
                reducedKeyValuePairs.add(new Pair(pairsFromContext.getKey(), new ArrayList(Collections.singletonList(pairsFromContext.getValue()))));
            }
        }
    }
}
