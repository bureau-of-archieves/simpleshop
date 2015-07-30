package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.Supplier;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.dto.SupplierSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.service.SupplierService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public class SupplierController extends BaseJsonController {

    @Autowired
    private SupplierService supplierService;

    @RequestMapping(value = "/supplier/search", method = RequestMethod.GET)
    public String supplierSearch(Model model) {
        JsonResponse<SupplierSearch> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, new SupplierSearch());
        return super.outputJson(model, response, supplierService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/supplier/search", method = RequestMethod.POST, consumes = "application/json")
    public String supplierSearch(@Valid @RequestBody final SupplierSearch supplierSearch, Model model, BindingResult bindingResult) {
        JsonResponse<Iterable<Supplier>> response = modelSearch(supplierSearch, supplierService, bindingResult);
        return super.outputJson(model, response, supplierService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/supplier/new", method = RequestMethod.GET)
    public String supplierCreate(Model model) {
        JsonResponse<Supplier> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, supplierService.create());
        return super.outputJson(model, response, supplierService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/supplier/{id}", method = RequestMethod.GET)
    public String supplierDetails(@PathVariable int id, Model model) {
        JsonResponse<Supplier> response = modelDetails(id, supplierService);
        return super.outputJson(model, response, supplierService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/supplier/save", method = RequestMethod.POST, consumes = "application/json")
    public String supplierSave(@Valid @RequestBody final Supplier supplier, Model model,  BindingResult bindingResult) {
        JsonResponse<Supplier>  response =  saveModel(supplier, supplierService, bindingResult);
        return super.outputJson(model, response, supplierService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/supplier/delete", method = RequestMethod.POST, consumes = "application/json")
    public String supplierSave(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, supplierService);
        return super.outputJson(model, response, null);
    }

    @RequestMapping(value = "/supplier/list", method = RequestMethod.POST, consumes = "application/json")
    public String suburbList(@Valid @RequestBody final ModelQuickSearch quickSearch, Model model){
        JsonResponse<Iterable<Supplier>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,supplierService.quickSearch(quickSearch.getKeywords(), quickSearch));
        return super.outputJson(model, response, null);
    }
}
