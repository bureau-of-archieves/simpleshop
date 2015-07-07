package simpleshop.data.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JOHNZ
 * Date: 20/10/14
 * Time: 2:19 PM
 */
@JsonIgnoreProperties({"getter", "returnType", "returnTypeMetadata", "propertyFilters"})
public class PropertyMetadata {

    /**
     * The name of the northwind object property in camel case name.
     */
    private String propertyName;

    /**
     * The Java simple type name of the field.
     */
    private String propertyType = "";

    /**
     * true if this property is a part of the primary key.
     */
    private boolean idProperty = false;

    /**
     * true if this property is a party of summary (list) view.
     * If false this property will be cleared if the northwind object is retrieved as a property value or in a collection.
     */
    private boolean summaryProperty = false;


    /**
     * true if this property cannot be null from the client's point of view.
     */
    private boolean required = false;

    /**
     * true if this property can be updated from the client's point of view.
     */
    private boolean updatable = true;

    /**
     * true if this property can be inserted from the client's point of view.
     */
    private boolean insertable = true;

    /**
     * Label text of this property.
     */
    private String label;

    /**
     * Description text for this property that might be displayed as a tooltip in the GUI.
     */
    private String description;

    /**
     * The watermark text to show in the input text field for this property.
     */
    private String watermark;

    /**
     * A list of AJS filters to apply when display this field.
     */
    private String displayFormat;

    /**
     * A regex that the user text input for this property must conform to.
     */
    private String inputFormat;

    /**
     * Minimum length of this field.
     */
    private Integer minLength;

    /**
     * Maximum length of this field.
     */
    private Integer maxLength;

    /**
     * The string representation of min value. Will be converted to the target datatype before comparing.
     */
    private String min;

    /**
     * The min value is exclusive.
     */
    private boolean minExclusive = false;

    /**
     * The string representation of max value. Will be converted to the target datatype before comparing.
     */
    private String max;

    /**
     * The max value is exclusive.
     */
    private boolean maxExclusive = false;

    //the properties below are not sent to the client side.

    private Method getter;
    private Class<?> returnType;
    private  ModelMetadata returnTypeMetadata;
    private List<PropertyFilter> propertyFilters;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public boolean isIdProperty() {
        return idProperty;
    }

    public void setIdProperty(boolean idProperty) {
        this.idProperty = idProperty;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSummaryProperty() {
        return summaryProperty;
    }

    public void setSummaryProperty(boolean summaryProperty) {
        this.summaryProperty = summaryProperty;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public ModelMetadata getReturnTypeMetadata() {
        return returnTypeMetadata;
    }

    public void setReturnTypeMetadata(ModelMetadata returnTypeMetadata) {
        this.returnTypeMetadata = returnTypeMetadata;
    }

    public List<PropertyFilter> getPropertyFilters() {
        return propertyFilters;
    }

    public void setPropertyFilters(List<PropertyFilter> propertyFilters) {
        this.propertyFilters = propertyFilters;
    }

    public boolean isMinExclusive() {
        return minExclusive;
    }

    public void setMinExclusive(boolean minExclusive) {
        this.minExclusive = minExclusive;
    }

    public boolean isMaxExclusive() {
        return maxExclusive;
    }

    public void setMaxExclusive(boolean maxExclusive) {
        this.maxExclusive = maxExclusive;
    }

    public Class<?> getReturnType(){
        if(getGetter() != null)
            return getGetter().getReturnType();

        if(getReturnTypeMetadata() != null)
            return getReturnTypeMetadata().getModelClass();

        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
