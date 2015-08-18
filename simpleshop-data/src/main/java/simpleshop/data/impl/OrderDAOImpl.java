package simpleshop.data.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.OrderDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Order;

import java.io.Serializable;
import java.util.List;

@Repository
public class OrderDAOImpl extends ModelDAOImpl<Order> implements OrderDAO {

    @Override
    public Order load(Serializable id) {
        return super.load(Order.class, id);
    }

    @Override
    public Order get(Serializable id) {
        return super.get(Order.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> quickSearch(String keywords, PageInfo pageInfo) {
        keywords = StringUtils.wrapLikeKeywords(keywords);
        Query query = super.createQuery("SELECT ord FROM Order ord INNER JOIN ord.customer c INNER JOIN c.contact ct WHERE ct.name LIKE ?1 or ct.contactName LIKE ?1 or ord.shipName like ?1", pageInfo, keywords);
        return query.list();
    }
}
