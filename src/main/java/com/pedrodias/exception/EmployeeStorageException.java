package com.pedrodias.exception;

// Thrown when employee data cannot be loaded from or saved to persistent storage.
public class EmployeeStorageException extends EmployeeManagementException {

    public EmployeeStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
