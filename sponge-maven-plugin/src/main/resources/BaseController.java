@args String projectName, String modelName;
@{String basePackage = projectName.toLowerCase()}
package @(basePackage).webapp.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import @(basePackage).domain.model.Employee;
import @(basePackage).dto.EmployeeSearch;
import @(basePackage).dto.JsonResponse;
import @(basePackage).dto.ModelQuickSearch;
import @(basePackage).service.EmployeeService;
import @(basePackage).webapp.infrastructure.BaseJsonController;

import javax.validation.Valid;
import java.io.Serializable;

@Controller
@RequestMapping(produces = "application/json")
public class EmployeeController extends BaseJsonController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/employee/search", method = RequestMethod.GET)
    public String employeeSearch(Model model) {
        JsonResponse<EmployeeSearch> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, new EmployeeSearch());
        return super.outputJson(model, response, employeeService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/employee/search", method = RequestMethod.POST, consumes = "application/json")
    public String employeeSearch(@Valid @RequestBody final EmployeeSearch employeeSearch, Model model, BindingResult bindingResult) {
        JsonResponse<Iterable<Employee>> response = modelSearch(employeeSearch, employeeService, bindingResult);
        return super.outputJson(model, response, employeeService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/employee/new", method = RequestMethod.GET)
    public String employeeCreate(Model model) {
        JsonResponse<Employee> response = new JsonResponse<>(JsonResponse.STATUS_OK, null, employeeService.create());
        return super.outputJson(model, response, employeeService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
    public String employeeDetails(@PathVariable int id, Model model) {
        JsonResponse<Employee> response = modelDetails(id, employeeService);
        return super.outputJson(model, response, employeeService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/employee/save", method = RequestMethod.POST, consumes = "application/json")
    public String employeeSave(@Valid @RequestBody final Employee employee,Model model,  BindingResult bindingResult) {
        JsonResponse<Employee>  response =  saveModel(employee, employeeService, bindingResult);
        return super.outputJson(model, response, employeeService.ignoredJsonProperties());
    }

    @RequestMapping(value = "/employee/delete", method = RequestMethod.POST, consumes = "application/json")
    public String employeeDelete(@Valid @RequestBody final int id, Model model) {
        JsonResponse<Serializable> response = deleteModel(id, employeeService);
        return super.outputJson(model, response, null);
    }

    @RequestMapping(value = "/employee/list", method = RequestMethod.POST, consumes = "application/json")
    public String suburbList(@Valid @RequestBody final ModelQuickSearch quickSearch, Model model){
        JsonResponse<Iterable<Employee>> response = new JsonResponse<>(JsonResponse.STATUS_OK, null,employeeService.quickSearch(quickSearch.getKeywords(), quickSearch));
        return super.outputJson(model, response, null);
    }
}
