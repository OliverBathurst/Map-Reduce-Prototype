import javafx.util.Pair;
import java.util.ArrayList;

/**
 * The combiner takes the buffer (intermediate key-value pairs) from a Mapper and groups the values for the same key.
 * This class creates a unique list for each key, such that any given list contains all of the associated
 * values 'v' for a key 'k', this is known as the 'Combiner' phase.
 */
class Combiner {
    private final ArrayList<Pair<Object,Object>> intermediateOutput; //intermediate key-value pairs
    private final ArrayList<GroupedValuesList> groupedValuesLists; //to store lists of key-value pairs

    Combiner(ArrayList<Pair<Object,Object>> iO, ArrayList<GroupedValuesList> combinerList){
        this.intermediateOutput = iO;
        this.groupedValuesLists = combinerList;
    }
    /**
     * Iterate over all intermediate key-value pairs from the mapper and pass the pair to the checking function
     */
    void combine(){
        for (Pair<Object, Object> anIntermediateOutput : intermediateOutput) {
            checkBuffers(anIntermediateOutput);
        }
    }
    /**
     * Searches all the current GroupedValuesLists for the key,
     * if it exists in a list, add the key and its accompanying value to that list.
     * Else, create a new GroupedValuesList to store the key-value pairs for that key.
     * This ensures that there is a different list for each key.
     * The result of this function is many different lists, each containing a unique key and the associated values.
     */
    private void checkBuffers(Pair<Object, Object> keyPairValue){
        boolean contains = false;
        for (GroupedValuesList c : groupedValuesLists) {
            if (c.getPair().getKey().equals(keyPairValue.getKey())) {
                c.add(keyPairValue.getValue());
                contains = true;
                break;
            }
        }
        if(!contains){
            groupedValuesLists.add(new GroupedValuesList(keyPairValue.getKey()) {{ add(keyPairValue.getValue()); }});
        }
    }
}