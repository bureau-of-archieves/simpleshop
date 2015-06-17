package simpleshop.data.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.PageInfo;
import simpleshop.data.SupplierDAO;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Supplier;

import java.io.Serializable;
import java.util.List;


@Repository
public class SupplierDAOImpl extends ModelDAOImpl<Supplier> implements SupplierDAO {

    @Override
    public Supplier load(Serializable id) {
        return super.load(Supplier.class, id);
    }

    @Override
    public Supplier get(Serializable id) {
        return super.get(Supplier.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Supplier> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        Query query = super.createQuery("SELECT su FROM Supplier su INNER JOIN su.contact ct WHERE ct.name LIKE ?1 or ct.contactName LIKE ?1", pageInfo, keywords);
        return query.list();

    }
}
