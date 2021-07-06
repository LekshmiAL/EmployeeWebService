package com.stackroute.EmployeeWebService.service;

import com.stackroute.EmployeeWebService.Model.Employee;
import com.stackroute.EmployeeWebService.Model.Project;
import com.stackroute.EmployeeWebService.repository.EmployeeRepository;
import com.stackroute.EmployeeWebService.vo.EmployeeResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
            EmployeeResponseVo responseVo = new EmployeeResponseVo();
            Project project;
            ResponseEntity<Integer[]> response =
                    restTemplate.getForEntity(
                            "http://localhost:8083/api/allocation/projectIds/" + emp.getId(),
                            Integer[].class);

            Integer[] projIds = response.getBody();
            if (projIds != null) {
                for (Integer id : projIds) {
                    project = restTemplate
                            .getForObject("http://localhost:8082/api/project/" + id, Project.class);
                    projects.add(project);
                }
                responseVo.setProjects(projects);
            }

            responseVo.setEmployee(emp);
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
        }else{
            returnStatus = false;
        }
        return returnStatus;
    }

    @Override
    public EmployeeResponseVo getEmployeeProjects(int empId) {
        EmployeeResponseVo responseVo = new EmployeeResponseVo();
        Employee employee = getEmployeeById(empId);
        Set<Project> projects = new HashSet<>();
        if(employee != null){
            responseVo.setEmployee(employee);
            ResponseEntity<Integer[]> projectIdsResponse=
                    restTemplate.getForEntity("http://localhost:8090/api/allocation/projectIds/"+empId,Integer[].class);
            Integer[] projIds = projectIdsResponse.getBody();
            if(projIds!=null){
                for(int pid: projIds){
                    Project project =
                            restTemplate.getForObject("http://localhost:8082/api/project/"+pid,Project.class);
                    projects.add(project);
                }
            }
            responseVo.setProjects(projects);
        }
        return responseVo;
    }

}
