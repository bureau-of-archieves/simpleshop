package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.dto.JsonResponse;
import simpleshop.service.MetadataService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import java.util.Map;

@Controller
@RequestMapping(produces = "application/json")
public class MetadataController extends BaseJsonController {

    @Autowired
    private MetadataService metadataService;

    @RequestMapping(value = "/metadata", method = RequestMethod.GET)
    public String getMetadata(Model model) {
        JsonResponse<Map<String, ModelMetadata>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, metadataService.getMetadata());
        return super.outputJson(model, response, null);
    }

    @RequestMapping(value = "/metadata/{modelName}", method = RequestMethod.GET)
    public String getMetadata(@PathVariable String modelName, Model model) {
        JsonResponse<ModelMetadata> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, metadataService.getMetadata(modelName));
        return super.outputJson(model, response, null);
    }
}
