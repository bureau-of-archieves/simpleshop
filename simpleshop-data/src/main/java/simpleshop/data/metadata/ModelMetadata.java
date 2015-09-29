package simpleshop.data.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import simpleshop.common.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Model metadata extracted from the domain model.
 * Some information is only available on the server side (i.e. json ignored).
 */
@JsonIgnoreProperties({"modelClass", "aliases", "sortProperties", "autoLoadProperties"})
public class ModelMetadata {

    public ModelMetadata(){}

    /**
     * Automatically set the name, type and modelClass fields.
     * @param modelClass which model class this metadata is for.
     */
    public ModelMetadata(Class<?> modelClass){

        this.modelClass = modelClass;
        this.name = modelClass.getSimpleName();

        String fullName = modelClass.getName();
        if (fullName.contains(".component.")) {
            this.type = ModelType.EMBEDDED;
        } else if (fullName.contains(".domain.")) {
            this.type = ModelType.DOMAIN;
        } else {
            this.type = ModelType.DTO;
        }
    }

    /**
     * Name of the model in Pascal casing.
     */
    private String name;

    /**
     * Icon name for the model in the format of [prefix:]icon_name.
     * If no prefix is specified the name indicate Bootstrap graph icon.
     */
    private String icon;

    /**
     * Model type, see {@link ModelType}.
     */
    private ModelType type;

    /**
     * Decide if this type of model can be listed in the search menu.
     */
    private boolean searchable;

    /**
     * Pipelined ajs filters converting a model instance to a string for display.
     */
    private String displayFormat;

    /**
     * An ajs expression string which will be passed to $interpolate service to convert a model instance to string.
     * If both interpolateFormat and displayFormat are set, interpolateFormat will be called first.
     * See {@link simpleshop.webapp.util.Functions#combineDisplayFormat(simpleshop.data.metadata.PropertyMetadata, java.lang.String).}
     */
    private String interpolateFormat;

    /**
     * Name of the id property.
     * [sponge]In Sponge framework all models should have a single property for the primary key.
     */
    private String idPropertyName;

    /**
     * Metadata for all properties.
     */
    private Map<String, PropertyMetadata> PropertyMetadataMap;

    //region Server side information

    private Class<?> modelClass;
    private Map<String, AliasDeclaration> aliases;
    private List<SortProperty> sortProperties;
    private Set<String> autoLoadProperties;

    //endregion

    /**
     * Get the metadata of a property.
     * @param path dot separated Path to the property from this model.
     * @return the property metadata and the defining model metadata.
     */
    public Pair<ModelMetadata, PropertyMetadata> getPathMetadata(String path){
        String[] parts = path.split("\\.");
        ModelMetadata modelMetadata = this;
        PropertyMetadata propertyMetadata = null;
        for (int i = 0; i < parts.length; i++) {
            if (modelMetadata == null)
                throw new RuntimeException("Failed to reflect property path: " + path);
            propertyMetadata = modelMetadata.getPropertyMetadataMap().get(parts[i]);
            if(i < parts.length - 1)
                modelMetadata = propertyMetadata.getReturnTypeMetadata();
        }
        return new Pair<>(modelMetadata, propertyMetadata);
    }

    /**
     * Get the metadata of the model which defines the property.
     * @param path the dot separated path to a property.
     * @return metadata of the owner model.
     */
    public ModelMetadata getPropertyOwnerMetadata(String path){
        Pair<ModelMetadata, PropertyMetadata> pair = getPathMetadata(path);
        return pair.getKey();
    }

    /**
     * Get the metadata of a property.
     * @param path the dot separated path to a property.
     * @return metadata of the property.
     */
    public PropertyMetadata getPropertyMetadata(String path) {
        Pair<ModelMetadata, PropertyMetadata> pair = getPathMetadata(path);
        return pair.getValue();
    }

    /**
     * Check if this model is search DTO, which has additional metadata.
     * @return true if this model is a search dto.
     */
    public boolean isSearchDTO(){
        return this.type == ModelType.DTO && this.name.endsWith("Search");
    }

    //region generated properties

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ModelType getType() {
        return type;
    }

    public void setType(ModelType type) {
        this.type = type;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public String getInterpolateFormat() {
        return interpolateFormat;
    }

    public void setInterpolateFormat(String interpolateFormat) {
        this.interpolateFormat = interpolateFormat;
    }

    public String getIdPropertyName() {
        return idPropertyName;
    }

    public void setIdPropertyName(String idPropertyName) {
        this.idPropertyName = idPropertyName;
    }

    public Map<String, PropertyMetadata> getPropertyMetadataMap() {
        return PropertyMetadataMap;
    }

    public void setPropertyMetadataMap(Map<String, PropertyMetadata> propertyMetadataMap) {
        PropertyMetadataMap = propertyMetadataMap;
    }

    /////////////////////////////// SERVER SIDE ONLY METADATA /////////////////////////////////////////////

    public Map<String, AliasDeclaration> getAliases() {
        return aliases;
    }

    public void setAliases(Map<String, AliasDeclaration> aliases) {
        this.aliases = aliases;
    }

    public List<SortProperty> getSortProperties() {
        return sortProperties;
    }

    public void setSortProperties(List<SortProperty> sortProperties) {
        this.sortProperties = sortProperties;
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

    public Set<String> getAutoLoadProperties() {
        return autoLoadProperties;
    }

    public void setAutoLoadProperties(Set<String> autoLoadProperties) {
        this.autoLoadProperties = autoLoadProperties;
    }

//endregion

}
