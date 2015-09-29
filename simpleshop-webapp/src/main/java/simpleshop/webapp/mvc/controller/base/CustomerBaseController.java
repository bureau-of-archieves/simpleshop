
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import simpleshop.domain.model.Customer;
import simpleshop.dto.CustomerSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.CustomerService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public abstract class CustomerBaseController extends BaseJsonController<Customer> {

    @Autowired
    protected CustomerService customerService;

    @RequestMapping(value = "/customer/search", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<CustomerSearch> customerSearch() {
        return JsonResponse.createSuccess(new CustomerSearch());
    }

    @RequestMapping(value = "/customer/search", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Customer>> customerSearch(@Valid @RequestBody final CustomerSearch customerSearch, BindingResult bindingResult) {
        return modelSearch(customerSearch, bindingResult, customerService);
    }

    @RequestMapping(value = "/customer/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Customer> customerCreate() {
        return modelCreate(customerService);
    }

    @RequestMapping(value = "/customer/save", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Customer> customerSave(@Valid @RequestBody final Customer customer, BindingResult bindingResult) {
        return modelSave(customer, bindingResult, customerService);
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Customer> customerDetails(@PathVariable int id) {
        return modelDetails(id, customerService);
    }

    @RequestMapping(value = "/customer/delete", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Serializable> customerDelete(@Valid @RequestBody final int id) {
        return modelDelete(id, customerService);
    }

    @RequestMapping(value = "/customer/list", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Customer>> customerList(@Valid @RequestBody final ModelQuickSearch quickSearch){
        return modelList(quickSearch, customerService);
    }
}
