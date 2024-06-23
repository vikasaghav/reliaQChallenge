package com.example.rqchallenge.service;

import com.example.rqchallenge.entities.Employee;
import com.example.rqchallenge.exceptions.TooManyRequestsException;
import com.example.rqchallenge.response.EmployeeResponse;
import com.example.rqchallenge.response.EmployeeResponseSingle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Value("${api.base-url}")
    private String BASE_URL;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Employee> getAllEmployees() {
        String url = BASE_URL + "/employees";
        try {
            EmployeeResponse response = restTemplate.getForObject(url, EmployeeResponse.class);
            return response.getData();
        } catch (HttpClientErrorException.TooManyRequests e) {
            logger.error("Too Many Requests encountered while fetching all employees: {}", e.getMessage());
            throw new TooManyRequestsException("Too Many Requests: Please try again later. " + BASE_URL + " allows 1 request per minute");
        }
    }

    public List<Employee> getEmployeesByNameSearch(String name) {
        return getAllEmployees().stream()
                .filter(employee -> employee.getEmployeeName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById(String id) {
        String url = BASE_URL + "/employee/" + id;
        try {
            EmployeeResponseSingle response = restTemplate.getForObject(url, EmployeeResponseSingle.class);
            return response.getData();
        } catch (HttpClientErrorException.TooManyRequests e) {
            logger.error("Too Many Requests encountered while fetching employee by ID {}: {}", id, e.getMessage());
            throw new TooManyRequestsException("Too Many Requests: Please try again later. " + BASE_URL + " allows 1 request per minute");
        }
    }

    public Integer getHighestSalaryOfEmployees() {
        return getAllEmployees().stream()
                .mapToInt(Employee::getEmployeeSalary)
                .max()
                .orElse(0);
    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        List<String> collect = getAllEmployees().stream()
                .sorted((a,b) -> b.getEmployeeSalary()-a.getEmployeeSalary())
                .limit(10)
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList());
        return collect;
    }

    public Employee createEmployee(String name, int salary, int age) {
        String url = BASE_URL + "/create";
        Employee newEmployee = new Employee(name, salary, age);
        try {
            EmployeeResponseSingle response = restTemplate.postForObject(url, newEmployee, EmployeeResponseSingle.class);
            return response.getData();
        } catch (HttpClientErrorException.TooManyRequests e) {
            logger.error("Too Many Requests encountered while creating employee: {}", e.getMessage());
            throw new TooManyRequestsException("Too Many Requests: Please try again later. " + BASE_URL + " allows 1 request per minute");
        }
    }

    public String deleteEmployee(String id) {
        String url = BASE_URL + "/delete/" + id;
        restTemplate.delete(url);
        return "Employee with ID " + id + " deleted successfully.";
    }
}
