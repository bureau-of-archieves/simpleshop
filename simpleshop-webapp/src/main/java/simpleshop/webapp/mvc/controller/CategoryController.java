package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class CategoryController extends BaseJsonController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/category/search", method = RequestMethod.POST, consumes = "application/json")
    public String CategorySearch(@Valid @RequestBody final CategorySearch categorySearch, Model model, BindingResult bindingResult) {
        JsonResponse<Iterable<Category>> response = modelSearch(categorySearch, categoryService, bindingResult);

        //test code
        for(Category category : response.getContent()){
            category.setImagePath("category" + category.getId() + ".jpg");
        }

        return super.outputJson(model, response, categoryService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/category/list", method = RequestMethod.POST, consumes = "application/json")
    public String categoryList(@Valid @RequestBody final ModelQuickSearch quickSearch, Model model){
        JsonResponse<Iterable<Category>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,categoryService.quickSearch(quickSearch.getKeywords(), quickSearch));
        return super.outputJson(model, response, null);
    }

    private static final String[] jsonIgnoreDropdown = {"parent"};

    @RequestMapping(value = "/category/dropdown", method = RequestMethod.GET, consumes = "application/json")
    public String categoryDropdown(Model model){
        JsonResponse<Iterable<Category>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,categoryService.getDropdownItems());
        return super.outputJson(model, response, jsonIgnoreDropdown);
    }

    @RequestMapping(value = "/category/new", method = RequestMethod.GET)
    public String categoryCreate(Model model) {
        JsonResponse<Category> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, categoryService.create());
        return super.outputJson(model, response, categoryService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public String categoryDetails(@PathVariable int id, Model model) {
        JsonResponse<Category> response = modelDetails(id, categoryService);
        return super.outputJson(model, response, categoryService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/category/save", method = RequestMethod.POST, consumes = "application/json")
    public String categorySave(@Valid @RequestBody final Category category,Model model,  BindingResult bindingResult) {
        JsonResponse<Category>  response =  saveModel(category, categoryService, bindingResult);
        return super.outputJson(model, response, categoryService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/category/delete", method = RequestMethod.POST, consumes = "application/json")
    public String categoryDelete(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, categoryService);
        return super.outputJson(model, response, null);
    }

}
