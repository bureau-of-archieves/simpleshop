
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import simpleshop.domain.model.Order;
import simpleshop.dto.OrderSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.OrderService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public abstract class OrderBaseController extends BaseJsonController<Order> {

    @Autowired
    protected OrderService orderService;

    @RequestMapping(value = "/order/search", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<OrderSearch> orderSearch() {
        JsonResponse<OrderSearch> jsonResponse = JsonResponse.createSuccess(new OrderSearch());
        populateSortInfoList(OrderSearch.class, jsonResponse);
        return jsonResponse;
    }

    @RequestMapping(value = "/order/search", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Order>> orderSearch(@Valid @RequestBody final OrderSearch orderSearch, BindingResult bindingResult) {
        return modelSearch(orderSearch, bindingResult, orderService);
    }

    @RequestMapping(value = "/order/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Order> orderCreate() {
        return modelCreate(orderService);
    }

    @RequestMapping(value = "/order/save", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Order> orderSave(@Valid @RequestBody final Order order, BindingResult bindingResult) {
        return modelSave(order, bindingResult, orderService);
    }

    @RequestMapping(value = "/order/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Order> orderDetails(@PathVariable int id) {
        return modelDetails(id, orderService);
    }

    @RequestMapping(value = "/order/delete", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Serializable> orderDelete(@Valid @RequestBody final int id) {
        return modelDelete(id, orderService);
    }

    @RequestMapping(value = "/order/list", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Order>> orderList(@Valid @RequestBody final ModelQuickSearch quickSearch){
        return modelList(quickSearch, orderService);
    }
}
