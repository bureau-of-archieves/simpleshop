package simpleshop.data.util;

import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.validator.constraints.URL;
import simpleshop.common.ReflectionUtils;
import simpleshop.common.StringUtils;
import simpleshop.data.metadata.*;
import simpleshop.domain.metadata.*;
import simpleshop.domain.metadata.validation.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.lang.reflect.Method;
import java.util.*;

public final class DomainUtils {

    private DomainUtils() {
    }

    /**
     * If obj is a proxy, return the proxied class.
     */
    public static Class<?> getProxiedClass(Object obj) {
        if (obj == null)
            return null;

        Class<?> realClass = obj.getClass();
        while (realClass.getSimpleName().indexOf('$') >= 0) {
            realClass = realClass.getSuperclass();
        }
        if (realClass == PersistentSet.class)
            return Set.class;

        if (realClass == PersistentBag.class)
            return List.class;

        return realClass;
    }


    private static volatile Map<String, ModelMetadata> modelMetadataMap;

    /**
     * This method can only be called once in MetadataServiceImpl constructor.
     *
     * @param classes all model classes, this is only know in service layer, as data layer does not depend on dto.
     * @return unmodifiable map for concurrent access.
     */
    public static Map<String, ModelMetadata> createModelMetadataMap(Class<?>[] classes) {

        if (modelMetadataMap != null)
            throw new UnsupportedOperationException("DomainUtils.createModelMetadataMap(Class<?>[]) can only be called once.");

        HashMap<String, ModelMetadata> metadataMap = new HashMap<>();
        for (Class<?> clazz : classes) {
            String modelName = clazz.getSimpleName();
            ModelMetadata modelMetadata = createModelMetadata(clazz);
            metadataMap.put(modelName, modelMetadata);
        }
        modelMetadataMap = Collections.unmodifiableMap(metadataMap);

        for (String modelName : modelMetadataMap.keySet()) {

            //setSearchable
            ModelMetadata modelMetadata = metadataMap.get(modelName);
            if (modelMetadata.getType() == ModelMetadata.ModelType.DOMAIN) {
                String searchModelName = modelName + "Search";
                if (metadataMap.containsKey(searchModelName))
                    modelMetadata.setSearchable(true);
            }

            //set returnTypeMetadata
            for (String fieldName : modelMetadata.getPropertyMetadataMap().keySet()) {
                PropertyMetadata PropertyMetadata = modelMetadata.getPropertyMetadataMap().get(fieldName);
                String possibleModelName = PropertyMetadata.getPropertyType();
                if (metadataMap.containsKey(possibleModelName)) {
                    PropertyMetadata.setReturnTypeMetadata(metadataMap.get(possibleModelName));
                }
            }
        }
        return modelMetadataMap;
    }

    public static ModelMetadata getModelMetadata(String modelName){
        if(modelMetadataMap != null && modelMetadataMap.containsKey(modelName))
            return modelMetadataMap.get(modelName);

        return null;
    }

