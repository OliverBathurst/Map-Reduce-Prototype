import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Sorter class takes intermediate output in the form of a list of key-value pairs
 * It then sorts this list based on the pair's key
 */
class Sorter {
    private final ArrayList<Pair<Object, Object>> iO;

    /**
     * Initialise with intermediate output
     */
    Sorter(ArrayList<Pair<Object, Object>> intermediateOutput){
        this.iO = intermediateOutput;
    }

    /**
     * Sort list based on each Pair's key
     */
    void sort(){
        iO.sort((o1, o2) -> o1.getKey().equals(o2.getKey()) ? 0 : 1);
    }
}
