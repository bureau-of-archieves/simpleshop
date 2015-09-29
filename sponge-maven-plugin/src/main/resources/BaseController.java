@args String projectName, String modelName;
@{String basePackage = projectName.toLowerCase()}
@{String modelNameCamel = modelName.substring(0,1).toLowerCase() + modelName.substring(1)}
@{String modelNameUrl = modelNameCamel.replaceAll("(?=[A-Z])", "\\_").toLowerCase()}
package @(basePackage).webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import @(basePackage).domain.model.@(modelName);
import @(basePackage).dto.@(modelName)Search;
import @(basePackage).dto.JsonResponse;
import @(basePackage).dto.ModelQuickSearch;
import @(basePackage).service.@(modelName)Service;
import @(basePackage).webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@@Controller
@@RequestMapping(produces = "application/json")
public abstract class @(modelName)BaseController extends BaseJsonController<@modelName> {

    @@Autowired
    protected @(modelName)Service @(modelNameCamel)Service;

    @@RequestMapping(value = "/@(modelNameUrl)/search", method = RequestMethod.GET)
    public @@ResponseBody JsonResponse<@(modelName)Search> @(modelNameCamel)Search() {
        return JsonResponse.createSuccess(new @(modelName)Search());
    }

    @@RequestMapping(value = "/@(modelNameUrl)/search", method = RequestMethod.POST, consumes = "application/json")
    public @@ResponseBody JsonResponse<Iterable<@(modelName)>> @(modelNameCamel)Search(@@Valid @@RequestBody final @(modelName)Search @(modelNameCamel)Search, BindingResult bindingResult) {
        return modelSearch(@(modelNameCamel)Search, bindingResult, @(modelNameCamel)Service);
    }

    @@RequestMapping(value = "/@(modelNameUrl)/new", method = RequestMethod.GET)
    public @@ResponseBody JsonResponse<@(modelName)> @(modelNameCamel)Create() {
        return modelCreate(@(modelNameCamel)Service);
    }

    @@RequestMapping(value = "/@(modelNameUrl)/save", method = RequestMethod.POST, consumes = "application/json")
    public @@ResponseBody JsonResponse<@(modelName)> @(modelNameCamel)Save(@@Valid @@RequestBody final @(modelName) @(modelNameCamel), BindingResult bindingResult) {
        return modelSave(@(modelNameCamel), bindingResult, @(modelNameCamel)Service);
    }

    @@RequestMapping(value = "/@(modelNameUrl)/{id}", method = RequestMethod.GET)
    public @@ResponseBody JsonResponse<@(modelName)> @(modelNameCamel)Details(@@PathVariable int id) {
        return modelDetails(id, @(modelNameCamel)Service);
    }

    @@RequestMapping(value = "/@(modelNameUrl)/delete", method = RequestMethod.POST, consumes = "application/json")
    public @@ResponseBody JsonResponse<Serializable> @(modelNameCamel)Delete(@@Valid @@RequestBody final int id) {
        return modelDelete(id, @(modelNameCamel)Service);
    }

    @@RequestMapping(value = "/@(modelNameUrl)/list", method = RequestMethod.POST, consumes = "application/json")
    public @@ResponseBody JsonResponse<Iterable<@(modelName)>> @(modelNameCamel)List(@@Valid @@RequestBody final ModelQuickSearch quickSearch){
        return modelList(quickSearch, @(modelNameCamel)Service);
    }
}
