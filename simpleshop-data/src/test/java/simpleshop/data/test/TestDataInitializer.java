package simpleshop.data.test;

import junit.framework.Test;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simpleshop.data.CategoryDAO;
import simpleshop.domain.model.*;
import simpleshop.domain.model.Order;
import simpleshop.domain.model.component.Address;
import simpleshop.domain.model.component.OrderItem;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

            seedData(session);

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

    /**
     * Create the test data.
     *
     * @param session an opened session.
     */
    private void seedData(Session session) {

        createCountry("AUS", "Australia", "AUD", session);
        createCountry("USA", "United States", "USD", session);
        createCountry("CHN", "China", "CNY", session);
        createCountry("DEU", "Germany", "EUR", session);
        session.flush();

        Suburb wollongong = createSuburb(TestConstants.SUBURB_AUS_2, null, "NSW", TestConstants.SUBURB_AUS_2_POSTCODE, "AUS", session);
        createSuburb("Figtree", null, "NSW", "2525", "AUS", session);
        createSuburb(TestConstants.SUBURB_AUS_1, null, "NSW", TestConstants.SUBURB_AUS_1_POSTCODE, "AUS", session);
        createSuburb("Gwynneville", null, "NSW", "2500", "AUS", session);
        createSuburb("Keiraville", null, "NSW", "2500", "AUS", session);
        Suburb linzi = createSuburb("Linzi", "ZIBO", "Shandong", "255400", "CHN", session);
        createSuburb("Zhifu", "Yantai", "Shandong", "264005", "CHN", session);
        createSuburb("Manhattan", "New York", "NY", "10001", "USA", session);
        createSuburb("Brooklyn", "New York", "NY", "11201", "USA", session);
        session.flush();

        Customer customer1 = createCustomer(TestConstants.CUSTOMER_NAME_1, "Larry Page", session);
        createCustomer("Apple", "Steve Jobs", session);

        Map<String, String> billContacts = new HashMap<>();
        billContacts.put("Work Phone", TestConstants.WORK_PHONE_NUMBER_1);
        billContacts.put("Home Phone", "987654321");
        billContacts.put("Email", "bill@microsoft.com");
        createCustomer(TestConstants.CUSTOMER_NAME_2, "Bill Gates", billContacts, null, session);

        Address address = new Address();
        address.setSuburb(wollongong);
        address.setAddressLine1("63 Market St.");
        createCustomer("Bill Gates", "Mr. Gates", null, address, session);
        session.flush();

        Employee employee1 = createEmployee(TestConstants.EMPLOYEE_NAME_1, "Mr. Ballmer", null, null, session);
        session.flush();

        Employee employee2 = createEmployee(TestConstants.EMPLOYEE_NAME_2, "Mr. Drake", null, null, session);
        session.flush();

        Category allProducts = createCategory(TestConstants.ROOT_CATEGORY, null, null, session);

        Category food = createCategory(TestConstants.SUB_CATEGORY_1, null, allProducts, session);
        Category toy = createCategory(TestConstants.SUB_CATEGORY_2, null, allProducts, session);
        Category pharmaceutical = createCategory(TestConstants.SUB_CATEGORY_3, null, allProducts, session);
        Category fashion = createCategory("Fashion", null, allProducts, session);
        createCategory("Clothes", null, fashion, session);
        createCategory("Jewellery", null, fashion, session);
        createCategory("Decoration", null, fashion, session);

        Category electronics = createCategory("Electronics", null, allProducts, session);
        createCategory("Tablet", null, electronics, session);
        createCategory("Laptop", null, electronics, session);
        createCategory("Printer", null, electronics, session);
        session.flush();

        Product product1 = createProduct(TestConstants.PRODUCT_NAME_1, toy, "1", session);
        Product product2 = createProduct(TestConstants.PRODUCT_NAME_2, toy, "1", session);
        createProduct("Fried Chicken", food, "6pcs", session);
        createProduct("Sun Cream", pharmaceutical, "250g", session);
        session.flush();

        createShipper("EMS", "Ms. Li", null, null, session);
        session.flush();

        Supplier supplier1 = createSupplier(TestConstants.SUPPLIER_NAME_1, "Mr. Cohen", null, null, session);
        session.flush();

        if (customer1.getOrders().size() == 0) {
            Order order = new Order();
            order.setCustomer(customer1);
            order.setEmployee(employee1);
            order.setOrderDate(LocalDateTime.of(2015, 8, 17, 11, 25, 33));
            order.setShipName("Mr. " + customer1.getContact().getName());
            Address address1 = new Address();
            address1.setSuburb(linzi);
            address1.setAddressLine1("Build 3 201");
            order.setShipAddress(address1);

            OrderItem orderItem1 = new OrderItem();
            orderItem1.setProduct(product1);
            orderItem1.setQuantity(1);
            orderItem1.setSellPrice(new BigDecimal("20.15"));
            orderItem1.setSupplier(supplier1);

            order.getOrderItems().add(orderItem1);

            OrderItem orderItem2 = new OrderItem();
            orderItem2.setProduct(product2);
            orderItem2.setQuantity(2);
            orderItem2.setSellPrice(new BigDecimal(8.50));

            order.getOrderItems().add(orderItem2);

            session.save(order);
        }
        session.flush();
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

    private Customer createCustomer(String name, String contactName, Session session) {
        return createCustomer(name, contactName, null, null, session);
    }

    private Customer createCustomer(String name, String contactName, Map<String, String> contactNumbers, Address address, Session session) {
        List<Customer> result = getCustomerListByName(name, session);
        if (result.size() > 0)
            return result.get(0);

        Customer customer = new Customer();
        customer.setContact(new Contact());
        customer.getContact().setName(name);
        customer.getContact().setContactName(contactName);
        customer.getContact().setContactNumbers(contactNumbers);
        customer.getContact().setAddress(address);
        session.save(customer);
        return customer;

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

        //copied from Service save function
        String parentPrefix = "";
        if(category.getParent() != null){
            parentPrefix = category.getParent().getPrefix();
        }
        category.setPrefix(parentPrefix + "_" + category.getId());

        return category;
    }

    @SuppressWarnings("unchecked")
    private List<Category> getCategoryListByName(String name, Session session) {
        return session.createCriteria(Category.class).add(Restrictions.like("name", "%" + name + "%")).list();
    }

    private Employee createEmployee(String name, String contactName, Map<String, String> contactNumbers, Address address, Session session) {
        List<Employee> result = getEmployeeListByName(name, session);
        if (result.size() > 0)
            return result.get(0);

        Employee employee = new Employee();
        employee.setContact(new Contact());
        employee.getContact().setName(name);
        employee.getContact().setContactName(contactName);
        employee.getContact().setContactNumbers(contactNumbers);
        employee.getContact().setAddress(address);
        session.save(employee);
        return employee;
    }

    @SuppressWarnings("unchecked")
    private List<Employee> getEmployeeListByName(String name, Session session) {
        return session.createCriteria(Employee.class).createCriteria("contact").add(Restrictions.like("name", "%" + name + "%")).list();
    }

    private Product createProduct(String name, Category category, String quantityPerUnit, Session session) {
        List<Product> result = getProductListByName(name, session);
        if (result.size() > 0)
            return result.get(0);

        Product product = new Product();
        product.setName(name);
        product.setQuantityPerUnit(quantityPerUnit);
        while (category != null) {
            product.getCategories().add(category);
            category = category.getParent();
        }
        product.setStock(50);
        session.save(product);
        return product;
    }

    @SuppressWarnings("unchecked")
    private List<Product> getProductListByName(String name, Session session) {
        return session.createCriteria(Product.class).add(Restrictions.like("name", "%" + name + "%")).list();
    }

    private void createShipper(String name, String contactName, Map<String, String> contactNumbers, Address address, Session session) {
        List<Shipper> result = getShipperListByName(name, session);
        if (result.size() == 0) {
            Shipper shipper = new Shipper();
            shipper.setContact(new Contact());
            shipper.getContact().setName(name);
            shipper.getContact().setContactName(contactName);
            shipper.getContact().setContactNumbers(contactNumbers);
            shipper.getContact().setAddress(address);
            session.save(shipper);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Shipper> getShipperListByName(String name, Session session) {
        return session.createCriteria(Shipper.class).createCriteria("contact").add(Restrictions.like("name", "%" + name + "%")).list();
    }

    private Supplier createSupplier(String name, String contactName, Map<String, String> contactNumbers, Address address, Session session) {
        List<Supplier> result = getSupplierListByName(name, session);
        if (result.size() > 0) {
            return result.get(0);
        }

        Supplier supplier = new Supplier();
        supplier.setContact(new Contact());
        supplier.getContact().setName(name);
        supplier.getContact().setContactName(contactName);
        supplier.getContact().setContactNumbers(contactNumbers);
        supplier.getContact().setAddress(address);
        session.save(supplier);
        return supplier;
    }

    @SuppressWarnings("unchecked")
    private List<Supplier> getSupplierListByName(String name, Session session) {
        return session.createCriteria(Supplier.class).createCriteria("contact").add(Restrictions.like("name", "%" + name + "%")).list();
    }
}
