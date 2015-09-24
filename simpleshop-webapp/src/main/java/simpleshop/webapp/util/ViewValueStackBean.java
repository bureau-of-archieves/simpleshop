package simpleshop.webapp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Request level value stacks used in jsp view.
 */
public class ViewValueStackBean {

    private Map<String, Stack<Object>> storage = new HashMap<>();

    public void push(String key, Object value) {
        if(!storage.containsKey(key)){
            storage.put(key, new Stack<>());
        }
        storage.get(key).push(value);
    }

    public Object peek(String key){
        if(!storage.containsKey(key))
            return null;
        return storage.get(key).peek();
    }

    public Object pop(String key){
        if(!storage.containsKey(key))
            return null;
        return storage.get(key).pop();
    }
}
