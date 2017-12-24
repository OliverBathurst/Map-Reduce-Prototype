import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Mapper {
    private final Context context = new Context();
    private final Logger logger = new Logger();
    private final ArrayList<String> singleChunk;
    private final Config c;

    Mapper(ArrayList<String> chunk, Config config){
        this.singleChunk = chunk;
        this.c = config;
    }

    ArrayList<Pair<Object, Object>> getIntermediateOutput(){
        return context.getMap();
    }
    @SuppressWarnings("unchecked")
    void run(){
        if (c.getMapper() != null) {
            for (String chunk : singleChunk) {
                try {
                    Constructor<?> cons = c.getMapper().getConstructor(String.class, Context.class);
                    synchronized (this) { //only one thread should access at a time (not thread safe call)
                        cons.newInstance(chunk, context);//Invoke the map method with each string (line) and the context
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            logger.logCritical("Mapper method 'map' not defined\n" +
                    "use config.setMapper(class);");
        }
    }
}