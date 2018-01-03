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

    /**
     * Initialise with list of key-value pairs (from a mapper's intermediate output) and a list of combiner outputs for storage
     */
    Combiner(ArrayList<Pair<Object,Object>> iO, ArrayList<CombinerOutput> combinerList){
        this.intermediateOutput = iO;
        this.combinerOutputs = combinerList;
    }
    /**
     * A CombinerOutput object is used to store a unique key and all of the associated values
     * Such that the number of CombinerOutput objects is equal to the number of unique keys.
     * Combine iterates over all intermediate key-value pairs from a mapper.
     * It then searches all the current CombinerOutputs objects for the key,
     * if the key already exists in a CombinerOutput object, add the key's accompanying value to that object's list.
     * Else, create a new CombinerOutput to store the values for that key.
     * This ensures that there is a different list for each key.
     * The result of this function is many different lists, each containing a unique key and the associated values.
     */
    void combine(){
        for (Pair<Object, Object> anIntermediateOutput : intermediateOutput) {//iterate over intermediate key-value pairs
            boolean contains = false;//set a flag for whether the key has been found in an existing CombinerOutput obj or not
            synchronized (combinerOutputs) { //make accessing the existing CombinerOutputs thread-safe such that combiners can run in parallel
                for (CombinerOutput c : combinerOutputs) {//iterate over existing outputs
                    if (c.getKey().equals(anIntermediateOutput.getKey())) {//if the output's key equals the intermediate pair's key
                        c.add(anIntermediateOutput.getValue());//add the intermediate key pair's value to the existing CombinerOutput
                        contains = true;//set flag to 'true' i.e. value has been added to an existing list
                        break; //break, no further searching is required as the key can only exist in one CombinerOutput object
                    }
                }
                if (!contains) {//if the key was not found in existing CombinerOutputs
                    combinerOutputs.add(new CombinerOutput(anIntermediateOutput.getKey()) {{ //add a new CombinerOutput object to the list with the key
                        add(anIntermediateOutput.getValue());//and then add the value to the object's list
                    }});
                }
            }
        }
    }
}