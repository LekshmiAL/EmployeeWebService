package com.stackroute.EmployeeWebService.service;

import com.stackroute.EmployeeWebService.Model.Employee;
import com.stackroute.EmployeeWebService.Model.Project;
import com.stackroute.EmployeeWebService.repository.EmployeeRepository;
import com.stackroute.EmployeeWebService.vo.EmployeeResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImp implements EmployeeService{

    @Autowired
    private EmployeeRepository empRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Employee saveEmployee(Employee emp) {
        return empRepo.save(emp);
    }

    @Override
    public Employee getEmployeeById(int empid) {
        return empRepo.findById(empid).get();
    }

    @Override
    public Set<EmployeeResponseVo> getAllEmployees() {
        Set<EmployeeResponseVo> empDetails = new HashSet<>();
        Set<Project> projects = new HashSet<>();

        Set<Employee> employeeList = empRepo.findAll().stream().collect(Collectors.toSet());

        employeeList.forEach(emp -> {
            EmployeeResponseVo responseVo = getEmployeeProjects(emp.getId());
            empDetails.add(responseVo);
        });
        return empDetails;
    }

    @Override
    public boolean deleteEmployee(int empId) {
        boolean returnStatus = false;
        if(getEmployeeById(empId)!= null){
            empRepo.deleteById(empId);
            returnStatus = true;
        }
        return returnStatus;
    }

    @Override
    public EmployeeResponseVo getEmployeeProjects(int empId) {
        EmployeeResponseVo responseVo = new EmployeeResponseVo();
        Employee employee = getEmployeeById(empId);
        if(employee != null){
            responseVo.setEmployee(employee);
            List<Project> projectList=
                    restTemplate.getForObject("http://ALLOCATION-SERVICE/api/allocation/projects/"+empId, List.class);
           Set<Project> projectSet = projectList.stream().collect(Collectors.toSet());
           responseVo.setProjects(projectSet);
        }
        return responseVo;
    }

}
