package com.example.rqchallenge.response;

import com.example.rqchallenge.entities.Employee;

public class EmployeeResponseSingle {
    private String status;
    private Employee data;

    public EmployeeResponseSingle() {

    }
    public EmployeeResponseSingle(Employee employee) {
        this.data = employee;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Employee getData() { return data; }
    public void setData(Employee data) { this.data = data; }
}