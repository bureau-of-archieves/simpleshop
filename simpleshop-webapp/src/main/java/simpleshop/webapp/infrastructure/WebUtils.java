package simpleshop.webapp.infrastructure;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * Utility class for the web layer.
 */
public final class WebUtils {

    public static String getBindingErrorMessage(MessageSource messageSource, BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectError error : bindingResult.getAllErrors()) {
            stringBuilder.append(error.getObjectName());
            stringBuilder.append(":");
            stringBuilder.append(messageSource.getMessage(error, LocaleContextHolder.getLocale()));
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }
}
