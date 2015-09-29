

package simpleshop.service.impl.base;

import org.springframework.beans.factory.annotation.Autowired;
import simpleshop.data.EmployeeDAO;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.domain.model.Employee;
import simpleshop.dto.EmployeeSearch;
import simpleshop.service.EmployeeService;
import simpleshop.service.infrastructure.impl.ModelServiceImpl;

public abstract class EmployeeBaseService extends ModelServiceImpl<Employee, EmployeeSearch> implements EmployeeService {

    @Autowired
    protected EmployeeDAO employeeDAO;

    @Override
    protected ModelDAO<Employee> getModelDAO() {
        return employeeDAO;
    }

    @Override
    public Employee create() {
        return new Employee();
    }

}
