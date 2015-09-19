
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public abstract class CategoryBaseController extends BaseJsonController {

    @Autowired
    protected CategoryService categoryService;

    @RequestMapping(value = "/category/search", method = RequestMethod.GET)
    public String categorySearch(Model model) {
        JsonResponse<CategorySearch> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, new CategorySearch());
        return super.outputJson(model, response, categoryService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/category/search", method = RequestMethod.POST, consumes = "application/json")
    public String categorySearch(@Valid @RequestBody final CategorySearch categorySearch, BindingResult bindingResult, Model model) {
        JsonResponse<Iterable<Category>> response = modelSearch(categorySearch, categoryService, bindingResult);
        return super.outputJson(model, response, categoryService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/category/new", method = RequestMethod.GET)
    public String categoryCreate(Model model) {
        JsonResponse<Category> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, categoryService.create());
        return super.outputJson(model, response, categoryService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/category/save", method = RequestMethod.POST, consumes = "application/json")
    public String categorySave(@Valid @RequestBody final Category category, BindingResult bindingResult, Model model) {
        JsonResponse<Category>  response =  saveModel(category, categoryService, bindingResult);
        return super.outputJson(model, response, categoryService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public String categoryDetails(@PathVariable int id, Model model) {
        JsonResponse<Category> response = modelDetails(id, categoryService);
        return super.outputJson(model, response, categoryService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/category/delete", method = RequestMethod.POST, consumes = "application/json")
    public String categoryDelete(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, categoryService);
        return super.outputJson(model, response, null);
    }

    @RequestMapping(value = "/category/list", method = RequestMethod.POST, consumes = "application/json")
    public String categoryList(@Valid @RequestBody final ModelQuickSearch quickSearch, Model model){
        JsonResponse<Iterable<Category>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,categoryService.quickSearch(quickSearch.getKeywords(), quickSearch));
        return super.outputJson(model, response, null);
    }
}
