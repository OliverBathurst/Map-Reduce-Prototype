import javafx.util.Pair;
import java.util.ArrayList;

/**
 * A context object stores a list of key-value pairs
 */

class Context {
    private final ArrayList<Pair<Object, Object>> mapped = new ArrayList<>();//stores the key/value pairs

    Context(){}
    /**
     * write the object,object pair to the list for this context
     * context.write(a,b); creates a new pair of key a and value b
     * the pair list can be retrieved by calling getMap();
     */
    void write(Object a, Object b){
        mapped.add(new Pair<>(a, b));//add to the list
    }
    /**
     * return the list of pairs
     */
    ArrayList<Pair<Object, Object>> getMap(){return mapped;}//return the list
}
