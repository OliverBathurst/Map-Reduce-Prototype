import javafx.util.Pair;

import java.util.ArrayList;

class Sorter {
    private final ArrayList<Pair<Object, Object>> iO;

    Sorter(ArrayList<Pair<Object, Object>> intermediateOutput){
        this.iO = intermediateOutput;
    }
    void sort(){
        iO.sort((o1, o2) -> o1.getKey().equals(o2.getKey()) ? 0 : 1);
    }
}
