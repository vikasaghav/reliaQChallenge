package com.example.rqchallenge.service;

import com.example.rqchallenge.entities.Employee;

import java.util.List;

public interface EmployeeServiceInterface {

    List<Employee> getAllEmployees();

    List<Employee> getEmployeesByNameSearch(String name);

    Employee getEmployeeById(String id);

    Integer getHighestSalaryOfEmployees();

    List<String> getTop10HighestEarningEmployeeNames();

    Employee createEmployee(String name, int salary, int age);

    String deleteEmployee(String id);
}
