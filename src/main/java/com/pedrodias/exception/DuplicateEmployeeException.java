package com.pedrodias.exception;

// Thrown when a business rule prevents saving an employee because it duplicates an existing one.
public class DuplicateEmployeeException extends EmployeeManagementException {

    public DuplicateEmployeeException(String message) {
        super(message);
    }
}
