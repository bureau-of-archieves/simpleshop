package simpleshop.service;

import simpleshop.domain.model.Employee;
import simpleshop.dto.EmployeeSearch;
import simpleshop.service.infrastructure.ModelService;

public interface EmployeeService extends ModelService<Employee, EmployeeSearch> {

}
