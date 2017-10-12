import java.util.HashMap;

public class Context {
    private HashMap<Object, Object> mapped;

    Context(){
        mapped = new HashMap<>();
    }
    void write(Object a, Object b){
        mapped.put(a,b);
    }
    HashMap getMap(){ return mapped; }
}
