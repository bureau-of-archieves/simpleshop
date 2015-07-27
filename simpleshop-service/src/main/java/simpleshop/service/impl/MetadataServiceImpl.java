package simpleshop.service.impl;

import org.springframework.stereotype.Service;
import simpleshop.common.*;
import simpleshop.data.metadata.*;
import simpleshop.data.util.*;
import simpleshop.domain.metadata.*;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.Address;
import simpleshop.domain.model.component.OrderItem;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.dto.*;
import simpleshop.service.*;
import simpleshop.service.infrastructure.impl.BaseServiceImpl;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

/**
 * Get metadata of the domain models.
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
                Address.class,
                OrderItem.class,
                ProductSupplier.class
        };

    }

    @PostConstruct
    public void init() {
        DomainUtils.createModelMetadataMap(classes);
    }

    @Override
    public Map<String, ModelMetadata> getMetadata() {
        return DomainUtils.getModelMetadata();
    }

    @Override
    public ModelMetadata getMetadata(String modelName) {
        return DomainUtils.getModelMetadata(modelName);
    }


}
