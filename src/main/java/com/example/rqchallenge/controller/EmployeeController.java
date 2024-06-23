package com.example.rqchallenge.controller;

import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.entities.Employee;
import com.example.rqchallenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController implements IEmployeeController {

    private final EmployeeService employeeService;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        logger.info("Retrieved {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        logger.info("Searching employees by name: {}", searchString);
        List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
        logger.info("Found {} employees matching the search criteria", employees.size());
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        logger.info("Fetching employee by ID: {}", id);
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            logger.info("Retrieved employee with ID {}", id);
            return ResponseEntity.ok(employee);
        } else {
            logger.warn("Employee with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        logger.info("Fetching highest salary of employees");
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        logger.info("Highest salary found: {}", highestSalary);
        return ResponseEntity.ok(highestSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        logger.info("Fetching top 10 highest earning employee names");
        List<String> topNames = employeeService.getTop10HighestEarningEmployeeNames();
        logger.info("Top 10 highest earning employee names: {}", topNames);
        return ResponseEntity.ok(topNames);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        String name = (String) employeeInput.get("name");
        int salary = (int) employeeInput.get("salary");
        int age = (int) employeeInput.get("age");
        logger.info("Creating employee: Name={}, Salary={}, Age={}", name, salary, age);
        Employee newEmployee = employeeService.createEmployee(name, salary, age);
        if (newEmployee != null) {
            logger.info("Employee created successfully with ID {}", newEmployee.getId());
            return ResponseEntity.ok(newEmployee);
        } else {
            logger.error("Failed to create employee");
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        logger.info("Deleting employee with ID: {}", id);
        String message = employeeService.deleteEmployee(id);
        logger.info("Deletion response: {}", message);
        return ResponseEntity.ok(message);
    }
}
