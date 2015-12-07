package simpleshop.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import simpleshop.Constants;
import simpleshop.common.StringUtils;
import simpleshop.data.SortInfo;
import simpleshop.domain.model.Category;
import simpleshop.dto.CategorySearch;
import simpleshop.service.CategoryService;
import simpleshop.service.impl.base.CategoryBaseService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class CategoryServiceImpl extends CategoryBaseService implements CategoryService {


    @Override
    protected void initialize(@NotNull Category model) {
        categoryDAO.initialize(model.getParent()); //load direct parent as well.
    }

    /**
     * Update search prefix to the correct value.
     * @param model Category instance to save..
     */
    @Transactional
    @Override
    public void save(@NotNull Category model) {

        super.save(model);

        String parentPrefix = "";
        if(model.getParent() != null){
            parentPrefix = model.getParent().getPrefix();
        }
        model.setPrefix(parentPrefix + "_" + model.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Category> search(@NotNull CategorySearch searchParams) {
        //default sort order
        searchParams.addSortInfo(new SortInfo("prefix"));

        List<Category> categories = super.search(searchParams);
        categories.forEach(category -> category.setParent(null));
//        boolean leaveIdOnly = StringUtils.isNullOrEmpty(searchParams.getName());
//        stripParent(categories, leaveIdOnly);
        return categories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Category> getDropdownItems() {

        List<Category> categories = categoryDAO.getDropdownItems(Constants.MAX_DROPDOWN_LIST_SIZE);
        for (Object obj : categories){
            resolveObjectGraph(obj);
            getModelDAO().detach(obj);
        }
        return categories;
    }

    /**
     * Null out parent's fields..
     * @param categories list of category.
     */
//    private void stripParent(List<Category> categories, boolean leaveIdOnly) {
//        for(Category category : categories){
//            Category parent = category.getParent();
//            if(parent != null){
//                category.setParent(new Category());
//                category.getParent().setId(parent.getId());
//                if(!leaveIdOnly){
//                    category.getParent().setName(parent.getName());
//                    category.getParent().setDescription(parent.getDescription());
//                    category.getParent().setImagePath(parent.getImagePath());
//                }
//            }
//        }
//    }
}
