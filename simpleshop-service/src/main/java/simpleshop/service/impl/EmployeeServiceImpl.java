package simpleshop.service.impl;

import org.springframework.stereotype.Service;
import simpleshop.domain.model.Employee;
import simpleshop.service.EmployeeService;
import simpleshop.service.impl.base.EmployeeBaseService;

@Service
public class EmployeeServiceImpl extends EmployeeBaseService implements EmployeeService {

    public Employee getDefaultEmployee(){
        Employee employee = new Employee();
        employee.setId(1);
        return employee; //todo figure out how to decide default employee
    }
}
