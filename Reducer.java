import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Collections;

class Reducer {
    private final ArrayList<Pair<Object, ArrayList<Object>>> pairs = new ArrayList<>();
    private final Mapper toReduce;

    Reducer(Mapper map){
        this.toReduce = map;
    }

    ArrayList<Pair<Object, ArrayList<Object>>> returnReduced(){
        return pairs;
    }

    @SuppressWarnings("unchecked")
    void run(){
        for(Pair<Object, Object> pairsFromContext : toReduce.returnMap().getMap()){
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
