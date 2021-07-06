package com.stackroute.EmployeeWebService.vo;

import com.stackroute.EmployeeWebService.Model.Employee;
import com.stackroute.EmployeeWebService.Model.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseVo {

    private Employee employee;
    private Set<Project> projects;
}
