package simpleshop.webapp.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.dto.ModelSearch;
import simpleshop.service.infrastructure.ModelService;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * The base controller for all controllers that return json data.
 */
public abstract class BaseJsonController<T> {

    @Autowired
    private MessageSource messageSource;

    public String getBindingErrorMessage(BindingResult bindingResult) {
        return WebUtils.getBindingErrorMessage(messageSource, bindingResult);
    }

    protected JsonResponse<T> modelCreate(ModelService<T, ?> modelService){
        return JsonResponse.createSuccess(modelService.create());
    }

    protected JsonResponse<T> modelSave(T model, BindingResult bindingResult, ModelService<T, ?> modelService) {
        //handle binding errors
        if (bindingResult != null && bindingResult.hasErrors()) {
            return JsonResponse.createError(getBindingErrorMessage(bindingResult), model);
        }

        modelService.save(model);
        return JsonResponse.createSuccess(model);
    }

    protected JsonResponse<T> modelDetails(Serializable id, ModelService<T, ?> modelService) {
        T domainObject = modelService.getById(id);
        if (domainObject != null) {
            return JsonResponse.createSuccess(domainObject);
        } else {
            return JsonResponse.createError("Not found (id=" + id + ").", null);
        }
    }

    protected JsonResponse<Serializable> modelDelete(Serializable id, ModelService<T, ?> modelService) {
        modelService.delete(id);
        return JsonResponse.createSuccess(id);
    }

    protected <S extends ModelSearch> JsonResponse<Iterable<T>> modelSearch(S criteria, BindingResult bindingResult, ModelService<T, S> modelService) {
        if (bindingResult != null && bindingResult.hasErrors())
            return JsonResponse.createError(getBindingErrorMessage(bindingResult), null);

        int actualPageSize = criteria.getPageSize();
        criteria.setPageSize(actualPageSize);
        criteria.setPageSizePlusOne(true);
        List<T> result = modelService.search(criteria);

        JsonResponse<Iterable<T>> response = JsonResponse.createSuccess(result);
        if (criteria.getPageIndex() > 0) {
            response.getTags().put("prevPage", true);
        }
        if (result.size() > actualPageSize) {
            response.getTags().put("nextPage", true);
            result.remove(result.size() - 1);//retrieved one more to decide if there is next page
        }
        return response;
    }

    protected JsonResponse<Iterable<T>> modelList(ModelQuickSearch quickSearch, ModelService<T, ?> modelService) {

        List<T> result = modelService.quickSearch(quickSearch.getKeywords(), quickSearch);
        return JsonResponse.createSuccess(result);
    }

}
