
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import simpleshop.domain.model.Supplier;
import simpleshop.dto.SupplierSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.SupplierService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public abstract class SupplierBaseController extends BaseJsonController<Supplier> {

    @Autowired
    protected SupplierService supplierService;

    @RequestMapping(value = "/supplier/search", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<SupplierSearch> supplierSearch() {
        JsonResponse<SupplierSearch> jsonResponse = JsonResponse.createSuccess(new SupplierSearch());
        populateSortInfoList(SupplierSearch.class, jsonResponse);
        return jsonResponse;
    }

    @RequestMapping(value = "/supplier/search", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Supplier>> supplierSearch(@Valid @RequestBody final SupplierSearch supplierSearch, BindingResult bindingResult) {
        return modelSearch(supplierSearch, bindingResult, supplierService);
    }

    @RequestMapping(value = "/supplier/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Supplier> supplierCreate() {
        return modelCreate(supplierService);
    }

    @RequestMapping(value = "/supplier/save", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Supplier> supplierSave(@Valid @RequestBody final Supplier supplier, BindingResult bindingResult) {
        return modelSave(supplier, bindingResult, supplierService);
    }

    @RequestMapping(value = "/supplier/{id}", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Supplier> supplierDetails(@PathVariable int id) {
        return modelDetails(id, supplierService);
    }

    @RequestMapping(value = "/supplier/delete", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Serializable> supplierDelete(@Valid @RequestBody final int id) {
        return modelDelete(id, supplierService);
    }

    @RequestMapping(value = "/supplier/list", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Supplier>> supplierList(@Valid @RequestBody final ModelQuickSearch quickSearch){
        return modelList(quickSearch, supplierService);
    }
}
