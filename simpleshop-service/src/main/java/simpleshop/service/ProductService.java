package simpleshop.service;

import simpleshop.domain.model.Product;
import simpleshop.dto.ProductSearch;
import simpleshop.service.infrastructure.ModelService;

public interface ProductService extends ModelService<Product, ProductSearch> {

}
