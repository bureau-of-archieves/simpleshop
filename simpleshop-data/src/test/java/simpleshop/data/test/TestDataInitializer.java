package simpleshop.data.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.domain.model.Contact;
import simpleshop.domain.model.Country;
import simpleshop.domain.model.Customer;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Ensure test data is present.
 */
public class TestDataInitializer {

    @Autowired
    private SessionFactory sessionFactory;

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
            deleteRambo(session);

            session.getTransaction().commit();
        }catch(Throwable ex){
            if(session.isOpen() && session.getTransaction() != null && session.getTransaction().isActive())
            session.getTransaction().rollback();
        }finally {
            if(session != null)
                session.close();
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

    private void deleteRambo(Session session) {
        List<Customer> result = getCustomerListByName("Rambo", session);
        for(Customer customer : result){
            session.delete(customer);
        }
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
