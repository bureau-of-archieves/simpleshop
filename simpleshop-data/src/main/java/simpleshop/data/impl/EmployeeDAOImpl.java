package simpleshop.data.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.EmployeeDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Employee;

import java.io.Serializable;
import java.util.List;


@Repository
public class EmployeeDAOImpl extends ModelDAOImpl<Employee> implements EmployeeDAO {

    @Override
    public Employee load(Serializable id) {
        return super.load(Employee.class, id);
    }

    @Override
    public Employee get(Serializable id) {
        return super.get(Employee.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        Query query = super.createQuery("SELECT c FROM Employee c INNER JOIN c.contact ct WHERE ct.name LIKE ?1 or ct.contactName LIKE ?1", pageInfo, keywords);
        return query.list();
    }
}
