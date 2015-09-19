@args String projectName, String modelName;
@{String basePackage = projectName.toLowerCase()}
@{String modelNameCamel = modelName.substring(0,1).toLowerCase() + modelName.substring(1)}
@{String modelNameUrl = modelNameCamel.replaceAll("(?=[A-Z])", "\\_").toLowerCase()}
package @(basePackage).webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public abstract class @(modelName)BaseController extends BaseJsonController {

    @@Autowired
    protected @(modelName)Service @(modelNameCamel)Service;

    @@RequestMapping(value = "/@(modelNameUrl)/search", method = RequestMethod.GET)
    public String @(modelNameCamel)Search(Model model) {
        JsonResponse<@(modelName)Search> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, new @(modelName)Search());
        return super.outputJson(model, response, @(modelNameCamel)Service.ignoredJsonProperties());
    }

    @@RequestMapping(value = "/@(modelNameUrl)/search", method = RequestMethod.POST, consumes = "application/json")
    public String @(modelNameCamel)Search(@@Valid @@RequestBody final @(modelName)Search @(modelNameCamel)Search, BindingResult bindingResult, Model model) {
        JsonResponse<Iterable<@(modelName)>> response = modelSearch(@(modelNameCamel)Search, @(modelNameCamel)Service, bindingResult);
        return super.outputJson(model, response, @(modelNameCamel)Service.ignoredJsonProperties());
    }

    @@RequestMapping(value = "/@(modelNameUrl)/new", method = RequestMethod.GET)
    public String @(modelNameCamel)Create(Model model) {
        JsonResponse<@(modelName)> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, @(modelNameCamel)Service.create());
        return super.outputJson(model, response, @(modelNameCamel)Service.ignoredJsonProperties());
    }

    @@RequestMapping(value = "/@(modelNameUrl)/save", method = RequestMethod.POST, consumes = "application/json")
    public String @(modelNameCamel)Save(@@Valid @@RequestBody final @(modelName) @(modelNameCamel), BindingResult bindingResult, Model model) {
        JsonResponse<@(modelName)>  response =  saveModel(@(modelNameCamel), @(modelNameCamel)Service, bindingResult);
        return super.outputJson(model, response, @(modelNameCamel)Service.ignoredJsonProperties());
    }

    @@RequestMapping(value = "/@(modelNameUrl)/{id}", method = RequestMethod.GET)
    public String @(modelNameCamel)Details(@@PathVariable int id, Model model) {
        JsonResponse<@(modelName)> response = modelDetails(id, @(modelNameCamel)Service);
        return super.outputJson(model, response, @(modelNameCamel)Service.ignoredJsonProperties());
    }

    @@RequestMapping(value = "/@(modelNameUrl)/delete", method = RequestMethod.POST, consumes = "application/json")
    public String @(modelNameCamel)Delete(@@Valid @@RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, @(modelNameCamel)Service);
        return super.outputJson(model, response, null);
    }

    @@RequestMapping(value = "/@(modelNameUrl)/list", method = RequestMethod.POST, consumes = "application/json")
    public String @(modelNameCamel)List(@@Valid @@RequestBody final ModelQuickSearch quickSearch, Model model){
        JsonResponse<Iterable<@(modelName)>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,@(modelNameCamel)Service.quickSearch(quickSearch.getKeywords(), quickSearch));
        return super.outputJson(model, response, null);
    }
}
