package com.example.rqchallenge.controller;

import com.example.rqchallenge.entities.Employee;
import com.example.rqchallenge.exceptions.NotFoundException;
import com.example.rqchallenge.service.EmployeeServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    private final EmployeeServiceInterface employeeService;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    public EmployeeController(EmployeeServiceInterface employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        try {
            logger.info("Fetching all employees");
            return employeeService.getAllEmployees();
        } catch (Exception e) {
            logger.error("Error fetching all employees: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/search/{searchString}")
    public List<Employee> getEmployeesByNameSearch(@PathVariable @NotBlank String searchString) {
        try {
            logger.info("Searching employees by name: {}", searchString);
            return employeeService.getEmployeesByNameSearch(searchString);
        } catch (Exception e) {
            logger.error("Error searching employees by name {}: {}", searchString, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable @NotBlank String id) {
        try {
            logger.info("Fetching employee by ID: {}", id);
            Employee employee = employeeService.getEmployeeById(id);
            if (employee == null) {
                logger.warn("Employee with ID {} not found", id);
                throw new NotFoundException("Employee not found with ID " + id);
            }
            logger.info("Retrieved employee with ID {}", id);
            return employee;
        } catch (NotFoundException e) {
            logger.warn("Employee with ID {} not found", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching employee with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/highest-salary")
    public Integer getHighestSalaryOfEmployees() {
        try {
            logger.info("Fetching highest salary of employees");
            return employeeService.getHighestSalaryOfEmployees();
        } catch (Exception e) {
            logger.error("Error fetching highest salary of employees: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/top-ten-highest-earning-names")
    public List<String> getTopTenHighestEarningEmployeeNames() {
        try {
            logger.info("Fetching top 10 highest earning employee names");
            return employeeService.getTop10HighestEarningEmployeeNames();
        } catch (Exception e) {
            logger.error("Error fetching top 10 highest earning employee names: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@Valid @RequestBody Map<String, Object> employeeInput) {
        try {
            String name = (String) employeeInput.get("name");
            Integer salary = Integer.parseInt(String.valueOf(employeeInput.get("salary")));
            Integer age = Integer.parseInt(String.valueOf(employeeInput.get("age")));

            if (name == null || name.isBlank()) {
                logger.warn("Invalid employee name: {}", name);
                throw new IllegalArgumentException("Employee name cannot be blank");
            }
            if (salary < 0) {
                logger.warn("Invalid employee salary: {}", salary);
                throw new IllegalArgumentException("Employee salary must be a positive number");
            }
            if (age < 0) {
                logger.warn("Invalid employee age: {}", age);
                throw new IllegalArgumentException("Employee age must be a positive number");
            }

            logger.info("Creating employee: Name={}, Salary={}, Age={}", name, salary, age);
            return employeeService.createEmployee(name, salary, age);
        } catch (NumberFormatException e) {
            logger.error("Error parsing salary or age: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid salary or age format");
        } catch (Exception e) {
            logger.error("Error creating employee: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteEmployeeById(@PathVariable @NotBlank String id) {
        try {
            logger.info("Deleting employee with ID: {}", id);
            return employeeService.deleteEmployee(id);
        } catch (Exception e) {
            logger.error("Error deleting employee with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}
