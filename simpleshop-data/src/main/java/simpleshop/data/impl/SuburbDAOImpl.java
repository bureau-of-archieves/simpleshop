package simpleshop.data.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.SuburbDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Suburb;

import java.io.Serializable;
import java.util.List;


@Repository
public class SuburbDAOImpl extends ModelDAOImpl<Suburb> implements SuburbDAO {

    @Override
    public Suburb load(Serializable id) {
        return super.load(Suburb.class, id);
    }

    @Override
    public Suburb get(Serializable id) {
        return super.get(Suburb.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Suburb> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        Query query = super.createQuery("SELECT sub FROM Suburb sub WHERE sub.suburb LIKE ?1 or sub.postcode like ?1", pageInfo, keywords);
        return query.list();
    }
}
