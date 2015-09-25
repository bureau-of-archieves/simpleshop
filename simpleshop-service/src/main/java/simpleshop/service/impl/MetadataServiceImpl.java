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
 * //todo code generation base class to inject all domain classes.
 */
@Service
public class MetadataServiceImpl extends BaseServiceImpl implements MetadataService {

    private final Class<?>[] classes;

    public MetadataServiceImpl() {

        this.classes = new Class<?>[]{
                Category.class,
                Contact.class,
                Country.class,
                Customer.class,
                Employee.class,
                ExchangeRate.class,
                Order.class,
                Product.class,
                Shipper.class,
                Suburb.class,
                Supplier.class,
                CustomerSearch.class,
                ShipperSearch.class,
                SupplierSearch.class,
                EmployeeSearch.class,
                OrderSearch.class,
                ProductSearch.class,
                SupplierSearch.class,
                CategorySearch.class,
                SuburbSearch.class,
                Address.class,
                OrderItem.class,
                ProductSupplier.class
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
