import javafx.util.Pair;
import java.util.ArrayList;

class Combiner {
    private final ArrayList<Pair<Object,Object>> groupedKeys = new ArrayList<>();

    Combiner(){}

    void add(Pair<Object,Object> pair){
        groupedKeys.add(pair);
    }

    ArrayList<Pair<Object,Object>> getBuffer(){
        return groupedKeys;
    }
}
