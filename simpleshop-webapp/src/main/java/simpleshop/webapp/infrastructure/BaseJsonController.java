package simpleshop.webapp.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import simpleshop.common.StringUtils;
import simpleshop.data.SortInfo;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.SortDirection;
import simpleshop.data.metadata.SortProperty;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelSearch;
import simpleshop.service.MetadataService;
import simpleshop.service.infrastructure.ModelService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The base controller for all controllers that return json data.
 */

public abstract class BaseJsonController {

    protected String outputJson(Model model, JsonResponse<?> response, String[] excludedFields) {
        if (excludedFields == null)
            excludedFields = StringUtils.emptyArray();

        model.addAttribute("content", response);
        model.addAttribute("excludedFields", excludedFields);
        return "json"; //call the json view.
    }

    @Autowired
    private MessageSource messageSource;

    public String getBindingErrorMessage(BindingResult bindingResult) {
        return WebUtils.getBindingErrorMessage(messageSource, bindingResult);
    }

    protected <T> JsonResponse<T> saveModel(T model, ModelService<T, ?> modelService, BindingResult bindingResult) {
        JsonResponse<T> response;

        //handle binding errors
        if (bindingResult != null && bindingResult.hasErrors()) {
            response = new JsonResponse<>(JsonResponse.STATUS_ERROR, getBindingErrorMessage(bindingResult), model);
        } else {
            //handle service error
            try {
                modelService.save(model);
                response = new JsonResponse<>(JsonResponse.STATUS_OK, null, model);
            } catch (Throwable ex) {
                response = new JsonResponse<>(JsonResponse.STATUS_ERROR, ex.getMessage() == null ? ex.toString() : ex.getMessage(), model);
            }
        }
        return response;
    }

    protected <T> JsonResponse<T> modelDetails(Serializable id, ModelService<T, ?> modelService) {
        T domainObject = modelService.getById(id);
        JsonResponse<T> response;
        if (domainObject != null) {
            response = new JsonResponse<>(JsonResponse.STATUS_OK, null, domainObject);
        } else {
            response = new JsonResponse<>(JsonResponse.STATUS_ERROR, "Not found (id=" + id + ").", null);
        }
        return response;
    }

    protected <S extends ModelSearch, T> JsonResponse<Iterable<T>> modelSearch(S criteria, ModelService<T, S> modelService, BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors())
            return new JsonResponse<>(JsonResponse.STATUS_ERROR, getBindingErrorMessage(bindingResult), null);

        int actualPageSize = criteria.getPageSize();
        criteria.setPageSize(actualPageSize);
        criteria.setPageSizePlusOne(true);
        List<T> result = modelService.search(criteria);

        JsonResponse<Iterable<T>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, result);
        if (criteria.getPageIndex() > 0) {
            response.getTags().put("prevPage", true);
        }
        if (result.size() > actualPageSize) {
            response.getTags().put("nextPage", true);
            result.remove(result.size() - 1);//retrieved one more to decide if there is next page
        }

        return response;
    }

    protected <T> JsonResponse<Serializable> deleteModel(Serializable id, ModelService<T, ?> modelService) {
        JsonResponse<Serializable> response;
        try {
            modelService.delete(id);
            response = new JsonResponse<>(JsonResponse.STATUS_OK, null, id);
        } catch (Exception ex) {
            response = new JsonResponse<>(JsonResponse.STATUS_ERROR, ex.getMessage(), null);
        }
        return response;
    }

    @Autowired
    private MetadataService metadataService;

    protected <T extends ModelSearch> JsonResponse<T> createSearchResponse(T newInstance) {

        JsonResponse<T> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, newInstance);
        ModelMetadata searchMetadata = metadataService.getMetadata(newInstance.getClass().getSimpleName());
        List<SortProperty> sortProperties = searchMetadata.getSortProperties();
        List<SortInfo> sortInfos = new ArrayList<>();
        if (sortProperties != null) {
            for (SortProperty sortProperty : sortProperties) {
                SortInfo sortInfo = new SortInfo();
                sortInfo.setAlias(sortProperty.alias());
                sortInfo.setProperty(sortProperty.propertyName());
                if(sortProperty.sortDirection() == SortDirection.DESC){
                     sortInfo.setAscending(false);
                } else {
                    sortInfo.setAscending(true);
                }
                sortInfos.add(sortInfo);

                if(sortProperty.sortDirection() == SortDirection.BOTH){

                    sortInfo = new SortInfo();
                    sortInfo.setAlias(sortProperty.alias());
                    sortInfo.setProperty(sortProperty.propertyName());
                    sortInfo.setAscending(false);
                    sortInfos.add(sortInfo);
                }
            }
        }

        response.getTags().put("sortProperties", sortInfos);

        return response;
    }
}
