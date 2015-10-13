package simpleshop.webapp.mvc.controller;

import org.springframework.web.bind.annotation.*;
import simpleshop.dto.CartItem;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ShoppingCart;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Entry point for shopping cart related logic.
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResponse<String> addToCart(HttpSession session, @Valid @RequestBody final CartItem item){

        //todo check if product id is valid
        ShoppingCart cart = (ShoppingCart)session.getAttribute("shoppingCart");
        if(cart == null){
            cart = new ShoppingCart();
            session.setAttribute("shoppingCart", cart);
        }

        cart.getItems().add(item);
        return JsonResponse.createSuccess("OK");
    }

    @RequestMapping("get")
    public JsonResponse<ShoppingCart> getCart(HttpSession session){
        ShoppingCart cart = (ShoppingCart)session.getAttribute("shoppingCart");
        return JsonResponse.createSuccess(cart);
    }


}