    /**
     * Extract metadata of a class into an object.
     *
     * @param clazz class.
     * @return ModelMetadata object.
     */
    public static ModelMetadata createModelMetadata(Class<?> clazz) {

        ModelMetadata modelMetadata = new ModelMetadata();
        modelMetadata.setModelClass(clazz);
        modelMetadata.setName(clazz.getSimpleName());

        //set model type
        String fullName = clazz.getName();
        ModelMetadata.ModelType modelType;
        if (fullName.contains(".component.")) {
            modelType = ModelMetadata.ModelType.EMBEDDED;
        } else if (fullName.contains(".lookup.")) {
            modelType = ModelMetadata.ModelType.LOOKUP;
        } else if (fullName.contains(".domain.")) {
            modelType = ModelMetadata.ModelType.DOMAIN;
        } else {
            modelType = ModelMetadata.ModelType.DTO;
        }
        modelMetadata.setType(modelType);

        //set icon
        Icon icon = clazz.getAnnotation(Icon.class);
        if (icon != null) {
            modelMetadata.setIcon(icon.value());
        }

        //set alias annotations
        Stack<Class<?>> classes = new Stack<>();
        Class<?> superClass = clazz;
        while (superClass != Object.class){
            classes.push(superClass);
            superClass = superClass.getSuperclass();
        }
        List<AliasDeclaration> aliasDeclarations = new ArrayList<>();
        while (!classes.isEmpty()){
            superClass = classes.pop();
            AliasDeclaration aliasDeclaration = superClass.getAnnotation(AliasDeclaration.class);
            if(aliasDeclaration != null)
                aliasDeclarations.add(aliasDeclaration);
            AliasDeclaration.List aliasDeclarationList = superClass.getAnnotation(AliasDeclaration.List.class);
            if(aliasDeclarationList != null) {
                aliasDeclarations.addAll(Arrays.asList(aliasDeclarationList.value()));
            }
        }
        if (aliasDeclarations.size() > 0) {
            modelMetadata.setAliasDeclarations(Collections.unmodifiableList(aliasDeclarations));
        }

        //set property metadata
        Map<String, PropertyMetadata> propertyMap = new TreeMap<>();
        Set<String> noneSummaryProperties = new HashSet<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!ReflectionUtils.isPublicInstanceGetter(method))
                continue;

