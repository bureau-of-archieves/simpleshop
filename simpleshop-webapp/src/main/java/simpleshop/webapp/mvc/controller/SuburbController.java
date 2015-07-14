package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.domain.model.Suburb;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.SuburbService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;

@Controller
@RequestMapping(produces = "application/json")
public class SuburbController extends BaseJsonController {

    @Autowired
    private SuburbService suburbService;

    @RequestMapping(value = "/suburb/list", method = RequestMethod.POST, consumes = "application/json")
    public String suburbList(@Valid @RequestBody final ModelQuickSearch quickSearch, Model model){
        JsonResponse<Iterable<Suburb>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,suburbService.quickSearch(quickSearch.getKeywords(), quickSearch));
        return super.outputJson(model, response, null);
    }
}
