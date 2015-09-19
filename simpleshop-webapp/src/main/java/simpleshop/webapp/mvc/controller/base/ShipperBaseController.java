
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public abstract class ShipperBaseController extends BaseJsonController {

    @Autowired
    protected ShipperService shipperService;

    @RequestMapping(value = "/shipper/search", method = RequestMethod.GET)
    public String shipperSearch(Model model) {
        JsonResponse<ShipperSearch> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, new ShipperSearch());
        return super.outputJson(model, response, shipperService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/shipper/search", method = RequestMethod.POST, consumes = "application/json")
    public String shipperSearch(@Valid @RequestBody final ShipperSearch shipperSearch, BindingResult bindingResult, Model model) {
        JsonResponse<Iterable<Shipper>> response = modelSearch(shipperSearch, shipperService, bindingResult);
        return super.outputJson(model, response, shipperService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/shipper/new", method = RequestMethod.GET)
    public String shipperCreate(Model model) {
        JsonResponse<Shipper> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, shipperService.create());
        return super.outputJson(model, response, shipperService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/shipper/save", method = RequestMethod.POST, consumes = "application/json")
    public String shipperSave(@Valid @RequestBody final Shipper shipper, BindingResult bindingResult, Model model) {
        JsonResponse<Shipper>  response =  saveModel(shipper, shipperService, bindingResult);
        return super.outputJson(model, response, shipperService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/shipper/{id}", method = RequestMethod.GET)
    public String shipperDetails(@PathVariable int id, Model model) {
        JsonResponse<Shipper> response = modelDetails(id, shipperService);
        return super.outputJson(model, response, shipperService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/shipper/delete", method = RequestMethod.POST, consumes = "application/json")
    public String shipperDelete(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, shipperService);
        return super.outputJson(model, response, null);
    }

    @RequestMapping(value = "/shipper/list", method = RequestMethod.POST, consumes = "application/json")
    public String shipperList(@Valid @RequestBody final ModelQuickSearch quickSearch, Model model){
        JsonResponse<Iterable<Shipper>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,shipperService.quickSearch(quickSearch.getKeywords(), quickSearch));
        return super.outputJson(model, response, null);
    }
}
