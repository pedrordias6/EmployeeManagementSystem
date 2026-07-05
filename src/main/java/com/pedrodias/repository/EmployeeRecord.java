package com.pedrodias.repository;

import com.pedrodias.model.Employee;

import java.time.LocalDate;

// Plain JSON-serializable snapshot of an Employee, kept separate from the domain
// model so the persistence format never leaks Jackson concerns into it.
class EmployeeRecord {

    private int id;
    private String name;
    private String email;
    private String position;
    private String department;
    private double salary;
    private String hireDate;

    public EmployeeRecord() {
    }

    EmployeeRecord(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.email = employee.getEmail();
        this.position = employee.getPosition();
        this.department = employee.getDepartment();
        this.salary = employee.getSalary();
        this.hireDate = employee.getHireDate().toString();
    }

    Employee toEmployee() {
        return new Employee(id, name, email, position, department, salary, LocalDate.parse(hireDate));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
}
