package simpleshop.dto;

import java.util.Map;
import java.util.TreeMap;

public class JsonResponse<T> {

    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";

    private final String status;//content type: e.g. model
    private final String description;//content name e.g. customer
    private final T content;
    private Map<String, Object> tags; //additional information about the model

    public JsonResponse(String status, String description, T content){
        this.status = status;
        this.description = description;
        this.content = content;
    }

    public String getStatus() {
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
