package simpleshop.data.infrastructure;

import java.io.Serializable;

/**
 * Common operations that all DAOs support.
 * All sql/hql should be confined within DAOs.
 */
public interface BaseDAO {

    void detach(Object domainObject);

    void initialize(Object domainObject);

    boolean isInitialized(Object domainObject);

    void setSessionDefaultReadonly(boolean isReadonly);

    boolean isSessionDefaultReadonly();

    void sessionFlush();

    void evict(Object domainObject);

    Serializable getIdentifier(Object domainObject);

}
