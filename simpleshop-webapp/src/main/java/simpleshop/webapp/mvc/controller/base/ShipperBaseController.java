
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import simpleshop.domain.model.Shipper;
import simpleshop.dto.ShipperSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.ShipperService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public abstract class ShipperBaseController extends BaseJsonController<Shipper> {

    @Autowired
    protected ShipperService shipperService;

    @RequestMapping(value = "/shipper/search", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<ShipperSearch> shipperSearch() {
        JsonResponse<ShipperSearch> jsonResponse = JsonResponse.createSuccess(new ShipperSearch());
        populateSortInfoList(ShipperSearch.class, jsonResponse);
        return jsonResponse;
    }

    @RequestMapping(value = "/shipper/search", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Shipper>> shipperSearch(@Valid @RequestBody final ShipperSearch shipperSearch, BindingResult bindingResult) {
        return modelSearch(shipperSearch, bindingResult, shipperService);
    }

    @RequestMapping(value = "/shipper/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Shipper> shipperCreate() {
        return modelCreate(shipperService);
    }

    @RequestMapping(value = "/shipper/save", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Shipper> shipperSave(@Valid @RequestBody final Shipper shipper, BindingResult bindingResult) {
        return modelSave(shipper, bindingResult, shipperService);
    }

    @RequestMapping(value = "/shipper/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Shipper> shipperDetails(@PathVariable int id) {
        return modelDetails(id, shipperService);
    }

    @RequestMapping(value = "/shipper/delete", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Serializable> shipperDelete(@Valid @RequestBody final int id) {
        return modelDelete(id, shipperService);
    }

    @RequestMapping(value = "/shipper/list", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Shipper>> shipperList(@Valid @RequestBody final ModelQuickSearch quickSearch){
        return modelList(quickSearch, shipperService);
    }
}
