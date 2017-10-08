import java.util.HashMap;
import java.util.Map;

class Mapper {
    public void Mapper(){}
    private static HashMap<Object, Object> mapped = new HashMap<>();

    void map(Object key, Object value) {
        mapped.put(key,value);
    }

    void run(Map<Object,Object> mapped){
        //for(Map.Entry<Object, Object> entry : mapped.entrySet()) {
        //    map(entry.getKey(), entry.getValue());
        //}
    }

}
