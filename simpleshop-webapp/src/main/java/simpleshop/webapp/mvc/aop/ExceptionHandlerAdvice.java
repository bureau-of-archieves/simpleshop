package simpleshop.webapp.mvc.aop;


import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import simpleshop.common.StringUtils;
import simpleshop.dto.JsonResponse;
import simpleshop.webapp.infrastructure.BaseJsonController;

@ControllerAdvice("simpleshop.webapp.mvc.controller")
public class ExceptionHandlerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ModelAndView returnErrorMessages(MethodArgumentNotValidException ex){

        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = BaseJsonController.getBindingErrorMessage(bindingResult);
        JsonResponse<?> jsonResponse = new JsonResponse<>(JsonResponse.STATUS_ERROR, errorMessage, null);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("content", jsonResponse);
        modelAndView.addObject("excludedFields", StringUtils.emptyArray());
        modelAndView.setViewName("json");
        return modelAndView;
    }

    @ExceptionHandler({ServletRequestBindingException.class})
    public ModelAndView returnErrorMessages(ServletRequestBindingException ex){

        return null;

    }

    @ExceptionHandler({Exception.class})
    public ModelAndView returnErrorMessages(Exception ex){

        return null;

    }

}
