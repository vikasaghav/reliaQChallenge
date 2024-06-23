package com.example.rqchallenge.response;

import com.example.rqchallenge.entities.Employee;

import java.util.List;

public class EmployeeResponse {
    private String status;
    private List<Employee> data;

    public EmployeeResponse() {
    }

    public <T> EmployeeResponse(List<T> list) {
        this.data = (List<Employee>) list;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Employee> getData() { return data; }
    public void setData(List<Employee> data) { this.data = data; }
}