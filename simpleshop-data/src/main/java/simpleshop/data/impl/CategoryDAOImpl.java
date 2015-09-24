package simpleshop.data.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.CategoryDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Category;

import java.io.Serializable;
import java.util.List;


@Repository
public class CategoryDAOImpl extends ModelDAOImpl<Category> implements CategoryDAO {

    @Override
    public Category load(Serializable id) {
        return super.load(Category.class, id);
    }

    @Override
    public Category get(Serializable id) {
        return super.get(Category.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        Query query = super.createQuery("SELECT cat FROM Category cat WHERE lower(cat.name) LIKE ?1 or lower(cat.description) LIKE ?1", pageInfo, keywords.toLowerCase());
        return query.list();

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Category> getDropdownItems(int maxSize) {

        PageInfo pageInfo = new PageInfo(0, maxSize);

        Query query = super.createQuery("SELECT cat FROM Category cat WHERE NOT EXISTS(FROM Category cat2 WHERE cat2.parent = cat)", pageInfo);
        return query.list();
    }
}
