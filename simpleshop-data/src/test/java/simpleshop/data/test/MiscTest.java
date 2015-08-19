package simpleshop.data.test;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.PageInfo;
import simpleshop.data.SuburbDAO;
import simpleshop.domain.model.Customer;
import simpleshop.domain.model.Order;
import simpleshop.domain.model.Shipper;
import simpleshop.domain.model.Suburb;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * A unit test dedicated to verify current Hibernate behaviour.
 */
public class MiscTest extends TransactionalTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private SuburbDAO suburbDAO;

    @Test
    @SuppressWarnings("unchecked")
    public void accessEmbeddedObjectPropertyTest(){

        List<Suburb> suburbs = suburbDAO.quickSearch(TestConstants.SUBURB_AUS_2, new PageInfo());
        assertThat(suburbs.size(), equalTo(1));

        Suburb wollongong = suburbs.get(0);

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Customer.class);
        criteria.createCriteria("contact", "ct").add(Restrictions.eq("address.suburb", wollongong));
        List<Customer> customers = criteria.list();
        assertThat(customers.size(), greaterThanOrEqualTo(1));

        for(Customer customer : customers){
            assertThat(customer.getContact().getAddress().getSuburb(), sameInstance(wollongong));
        }
    }


    @Test
    @SuppressWarnings("unchecked")
    public void accessEmbeddedPropertyTest() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Customer.class, "c");
        Criteria criteria1 = criteria.createCriteria("contact", "ct");
        Criteria criteria2 = criteria1.createCriteria("address.suburb", "sb");
        criteria2.add(Restrictions.ilike("sb.suburb", "%" + TestConstants.SUBURB_AUS_2 + "%"));
        List<Customer> customers = criteria.list();
        assertThat(customers.size(), greaterThanOrEqualTo(1));
        for(Customer customer : customers){
            assertThat(customer.getContact().getAddress().getSuburb().getSuburb(), containsString(TestConstants.SUBURB_AUS_2));
        }
    }


    @Test
    @SuppressWarnings("unchecked")
    public void criteriaSubQueryTest(){

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Customer.class, "c");

        DetachedCriteria subQuery = DetachedCriteria.forClass(Order.class, "sub1");
        subQuery.setProjection(Property.forName("id"));

        DetachedCriteria subQuery2 = DetachedCriteria.forClass(Customer.class, "sub2").add(Restrictions.eqProperty("id", "c.id")).createCriteria("orders", "ord").setProjection(Property.forName("ord.id"));
        subQuery.add(Subqueries.propertyIn("id", subQuery2));
        subQuery.add(Restrictions.sizeGe("orderItems", 2));

        criteria.add(Subqueries.exists(subQuery));
        List<Customer> customers = criteria.list();
        assertThat(customers.size(), greaterThanOrEqualTo(1));

    }

    @Test
    public void mapValueTest() {
        Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(Customer.class);
        Criteria ct = criteria.createCriteria("contact", "ct").createCriteria("contactNumbers", "cn");
        Criterion criterion = Restrictions.like("cn.elements", TestConstants.WORK_PHONE_NUMBER_1);
        criteria.add(criterion);
        List result = criteria.list();
        assertThat(result.size(), greaterThanOrEqualTo(1));

    }

}
