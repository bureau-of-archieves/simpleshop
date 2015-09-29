package simpleshop.data.impl;

import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.CustomerDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Customer;

import java.util.List;



@Repository
public class CustomerDAOImpl extends ModelDAOImpl<Customer> implements CustomerDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<Customer> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        return super.getList("SELECT c FROM Customer c INNER JOIN c.contact ct WHERE ct.name LIKE ?1 or ct.contactName LIKE ?1", pageInfo, keywords);
    }
}
