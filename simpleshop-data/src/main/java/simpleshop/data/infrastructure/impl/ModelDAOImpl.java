package simpleshop.data.infrastructure.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import simpleshop.Constants;
import simpleshop.common.Pair;
import simpleshop.common.StringUtils;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.SortInfo;
import simpleshop.data.infrastructure.ModelSearchCriteriaBuilder;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.*;
import simpleshop.data.util.DomainUtils;

import java.io.Serializable;
import java.util.*;


@Repository
public abstract class ModelDAOImpl<T> extends BaseDAOImpl implements ModelDAO<T> {

    /**
     * Cannot implement at ModelDAOImpl level because Java generic type erasure.
     * @param id id of the domain object.
     * @return the proxy to the domain object.
     */
    @Override
    public abstract T load(Serializable id);

    /**
     * Cannot implement at ModelDAOImpl level because Java generic type erasure.
     * @param id id of the domain object.
     * @return the domain object or null if cannot find.
     */
    @Override
    public abstract T get(Serializable id);

    public void save(T domainObject) {
        super.saveDomainObject(domainObject);
    }

    public void delete(T domainObject) {
        super.deleteDomainObject(domainObject);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> search(ModelMetadata searchMetadata, ModelMetadata modelMetadata, PageInfo searchObject){

        ModelSearchCriteriaBuilder builder = new ModelSearchCriteriaBuilder(searchMetadata, modelMetadata, getSession());

        builder.buildCriteria(searchObject);

        //sorting
        Criteria rootCriteria = builder.getCriteria(AliasDeclaration.ROOT_CRITERIA_ALIAS);
        if(searchObject.getSortInfoList() != null){
            for(SortInfo sortInfo : searchObject.getSortInfoList()){
                Criteria parentCriteria = builder.getCriteria(sortInfo.getAlias());
                Order sortOrder = sortInfo.isAscending() ? Order.asc(sortInfo.getProperty()) : Order.desc(sortInfo.getProperty());
                parentCriteria.addOrder(sortOrder);
            }
        }

        //paging
        if(searchObject.getPageSize() > 0){
            rootCriteria.setMaxResults(searchObject.getPageSize() + (searchObject.isPageSizePlusOne() ? 1 : 0));
            rootCriteria.setFirstResult(searchObject.getPageIndex() * searchObject.getPageSize());
        }
        return rootCriteria.list();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<T> quickSearch(String keywords, PageInfo pageInfo){
        throw new UnsupportedOperationException("Quick search is not implemented for " + this.getClass().getSimpleName());
    }

    ///////////////////////// protected methods //////////////////////////////////////////

    @SuppressWarnings("unchecked")
    protected T load(Class<T> clazz, Serializable id) {
        return (T) getSession().load(clazz, id);
    }

    @SuppressWarnings("unchecked")
    protected T get(Class<T> clazz, Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    protected List<T> getList(String hql, PageInfo pageInfo, Object... parameters){
        return createQuery(hql, pageInfo, parameters).list();
    }

}
