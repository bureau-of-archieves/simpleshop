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
import simpleshop.webapp.mvc.controller.base.CategoryBaseController;

import javax.validation.Valid;
import java.io.Serializable;

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