            PropertyMetadata propertyMetadata = extraPropertyMetadata(method);
            propertyMap.put(propertyMetadata.getPropertyName(), propertyMetadata);
            if(!propertyMetadata.isSummaryProperty() && !propertyMetadata.isIdProperty()){
                noneSummaryProperties.add(propertyMetadata.getPropertyName());
            }
        }
        modelMetadata.setPropertyMetadataMap(Collections.unmodifiableMap(propertyMap));
        modelMetadata.setNoneSummaryProperties(Collections.unmodifiableSet(noneSummaryProperties));

        return modelMetadata;
    }

    private static PropertyMetadata extraPropertyMetadata(Method method) {

        PropertyMetadata propertyMetadata;
        propertyMetadata = new PropertyMetadata();
        propertyMetadata.setPropertyName(StringUtils.getPropertyName(method.getName()));
        propertyMetadata.setPropertyType(method.getReturnType().getSimpleName());
        propertyMetadata.setGetter(method);

        //setIdProperty
        Id idAnnotation = method.getAnnotation(Id.class);
        if (idAnnotation != null)
            propertyMetadata.setIdProperty(true);
        EmbeddedId embeddedIdAnnotation = method.getAnnotation(EmbeddedId.class);
        if (embeddedIdAnnotation != null)
            propertyMetadata.setIdProperty(true);

        //insertable, updatable, length
        Column columnAnnotation = method.getAnnotation(Column.class);
        if (columnAnnotation != null) {
            propertyMetadata.setInsertable(columnAnnotation.insertable());
            propertyMetadata.setUpdatable(columnAnnotation.updatable());
            if(columnAnnotation.length() > 0)
                propertyMetadata.setMaxLength(columnAnnotation.length());
        }

        setLabel(propertyMetadata, method);
        setDescription(propertyMetadata, method);
        setDisplayFormat(propertyMetadata, method);
        setSummary(method, propertyMetadata);
        setWatermark(method, propertyMetadata);
        setBeanValidation(method, propertyMetadata);
        setPropertyFilters(method, propertyMetadata);

        return propertyMetadata;
    }

    private static void setLabel(PropertyMetadata PropertyMetadata, Method method) {
        Label labelAnnotation = method.getAnnotation(Label.class);
        if (labelAnnotation != null)
            PropertyMetadata.setLabel(labelAnnotation.value());
        else
            PropertyMetadata.setLabel(StringUtils.camelNameToFriendlyName(PropertyMetadata.getPropertyName()));
    }

    private static void setDescription(PropertyMetadata PropertyMetadata, Method method) {
        Description descriptionAnnotation = method.getAnnotation(Description.class);
        if (descriptionAnnotation != null) {
            PropertyMetadata.setDescription(descriptionAnnotation.value());
        }
    }

    private static void setDisplayFormat(PropertyMetadata PropertyMetadata, Method method) {
        DisplayFormat displayFormatAnnotation = method.getAnnotation(DisplayFormat.class);
        if (displayFormatAnnotation != null)
            PropertyMetadata.setDisplayFormat(displayFormatAnnotation.value());
        if(PropertyMetadata.getDisplayFormat() == null && "Date".equals(PropertyMetadata.getPropertyType())){
            PropertyMetadata.setDisplayFormat("date : 'yyyy-MM-dd'");
        }
    }

    private static void setSummary(Method method, PropertyMetadata propertyMetadata) {
        Summary summaryAnnotation = method.getAnnotation(Summary.class);
        if (summaryAnnotation != null)
            propertyMetadata.setSummaryProperty(true);
    }

    private static void setWatermark(Method method, PropertyMetadata propertyMetadata) {
        Watermark watermarkAnnotation = method.getAnnotation(Watermark.class);
        if (watermarkAnnotation != null)
            propertyMetadata.setWatermark(watermarkAnnotation.value());
    }

    private static void setBeanValidation(Method method, PropertyMetadata propertyMetadata) {
        NotNull notNullAnnotation = method.getAnnotation(NotNull.class);
        if (notNullAnnotation != null)
            propertyMetadata.setRequired(true);

        Pattern patternAnnotation = method.getAnnotation(Pattern.class);
        if (patternAnnotation != null)
            propertyMetadata.setInputFormat(patternAnnotation.regexp());

        Size sizeAnnotation = method.getAnnotation(Size.class);
        if (sizeAnnotation != null) {
            propertyMetadata.setMinLength(sizeAnnotation.min());
            propertyMetadata.setMaxLength(sizeAnnotation.max());
        }

        MinString minValue = method.getAnnotation(MinString.class);
        if (minValue != null) {
            propertyMetadata.setMin(minValue.value());
        } else {
            Min min = method.getAnnotation(Min.class);
            if (min != null) {
                propertyMetadata.setMin(String.valueOf(min.value()));
            } else {
                DecimalMin decimalMin = method.getAnnotation(DecimalMin.class);
                if (decimalMin != null) {
                    propertyMetadata.setMin(decimalMin.value());
                }
            }
        }

        MaxString maxValue = method.getAnnotation(MaxString.class);
        if (maxValue != null) {
            propertyMetadata.setMax(maxValue.value());
        } else {
            Max max = method.getAnnotation(Max.class);
            if (max != null) {
                propertyMetadata.setMax(String.valueOf(max.value()));
            } else {
                DecimalMax decimalMax = method.getAnnotation(DecimalMax.class);
                if (decimalMax != null) {
                    propertyMetadata.setMax(decimalMax.value());
                }
            }
        }

        URL url = method.getAnnotation(URL.class);
        if(url!=null && propertyMetadata.getDisplayFormat() == null){
            propertyMetadata.setDisplayFormat("url");
        }

        String propertyType = propertyMetadata.getPropertyType();
        if(propertyMetadata.getInputFormat() == null){
            if("BigDecimal".equals(propertyType)){
                propertyMetadata.setInputFormat("^\\d+(\\.\\d+)?$");
            } else if(propertyType.equals("Integer") || propertyType.equals("Long") || propertyType.contains("Date")){//date is expressed as an integer in JSON.
                propertyMetadata.setInputFormat("^[+-]*[1-9]\\d*$");
            }
        }
    }

    private static void setPropertyFilters(Method method, PropertyMetadata propertyMetadata) {
        List<PropertyFilter> propertyFilters = new ArrayList<>();
        PropertyFilter propertyFilter = method.getAnnotation(PropertyFilter.class);
        if(propertyFilter != null)
            propertyFilters.add(propertyFilter);
        PropertyFilter.List propertyFilterList = method.getAnnotation(PropertyFilter.List.class);
        if(propertyFilterList != null) {
            propertyFilters.addAll(Arrays.asList(propertyFilterList.value()));
        }

        if(propertyFilters.size() > 0){
            propertyFilters.sort(new PropertyFilter.Comparator());
            propertyMetadata.setPropertyFilters(Collections.unmodifiableList(propertyFilters));
        }
    }

}
