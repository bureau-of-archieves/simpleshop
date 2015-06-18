package simpleshop.data.infrastructure;

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
}
