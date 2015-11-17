package simpleshop.business;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * Helper methods for testing rules.
 */
public final class RuleTestHelper {

    public static boolean hasError(Errors errors, String ruleName){
        for(ObjectError error : errors.getAllErrors()){
            if(ruleName.equals(error.getDefaultMessage()))
                return true;
        }
        return false;
    }
}
