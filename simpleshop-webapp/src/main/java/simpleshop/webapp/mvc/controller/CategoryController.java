package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import simpleshop.domain.model.Category;
import simpleshop.webapp.infrastructure.BaseJsonController;

@Controller
@RequestMapping(produces = "application/json")
public class CategoryController extends BaseJsonController {

    //test url: http://localhost:8080/json/category/john
    @RequestMapping(value = "/category/{name}", method = RequestMethod.GET)
    public @ResponseBody Category test1(@PathVariable String name){
        Category category = new Category();
        category.setId(-1);
        category.setName(name);
        category.setDescription("Dummy category for testing");
        return category;
    }
}
