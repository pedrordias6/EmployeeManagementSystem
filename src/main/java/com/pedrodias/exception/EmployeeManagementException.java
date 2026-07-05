package com.pedrodias.exception;

// Common base for every business-rule violation raised by the employee management domain.
public abstract class EmployeeManagementException extends RuntimeException {

    protected EmployeeManagementException(String message) {
        super(message);
    }

    protected EmployeeManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}
