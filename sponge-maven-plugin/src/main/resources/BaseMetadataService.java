@args List<String> modelNames;
package simpleshop.service.impl;

import org.springframework.stereotype.Service;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.util.DomainUtils;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.Address;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.dto.*;
import simpleshop.service.MetadataService;
import simpleshop.service.infrastructure.impl.BaseServiceImpl;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Get metadata of the domain models.
 */
@@Service
public abstract class BaseMetadataService extends BaseServiceImpl implements MetadataService {

    private final Class<?>[] classes;

    public BaseMetadataService() {

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
