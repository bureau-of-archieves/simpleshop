package simpleshop.data.infrastructure.impl;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Additional settings for Hibernate.
 */
public class HibernateInterceptor extends EmptyInterceptor {

    private static Logger logger = LoggerFactory.getLogger(HibernateInterceptor.class);


}
