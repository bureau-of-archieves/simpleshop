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
public abstract class BaseMetadataService extends BaseServiceImpl implements MetadataService {

    private final Class<?>[] classes;

    public BaseMetadataService() {

        this.classes = new Class<?>[]{
Category.class
,Address.class
,OrderItem.class
,ProductSupplier.class
,Contact.class
,Country.class
,Customer.class
,Employee.class
,ExchangeRate.class
,Order.class
,Product.class
,Shipper.class
,Suburb.class
,Supplier.class
,CategorySearch.class
,ContactSearch.class
,CustomerSearch.class
,EmployeeSearch.class
,ModelQuickSearch.class
,ModelSearch.class
,OrderSearch.class
,ProductSearch.class
,ShipperSearch.class
,SuburbSearch.class
,SupplierSearch.class
        };

    }

    @PostConstruct
    public void init() {
        DomainUtils.createModelMetadataMap(classes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, ModelMetadata> getMetadata() {
        return DomainUtils.getModelMetadata();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelMetadata getMetadata(String modelName) {
        return DomainUtils.getModelMetadata(modelName);
    }


}
