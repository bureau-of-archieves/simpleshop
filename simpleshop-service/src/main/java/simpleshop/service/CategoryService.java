package simpleshop.service;


import simpleshop.domain.model.Category;
import simpleshop.dto.CategorySearch;
import simpleshop.service.infrastructure.ModelService;

import java.util.List;

public interface CategoryService extends ModelService<Category, CategorySearch> {

    /**
     * Dropdown list items.
     * @return list of categories that has no children.
     */
    List<Category> getDropdownItems();
}
