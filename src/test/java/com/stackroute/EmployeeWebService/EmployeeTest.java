package com.stackroute.EmployeeWebService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.stackroute.EmployeeWebService.Controller.EmployeeController;
import com.stackroute.EmployeeWebService.Model.Employee;
import com.stackroute.EmployeeWebService.repository.EmployeeRepository;
import com.stackroute.EmployeeWebService.service.EmployeeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTest {

    @Autowired
    private EmployeeService service;

    @Autowired
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

        mockMvc.perform(get("/api/employee/")).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.firstName", Matchers.equalTo("Janvi")))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo("koul")))
                .andExpect(jsonPath("$.designation", Matchers.equalTo("janvi@gmail.com")))
                .andExpect(jsonPath("$.email", Matchers.equalTo("7998467362")))
                .andExpect(jsonPath("$.joiningDate", Matchers.equalTo(LocalDate.of(2019,05,20))));
    }
    @Test
    public void getAllEmployeesTest(){
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(101,"Janvi","koul","Developer","janvi@gmail.com","7998467362", LocalDate.of(2019,05,20)));
        employeeList.add(new Employee(102,"Micheal","Loui","HR","loui@gmail.com","9994447362", LocalDate.of(2020,02,10)));
        employeeList.add(new Employee(103,"Harvy","Smith","Manager","harvys@gmail.com","9996757362", LocalDate.of(2021,01,22)));
        employeeList.add(new Employee(104,"Kishore","shah","Tester","shah@gmail.com","8018467362", LocalDate.of(2018,04,20)));

        //Mockito.when(service.getAllEmployees()).thenReturn();

    }


}
