package simpleshop.data.impl;

import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.ShipperDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Shipper;

import java.util.List;


@Repository
public class ShipperDAOImpl extends ModelDAOImpl<Shipper> implements ShipperDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<Shipper> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        return super.getList("SELECT sh FROM Shipper sh INNER JOIN sh.contact ct WHERE ct.name LIKE ?1 or ct.contactName LIKE ?1", pageInfo, keywords);
    }
}
