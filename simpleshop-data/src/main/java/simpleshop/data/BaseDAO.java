package simpleshop.data;

/**
 * Common operations that all DAOs support.
 * All sql/hql should be confined within DAOs.
 */
public interface BaseDAO {

    public void detach(Object domainObject);

    public void initialize(Object domainObject);

    public boolean isInitialized(Object domainObject);

    public void setSessionDefaultReadonly(boolean isReadonly);

    public boolean isSessionDefaultReadonly();

    public void sessionFlush();

    public void evict(Object domainObject);
}
