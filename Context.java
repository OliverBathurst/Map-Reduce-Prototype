import java.util.HashMap;

public class Context {
    private final HashMap<Object, Object> mapped;

    Context(){
        mapped = new HashMap<>();
    }
    void write(Object a, Object b){
        mapped.put(a,b);
    }
    HashMap getMap(){ return mapped; }
}
