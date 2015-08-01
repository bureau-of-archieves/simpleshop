package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.Product;
import simpleshop.dto.ProductSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.service.ProductService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public class ProductController extends BaseJsonController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/product/search", method = RequestMethod.GET)
    public String productSearch(Model model) {
        JsonResponse<ProductSearch> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, new ProductSearch());
        return super.outputJson(model, response, productService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/product/search", method = RequestMethod.POST, consumes = "application/json")
    public String productSearch(@Valid @RequestBody final ProductSearch productSearch, Model model, BindingResult bindingResult) {
        JsonResponse<Iterable<Product>> response = modelSearch(productSearch, productService, bindingResult);
        return super.outputJson(model, response, productService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/product/new", method = RequestMethod.GET)
    public String productCreate(Model model) {
        JsonResponse<Product> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, productService.create());
        return super.outputJson(model, response, productService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String productDetails(@PathVariable int id, Model model) {
        JsonResponse<Product> response = modelDetails(id, productService);
        return super.outputJson(model, response, productService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/product/save", method = RequestMethod.POST, consumes = "application/json")
    public String productSave(@Valid @RequestBody final Product product,Model model,  BindingResult bindingResult) {
        JsonResponse<Product>  response =  saveModel(product, productService, bindingResult);
        return super.outputJson(model, response, productService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/product/delete", method = RequestMethod.POST, consumes = "application/json")
    public String productDelete(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, productService);
        return super.outputJson(model, response, null);
    }
}
