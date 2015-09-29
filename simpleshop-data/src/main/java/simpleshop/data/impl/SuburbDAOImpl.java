package simpleshop.data.impl;

import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.SuburbDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Suburb;

import java.util.List;


@Repository
public class SuburbDAOImpl extends ModelDAOImpl<Suburb> implements SuburbDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<Suburb> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        return super.getList("SELECT sub FROM Suburb sub WHERE sub.suburb LIKE ?1 or sub.postcode like ?1", pageInfo, keywords);
    }
}
