import java.util.Map;

class Mapper {
    private Map<Object, Object> mapped;

    private void map(Object key, Object value) {
        mapped.put(key,value);
    }

    void run(Map<Object,Object> mapped){
        for(Map.Entry<Object, Object> entry : mapped.entrySet()) {
            map(entry.getKey(), entry.getValue());
        }
    }

}
