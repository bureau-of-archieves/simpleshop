package simpleshop.data.infrastructure;

import org.hibernate.EmptyInterceptor;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * Additional settings for Hibernate.
 */
public class HibernateInterceptor extends EmptyInterceptor {

    private static Logger logger = LoggerFactory.getLogger(HibernateInterceptor.class);


}
