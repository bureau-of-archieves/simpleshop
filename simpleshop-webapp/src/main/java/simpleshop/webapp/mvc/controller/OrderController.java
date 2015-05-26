package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import simpleshop.webapp.infrastructure.BaseJsonController;

@Controller
@RequestMapping(value = "/json", produces = "application/json")
public class OrderController extends BaseJsonController {
}
