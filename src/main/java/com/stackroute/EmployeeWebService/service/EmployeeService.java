package com.stackroute.EmployeeWebService.service;

import com.stackroute.EmployeeWebService.Model.Employee;
import com.stackroute.EmployeeWebService.vo.EmployeeResponseVo;

import java.util.Set;

public interface EmployeeService {

    Employee saveEmployee(Employee emp);

    Employee getEmployeeById(int empid);

    Set<EmployeeResponseVo> getAllEmployees();

    boolean deleteEmployee(int empId);

    EmployeeResponseVo getEmployeeProjects(int empId);
}
