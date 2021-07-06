package com.stackroute.EmployeeWebService.repository;

import com.stackroute.EmployeeWebService.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
