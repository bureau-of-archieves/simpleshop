

package simpleshop.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.CategoryDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Category;
import simpleshop.dto.CategorySearch;
import simpleshop.service.CategoryService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

public abstract class CategoryBaseService extends ModelServiceImpl<Category, CategorySearch> implements CategoryService {

    @Autowired
    protected CategoryDAO categoryDAO;

    @Override
    protected ModelDAO<Category> getModelDAO() {
        return categoryDAO;
    }

    @Override
    public Category create() {
        return new Category();
    }

}
