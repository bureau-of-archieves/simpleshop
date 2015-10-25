package simpleshop.webapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import simpleshop.common.Pair;
import simpleshop.common.StringUtils;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.ModelType;
import simpleshop.data.metadata.PropertyMetadata;
import simpleshop.service.MetadataService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Map;
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

    @Resource(name = "messageSource")
    private MessageSource messageSource;

    private static final Pattern dateFormatPattern1;
    private static final Pattern dateFormatPattern2;

    static {
        dateFormatPattern1 = Pattern.compile("moment\\s*:\\s*'([^']+)'");
        dateFormatPattern2 = Pattern.compile("moment\\s*:\\s*\"([^\"]+)\"");
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

        String idPropertyName = modelMetadata.getIdPropertyName();
        PropertyMetadata PropertyMetadata = modelMetadata.getPropertyMetadata(idPropertyName);

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

        throw new NotImplementedException();//do not support id fieldType.

    }

    public static void validateViewId(String viewName, String viewId) {

        int index = viewId.indexOf(viewName + "-");
        if (index != 0)
            throw new RuntimeException("View Id '" + viewId + "' is invalid; it must begin with the view type '" + viewName + "'.");

        StringUtils.parseId(viewId.substring(viewName.length() + 1));
    }

    public static boolean isSubViewTypeOf(String viewType, String rootViewType){
        if(viewType == null || viewType.length() == 0)
            return false;

        return viewType.equals(rootViewType) || viewType.startsWith(rootViewType + "_");
    }

    public static void throwIfNotNull(Throwable ex) throws Throwable {
        if(ex != null)
            throw ex;
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
        try {
            return stack.peek(key);
        } catch (EmptyStackException ex){
            throw new SpongeConfigurationException("Cannot find key in page value stack: " + key, ex);
        }
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

    public static String combineDisplayFormat(PropertyMetadata propertyMetadata, String tagFormat) {

        StringBuilder format = new StringBuilder();

        if(propertyMetadata.getReturnTypeMetadata() != null){
            String interpolateFormat = propertyMetadata.getReturnTypeMetadata().getInterpolateFormat();
            if(!StringUtils.isNullOrEmpty(interpolateFormat)){
                String targetModelName = propertyTargetModelName(propertyMetadata);
                format.append("interpolate:'");
                format.append(targetModelName);
                format.append("'"); //will read interpolateFormat from client side metadata.
            }
        }

        String combinedFormat = combineDisplayFormat(propertyMetadata.getDisplayFormat(), tagFormat);
        if(!StringUtils.isNullOrEmpty(combinedFormat)){
           if(format.length() != 0){
               format.append("|");
           }
            format.append(combinedFormat);
        }
        return format.toString();
    }

    private static String combineDisplayFormat(String metadataFormat, String tagFormat){

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
        if (collectionMetadata.getType() != ModelType.COLLECTION) {
            throw new RuntimeException("Property " + propertyMetadata.getPropertyName() + " does not return a collection.");
        }

        PropertyMetadata metadata = collectionMetadata.getPropertyMetadataMap().get("elements");
        return metadata.getPropertyType();

    }

    public static String getDateFormatString(String displayFormat, String propertyType) {

        if (!StringUtils.isNullOrEmpty(displayFormat)) {
            Matcher matcher = dateFormatPattern1.matcher(displayFormat);
            if (matcher.find()) { //grab the datetime format from the moment filter
                return matcher.group(1);
            }
            matcher = dateFormatPattern2.matcher(displayFormat);
            if (matcher.find()) { //grab the datetime format from the moment filter
                return matcher.group(1);
            }
        }

        if (propertyType.endsWith("Date"))
            return "LL";
        else
            return "LLL";

    }

    public static String propertyTargetModelName(PropertyMetadata propertyMetadata){
        if(propertyMetadata.getReturnTypeMetadata() == null)
            return StringUtils.camelNameToPascalName(propertyMetadata.getPropertyName());
        else
            return propertyMetadata.getReturnTypeMetadata().getName();
    }

    public static String msg(String code, Object[] args){
        return springBean.messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static List<Pair<String, String>> getOptions(Class<Enum> clazz){

        List<Pair<String, String>> options = new ArrayList<>();
        Enum[] values = clazz.getEnumConstants();
        String codePrefix = "enum." + clazz.getSimpleName() + ".";
        for(Enum val : values){
            Pair<String, String> pair = new Pair<>();
            pair.setKey(val.toString());
            try{
                String value = springBean.messageSource.getMessage(codePrefix + pair.getKey(), null, LocaleContextHolder.getLocale());
                pair.setValue(value);
            } catch(NoSuchMessageException ex){
                pair.setValue(pair.getKey().replaceAll("_", " "));
            }
            options.add(pair);
        }

        return options;
    }

    public static String userLocale(){

        return LocaleContextHolder.getLocale().toString().toLowerCase().replaceAll("_", "-");

    }
}
