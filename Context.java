import java.util.HashMap;
/**
 * Created by Oliver Bathurst on 13/10/2017.
 * All Rights Reserved
 * Unauthorized copying of this file via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Context {
    private final HashMap<Object, Object> mapped = new HashMap<>();

    Context(){}
    void write(Object a, Object b){
        mapped.put(a,b);
    }
    HashMap getMap(){ return mapped; }
}
