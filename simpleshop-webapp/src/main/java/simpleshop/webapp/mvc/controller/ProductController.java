package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.dto.JsonResponse;
import simpleshop.webapp.mvc.controller.base.ProductBaseController;

@Controller
@RequestMapping(produces = "application/json")
public class ProductController extends ProductBaseController {

    @RequestMapping(value = "/product_supplier/new", method = RequestMethod.GET)
    public String orderItemCreate(Model model) {
        JsonResponse<ProductSupplier> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, productService.createProductSupplier());
        return super.outputJson(model, response, null);
    }

}
