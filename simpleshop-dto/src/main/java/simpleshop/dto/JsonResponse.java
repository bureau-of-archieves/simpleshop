package simpleshop.dto;

import java.util.Map;
import java.util.TreeMap;

public class JsonResponse<T> {

    private String type;//content type: e.g. model
    private String name;//content name e.g. customer
    private T content;
    private Map<String, Object> tags; //additional information about the model

    public JsonResponse(String type, String name, T content){
        this.type = type;
        this.name = name;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public T getContent() {
        return content;
    }

    public Map<String, Object> getTags() {
        if(tags == null)
            tags = new TreeMap<>();
        return tags;
    }
}
