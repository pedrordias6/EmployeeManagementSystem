package com.pedrodias.repository;

import com.pedrodias.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    // Adds a new employee to the repository, rejecting duplicates by id.
    void addEmployee(Employee employee);

    // Removes the employee with the given id, if present.
    boolean removeEmployee(int id);

    // Finds an employee by id, returning an empty Optional if none is found.
    Optional<Employee> findById(int id);

    // Returns an unmodifiable view of all stored employees.
    List<Employee> findAll();

    // Replaces the stored employee that shares the given employee's id with the new data.
    void updateEmployee(Employee employee);
}
