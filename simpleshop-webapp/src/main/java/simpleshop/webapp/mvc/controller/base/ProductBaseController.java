
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import simpleshop.domain.model.Product;
import simpleshop.dto.ProductSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.ProductService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public abstract class ProductBaseController extends BaseJsonController<Product> {

    @Autowired
    protected ProductService productService;

    @RequestMapping(value = "/product/search", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<ProductSearch> productSearch() {
        JsonResponse<ProductSearch> jsonResponse = JsonResponse.createSuccess(new ProductSearch());
        populateSortInfoList(ProductSearch.class, jsonResponse);
        return jsonResponse;
    }

    @RequestMapping(value = "/product/search", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Product>> productSearch(@Valid @RequestBody final ProductSearch productSearch, BindingResult bindingResult) {
        return modelSearch(productSearch, bindingResult, productService);
    }

    @RequestMapping(value = "/product/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Product> productCreate() {
        return modelCreate(productService);
    }

    @RequestMapping(value = "/product/save", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Product> productSave(@Valid @RequestBody final Product product, BindingResult bindingResult) {
        return modelSave(product, bindingResult, productService);
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Product> productDetails(@PathVariable int id) {
        return modelDetails(id, productService);
    }

    @RequestMapping(value = "/product/delete", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Serializable> productDelete(@Valid @RequestBody final int id) {
        return modelDelete(id, productService);
    }

    @RequestMapping(value = "/product/list", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Product>> productList(@Valid @RequestBody final ModelQuickSearch quickSearch){
        return modelList(quickSearch, productService);
    }
}
