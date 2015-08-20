package simpleshop.data.infrastructure;

import org.hibernate.Criteria;
import simpleshop.data.PageInfo;

/**
 * An interfact the client can use to build a criteria base on a search object.
 */
public interface CriteriaBuilder {

    /**
     * Build a criteria based on the search object.
     * @param searchObject annotated search object.
     */
    void buildCriteria(PageInfo searchObject);

    /**
     * Get the created criteria.
     * @param alias the declared alias.
     * @return created criteria.
     */
    Criteria getCriteria(String alias);



}
