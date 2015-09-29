package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.dto.JsonResponse;
import simpleshop.webapp.mvc.controller.base.OrderBaseController;

@Controller
@RequestMapping( produces = "application/json")
public class OrderController extends OrderBaseController {

    @RequestMapping(value = "/order_item/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<OrderItem> orderItemCreate() {
        return JsonResponse.createSuccess(orderService.createOrderItem());
    }
}
