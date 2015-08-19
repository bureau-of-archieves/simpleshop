package simpleshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.common.StringUtils;
import simpleshop.data.CategoryDAO;
import simpleshop.data.SortInfo;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Category;
import simpleshop.dto.CategorySearch;
import simpleshop.service.CategoryService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class CategoryServiceImpl extends ModelServiceImpl<Category, CategorySearch> implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    protected ModelDAO<Category> getModelDAO() {
        return categoryDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category create() {
        return new Category();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Category> search(@NotNull CategorySearch searchParams) {
        //default sort order
        searchParams.setSortInfo(new SortInfo("name"));

        List<Category> categories = super.search(searchParams);

        boolean leaveIdOnly = StringUtils.isNullOrEmpty(searchParams.getName());
        stripParent(categories, leaveIdOnly);
        return categories;
    }

    /**
     * Null out parent's fields..
     * @param categories list of category.
     */
    private void stripParent(List<Category> categories, boolean leaveIdOnly) {
        for(Category category : categories){
            Category parent = category.getParent();
            if(parent != null){
                category.setParent(new Category());
                category.getParent().setId(parent.getId());
                if(!leaveIdOnly){
                    category.getParent().setName(parent.getName());
                    category.getParent().setDescription(parent.getDescription());
                    category.getParent().setImagePath(parent.getDescription());
                }
            }
        }
    }
}
