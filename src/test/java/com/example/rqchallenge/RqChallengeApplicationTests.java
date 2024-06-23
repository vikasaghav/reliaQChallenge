package com.example.rqchallenge;

import com.example.rqchallenge.entities.Employee;
import com.example.rqchallenge.exceptions.TooManyRequestsException;
import com.example.rqchallenge.response.EmployeeResponse;
import com.example.rqchallenge.response.EmployeeResponseSingle;
import com.example.rqchallenge.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class RqChallengeApplicationTests {

    @Mock
    private RestTemplate restTemplate;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService(restTemplate);
    }

    @Test
    void getAllEmployees_Success() {

        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setData(Collections.singletonList(new Employee(1, "John Doe", 50000, 30)));
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponse.class))).thenReturn(mockResponse);

        List<Employee> employees = employeeService.getAllEmployees();
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("John Doe", employees.get(0).getEmployeeName());
    }



    @Test
    void getEmployeesByNameSearch_Success() {

        Employee employee1 = new Employee(1, "John Doe", 50000, 30);
        Employee employee2 = new Employee(2, "Jane Doe", 60000, 35);
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new EmployeeResponse(Arrays.asList(employee1, employee2)));

        List<Employee> employees = employeeService.getEmployeesByNameSearch("Doe");
        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertTrue(employees.stream().allMatch(employee -> employee.getEmployeeName().contains("Doe")));
    }

    @Test
    void getEmployeeById_Success() {

        EmployeeResponseSingle mockResponse = new EmployeeResponseSingle(new Employee(1, "John Doe", 50000, 30));
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponseSingle.class))).thenReturn(mockResponse);

        Employee employee = employeeService.getEmployeeById("1");
        assertNotNull(employee);
        assertEquals("John Doe", employee.getEmployeeName());
    }

    @Test
    void getHighestSalaryOfEmployees_Success() {

        Employee employee1 = new Employee(1, "John Doe", 50000, 30);
        Employee employee2 = new Employee(2, "Jane Doe", 60000, 35);
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new EmployeeResponse(Arrays.asList(employee1, employee2)));

        int highestSalary = employeeService.getHighestSalaryOfEmployees();
        assertEquals(60000, highestSalary);
    }

    @Test
    void getTop10HighestEarningEmployeeNames_Success() {

        Employee employee1 = new Employee(1, "John Doe", 50000, 30);
        Employee employee2 = new Employee(2, "Jane Doe", 60000, 35);
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new EmployeeResponse(Arrays.asList(employee1, employee2)));

        List<String> topNames = employeeService.getTop10HighestEarningEmployeeNames();
        assertNotNull(topNames);
        assertEquals(2, topNames.size());
        assertEquals("Jane Doe", topNames.get(0));
        assertEquals("John Doe", topNames.get(1));
    }

    @Test
    void createEmployee_Success() {

        EmployeeResponseSingle mockResponse = new EmployeeResponseSingle(new Employee(1, "John Doe", 50000, 30));
        when(restTemplate.postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class)))
                .thenReturn(mockResponse);

        Employee employee = employeeService.createEmployee("John Doe", 50000, 30);
        assertNotNull(employee);
        assertEquals("John Doe", employee.getEmployeeName());
    }

    @Test
    void deleteEmployee_Success() {

        doNothing().when(restTemplate).delete(anyString());

        String result = employeeService.deleteEmployee("1");
        assertEquals("Employee with ID 1 deleted successfully.", result);
    }

}
