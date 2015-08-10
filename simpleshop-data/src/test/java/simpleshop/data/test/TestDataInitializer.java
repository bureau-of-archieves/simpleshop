package simpleshop.data.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simpleshop.data.CategoryDAO;
import simpleshop.domain.model.*;
import simpleshop.domain.model.component.Address;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ensure test data is present.
 */
public class TestDataInitializer {


    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @PostConstruct
    public void init() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            createCountry("AUS", "Australia", "AUD", session);
            createCountry("USA", "United States", "USD", session);
            createCountry("CHN", "China", "CNY", session);

            Suburb wollong = createSuburb("Wollongong", null, "NSW", "2500", "AUS", session);
            createSuburb("Figtree", null, "NSW", "2525", "AUS", session);
            createSuburb("Gwynneville", null, "NSW", "2500", "AUS", session);
            createSuburb("Keiraville", null, "NSW", "2500", "AUS", session);

            Address address = new Address();
            address.setSuburb(wollong);
            address.setAddressLine1("63 Market St.");

            Map<String, String> billContacts = new HashMap<>();
            billContacts.put("Work Phone", "987654321");
            billContacts.put("Home Phone", "22 9384711");
            billContacts.put("Email", "bill@microsoft.com");
            createCustomer("Apple", "Steve Jobs", session);
            createCustomer("Microsoft", "Bill Gates", billContacts, null, session);
            createCustomer("Google", "Larry Page", session);
            createCustomer("Bill Gates", "The Great", null, address, session);

            createEmployee("Steve Ballmer", "The terrible", null, null, session);


            Category allProducts = createCategory("All Products", null, null, session);
            createCategory("Pharmaceutical", null, allProducts, session);
            createCategory("Food", null, allProducts, session);
            Category toy = createCategory("Toys", null, allProducts, session);

            createProduct("Race Car", toy, "1", session);

            session.getTransaction().commit();
        } catch (Throwable ex) {
            if (session.isOpen() && session.getTransaction() != null && session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw ex;
        } finally {
            if (session != null)
                session.close();
        }
    }

    private Suburb createSuburb(String name, String city, String state, String postcode, String countryCode, Session session) {
        List<Suburb> result = getSuburbListByName(name, session);
        if (result.size() > 0)
            return result.get(0);

        Suburb suburb = new Suburb();
        suburb.setSuburb(name);
        suburb.setState(state);
        suburb.setCity(city);
        suburb.setPostcode(postcode);
        Country country = new Country();
        country.setCountryCode(countryCode);
        suburb.setCountry(country);
        session.save(suburb);
        return suburb;
    }

    @SuppressWarnings("unchecked")
    private List<Suburb> getSuburbListByName(String name, Session session) {
        return session.createCriteria(Suburb.class).add(Restrictions.like("suburb", "%" + name + "%")).list();
    }

    private void createCustomer(String name, String contactName, Session session) {
        createCustomer(name, contactName, null, null, session);
    }

    private void createCustomer(String name, String contactName, Map<String, String> contactNumbers, Address address, Session session) {
        List<Customer> result = getCustomerListByName(name, session);
        if (result.size() == 0) {
            Customer customer = new Customer();
            customer.setContact(new Contact());
            customer.getContact().setName(name);
            customer.getContact().setContactName(contactName);
            customer.getContact().setContactNumbers(contactNumbers);
            customer.getContact().setAddress(address);
            session.save(customer);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Customer> getCustomerListByName(String name, Session session) {
        return session.createCriteria(Customer.class).createCriteria("contact").add(Restrictions.like("name", "%" + name + "%")).list();
    }

    private void createCountry(String countryCode, String name, String currency, Session session) {
        Country australia = (Country) session.get(Country.class, countryCode);
        if (australia == null) {
            australia = new Country();
            australia.setName(name);
            australia.setCountryCode(countryCode);
            australia.setCurrencySymbol(currency);
            session.save(australia);
        }
    }

    private Category createCategory(String name, String description, Category parent, Session session) {

        List<Category> categories = getCategoryListByName(name, session);
        if (categories.size() > 0)
            return categories.get(0);

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setParent(parent);

        session.save(category);
        return category;
    }

    @SuppressWarnings("unchecked")
    private List<Category> getCategoryListByName(String name, Session session) {
        return session.createCriteria(Category.class).add(Restrictions.like("name", "%" + name + "%")).list();
    }

    private void createEmployee(String name, String contactName, Map<String, String> contactNumbers, Address address, Session session) {
        List<Employee> result = getEmployeeListByName(name, session);
        if (result.size() == 0) {
            Employee employee = new Employee();
            employee.setContact(new Contact());
            employee.getContact().setName(name);
            employee.getContact().setContactName(contactName);
            employee.getContact().setContactNumbers(contactNumbers);
            employee.getContact().setAddress(address);
            session.save(employee);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Employee> getEmployeeListByName(String name, Session session) {
        return session.createCriteria(Employee.class).createCriteria("contact").add(Restrictions.like("name", "%" + name + "%")).list();
    }

    private void createProduct(String name, Category category, String quantityPerUnit, Session session) {
        List<Product> result = getProductListByName(name, session);
        if (result.size() == 0) {
            Product product = new Product();
            product.setName(name);
            product.setQuantityPerUnit(quantityPerUnit);
            product.getCategories().add(category);
            product.setStock(50);
            session.save(product);
        }

    }

    @SuppressWarnings("unchecked")
    private List<Product> getProductListByName(String name, Session session) {
        return session.createCriteria(Product.class).add(Restrictions.like("name", "%" + name + "%")).list();
    }

}
