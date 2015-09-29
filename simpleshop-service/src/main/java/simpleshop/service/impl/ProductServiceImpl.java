package simpleshop.service.impl;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import simpleshop.domain.model.Product;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.service.ProductService;
import simpleshop.service.impl.base.ProductBaseService;

import javax.validation.constraints.NotNull;

@Service
public class ProductServiceImpl extends ProductBaseService implements ProductService {

    @Override
    protected void initialize(@NotNull Product model) {
        Hibernate.initialize(model.getImages());
        Hibernate.initialize(model.getProductSuppliers());
    }

    @Override
    public ProductSupplier createProductSupplier(){
        return new ProductSupplier();
    }

}
