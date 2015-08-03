package simpleshop.webapp.util;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.h2.value.ValueUuid;
import org.hibernate.id.GUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simpleshop.common.StringUtils;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyMetadata;
import simpleshop.service.MetadataService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: JOHNZ
 * Date: 21/10/14
 * Time: 3:37 PM
 */
@Component
public class Functions {

    private static Functions springBean;

    @Autowired
    private MetadataService metadataService;

    private static Pattern dateFormatPattern;

    static {
        dateFormatPattern = Pattern.compile("moment\\s*:\\s*'(.+)'");
    }

    @PostConstruct
    public void init() {
        springBean = this;
    }

    public static Object deNull(Object value, Object defaultValue) {
        if (value == null)
            return defaultValue;
        return value;
    }


    /**
     * Convert the string representation of modelId to its domain format.
     *
     * @param modelId   the model id.
     * @param modelName the model name.
     * @return currently will return whatever modelId argument is. As we only support a single argument now.
     */
    public static Object parseModelId(String modelId, String modelName) {
        ModelMetadata modelMetadata = getMetadata().get(modelName);
        if (modelMetadata == null)
            throw new IllegalArgumentException();

        for (PropertyMetadata PropertyMetadata : modelMetadata.getPropertyMetadataMap().values()) {
            if (PropertyMetadata.isIdProperty()) { //[convention]a model must have a single id column
                String fieldType = PropertyMetadata.getPropertyType();
                if ("Integer".equals(fieldType)) {
                    return new Integer(modelId);
                }

                if ("Long".equals(fieldType)) {
                    return new Long(modelId);
                }

                if ("BigDecimal".equals(fieldType)) {
                    return new BigDecimal(modelId);
                }

                if ("String".equals(fieldType)) {
                    String pattern = PropertyMetadata.getInputFormat();
                    if (!StringUtils.isNullOrEmpty(pattern)) {
                        Pattern regex = Pattern.compile(pattern);//strip of the / at the beginning and end which are required by ng-pattern.
                        if (!regex.matcher(modelId).matches())
                            throw new IllegalArgumentException();
                        return "\"" + modelId + "\"";
                    }

                }
                break;
            }
        }

        throw new NotImplementedException();//do not support id fieldType.


    }

    public static void validateViewId(String viewName, String viewId) {

        int index = viewId.indexOf(viewName + "-");
        if (index != 0)
            throw new RuntimeException("View Id '" + viewId + "' is invalid; it must begin with the view type '" + viewName + "'.");

        StringUtils.parseId(viewId.substring(viewName.length() + 1));
    }

    public static Map<String, ModelMetadata> getMetadata() {
        return springBean.metadataService.getMetadata();
    }

    public static ModelMetadata getMetadata(String modelName) {
        return springBean.metadataService.getMetadata().get(modelName);
    }

    public static PropertyMetadata getMetadata(String modelName, String propertyName) {

        propertyName = StringUtils.subStrB4(propertyName, ".");
        Map<String, ModelMetadata> metadata = getMetadata();
        if (metadata.containsKey(modelName)) {
            Map<String, PropertyMetadata> fields = metadata.get(modelName).getPropertyMetadataMap();
            if (fields.containsKey(propertyName)) {
                return fields.get(propertyName);
            }
        }
        return null;
    }

    public static Object push(ViewValueStackBean stack, String key, Object value) {
        stack.push(key, value);
        return value;
    }

    public static Object peek(ViewValueStackBean stack, String key) {
        return stack.peek(key);
    }

    public static Object pop(ViewValueStackBean stack, String key) {
        return stack.pop(key);
    }

    public static void _push(ViewValueStackBean stack, String key, Object value) {
        push(stack, key, value);
    }

    public static void _pop(ViewValueStackBean stack, String key) {
        pop(stack, key);
    }

    public static void raiseError(String errorMessage) {
        throw new RuntimeException(errorMessage);
    }

    public static String format(String message, Object arg) {
        return String.format(message, arg);
    }

    public static String combineDisplayFormat(String metadataFormat, String tagFormat) {
        if (StringUtils.isNullOrEmpty(tagFormat)) {
            return metadataFormat;
        }

        tagFormat = tagFormat.trim();
        if (!tagFormat.startsWith("|")) {
            return tagFormat;
        }

        if (StringUtils.isNullOrEmpty(metadataFormat)) {
            return tagFormat.substring(1);
        }

        return metadataFormat.trim() + " " + tagFormat;
    }

    public static String collectionModelName(PropertyMetadata propertyMetadata) {
        if (propertyMetadata.getReturnTypeMetadata() == null) {
            throw new RuntimeException("Property " + propertyMetadata.getPropertyName() + " has no return type metadata.");
        }

        ModelMetadata collectionMetadata = propertyMetadata.getReturnTypeMetadata();
        if (collectionMetadata.getType() != ModelMetadata.ModelType.COLLECTION) {
            throw new RuntimeException("Property " + propertyMetadata.getPropertyName() + " does not return a collection.");
        }

        PropertyMetadata metadata = collectionMetadata.getPropertyMetadataMap().get("elements");
        return metadata.getPropertyType();

    }

    public static String getDateFormatString(String displayFormat, String propertyType) {

        if (!StringUtils.isNullOrEmpty(displayFormat)) {
            Matcher matcher = dateFormatPattern.matcher(displayFormat);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }

        if (propertyType.endsWith("Date"))
            return "LL";
        else
            return "LLL";

    }

}
