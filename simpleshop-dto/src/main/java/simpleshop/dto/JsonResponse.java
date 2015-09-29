package simpleshop.dto;

import java.util.Map;
import java.util.TreeMap;

public class JsonResponse<T> {

    private static final String STATUS_OK = "OK";
    private static final String STATUS_ERROR = "ERROR";

    public static <T> JsonResponse<T> createSuccess(T content){
       return new JsonResponse<>(STATUS_OK, null, content);
    }

    public static JsonResponse<?> createError(Throwable ex){
        return new JsonResponse<>(STATUS_ERROR, ex.getLocalizedMessage(), null);
    }

    public static <T> JsonResponse<T> createError(String msg, T content){
        return new JsonResponse<>(STATUS_ERROR, msg, content);
    }

    private final String status;//content type: e.g. model
    private final String description;//content name e.g. customer
    private final T content;
    private Map<String, Object> tags; //additional information about the model

    private JsonResponse(String status, String description, T content){
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
