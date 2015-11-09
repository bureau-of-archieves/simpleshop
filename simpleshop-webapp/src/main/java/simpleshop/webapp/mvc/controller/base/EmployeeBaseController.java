
package simpleshop.webapp.mvc.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import simpleshop.domain.model.Employee;
import simpleshop.dto.EmployeeSearch;
import simpleshop.dto.JsonResponse;
import simpleshop.dto.ModelQuickSearch;
import simpleshop.service.EmployeeService;
import simpleshop.webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public abstract class EmployeeBaseController extends BaseJsonController<Employee> {

    @Autowired
    protected EmployeeService employeeService;

    @RequestMapping(value = "/employee/search", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<EmployeeSearch> employeeSearch() {
        JsonResponse<EmployeeSearch> jsonResponse = JsonResponse.createSuccess(new EmployeeSearch());
        populateSortInfoList(EmployeeSearch.class, jsonResponse);
        return jsonResponse;
    }

    @RequestMapping(value = "/employee/search", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Employee>> employeeSearch(@Valid @RequestBody final EmployeeSearch employeeSearch, BindingResult bindingResult) {
        return modelSearch(employeeSearch, bindingResult, employeeService);
    }

    @RequestMapping(value = "/employee/new", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Employee> employeeCreate() {
        return modelCreate(employeeService);
    }

    @RequestMapping(value = "/employee/save", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Employee> employeeSave(@Valid @RequestBody final Employee employee, BindingResult bindingResult) {
        return modelSave(employee, bindingResult, employeeService);
    }

    @RequestMapping(value = "/employee/{id:\\d+}", method = RequestMethod.GET)
    public @ResponseBody JsonResponse<Employee> employeeDetails(@PathVariable int id) {
        return modelDetails(id, employeeService);
    }

    @RequestMapping(value = "/employee/delete", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Serializable> employeeDelete(@Valid @RequestBody final int id) {
        return modelDelete(id, employeeService);
    }

    @RequestMapping(value = "/employee/list", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody JsonResponse<Iterable<Employee>> employeeList(@Valid @RequestBody final ModelQuickSearch quickSearch){
        return modelList(quickSearch, employeeService);
    }
}
