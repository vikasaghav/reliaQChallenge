package com.example.rqchallenge.dao;

import com.example.rqchallenge.entities.Employee;
import com.example.rqchallenge.exceptions.TooManyRequestsException;
import com.example.rqchallenge.response.EmployeeResponse;
import com.example.rqchallenge.response.EmployeeResponseSingle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.*;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeDAO implements EmployeeDAOInterface {

    @Value("${api.base-url}")
    private String BASE_URL;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAO.class);

    public EmployeeDAO(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Employee> getAllEmployees() {
        String url = BASE_URL + "/employees";
        try {
            EmployeeResponse response = restTemplate.getForObject(url, EmployeeResponse.class);
            return response.getData();
        } catch (HttpClientErrorException.TooManyRequests e) {
            logger.error("Too Many Requests encountered while fetching all employees: {}", e.getMessage());
            throw new TooManyRequestsException("Too Many Requests: Please try again later. " + BASE_URL + " allows 1 request per minute");
        } catch (ResourceAccessException e) {
            logger.error("Network issue encountered while fetching all employees: {}", e.getMessage());
            throw new RuntimeException("Network issue: Please check your connection.");
        } catch (HttpServerErrorException e) {
            logger.error("Server error encountered while fetching all employees: {}", e.getMessage());
            throw new RuntimeException("Server error: Please try again later.");
        } catch (Exception e) {
            logger.error("Unexpected error encountered while fetching all employees: {}", e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String name) {
        List<Employee> allEmployees = getAllEmployees();
        return allEmployees.stream()
                .filter(employee -> employee.getEmployeeName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(String id) {
        String url = BASE_URL + "/employee/" + id;
        try {
            EmployeeResponseSingle response = restTemplate.getForObject(url, EmployeeResponseSingle.class);
            return response.getData();
        } catch (HttpClientErrorException.TooManyRequests e) {
            logger.error("Too Many Requests encountered while fetching employee by ID {}: {}", id, e.getMessage());
            throw new TooManyRequestsException("Too Many Requests: Please try again later. " + BASE_URL + " allows 1 request per minute");
        } catch (ResourceAccessException e) {
            logger.error("Network issue encountered while fetching employee by ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Network issue: Please check your connection.");
        } catch (HttpServerErrorException e) {
            logger.error("Server error encountered while fetching employee by ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Server error: Please try again later.");
        } catch (Exception e) {
            logger.error("Unexpected error encountered while fetching employee by ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        List<Employee> allEmployees = getAllEmployees();
        return allEmployees.stream()
                .mapToInt(Employee::getEmployeeSalary)
                .max()
                .orElse(0);
    }

    @Override
    public List<String> getTop10HighestEarningEmployeeNames() {
        List<Employee> allEmployees = getAllEmployees();
        List<String> topNames = allEmployees.stream()
                .sorted((a, b) -> b.getEmployeeSalary() - a.getEmployeeSalary())
                .limit(10)
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList());
        return topNames;
    }

    @Override
    public Employee createEmployee(String name, int salary, int age) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary must be greater than or equal to 0");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age must be greater than or equal to 0");
        }

        String url = BASE_URL + "/create";
        Employee newEmployee = new Employee(name, salary, age);
        try {
            EmployeeResponseSingle response = restTemplate.postForObject(url, newEmployee, EmployeeResponseSingle.class);
            return response.getData();
        } catch (HttpClientErrorException.TooManyRequests e) {
            logger.error("Too Many Requests encountered while creating employee: {}", e.getMessage());
            throw new TooManyRequestsException("Too Many Requests: Please try again later. " + BASE_URL + " allows 1 request per minute");
        } catch (ResourceAccessException e) {
            logger.error("Network issue encountered while creating employee: {}", e.getMessage());
            throw new RuntimeException("Network issue: Please check your connection.");
        } catch (HttpServerErrorException e) {
            logger.error("Server error encountered while creating employee: {}", e.getMessage());
            throw new RuntimeException("Server error: Please try again later.");
        } catch (Exception e) {
            logger.error("Unexpected error encountered while creating employee: {}", e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }


    @Override
    public String deleteEmployee(String id) {
        String url = BASE_URL + "/delete/" + id;
        try {
            restTemplate.delete(url);
            return "Employee with ID " + id + " deleted successfully.";
        } catch (HttpClientErrorException.TooManyRequests e) {
            logger.error("Too Many Requests encountered while deleting employee with ID {}: {}", id, e.getMessage());
            throw new TooManyRequestsException("Too Many Requests: Please try again later. " + BASE_URL + " allows 1 request per minute");
        } catch (ResourceAccessException e) {
            logger.error("Network issue encountered while deleting employee with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Network issue: Please check your connection.");
        } catch (HttpServerErrorException e) {
            logger.error("Server error encountered while deleting employee with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Server error: Please try again later.");
        } catch (Exception e) {
            logger.error("Unexpected error encountered while deleting employee with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage());
        }
    }
}
