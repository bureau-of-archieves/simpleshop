package simpleshop.webapp.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import simpleshop.domain.model.Country;
import simpleshop.dto.JsonResponse;
import simpleshop.webapp.mvc.controller.base.SuburbBaseController;

@Controller
@RequestMapping(produces = "application/json")
public class SuburbController extends SuburbBaseController {

    @RequestMapping(value = "/countries", method = RequestMethod.GET, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Country>> countryList(){
        return JsonResponse.createSuccess(suburbService.getCountries());
    }

}
