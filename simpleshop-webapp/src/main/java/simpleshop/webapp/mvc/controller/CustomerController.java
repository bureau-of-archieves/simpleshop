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
import simpleshop.service.CustomerService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(produces = "application/json")
public class CustomerController extends BaseJsonController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/customer/search", method = RequestMethod.GET)
    public String customerSearch(Model model) {
        JsonResponse<CustomerSearch> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, new CustomerSearch());
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

        //todo remove this test code
        if(response.getContent().getContact().getContactNumbers() == null){
            Map<String, String> map = new HashMap<>();
            map.put("Phone", "1122342");
            response.getContent().getContact().setContactNumbers(map);
        }

        return super.outputJson(model, response, customerService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/customer/save", method = RequestMethod.POST, consumes = "application/json")
    public String customerSave(@Valid @RequestBody final Customer customer,Model model,  BindingResult bindingResult) {
        JsonResponse<Customer>  response =  saveModel(customer, customerService, bindingResult);
        return super.outputJson(model, response, customerService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/customer/delete", method = RequestMethod.POST, consumes = "application/json")
    public String customerSave(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, customerService);
        return super.outputJson(model, response, null);
    }
}
