

package simpleshop.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.ProductDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Product;
import simpleshop.dto.ProductSearch;
import simpleshop.service.ProductService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

public abstract class ProductBaseService extends ModelServiceImpl<Product, ProductSearch> implements ProductService {

    @Autowired
    protected ProductDAO productDAO;

    @Override
    protected ModelDAO<Product> getModelDAO() {
        return productDAO;
    }

    @Override
    public Product create() {
        return new Product();
    }

}
