package com.pedrodias.exception;

// Thrown when an employee's field data violates a domain validation rule.
public class InvalidEmployeeDataException extends EmployeeManagementException {

    public InvalidEmployeeDataException(String message) {
        super(message);
    }
}
