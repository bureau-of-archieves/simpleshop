package simpleshop.dto;

import java.util.Map;
import java.util.TreeMap;

public class JsonResponse<T> {

    private String status;//content type: e.g. model
    private String description;//content name e.g. customer
    private T content;
    private Map<String, Object> tags; //additional information about the model

    public JsonResponse(String status, String description, T content){
        this.status = status;
        this.description = description;
        this.content = content;
    }

    public String getType() {
        return status;
    }

    public String getDescription() {
        return description;
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
