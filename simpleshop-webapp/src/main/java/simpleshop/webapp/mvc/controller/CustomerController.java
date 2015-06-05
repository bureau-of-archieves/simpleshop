package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.Customer;
import simpleshop.dto.CustomerSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.service.CustomerService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;

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
    public String customerSearch(@Valid @RequestBody final CustomerSearch customerSearch, Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            throw new RuntimeException(getBindingErrorMessage(bindingResult));

        Iterable<Customer> result = customerService.search(customerSearch);
        JsonResponse<Iterable<Customer>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, result);
        return super.outputJson(model, response, customerService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/customer/new", method = RequestMethod.GET)
    public String customerCreate(Model model) {
        JsonResponse<Customer> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, customerService.create());
        return super.outputJson(model,response , customerService.ignoredJsonProperties());
    }
}
