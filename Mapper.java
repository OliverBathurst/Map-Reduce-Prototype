import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Mapper {
    private final ArrayList<String> singleChunk;
    private final Context context;
    private final Constructor<?> cons;

    Mapper(ArrayList<String> chunk, Context c, Constructor<?> constructor){
        this.singleChunk = chunk;
        this.context = c;
        this.cons = constructor;
    }

    Context returnMap(){
        return context;
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