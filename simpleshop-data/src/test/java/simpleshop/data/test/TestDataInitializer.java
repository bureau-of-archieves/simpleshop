package simpleshop.data.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.Country;
import simpleshop.domain.model.Customer;
import simpleshop.domain.model.Suburb;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Ensure test data is present.
 */
public class TestDataInitializer {


    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @PostConstruct
    public void init(){
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            createCountry("AUS", "Australia", "AUD", session);
            createCountry("USA", "United States", "USD", session);
            createCountry("CHN", "China", "CNY", session);

            createCustomer("Bill Gates", "CEO", session);
            createCustomer("Microsoft", "Bill Gates", session);
            createCustomer("Google", "Larry Page", session);

            createSuburb("Wollongong", null, "NSW", "2500", "AUS", session);
            createSuburb("Figtree", null, "NSW", "2525", "AUS", session);
            createSuburb("Gwynneville", null, "NSW", "2500", "AUS", session);
            createSuburb("Keiraville", null, "NSW", "2500", "AUS", session);

            session.getTransaction().commit();
        }catch(Throwable ex){
            if(session.isOpen() && session.getTransaction() != null && session.getTransaction().isActive())
            session.getTransaction().rollback();
        }finally {
            if(session != null)
                session.close();
        }
    }

    private void createSuburb(String name, String city, String state, String postcode, String countryCode, Session session){
        List<Suburb> result = getSuburbListByName(name, session);
        if(result.size() == 0){
            Suburb suburb = new Suburb();
            suburb.setSuburb(name);
            suburb.setState(state);
            suburb.setCity(city);
            suburb.setPostcode(postcode);
            Country country = new Country();
            country.setCountryCode(countryCode);
            suburb.setCountry(country);
            session.save(suburb);
        }
    }

    private void createCustomer(String name, String contactName, Session session) {
        List<Customer> result = getCustomerListByName(name, session);
        if(result.size() == 0){
            Customer customer = new Customer();
            customer.setContact(new Contact());
            customer.getContact().setName(name);
            customer.getContact().setContactName(contactName);
            session.save(customer);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Suburb> getSuburbListByName(String name, Session session){
        return session.createCriteria(Suburb.class).add(Restrictions.eq("suburb", name)).list();
    }

    @SuppressWarnings("unchecked")
    private List<Customer> getCustomerListByName(String name, Session session) {
        return session.createCriteria(Customer.class).createCriteria("contact").add(Restrictions.eq("name", name)).list();
    }

    private void createCountry(String countryCode, String name, String currency, Session session) {
        Country australia = (Country)session.get(Country.class, countryCode);
        if(australia == null){
            australia = new Country();
            australia.setName(name);
            australia.setCountryCode(countryCode);
            australia.setCurrencySymbol(currency);
            session.save(australia);
        }
    }
}
