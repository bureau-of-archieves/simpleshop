package simpleshop.data.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.CustomerDAO;
import simpleshop.data.PageInfo;
import simpleshop.domain.model.Customer;

import java.io.Serializable;
import java.util.List;



@Repository
public class CustomerDAOImpl extends ModelDAOImpl<Customer> implements CustomerDAO {

    @Override
    public Customer load(Serializable id) {
        return super.load(Customer.class, id);
    }

    @Override
    public Customer get(Serializable id) {
        return super.get(Customer.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Customer> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        Query query = super.createQuery("SELECT c FROM Customer c INNER JOIN c.contact ct WHERE ct.name LIKE ?1 or ct.contactName LIKE ?1", pageInfo, keywords);
        return query.list();

    }
}
