package com.example.rqchallenge;

import com.example.rqchallenge.dao.EmployeeDAO;
import com.example.rqchallenge.entities.Employee;
import com.example.rqchallenge.exceptions.TooManyRequestsException;
import com.example.rqchallenge.response.EmployeeResponse;
import com.example.rqchallenge.response.EmployeeResponseSingle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RqChallengeApplicationTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeDAO employeeDAO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllEmployees_Success() {
        EmployeeResponse mockResponse = new EmployeeResponse();
        Employee employee1 = new Employee("John Doe", 50000, 30);
        Employee employee2 = new Employee("Jane Doe", 60000, 28);
        mockResponse.setData(Arrays.asList(employee1, employee2));

        when(restTemplate.getForObject(anyString(), eq(EmployeeResponse.class))).thenReturn(mockResponse);

        List<Employee> employees = employeeDAO.getAllEmployees();

        assertNotNull(employees);
        assertEquals(2, employees.size());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testGetAllEmployees_TooManyRequests() {
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponse.class)))
                .thenThrow(HttpClientErrorException.TooManyRequests.create(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", null, null, null));

        TooManyRequestsException thrown = assertThrows(TooManyRequestsException.class, () -> {
            employeeDAO.getAllEmployees();
        });

        assertEquals("Too Many Requests: Please try again later. null allows 1 request per minute", thrown.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testGetAllEmployees_NetworkIssue() {
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponse.class)))
                .thenThrow(new ResourceAccessException("Network issue"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.getAllEmployees();
        });

        assertEquals("Network issue: Please check your connection.", thrown.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testGetAllEmployees_ServerError() {
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponse.class)))
                .thenThrow(HttpServerErrorException.InternalServerError.create(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", null, null, null));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.getAllEmployees();
        });

        assertEquals("Server error: Please try again later.", thrown.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testGetAllEmployees_UnexpectedError() {
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponse.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.getAllEmployees();
        });

        assertEquals("Unexpected error: Unexpected error", thrown.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponse.class));
    }

    @Test
    void testGetEmployeeById_Success() {
        EmployeeResponseSingle mockResponse = new EmployeeResponseSingle();
        Employee employee = new Employee("John Doe", 50000, 30);
        mockResponse.setData(employee);

        when(restTemplate.getForObject(anyString(), eq(EmployeeResponseSingle.class))).thenReturn(mockResponse);

        Employee result = employeeDAO.getEmployeeById("1");

        assertNotNull(result);
        assertEquals("John Doe", result.getEmployeeName());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testGetEmployeeById_TooManyRequests() {
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponseSingle.class)))
                .thenThrow(HttpClientErrorException.TooManyRequests.create(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", null, null, null));

        TooManyRequestsException thrown = assertThrows(TooManyRequestsException.class, () -> {
            employeeDAO.getEmployeeById("1");
        });

        assertEquals("Too Many Requests: Please try again later. null allows 1 request per minute", thrown.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testGetEmployeeById_NetworkIssue() {
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponseSingle.class)))
                .thenThrow(new ResourceAccessException("Network issue"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.getEmployeeById("1");
        });

        assertEquals("Network issue: Please check your connection.", thrown.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testGetEmployeeById_ServerError() {
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponseSingle.class)))
                .thenThrow(HttpServerErrorException.InternalServerError.create(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", null, null, null));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.getEmployeeById("1");
        });

        assertEquals("Server error: Please try again later.", thrown.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testGetEmployeeById_UnexpectedError() {
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponseSingle.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.getEmployeeById("1");
        });

        assertEquals("Unexpected error: Unexpected error", thrown.getMessage());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testCreateEmployee_Success() {
        EmployeeResponseSingle mockResponse = new EmployeeResponseSingle();
        Employee newEmployee = new Employee("John Doe", 50000, 30);
        mockResponse.setData(newEmployee);

        when(restTemplate.postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class))).thenReturn(mockResponse);

        Employee result = employeeDAO.createEmployee("John Doe", 50000, 30);

        assertNotNull(result);
        assertEquals("John Doe", result.getEmployeeName());
        verify(restTemplate, times(1)).postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testCreateEmployee_TooManyRequests() {
        when(restTemplate.postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class)))
                .thenThrow(HttpClientErrorException.TooManyRequests.create(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", null, null, null));

        TooManyRequestsException thrown = assertThrows(TooManyRequestsException.class, () -> {
            employeeDAO.createEmployee("John Doe", 50000, 30);
        });

        assertEquals("Too Many Requests: Please try again later. null allows 1 request per minute", thrown.getMessage());
        verify(restTemplate, times(1)).postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testCreateEmployee_NetworkIssue() {
        when(restTemplate.postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class)))
                .thenThrow(new ResourceAccessException("Network issue"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.createEmployee("John Doe", 50000, 30);
        });

        assertEquals("Network issue: Please check your connection.", thrown.getMessage());
        verify(restTemplate, times(1)).postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testCreateEmployee_ServerError() {
        when(restTemplate.postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class)))
                .thenThrow(HttpServerErrorException.InternalServerError.create(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", null, null, null));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.createEmployee("John Doe", 50000, 30);
        });

        assertEquals("Server error: Please try again later.", thrown.getMessage());
        verify(restTemplate, times(1)).postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testDeleteEmployee_Success() {
        String id = "1";
        String mockResponse = "Employee with ID 1 deleted successfully.";

        doNothing().when(restTemplate).delete(anyString());

        String result = employeeDAO.deleteEmployee(id);

        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(restTemplate, times(1)).delete(anyString());
    }

    @Test
    void testDeleteEmployee_TooManyRequests() {
        String id = "1";
        String expectedMessage = "Too Many Requests: Please try again later. null allows 1 request per minute";

        HttpClientErrorException.TooManyRequests exception = (HttpClientErrorException.TooManyRequests) HttpClientErrorException.TooManyRequests.create(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", null, null, null);
        doThrow(exception).when(restTemplate).delete(anyString());

        TooManyRequestsException thrown = assertThrows(TooManyRequestsException.class, () -> {
            employeeDAO.deleteEmployee(id);
        });

        assertEquals(expectedMessage, thrown.getMessage());
        verify(restTemplate, times(1)).delete(anyString());
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, exception.getStatusCode());
    }


    @Test
    void testDeleteEmployee_NetworkIssue() {
        String id = "1";

        doThrow(new ResourceAccessException("Network issue"))
                .when(restTemplate).delete(anyString());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.deleteEmployee(id);
        });

        assertEquals("Network issue: Please check your connection.", thrown.getMessage());
        verify(restTemplate, times(1)).delete(anyString());
    }

    @Test
    void testDeleteEmployee_ServerError() {
        String id = "1";

        doThrow(HttpServerErrorException.InternalServerError.create(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", null, null, null))
                .when(restTemplate).delete(anyString());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.deleteEmployee(id);
        });

        assertEquals("Server error: Please try again later.", thrown.getMessage());
        verify(restTemplate, times(1)).delete(anyString());
    }

    @Test
    void testDeleteEmployee_UnexpectedError() {
        String id = "1";

        doThrow(new RuntimeException("Unexpected error"))
                .when(restTemplate).delete(anyString());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.deleteEmployee(id);
        });

        assertEquals("Unexpected error: Unexpected error", thrown.getMessage());
        verify(restTemplate, times(1)).delete(anyString());
    }
    @Test
    void testCreateEmployee_InvalidName() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.createEmployee("", 50000, 30);
        });

        assertEquals("Name cannot be empty", thrown.getMessage());
        verify(restTemplate, never()).postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testCreateEmployee_InvalidSalary() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.createEmployee("John Doe", -50000, 30);
        });

        assertEquals("Salary must be greater than or equal to 0", thrown.getMessage());
        verify(restTemplate, never()).postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class));
    }

    @Test
    void testCreateEmployee_InvalidAge() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeDAO.createEmployee("John Doe", 50000, -30);
        });

        assertEquals("Age must be greater than or equal to 0", thrown.getMessage());
        verify(restTemplate, never()).postForObject(anyString(), any(Employee.class), eq(EmployeeResponseSingle.class));
    }
}






