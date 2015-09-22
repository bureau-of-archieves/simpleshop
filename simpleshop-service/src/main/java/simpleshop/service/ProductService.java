package simpleshop.service;

import simpleshop.domain.model.Product;
import simpleshop.domain.model.component.ProductSupplier;
import simpleshop.dto.ProductSearch;
import simpleshop.service.infrastructure.ModelService;

public interface ProductService extends ModelService<Product, ProductSearch> {

    ProductSupplier createProductSupplier();
}
