package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.Country;
import simpleshop.dto.JsonResponse;
import simpleshop.webapp.mvc.controller.base.SuburbBaseController;

import javax.validation.Valid;

@Controller
@RequestMapping(produces = "application/json")
public class SuburbController extends SuburbBaseController {

    @RequestMapping(value = "/countries", method = RequestMethod.GET, consumes = "application/json")
    public String suburbList(Model model){
        JsonResponse<Iterable<Country>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,suburbService.getCountries());
        return super.outputJson(model, response, null);
    }

}
