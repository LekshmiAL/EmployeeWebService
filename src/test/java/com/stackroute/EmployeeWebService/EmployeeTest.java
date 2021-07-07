package com.stackroute.EmployeeWebService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.EmployeeWebService.Controller.EmployeeController;
import com.stackroute.EmployeeWebService.Model.Employee;
import com.stackroute.EmployeeWebService.Model.Project;
import com.stackroute.EmployeeWebService.repository.EmployeeRepository;
import com.stackroute.EmployeeWebService.service.EmployeeService;
import com.stackroute.EmployeeWebService.vo.EmployeeResponseVo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTest {

    @MockBean
    private EmployeeService service;

    @MockBean
    private EmployeeRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc ;

    @Test
    public void employeeModelTest(){
        Employee emp = Mockito.mock(Employee.class);
        assertNotNull(emp);
    }

    @Test
    public void employeeControllerTest(){
        EmployeeController employeeController = Mockito.mock(EmployeeController.class);
        assertNotNull(employeeController);
    }

    @Test
    public void createEmployeeTest() throws Exception{

        LocalDate date = LocalDate.of(2020,10,10);
        Employee empNew = new Employee(100,"Janvi","koul","Developer",
                "janvi@gmail.com","9998467362", date);
        Mockito.when(service.saveEmployee(ArgumentMatchers.any())).thenReturn(empNew);
        String jsonContent = mapper.writeValueAsString(empNew);

        mockMvc.perform(post("/api/employee/add-Employee")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContent)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(100)));
    }

    @Test
    public void getEmployeeTest() throws Exception{
        Employee emp = new Employee(101,"Janvi","koul","Developer","janvi@gmail.com","7998467362", LocalDate.of(2019,05,20));
        Mockito.when(service.getEmployeeById(101)).thenReturn(emp);

        mockMvc.perform(get("/api/employee/101")).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", Matchers.equalTo("Janvi")))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo("koul")))
                .andExpect(jsonPath("$.designation", Matchers.equalTo("Developer")))
                .andExpect(jsonPath("$.email", Matchers.equalTo("janvi@gmail.com")))
                .andExpect(jsonPath("$.joiningDate", Matchers.equalTo(LocalDate.of(2019,05,20).toString())));
    }
    @Test
    public void getAllEmployeesTest() throws Exception{

        Employee e1 = new Employee(101,"Janvi","koul","Developer","janvi@gmail.com","7998467362", LocalDate.of(2019,05,20));
        Set<Project> projectSet1 = new HashSet<>();
        projectSet1.add(new Project(110, "dell","Application","dell client",
               new SimpleDateFormat("dd/MM/yyyy").parse("19/10/2020"),
               new SimpleDateFormat("dd/MM/yyyy").parse("19/09/2021")));

        Employee e2 = new Employee(102,"Micheal","Loui","HR","loui@gmail.com","9994447362", LocalDate.of(2020,02,10));
        Set<Project> projectSet2 = new HashSet<>();
        projectSet2.add(new Project(111, "apple","Application","Apple client",
                new SimpleDateFormat("dd/MM/yyyy").parse("10/10/2020"),
                new SimpleDateFormat("dd/MM/yyyy").parse("03/12/2021")));

        Set<EmployeeResponseVo> responseVoSet = new HashSet<>();
        responseVoSet.add(new EmployeeResponseVo(e1,projectSet1));
        responseVoSet.add(new EmployeeResponseVo(e2,projectSet2));

        Mockito.when(service.getAllEmployees()).thenReturn(responseVoSet);

        mockMvc.perform(get("/api/employee/list")).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].employee.id",Matchers.equalTo(101)))
                .andExpect(jsonPath("$[1].employee.id",Matchers.equalTo(102)));

    }
    @Test
    public void testDeleteEmployee() throws Exception {
        Employee e1 = new Employee(101,"Janvi","koul","Developer",
                "janvi@gmail.com","7998467362", LocalDate.of(2019,05,20));
        Mockito.when(service.deleteEmployee(101)).thenReturn(true);

        mockMvc.perform(delete("/api/employee/delete/101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee with id 101 deleted."));
    }

    @Test
    public void getEmployeeWithProjectListTest() throws Exception{
        Employee e1 = new Employee(101,"Janvi","koul","Developer",
                "janvi@gmail.com","7998467362", LocalDate.of(2019,05,20));
        Set<Project> projectSet1 = new HashSet<>();
        projectSet1.add(new Project(110, "dell","Application","dell client",
                new SimpleDateFormat("dd/MM/yyyy").parse("19/10/2020"),
                new SimpleDateFormat("dd/MM/yyyy").parse("19/09/2021")));
        EmployeeResponseVo responseVo = new EmployeeResponseVo(e1,projectSet1);
        Mockito.when(service.getEmployeeProjects(101)).thenReturn(responseVo);

        mockMvc.perform(get("/api/employee/employeeWithProjects/101")).andExpect(status().isOk())
                .andExpect(jsonPath("$.employee.id",Matchers.equalTo(101)))
                .andExpect(jsonPath("$.projects[0].projectId",Matchers.equalTo(110)));
    }

}
