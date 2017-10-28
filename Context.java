import javafx.util.Pair;
import java.util.ArrayList;

class Context {
    private final ArrayList<Pair<Object, Object>> mapped = new ArrayList<>();//stores the kay/value pairs

    Context(){}
    /**
     * write the object,object pair to the list for this context
     * context.write(a,b); creates a pair of key a and value b
     * the pair list can be retrieved by calling (context obj).getMap();
     * each mapper will have their own context to write to and each chunk will have
     * their own mapper
     */
    void write(Object a, Object b){
        mapped.add(new Pair<>(a,b));
    }

    /**
     * return the list of pairs
     */
    ArrayList<Pair<Object, Object>> getMap(){ return mapped; }
}
