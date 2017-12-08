import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Reducer {
    private final ArrayList<Pair<Object, ArrayList<Object>>> pairs = new ArrayList<>();
    private ArrayList<Pair<Object, Object>> fromMapper = null;

    Reducer(ArrayList<Pair<Object, Object>> map){
        this.fromMapper = map;
    }

    ArrayList<Pair<Object, ArrayList<Object>>> returnReduced(){
        return pairs;
    }

    @SuppressWarnings("unchecked")
    void reduce(){
        for(Pair<Object, Object> pairsFromContext : fromMapper){
            boolean contains = false;
            for (Pair<Object, ArrayList<Object>> pair : pairs) {
                if (pair.getKey().equals(pairsFromContext.getKey())) {
                    pair.getValue().add(pairsFromContext.getValue());//if it already exists add it's value to its list
                    contains = true; //if it contains it
                    break;
                }
            }
            if (!contains) {
                pairs.add(new Pair(pairsFromContext.getKey(), new ArrayList(Collections.singletonList(pairsFromContext.getValue()))));
            }
        }
    }
}
