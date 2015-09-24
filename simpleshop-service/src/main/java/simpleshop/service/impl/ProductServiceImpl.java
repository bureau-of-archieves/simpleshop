package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simpleshop.data.ProductDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Product;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.dto.ProductSearch;
import simpleshop.service.ProductService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class ProductServiceImpl extends ModelServiceImpl<Product, ProductSearch> implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    protected ModelDAO<Product> getModelDAO() {
        return productDAO;
    }

    @Override
    public Product create() {
        return new Product();
    }

    private static final ArrayList<String> LAZY_LOADED_PROPERTIES = new ArrayList<>();

    static {
        LAZY_LOADED_PROPERTIES.add("categories");
        LAZY_LOADED_PROPERTIES.add("images");
        LAZY_LOADED_PROPERTIES.add("productSuppliers");
    }

    @Override
    public ProductSupplier createProductSupplier(){
        return new ProductSupplier();
    }

    @Override
    public Collection<String> lazyLoadedProperties() {
        return LAZY_LOADED_PROPERTIES;
    }

    private static final String[] jsonIgnored = {"parent"}; //category.parent
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull
    String[] ignoredJsonProperties(){
        return jsonIgnored;
    }
}
