package simpleshop.data.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
import java.util.Set;


@JsonIgnoreProperties({"modelClass", "aliasDeclarations", "searchable"})
public class ModelMetadata {

    private String name;
    private String icon;
    private ModelType type;
    private Map<String, PropertyMetadata> PropertyMetadataMap;
    private Set<String> noneSummaryProperties;

    private List<AliasDeclaration> aliasDeclarations;
    private Class<?> modelClass;
    private boolean searchable = false;

    public Map<String, PropertyMetadata> getPropertyMetadataMap() {
        return PropertyMetadataMap;
    }

    public void setPropertyMetadataMap(Map<String, PropertyMetadata> PropertyMetadataMap) {
        this.PropertyMetadataMap = PropertyMetadataMap;
    }

    public PropertyMetadata getPropertyMetadata(String path){
        String[] parts = path.split("\\.");
        ModelMetadata modelMetadata = this;
        PropertyMetadata PropertyMetadata = null;
        for(int i=0; i< parts.length; i++){
            PropertyMetadata = modelMetadata.getPropertyMetadataMap().get(parts[i]);
            modelMetadata = PropertyMetadata.getReturnTypeMetadata();
        }
        return PropertyMetadata;
    }

    public Set<String> getNoneSummaryProperties() {
        return noneSummaryProperties;
    }

    public void setNoneSummaryProperties(Set<String> noneSummaryProperties) {
        this.noneSummaryProperties = noneSummaryProperties;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
    }

    public static enum ModelType{

        DOMAIN, DTO, LOOKUP, EMBEDDED
    }

    public List<AliasDeclaration> getAliasDeclarations() {
        return aliasDeclarations;
    }

    public void setAliasDeclarations(List<AliasDeclaration> aliasDeclarations) {
        this.aliasDeclarations = aliasDeclarations;
    }
}
