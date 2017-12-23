import javafx.util.Pair;
import java.util.ArrayList;

class Combine {
    private ArrayList<Mapper> mappers;
    private ArrayList<Combiner> combine;
    private static int CURRENT_MAPPER = 0;

    Combine(ArrayList<Mapper> mapper, ArrayList<Combiner> combinerList){
        this.mappers = mapper;
        this.combine = combinerList;
    }
    void combine(){
        for(int i = 0; i < mappers.size(); i++){
            CURRENT_MAPPER = i;

            for(int j = 0; j < mappers.get(i).getIntermediateOutput().size(); j++){
                checkMappers(mappers.get(i).getIntermediateOutput().get(j));
            }
        }
        sort();//finally sort the array (not really needed as all keys of type k are in one map anyway)
        //printDebug();
    }

    void checkMappers(Pair<Object, Object> keyPairValue){
        for(int i = CURRENT_MAPPER + 1; i < mappers.size(); i++){//check other mappers for key
            for(int j = 0; j < mappers.get(i).getIntermediateOutput().size(); j++) {
                Pair<Object, Object> keyPair = mappers.get(i).getIntermediateOutput().get(j);
                if(keyPair.getKey().equals(keyPairValue.getKey())){
                    boolean contains = false;
                    for(Combiner c: combine){
                        for(int z = 0; z < c.getBuffer().size(); z++){
                            if(c.getBuffer().get(z).getKey().equals(keyPairValue.getKey())){
                                c.add(keyPairValue);
                                contains = true;
                                break;
                            }
                        }
                    }
                    if(!contains){
                        combine.add(new Combiner() {{ add(keyPairValue); }});
                    }
                }
            }
        }
    }
    /**
     * sort by object
     */
    void sort(){
        for(Combiner c: combine){
            c.getBuffer().sort((o1, o2) -> o1.getKey().equals(o2.getKey()) ? 0 : 1);
        }
    }
    /*private void printDebug() {
        int i = 0;
        for(Combiner c: combine){
            System.out.println("Combiner: " + i + " Size: " + c.getBuffer().size());
            for(Pair<Object, Object> p: c.getBuffer()){
                System.out.println(p.getKey());
            }
            i++;
        }
    }*/
}