package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.Suburb;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.SuburbService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public class SuburbController extends BaseJsonController {

    @Autowired
    private SuburbService suburbService;

    @RequestMapping(value = "/suburb/list", method = RequestMethod.POST, consumes = "application/json")
    public String suburbList(@Valid @RequestBody final ModelQuickSearch quickSearch, Model model){
        JsonResponse<Iterable<Suburb>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,suburbService.quickSearch(quickSearch.getKeywords(), quickSearch));
        return super.outputJson(model, response, null);
    }

    @RequestMapping(value = "/suburb/new", method = RequestMethod.GET)
    public String suburbCreate(Model model) {
        JsonResponse<Suburb> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, suburbService.create());
        return super.outputJson(model, response, suburbService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/suburb/{id}", method = RequestMethod.GET)
    public String suburbDetails(@PathVariable int id, Model model) {
        JsonResponse<Suburb> response = modelDetails(id, suburbService);
        return super.outputJson(model, response, suburbService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/suburb/save", method = RequestMethod.POST, consumes = "application/json")
    public String suburbSave(@Valid @RequestBody final Suburb suburb,Model model,  BindingResult bindingResult) {
        JsonResponse<Suburb>  response =  saveModel(suburb, suburbService, bindingResult);
        return super.outputJson(model, response, suburbService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/suburb/delete", method = RequestMethod.POST, consumes = "application/json")
    public String suburbDelete(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, suburbService);
        return super.outputJson(model, response, null);
    }
}
