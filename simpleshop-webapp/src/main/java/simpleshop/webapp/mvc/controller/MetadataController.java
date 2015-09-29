package simpleshop.webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.dto.JsonResponse;
import simpleshop.service.MetadataService;

import java.util.Map;

@Controller
@RequestMapping(produces = "application/json")
public class MetadataController{

    @Autowired
    private MetadataService metadataService;

    @RequestMapping(value = "/metadata", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Map<String, ModelMetadata>> getMetadata() {
        return JsonResponse.createSuccess(metadataService.getMetadata());
    }

    @RequestMapping(value = "/metadata/{modelName}", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<ModelMetadata> getMetadata(@PathVariable String modelName) {
        return JsonResponse.createSuccess(metadataService.getMetadata(modelName));
    }
}
