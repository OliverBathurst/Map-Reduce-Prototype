import javafx.util.Pair;
import java.util.ArrayList;

/**
 * The combiner takes the buffer (intermediate key-value pairs) from a Mapper and groups the values for the same key.
 * This class creates a unique list for each key, such that any given list contains all of the associated
 * values 'v' for a key 'k', this is known as the 'Combiner' phase.
 */
class Combiner {
    private final ArrayList<Pair<Object,Object>> intermediateOutput; //intermediate key-value pairs
    private final ArrayList<CombinerOutput> combinerOutputs; //to store lists of key-value pairs

    Combiner(ArrayList<Pair<Object,Object>> iO, ArrayList<CombinerOutput> combinerList){
        this.intermediateOutput = iO;
        this.combinerOutputs = combinerList;
    }
    /**
     * Iterate over all intermediate key-value pairs from the mapper and pass the pair to the checking function
     */
    /**
     * Searches all the current GroupedValuesLists for the key,
     * if it exists in a list, add the key and its accompanying value to that list.
     * Else, create a new CombinerOutput to store the key-value pairs for that key.
     * This ensures that there is a different list for each key.
     * The result of this function is many different lists, each containing a unique key and the associated values.
     */
    void combine(){
        for (Pair<Object, Object> anIntermediateOutput : intermediateOutput) {
            boolean contains = false;
            synchronized (combinerOutputs) {
                for (CombinerOutput c : combinerOutputs) {
                    if (c.getKeyAndValuesPair().getKey().equals(anIntermediateOutput.getKey())) {
                        c.add(anIntermediateOutput.getValue());
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    combinerOutputs.add(new CombinerOutput(anIntermediateOutput.getKey()) {{
                        add(anIntermediateOutput.getValue());
                    }});
                }
            }
        }
    }
}