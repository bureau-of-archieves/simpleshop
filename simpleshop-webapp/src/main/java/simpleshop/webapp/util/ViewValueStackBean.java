package simpleshop.webapp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: JOHNZ
 * Date: 30/10/14
 * Time: 11:05 PM
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
