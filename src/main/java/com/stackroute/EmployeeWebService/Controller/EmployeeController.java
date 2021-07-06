package com.stackroute.EmployeeWebService.Controller;

import com.stackroute.EmployeeWebService.Model.Employee;
import com.stackroute.EmployeeWebService.service.EmployeeService;
import com.stackroute.EmployeeWebService.vo.EmployeeResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

// http://localhost:8091/swagger-ui.html

@Api(value = "Swagger - 2 EmployeeController")
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService empService;

    /**
     * add new employee
     * @param emp
     * @return
     */
    @ApiOperation(value = "add new Employee record",
            response = Employee.class, tags = "Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping("/add-Employee")
    public Employee createEmployee(@RequestBody Employee emp){
        return empService.saveEmployee(emp);
    }

    /**
     * Get a employee details
     * @param empId
     * @return
     */
    @ApiOperation(value = "Get an Employee details", response = Employee.class, tags = "Employee")
    @GetMapping("/byId/{empId}")
    public Employee getEmployeeById(@PathVariable int empId){
        return empService.getEmployeeById(empId);
    }

    @ApiOperation(value = "Get All Employees with Project Details",
            response = EmployeeResponseVo.class, tags = "Employee")
    @GetMapping("/list")
    public Set<EmployeeResponseVo> getAllEmployees(){
        return empService.getAllEmployees();
    }

    /**
     * update employee details
     * @param emp
     * @return
     */
    @ApiOperation(value = "Update an Employee Record", response = Iterable.class, tags = "Employee")
    @PutMapping("/update")
    public Employee updateEmployee(@RequestBody Employee emp){
        return empService.saveEmployee(emp);
    }

    /**
     * delete an employee
     * @param empId
     * @return
     */
    @ApiOperation(value = "Delete an Employee Record", response = Iterable.class, tags = "Employee")
    @DeleteMapping("/delete/{empId}")
    public String deleteEmployee(@PathVariable int empId){
        boolean status = empService.deleteEmployee(empId);
        if(status){
            return "Employee with id "+empId+" deleted.";
        }else {
            return "Employee with id "+empId+" not deleted.";
        }
    }

    /**
     * Get an Employee details along with allocated Project Details
     * @param empId
     * @return
     */
    @ApiOperation(value = "Get an Employee details along with allocated Project Details",
            response = EmployeeResponseVo.class, tags = "Employee")
    @GetMapping("/employeeWithProjects/{empId}")
    public EmployeeResponseVo getEmployeeWithProjectList(@PathVariable int empId){
        return empService.getEmployeeProjects(empId);
    }
}
