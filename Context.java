import javafx.util.Pair;
import java.util.ArrayList;

/**
 * Created by Oliver Bathurst on 13/10/2017.
 * All Rights Reserved
 * Unauthorized copying of this file via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Context {
    private final ArrayList<Pair<Object, Object>> mapped = new ArrayList<>();

    Context(){}

    void write(Object a, Object b){
        mapped.add(new Pair<>(a,b));
    }

    ArrayList getMap(){ return mapped; }
}
