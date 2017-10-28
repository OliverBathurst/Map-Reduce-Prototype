import javafx.util.Pair;
import java.util.ArrayList;

class Merger {
    private final ArrayList<Pair<Object, ArrayList<Object>>> pairs = new ArrayList<>(); //stores for "key, Iterable<values>" in reducer

    @SuppressWarnings("unchecked")
    Merger(ArrayList<Reducer> reducerList){
        for(Reducer reduce : reducerList){
            for (Pair<Object, ArrayList<Object>> reduced : reduce.returnReduced()) { //loop through mapper instances
                boolean contains = false;
                for (Pair<Object, ArrayList<Object>> pair : pairs) {
                    if (pair.getKey().equals(reduced.getKey())) {
                        pair.getValue().add(reduced.getValue());//if it already exists add it's value to its list
                        contains = true; //if it contains it
                        break;
                    }
                }
                if (!contains) {
                    pairs.add(new Pair(reduced.getKey(), new ArrayList(reduced.getValue())));
                }
            }
        }
    }
    ArrayList<Pair<Object, ArrayList<Object>>> returnFinalPairs(){
        return pairs;
    }
}
