package simpleshop.data.infrastructure;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZHY on 21/09/2014.
 */
public class HibernateInterceptor extends EmptyInterceptor {

    private static Logger logger = LoggerFactory.getLogger(HibernateInterceptor.class);

    public String onPrepareStatement(String sql) {

        //logger.info("Logger " + this.hashCode() + " says: " + sql);
        return super.onPrepareStatement(sql);
    }

}
