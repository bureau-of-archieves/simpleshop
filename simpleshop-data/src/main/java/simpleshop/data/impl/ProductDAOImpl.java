package simpleshop.data.impl;

import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.ProductDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Product;

import java.util.List;


@Repository
public class ProductDAOImpl extends ModelDAOImpl<Product> implements ProductDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        return super.getList("SELECT prod FROM Product prod WHERE prod.name LIKE ?1", pageInfo, keywords);
    }
}
