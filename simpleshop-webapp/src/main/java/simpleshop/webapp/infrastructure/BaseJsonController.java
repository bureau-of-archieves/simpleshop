package simpleshop.webapp.infrastructure;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import simpleshop.common.StringUtils;
import simpleshop.dto.JsonResponse;

/**
 * The base controller for all controllers that return json data.
 * User: JOHNZ
 * Date: 10/09/14
 * Time: 5:13 PM
 */

public abstract class BaseJsonController {

    protected String outputJson(Model model, JsonResponse<?> response, String[] excludedFields) {
        if (excludedFields == null)
            excludedFields = StringUtils.emptyArray();

        model.addAttribute("content", response);
        model.addAttribute("excludedFields", excludedFields);
        return "json"; //call the json view.
    }


//    protected <T> JsonResult<T> saveModel(T model, ModelService<T, ?> modelService, BindingResult bindingResult){
//        JsonResult<T> response = new JsonResult<T>(null);
//        response.setOperation("save");
//
//        //handle binding errors
//        if (bindingResult.hasErrors()) {
//            response.setStatus("validation_error");
//            response.setDescription(getBindingErrorMessage(bindingResult));
//        } else {
//            //handle service error
//            try{
//            modelService.save(model);
//                response.setStatus("success");
//            }catch (Throwable ex){
//                response.setStatus("failed");
//                response.setDescription(ex.getMessage());
//            }
//            response.setDomainObject(model); //id is set for new object, some other properties could change as well.
//        }
//
//        return response;
//    }
//
    protected String getBindingErrorMessage(BindingResult bindingResult){
        StringBuilder stringBuilder = new StringBuilder();
        for(ObjectError error : bindingResult.getAllErrors()){
            stringBuilder.append(error.getObjectName());
            stringBuilder.append(":");
            stringBuilder.append(error.getDefaultMessage());
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }

}
