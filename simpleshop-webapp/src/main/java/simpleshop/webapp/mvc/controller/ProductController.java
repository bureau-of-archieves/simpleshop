package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.dto.JsonResponse;
import simpleshop.webapp.mvc.controller.base.ProductBaseController;

@Controller
@RequestMapping(produces = "application/json")
public class ProductController extends ProductBaseController {

    @RequestMapping(value = "/product_supplier/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<ProductSupplier> orderItemCreate() {
        return JsonResponse.createSuccess(productService.createProductSupplier());
    }

}
