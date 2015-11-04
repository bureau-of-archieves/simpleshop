package simpleshop.service.impl;

import org.springframework.stereotype.Service;
import simpleshop.domain.model.Category;
import simpleshop.domain.model.Product;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.service.ProductService;
import simpleshop.service.impl.base.ProductBaseService;

import javax.validation.constraints.NotNull;

@Service
public class ProductServiceImpl extends ProductBaseService implements ProductService {

    @Override
    protected void initialize(@NotNull Product model) {
        productDAO.initialize(model.getImages());
        productDAO.initialize(model.getProductSuppliers());

        for(Category category : model.getCategories()){
            category.setParent(null);
        }
    }

    @Override
    public ProductSupplier createProductSupplier(){
        return new ProductSupplier();
    }

}
