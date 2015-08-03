package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.Order;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.dto.OrderSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.service.OrderService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDateTime;

@Controller
@RequestMapping( produces = "application/json")
public class OrderController extends BaseJsonController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/order/search", method = RequestMethod.GET)
    public String orderSearch(Model model) {
        JsonResponse<OrderSearch> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, new OrderSearch());
        return super.outputJson(model, response, orderService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/order/search", method = RequestMethod.POST, consumes = "application/json")
    public String orderSearch(@Valid @RequestBody final OrderSearch orderSearch, Model model) {
        JsonResponse<Iterable<Order>> response = modelSearch(orderSearch, orderService, null);
        return super.outputJson(model, response, orderService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/order/new", method = RequestMethod.GET)
    public String orderCreate(Model model) {
        JsonResponse<Order> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, orderService.create());
        return super.outputJson(model, response, orderService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/order_item/new", method = RequestMethod.GET)
    public String orderItemCreate(Model model) {
        JsonResponse<OrderItem> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, orderService.createOrderItem());
        return super.outputJson(model, response, null);
    }

    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    public String orderDetails(@PathVariable int id, Model model) {
        JsonResponse<Order> response = modelDetails(id, orderService);
        return super.outputJson(model, response, orderService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/order/save", method = RequestMethod.POST, consumes = "application/json")
    public String orderSave(@Valid @RequestBody final Order order,Model model) {
        JsonResponse<Order>  response =  saveModel(order, orderService, null);
        return super.outputJson(model, response, orderService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/order/delete", method = RequestMethod.POST, consumes = "application/json")
    public String orderDelete(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, orderService);
        return super.outputJson(model, response, null);
    }
}
