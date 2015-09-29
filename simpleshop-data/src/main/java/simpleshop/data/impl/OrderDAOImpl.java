package simpleshop.data.impl;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.OrderDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Order;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderDAOImpl extends ModelDAOImpl<Order> implements OrderDAO {

    @Override
    public void save(Order domainObject) {
        if(domainObject.getId() == null){
            if(domainObject.getOrderDate() == null){
                domainObject.setOrderDate(LocalDateTime.now());
            }
        }
        super.save(domainObject);
    }

    @Override
    public void delete(Order domainObject) {
        if(domainObject.getCustomer() != null){
            List<Order> orderList = domainObject.getCustomer().getOrders();
            if(orderList != null && Hibernate.isInitialized(orderList))
                orderList.remove(domainObject);
        }
        super.delete(domainObject);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> quickSearch(String keywords, PageInfo pageInfo) {
        keywords = StringUtils.wrapLikeKeywords(keywords);
        return super.getList("SELECT ord FROM Order ord INNER JOIN ord.customer c INNER JOIN c.contact ct WHERE ct.name LIKE ?1 or ct.contactName LIKE ?1 or ord.shipName like ?1", pageInfo, keywords);
    }

}
