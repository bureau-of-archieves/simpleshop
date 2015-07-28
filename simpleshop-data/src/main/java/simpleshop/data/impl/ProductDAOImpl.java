package simpleshop.data.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.ProductDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Product;

import java.io.Serializable;
import java.util.List;


@Repository
public class ProductDAOImpl extends ModelDAOImpl<Product> implements ProductDAO {

    @Override
    public Product load(Serializable id) {
        return super.load(Product.class, id);
    }

    @Override
    public Product get(Serializable id) {
        return super.get(Product.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        Query query = super.createQuery("SELECT prod FROM Product prod WHERE prod.name LIKE ?1", pageInfo, keywords);
        return query.list();

    }
}
