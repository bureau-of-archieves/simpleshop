package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simpleshop.data.ProductDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Product;
import simpleshop.dto.ProductSearch;
import simpleshop.service.ProductService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

@Service
public class ProductServiceImpl extends ModelServiceImpl<Product, ProductSearch> implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    protected ModelDAO getModelDAO() {
        return productDAO;
    }

    @Override
    public Product create() {
        return new Product();
    }

}
