@args String projectName, List<String> modelNames;
@{String basePackage = projectName.toLowerCase()}
package simpleshop.service.impl.base;

import @(basePackage).data.metadata.ModelMetadata;
import @(basePackage).data.util.DomainUtils;
import @(basePackage).domain.model.*;
import @(basePackage).domain.model.component.Address;
import @(basePackage).domain.model.component.OrderItem;
import @(basePackage).domain.model.component.ProductSupplier;
import @(basePackage).dto.*;
import @(basePackage).service.MetadataService;
import @(basePackage).service.infrastructure.impl.BaseServiceImpl;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Get metadata of the domain models.
 */
public abstract class MetadataBaseService extends BaseServiceImpl implements MetadataService {

    private final Class<?>[] classes;

    public MetadataBaseService() {

        this.classes = new Class<?>[]{
            @for(int i=0; i<modelNames.size(); i++) {
                @if(i != 0){,}@modelNames.get(i)
            }
        };

    }

    @@PostConstruct
    public void init() {
        DomainUtils.createModelMetadataMap(classes);
    }

    /**
     * {@@inheritDoc}
     */
    @@Override
    public Map<String, ModelMetadata> getMetadata() {
        return DomainUtils.getModelMetadata();
    }

    /**
     * {@@inheritDoc}
     */
    @@Override
    public ModelMetadata getMetadata(String modelName) {
        return DomainUtils.getModelMetadata(modelName);
    }


}
