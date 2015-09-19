package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import simpleshop.webapp.mvc.controller.base.ProductBaseController;

@Controller
@RequestMapping(produces = "application/json")
public class ProductController extends ProductBaseController {

}
