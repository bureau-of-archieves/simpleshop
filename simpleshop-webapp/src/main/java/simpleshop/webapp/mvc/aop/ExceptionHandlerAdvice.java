package simpleshop.webapp.mvc.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import simpleshop.dto.JsonResponse;

import java.util.Arrays;

@ControllerAdvice("simpleshop.webapp.mvc.controller")
public class ExceptionHandlerAdvice {

    @Autowired
    private ObjectMapper objectMapper;

    private static MultiValueMap<String, String> ERROR_RESPONSE_HEADER;

    static {
        ERROR_RESPONSE_HEADER = new LinkedMultiValueMap<>();
        ERROR_RESPONSE_HEADER.put(HttpHeaders.CONTENT_TYPE, Arrays.asList("application/json".split(",")));
    }

    private ResponseEntity<String> returnError(Throwable ex) {

        JsonResponse<?> jsonResponse = JsonResponse.createError(ex);
        try {                                                      //todo change this to: write as string is not good objectMapper.writeValue();
            return new ResponseEntity<>(objectMapper.writeValueAsString(jsonResponse), ERROR_RESPONSE_HEADER, HttpStatus.BAD_REQUEST);
        }catch (JsonProcessingException ex2) {
            return new ResponseEntity<>(ex2.getLocalizedMessage(), ERROR_RESPONSE_HEADER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler({ServletRequestBindingException.class})
    public ResponseEntity<String> returnErrorMessages(ServletRequestBindingException ex) {
        return returnError(ex);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> returnErrorMessages(RuntimeException ex) {
        return returnError(ex);
    }

}
