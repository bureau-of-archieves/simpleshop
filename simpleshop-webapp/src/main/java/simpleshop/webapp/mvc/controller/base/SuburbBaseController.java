
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import simpleshop.domain.model.Suburb;
import simpleshop.dto.SuburbSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.SuburbService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public abstract class SuburbBaseController extends BaseJsonController<Suburb> {

    @Autowired
    protected SuburbService suburbService;

    @RequestMapping(value = "/suburb/search", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<SuburbSearch> suburbSearch() {
        JsonResponse<SuburbSearch> jsonResponse = JsonResponse.createSuccess(new SuburbSearch());
        populateSortInfoList(SuburbSearch.class, jsonResponse);
        return jsonResponse;
    }

    @RequestMapping(value = "/suburb/search", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Suburb>> suburbSearch(@Valid @RequestBody final SuburbSearch suburbSearch, BindingResult bindingResult) {
        return modelSearch(suburbSearch, bindingResult, suburbService);
    }

    @RequestMapping(value = "/suburb/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Suburb> suburbCreate() {
        return modelCreate(suburbService);
    }

    @RequestMapping(value = "/suburb/save", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Suburb> suburbSave(@Valid @RequestBody final Suburb suburb, BindingResult bindingResult) {
        return modelSave(suburb, bindingResult, suburbService);
    }

    @RequestMapping(value = "/suburb/{id}", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Suburb> suburbDetails(@PathVariable int id) {
        return modelDetails(id, suburbService);
    }

    @RequestMapping(value = "/suburb/delete", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Serializable> suburbDelete(@Valid @RequestBody final int id) {
        return modelDelete(id, suburbService);
    }

    @RequestMapping(value = "/suburb/list", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Suburb>> suburbList(@Valid @RequestBody final ModelQuickSearch quickSearch){
        return modelList(quickSearch, suburbService);
    }
}
