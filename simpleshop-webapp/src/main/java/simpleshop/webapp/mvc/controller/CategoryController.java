package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import simpleshop.domain.model.Category;
import simpleshop.dto.JsonResponse;
import simpleshop.webapp.mvc.controller.base.CategoryBaseController;

@Controller
@RequestMapping(produces = "application/json")
public class CategoryController extends CategoryBaseController {

    @RequestMapping(value = "/category/dropdown", method = RequestMethod.GET, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Category>> categoryDropdown(){
        return JsonResponse.createSuccess(categoryService.getDropdownItems());
    }

}
