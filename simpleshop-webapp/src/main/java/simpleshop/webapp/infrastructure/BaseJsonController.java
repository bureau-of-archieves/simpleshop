package simpleshop.webapp.infrastructure;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import simpleshop.common.StringUtils;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelSearch;
import simpleshop.service.infrastructure.ModelService;

import java.io.Serializable;
import java.util.List;

/**
 * The base controller for all controllers that return json data.
 * User: JOHNZ
 * Date: 10/09/14
 * Time: 5:13 PM
 */

public abstract class BaseJsonController {

    protected String outputJson(Model model, JsonResponse<?> response, String[] excludedFields) {
        if (excludedFields == null)
            excludedFields = StringUtils.emptyArray();

        model.addAttribute("content", response);
        model.addAttribute("excludedFields", excludedFields);
        return "json"; //call the json view.
    }

    protected String getBindingErrorMessage(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            stringBuilder.append(error.getObjectName());
            stringBuilder.append(":");
            stringBuilder.append(error.getDefaultMessage());
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }


    protected <T> JsonResponse<T> saveModel(T model, ModelService<T, ?> modelService, BindingResult bindingResult) {
        JsonResponse<T> response;

        //handle binding errors
        if (bindingResult.hasErrors()) {
            response = new JsonResponse<>(JsonResponse.STATUS_ERROR, getBindingErrorMessage(bindingResult), model);
        } else {
            //handle service error
            try {
                modelService.save(model);
                response = new JsonResponse<>(JsonResponse.STATUS_OK, null, model);
            } catch (Throwable ex) {
                response = new JsonResponse<>(JsonResponse.STATUS_ERROR, ex.getMessage(), model);
            }
        }
        return response;
    }

    protected <T> JsonResponse<T> modelDetails(Serializable id, ModelService<T, ?> modelService){
        T domainObject = modelService.getById(id);
        JsonResponse<T> response;
        if(domainObject != null){
            response = new JsonResponse<>(JsonResponse.STATUS_OK, null, domainObject);
        } else {
            response = new JsonResponse<>(JsonResponse.STATUS_ERROR, "Not found (id=" + id + ").", null);
        }
        return response;
    }

    protected <S extends ModelSearch, T> JsonResponse<Iterable<T>> modelSearch(S criteria, ModelService<T, S> modelService, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new JsonResponse<>(JsonResponse.STATUS_OK, getBindingErrorMessage(bindingResult), null);

        int actualPageSize = criteria.getPageSize();
        criteria.setPageSize(actualPageSize + 1); //retrieve one more to decide if there is next page
        List<T> result = modelService.search(criteria);

        //todo set previous / next disabled here
        JsonResponse<Iterable<T>> response =  new JsonResponse<>(JsonResponse.STATUS_OK, null, result);
        if(criteria.getPageIndex() > 0){
            response.getTags().put("prevPage", true);
        }
        if(result.size() > actualPageSize){
            response.getTags().put("nextPage", true);
            result.remove(result.size() - 1);
        }

        response.getTags().put("page", criteria.getPageIndex());
        return response;
    }

    protected <T> JsonResponse<Serializable> deleteModel(Serializable id, ModelService<T, ?> modelService){
        JsonResponse<Serializable> response;
        try {
            modelService.delete(id);
            response = new JsonResponse<>(JsonResponse.STATUS_OK, null, id);
        } catch (Exception ex){
            response = new JsonResponse<>(JsonResponse.STATUS_ERROR, ex.getMessage(), null);
        }
        return response;
    }

}
