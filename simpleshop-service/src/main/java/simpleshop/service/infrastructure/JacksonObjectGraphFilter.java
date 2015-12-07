package simpleshop.service.infrastructure;

import simpleshop.common.AnnotationObjectGraphFilter;
import simpleshop.common.StringUtils;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.util.DomainUtils;
import java.lang.reflect.Method;

/**
 * Object graph filter which also ignores @JsonIgnore properties.
 */
public class JacksonObjectGraphFilter extends AnnotationObjectGraphFilter{

    @Override
    public boolean shouldIgnore(Object target, Method method) {

        Class<?> realClass = DomainUtils.getProxiedClass(target);
        ModelMetadata modelMetadata = DomainUtils.getModelMetadata(realClass.getSimpleName());
        if(modelMetadata != null && modelMetadata.getJsonIgnoreProperties().contains(StringUtils.getPropertyName(method.getName()))){
            return true;
        }
        return super.shouldIgnore(target, method);
    }
}
