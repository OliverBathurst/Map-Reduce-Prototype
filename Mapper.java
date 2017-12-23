import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Mapper {
    private final Context context = new Context();
    private final ArrayList<String> singleChunk;
    private final Constructor<?> cons;

    Mapper(ArrayList<String> chunk, Constructor<?> constructor){
        this.singleChunk = chunk;
        this.cons = constructor;
    }

    ArrayList<Pair<Object, Object>> getIntermediateOutput(){
        return context.getMap();
    }

    void run(){
        for(String chunk : singleChunk) {
            try {
                cons.newInstance(chunk, context);//Invoke the map method with each string (line) and the context
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}