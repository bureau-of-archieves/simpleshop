package simpleshop.webapp.mvc.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import simpleshop.dto.JsonResponse;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

@ControllerAdvice("simpleshop.webapp.mvc.controller")
public class ExceptionHandlerAdvice extends JsonViewResponseBodyAdvice {

    private static MultiValueMap<String, String> ERROR_RESPONSE_HEADER;

    static {
        ERROR_RESPONSE_HEADER = new LinkedMultiValueMap<>();
        ERROR_RESPONSE_HEADER.set(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    private ResponseEntity<JsonResponse<?>> returnError(Throwable ex) {

        JsonResponse<?> jsonResponse = JsonResponse.createError(ex);
        return new ResponseEntity<>(jsonResponse, ERROR_RESPONSE_HEADER, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ServletRequestBindingException.class})
    public ResponseEntity<JsonResponse<?>> returnErrorMessages(ServletRequestBindingException ex) {
        return returnError(ex);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<JsonResponse<?>> returnErrorMessages(RuntimeException ex) {
        return returnError(ex);
    }

}
