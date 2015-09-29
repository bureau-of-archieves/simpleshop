
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import simpleshop.domain.model.Category;
import simpleshop.dto.CategorySearch;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.CategoryService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public abstract class CategoryBaseController extends BaseJsonController<Category> {

    @Autowired
    protected CategoryService categoryService;

    @RequestMapping(value = "/category/search", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<CategorySearch> categorySearch() {
        return JsonResponse.createSuccess(new CategorySearch());
    }

    @RequestMapping(value = "/category/search", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Category>> categorySearch(@Valid @RequestBody final CategorySearch categorySearch, BindingResult bindingResult) {
        return modelSearch(categorySearch, bindingResult, categoryService);
    }

    @RequestMapping(value = "/category/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Category> categoryCreate() {
        return modelCreate(categoryService);
    }

    @RequestMapping(value = "/category/save", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Category> categorySave(@Valid @RequestBody final Category category, BindingResult bindingResult) {
        return modelSave(category, bindingResult, categoryService);
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Category> categoryDetails(@PathVariable int id) {
        return modelDetails(id, categoryService);
    }

    @RequestMapping(value = "/category/delete", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Serializable> categoryDelete(@Valid @RequestBody final int id) {
        return modelDelete(id, categoryService);
    }

    @RequestMapping(value = "/category/list", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Category>> categoryList(@Valid @RequestBody final ModelQuickSearch quickSearch){
        return modelList(quickSearch, categoryService);
    }
}
