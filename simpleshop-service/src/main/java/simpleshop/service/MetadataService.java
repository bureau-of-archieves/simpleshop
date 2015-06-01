package simpleshop.service;

import simpleshop.data.metadata.ModelMetadata;

import java.util.Map;

/**
 * Created by JOHNZ on 1/06/2015.
 */
public interface MetadataService extends BaseService{

    /**
     * Get all metadata of the sponge domain models.
     * @return map of class metadata of all domain models.
     */
    public Map<String, ModelMetadata> get();

    /**
     * Get the model metadata for the specified model name.
     * @param modelName model name in pascal case.
     * @return class metadata or null if not found.
     */
    public ModelMetadata get(String modelName);

    /**
     * Extract the properties marked as @ItemText.
     * @param domainObject the domain object used as a select item.
     * @return
     */
    public String extractItemText(Object domainObject);

    /**
     * Extract the properties marked as @ItemValue.
     * @param domainObject the domain object used as a select item.
     * @return
     */
    public String extractItemValue(Object domainObject);
}
