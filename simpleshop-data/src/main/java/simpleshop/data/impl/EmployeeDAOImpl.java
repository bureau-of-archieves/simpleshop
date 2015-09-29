package simpleshop.data.impl;

import org.springframework.stereotype.Repository;
import simpleshop.common.StringUtils;
import simpleshop.data.EmployeeDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.impl.ModelDAOImpl;
import simpleshop.domain.model.Employee;

import java.util.List;


@Repository
public class EmployeeDAOImpl extends ModelDAOImpl<Employee> implements EmployeeDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> quickSearch(String keywords, PageInfo pageInfo){
        keywords = StringUtils.wrapLikeKeywords(keywords);
        return super.getList("SELECT c FROM Employee c INNER JOIN c.contact ct WHERE ct.name LIKE ?1 or ct.contactName LIKE ?1", pageInfo, keywords);
    }
}
