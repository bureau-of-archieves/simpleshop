package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.Category;
import simpleshop.dto.JsonResponse;
import simpleshop.webapp.mvc.controller.base.CategoryBaseController;

@Controller
@RequestMapping(produces = "application/json")
public class CategoryController extends CategoryBaseController {

    private static final String[] jsonIgnoreDropdown = {"parent"};

    @RequestMapping(value = "/category/dropdown", method = RequestMethod.GET, consumes = "application/json")
    public String categoryDropdown(Model model){
        JsonResponse<Iterable<Category>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,categoryService.getDropdownItems());
        return super.outputJson(model, response, jsonIgnoreDropdown);
    }

}
