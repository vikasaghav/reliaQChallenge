package com.example.rqchallenge.service;

import com.example.rqchallenge.dao.EmployeeDAO;
import com.example.rqchallenge.entities.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements EmployeeServiceInterface {

    private final EmployeeDAO employeeDAO;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.info("Fetching all employees");
        List<Employee> employees = employeeDAO.getAllEmployees();
        logger.debug("Fetched {} employees", employees.size());
        return employees;
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String name) {
        logger.info("Searching employees by name: {}", name);
        List<Employee> employees = employeeDAO.getEmployeesByNameSearch(name);
        logger.debug("Found {} employees by name {}", employees.size(), name);
        return employees;
    }

    @Override
    public Employee getEmployeeById(String id) {
        logger.info("Fetching employee by ID: {}", id);
        Employee employee = employeeDAO.getEmployeeById(id);
        if (employee == null) {
            logger.warn("Employee with ID {} not found", id);
        } else {
            logger.debug("Fetched employee: {}", employee);
        }
        return employee;
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        logger.info("Fetching highest salary of employees");
        Integer highestSalary = employeeDAO.getHighestSalaryOfEmployees();
        logger.debug("Highest salary of employees: {}", highestSalary);
        return highestSalary;
    }

    @Override
    public List<String> getTop10HighestEarningEmployeeNames() {
        logger.info("Fetching top 10 highest earning employee names");
        List<String> topNames = employeeDAO.getTop10HighestEarningEmployeeNames();
        logger.debug("Top 10 highest earning employee names: {}", topNames);
        return topNames;
    }

    @Override
    public Employee createEmployee(String name, int salary, int age) {
        logger.info("Creating employee: Name={}, Salary={}, Age={}", name, salary, age);
        Employee newEmployee = employeeDAO.createEmployee(name, salary, age);
        logger.debug("Created employee: {}", newEmployee);
        return newEmployee;
    }

    @Override
    public String deleteEmployee(String id) {
        logger.info("Deleting employee with ID: {}", id);
        String deleteMessage = employeeDAO.deleteEmployee(id);
        logger.debug("Delete message: {}", deleteMessage);
        return deleteMessage;
    }
}
