package simpleshop.service;

import simpleshop.data.metadata.ModelMetadata;
import simpleshop.service.infrastructure.BaseService;

import java.util.Map;

/**
 * Created by JOHNZ on 1/06/2015.
 */
public interface MetadataService extends BaseService {

    /**
     * Get all metadata of the sponge domain models.
     * @return map of class metadata of all domain models.
     */
     Map<String, ModelMetadata> getMetadata();

    /**
     * Get the model metadata for the specified model name.
     * @param modelName model name in pascal case.
     * @return class metadata or null if not found.
     */
     ModelMetadata getMetadata(String modelName);

}
