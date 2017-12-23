import javafx.util.Pair;

import java.util.ArrayList;

class Combine {
    private final ArrayList<Combiner> combine;
    private final Mapper mapper;

    Combine(Mapper mapper, ArrayList<Combiner> combinerList){
        this.mapper = mapper;
        this.combine = combinerList;
    }
    void combine(){
        for(int j = 0; j < mapper.getIntermediateOutput().size(); j++){
            checkMappers(mapper.getIntermediateOutput().get(j)); 
        }
    }

    private void checkMappers(Pair<Object, Object> keyPairValue){
        boolean contains = false;
        for (Combiner c : combine) {
            for (int z = 0; z < c.getBuffer().size(); z++) {
                if (c.getBuffer().get(z).getKey().equals(keyPairValue.getKey())) {
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