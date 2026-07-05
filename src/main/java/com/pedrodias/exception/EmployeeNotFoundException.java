package com.pedrodias.exception;

// Thrown when an employee lookup by id yields no result.
public class EmployeeNotFoundException extends EmployeeManagementException {

    public EmployeeNotFoundException(int id) {
        super("Employee with id " + id + " was not found");
    }
}
