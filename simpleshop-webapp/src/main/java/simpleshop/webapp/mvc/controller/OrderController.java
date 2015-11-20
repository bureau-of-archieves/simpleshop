package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import simpleshop.domain.model.Order;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ShoppingCart;
import simpleshop.webapp.mvc.controller.base.OrderBaseController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping( produces = "application/json")
public class OrderController extends OrderBaseController {

    @Resource(name = "businessValidator")
    private Validator businessValidator;

    @InitBinder("cartItem")
    public void initBinder(WebDataBinder binder){
        binder.addValidators(businessValidator);
    }

    @RequestMapping(value = "/order_item/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<OrderItem> orderItemCreate() {
        return JsonResponse.createSuccess(orderService.createOrderItem());
    }

    @RequestMapping(value = "/order/process", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Order> orderProcess(@Valid @RequestBody final ShoppingCart cart, BindingResult bindingResult) {
        //todo take the cart and create order.
        //todo then the client script will fetch this order
        //todo add remove item from cart function
        throw new RuntimeException("Not implemented");
    }
}
