package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import simpleshop.business.BusinessValidator;
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

    @Autowired
    private BusinessValidator businessValidator;

    @InitBinder("cartItem")
    public void initBinder(WebDataBinder binder){
        binder.addValidators(businessValidator);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResponse<String> addToCart(HttpSession session, @Valid @RequestBody final CartItem cartItem){

        ShoppingCart cart = (ShoppingCart)session.getAttribute("shoppingCart");
        if(cart == null){
            cart = new ShoppingCart();
            session.setAttribute("shoppingCart", cart);
        }

        cart.getItems().add(cartItem);
        return JsonResponse.createSuccess("OK");
    }

    @RequestMapping("get")
    public JsonResponse<ShoppingCart> getCart(HttpSession session){
        ShoppingCart cart = (ShoppingCart)session.getAttribute("shoppingCart");
        return JsonResponse.createSuccess(cart);
    }


}
