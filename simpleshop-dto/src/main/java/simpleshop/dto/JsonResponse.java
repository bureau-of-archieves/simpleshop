package simpleshop.dto;

import simpleshop.common.StringUtils;

import java.util.Map;
import java.util.TreeMap;

public class JsonResponse<T> {

    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";

    public static <T> JsonResponse<T> createSuccess(T content){
       return new JsonResponse<>(STATUS_OK, null, content);
    }

    public static JsonResponse<?> createError(Throwable ex){
        String errorMessage = ex.getLocalizedMessage();
        if(StringUtils.isNullOrEmpty(errorMessage)){
            errorMessage = ex.getClass().getName();
        }
        return new JsonResponse<>(STATUS_ERROR, errorMessage, null);
    }

    public static <T> JsonResponse<T> createError(String msg, T content){
        return new JsonResponse<>(STATUS_ERROR, msg, content);
    }

    private String status;//content type: e.g. model
    private String description;//content name e.g. customer
    private T content;
    private Map<String, Object> tags; //additional information about the model

    public JsonResponse(){

    }

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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Map<String, Object> getTags() {
        if(tags == null)
            tags = new TreeMap<>();
        return tags;
    }
}
