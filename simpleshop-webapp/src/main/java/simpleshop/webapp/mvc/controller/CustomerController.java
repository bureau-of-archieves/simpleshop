package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.Customer;
import simpleshop.dto.CustomerSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.dto.ModelSearch;
import simpleshop.service.CustomerService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public class CustomerController extends BaseJsonController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/customer/search", method = RequestMethod.GET)
    public String customerSearch(Model model) {
        JsonResponse<CustomerSearch> response = createSearchResponse(new CustomerSearch());
        return super.outputJson(model, response, customerService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/customer/search", method = RequestMethod.POST, consumes = "application/json")
    public String customerSearch(@Valid @RequestBody final CustomerSearch customerSearch, Model model, BindingResult bindingResult) {
        JsonResponse<Iterable<Customer>> response = modelSearch(customerSearch, customerService, bindingResult);
        return super.outputJson(model, response, customerService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/customer/new", method = RequestMethod.GET)
    public String customerCreate(Model model) {
        JsonResponse<Customer> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, customerService.create());
        return super.outputJson(model, response, customerService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    public String customerDetails(@PathVariable int id, Model model) {
        JsonResponse<Customer> response = modelDetails(id, customerService);
        return super.outputJson(model, response, customerService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/customer/save", method = RequestMethod.POST, consumes = "application/json")
    public String customerSave(@Valid @RequestBody final Customer customer,Model model,  BindingResult bindingResult) {
        JsonResponse<Customer>  response =  saveModel(customer, customerService, bindingResult);
        return super.outputJson(model, response, customerService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/customer/delete", method = RequestMethod.POST, consumes = "application/json")
    public String customerDelete(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, customerService);
        return super.outputJson(model, response, null);
    }

    @RequestMapping(value = "/customer/list", method = RequestMethod.POST, consumes = "application/json")
    public String suburbList(@Valid @RequestBody final ModelQuickSearch quickSearch, Model model){
        JsonResponse<Iterable<Customer>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,customerService.quickSearch(quickSearch.getKeywords(), quickSearch));
        return super.outputJson(model, response, null);
    }
}
