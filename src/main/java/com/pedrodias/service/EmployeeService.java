package com.pedrodias.service;

import com.pedrodias.exception.DuplicateEmployeeException;
import com.pedrodias.exception.EmployeeNotFoundException;
import com.pedrodias.model.Employee;
import com.pedrodias.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = Objects.requireNonNull(employeeRepository, "EmployeeRepository cannot be null");
    }

    // Validates business rules, then persists a new employee.
    public void addEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");
        validateEmailIsUnique(employee, employee.getId());
        employeeRepository.addEmployee(employee);
    }

    // Ensures the employee exists and the new data respects business rules, then persists the update.
    public void updateEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");
        findById(employee.getId());
        validateEmailIsUnique(employee, employee.getId());
        employeeRepository.updateEmployee(employee);
    }

    // Ensures the employee exists, then removes it.
    public void removeEmployee(int id) {
        findById(id);
        employeeRepository.removeEmployee(id);
    }

    // Retrieves an employee by id, throwing EmployeeNotFoundException if none exists.
    public Employee findById(int id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    // Retrieves all employees currently stored.
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    // Business rule: no two employees may share the same email address.
    private void validateEmailIsUnique(Employee employee, int idToIgnore) {
        boolean emailTaken = employeeRepository.findAll().stream()
                .anyMatch(existing -> existing.getId() != idToIgnore
                        && existing.getEmail().equalsIgnoreCase(employee.getEmail()));
        if (emailTaken) {
            throw new DuplicateEmployeeException("Email " + employee.getEmail() + " is already in use");
        }
    }
}
