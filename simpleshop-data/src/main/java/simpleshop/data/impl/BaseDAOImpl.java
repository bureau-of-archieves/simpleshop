package simpleshop.data.impl;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import simpleshop.common.StringUtils;
import simpleshop.data.BaseDAO;
import simpleshop.data.PageInfo;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * Provide default implementation to all none type-specific methods in BassDAO.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)//transaction is managed at service level.
public abstract class BaseDAOImpl implements BaseDAO {

    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public void detach(Object domainObject) {
        getSession().evict(domainObject);
    }

    @Override
    public void initialize(Object domainObject) {
        Hibernate.initialize(domainObject);
    }

    @Override
    public boolean isInitialized(Object domainObject) {
        return Hibernate.isInitialized(domainObject);
    }

    @Override
    public void setSessionDefaultReadonly(boolean isReadonly) {
        getSession().setDefaultReadOnly(isReadonly);
    }

    @Override
    public boolean isSessionDefaultReadonly() {
        return getSession().isDefaultReadOnly();
    }

    @Override
    public void sessionFlush() {
        getSession().flush();
    }

    @Override
    public void evict(Object domainObject){
        getSession().evict(domainObject);
    }

    ////////////////////////// protected methods //////////////////////////////////////////////////////////

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected void saveDomainObject(Object domainObject) {

        Object mergedObject = mergeRemoveOrphan(domainObject); //fix inconsistency for bidirectional collections
        getSession().saveOrUpdate(mergedObject);
    }

    /**
     * Named "deleteDomainObject" to get around naming conflict.
     * @param domainObject northwind object.
     */
    protected void deleteDomainObject(Object domainObject) {
        getSession().delete(domainObject);
    }

    /**
     * A helper method that creates a hibernate query object.
     * @param hql hql with optional positional parameters.
     * @param pageInfo set pageIndex to 0 if not using paging; set pageSize to -1 to disable paging.
     * @param parameters positional parameters.
     * @return a query object.
     */
    protected Query createQuery(String hql, PageInfo pageInfo, Object... parameters) {

        assert hql != null;
        assert pageInfo.getPageIndex() >= 0;
        assert pageInfo.getPageSize() > 0 || pageInfo.getPageSize() == -1;

        if(pageInfo.getSortInfoList() != null){
            for(int i=0; i<pageInfo.getSortInfoList().size(); i++){
                if(i != 0)
                    hql += ",";
                hql += pageInfo.getSortInfoList().get(i);
            }
        }
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < parameters.length; i++)
            query.setParameter(String.valueOf(i + 1), parameters[i]);
        query.setFirstResult(pageInfo.getPageIndex() * pageInfo.getPageSize());
        if (pageInfo.getPageSize() > 0)
            query.setMaxResults(pageInfo.getPageSize());
        return query;
    }

    /**
     * Remove orphan collection items. When an item is removed from a bidirectional collection, set the mappedBy property to null on the other side.
     * This method does not do recursion, that is only the own collection properties of updated are processed.
     * @param updated the updated domain object.
     * @return updated if nothing is changed, else the reloaded and merged object.
     */
    @SuppressWarnings("unchecked")
    protected Object mergeRemoveOrphan(Object updated){

        //get original domain object
        Class<?> domainClass = Hibernate.getClass(updated);
        ClassMetadata metadata = sessionFactory.getClassMetadata(domainClass);
        Serializable id = metadata.getIdentifier(updated, (SessionImplementor)getSession());
        if(id == null)
            return updated;
        Object original = getSession().get(domainClass, id);
        if(original == null)
            return updated;
        //collections as before the update
        List<Stack<Object>> oldCollections = new ArrayList<>();
        String[] propertyNames = metadata.getPropertyNames();
        for (String propertyName : propertyNames){
            if(!metadata.getPropertyType(propertyName).isCollectionType())
                continue;

            String getterName = "get" + StringUtils.firstCharUpper(propertyName);
            Method getter = ReflectionUtils.findMethod(domainClass, getterName);
            if(getter == null)
                continue;

            //find inverse property
            String mappedBy = null;
            OneToMany oneToMany = getter.getAnnotation(OneToMany.class);
            if(oneToMany != null){
                mappedBy = oneToMany.mappedBy();
            } else {
                ManyToMany manyToMany = getter.getAnnotation(ManyToMany.class);
                if(manyToMany != null){
                    mappedBy = manyToMany.mappedBy();
                }
            }
            if(mappedBy == null)
                continue;

            Collection originalCollection = (Collection)ReflectionUtils.invokeMethod(getter, original);
            if(originalCollection != null && originalCollection.size() > 0){
                Stack<Object> stack = new Stack<>();
                stack.addAll(originalCollection);
                stack.push(mappedBy);
                stack.push(getter);
                oldCollections.add(stack);
            }
        }

        Object merged = getSession().merge(updated);
        for (Stack<Object> stack : oldCollections){
            Method getter = (Method)stack.pop();
            String mappedBy = (String)stack.pop();
            Collection mergedCollection = (Collection)ReflectionUtils.invokeMethod(getter, merged);
            while (!stack.isEmpty()){
                Object item = stack.pop();
                if(mergedCollection.contains(item))
                    continue;
                //set inverse property to null
                simpleshop.common.ReflectionUtils.setProperty(item, mappedBy, null);
            }
        }

        return merged;
    }

}
