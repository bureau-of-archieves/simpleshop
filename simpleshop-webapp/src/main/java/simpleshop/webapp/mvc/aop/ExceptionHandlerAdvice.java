package simpleshop.webapp.mvc.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import simpleshop.dto.JsonResponse;
import simpleshop.webapp.infrastructure.WebUtils;

@ControllerAdvice("simpleshop.webapp.mvc.controller")
public class ExceptionHandlerAdvice {

    private ModelAndView returnErrorMessage(String errorMessage){

        JsonResponse<?> jsonResponse = JsonResponse.createError(errorMessage, null);

        ModelAndView modelAndView = new ModelAndView("json");
        modelAndView.addObject("content", jsonResponse);
        return modelAndView;
    }

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ModelAndView returnErrorMessages(MethodArgumentNotValidException ex){

        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = WebUtils.getBindingErrorMessage(messageSource, bindingResult);
        return returnErrorMessage(errorMessage);
    }

    @ExceptionHandler({ServletRequestBindingException.class})
    public ModelAndView returnErrorMessages(ServletRequestBindingException ex){

        return returnErrorMessage(ex.getLocalizedMessage());
    }

    @ExceptionHandler({Throwable.class})
    public ModelAndView returnErrorMessages(Throwable ex){

        return returnErrorMessage(ex.getLocalizedMessage());
    }

}
