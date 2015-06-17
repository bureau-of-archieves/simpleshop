package simpleshop.data.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.ShipperDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Shipper;

import java.io.Serializable;
import java.util.List;


@Repository
public class ShipperDAOImpl extends ModelDAOImpl<Shipper> implements ShipperDAO {

    @Override
    public Shipper load(Serializable id) {
        return super.load(Shipper.class, id);
    }

    @Override
    public Shipper get(Serializable id) {
        return super.get(Shipper.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Shipper> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        Query query = super.createQuery("SELECT sh FROM Shipper sh INNER JOIN sh.contact ct WHERE ct.name LIKE ?1 or ct.contactName LIKE ?1", pageInfo, keywords);
        return query.list();

    }
}
