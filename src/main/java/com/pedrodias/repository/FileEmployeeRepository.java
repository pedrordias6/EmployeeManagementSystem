package com.pedrodias.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pedrodias.exception.DuplicateEmployeeException;
import com.pedrodias.exception.EmployeeNotFoundException;
import com.pedrodias.exception.EmployeeStorageException;
import com.pedrodias.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

// Employee repository backed by a JSON file: loads its contents once at
// construction time and rewrites the whole file after every mutation.
@Repository
public class FileEmployeeRepository implements EmployeeRepository {

    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final Path filePath;
    private final Map<Integer, Employee> employeesById = new LinkedHashMap<>();

    @Autowired
    public FileEmployeeRepository(@Value("${employee.storage.file-path:employees.json}") String filePath) {
        this(Path.of(filePath));
    }

    public FileEmployeeRepository(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath, "File path cannot be null");
        load();
    }

    @Override
    public void addEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");
        if (employeesById.containsKey(employee.getId())) {
            throw new DuplicateEmployeeException("Employee with id " + employee.getId() + " already exists");
        }
        employeesById.put(employee.getId(), employee);
        save();
    }

    @Override
    public boolean removeEmployee(int id) {
        boolean removed = employeesById.remove(id) != null;
        if (removed) {
            save();
        }
        return removed;
    }

    @Override
    public Optional<Employee> findById(int id) {
        return Optional.ofNullable(employeesById.get(id));
    }

    @Override
    public List<Employee> findAll() {
        return List.copyOf(employeesById.values());
    }

    @Override
    public void updateEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");
        if (!employeesById.containsKey(employee.getId())) {
            throw new EmployeeNotFoundException(employee.getId());
        }
        employeesById.put(employee.getId(), employee);
        save();
    }

    private void load() {
        if (!Files.exists(filePath)) {
            return;
        }
        try {
            EmployeeRecord[] records = objectMapper.readValue(filePath.toFile(), EmployeeRecord[].class);
            for (EmployeeRecord record : records) {
                Employee employee = record.toEmployee();
                employeesById.put(employee.getId(), employee);
            }
        } catch (IOException e) {
            throw new EmployeeStorageException("Failed to load employees from " + filePath, e);
        }
    }

    private void save() {
        List<EmployeeRecord> records = employeesById.values().stream()
                .map(EmployeeRecord::new)
                .toList();
        try {
            objectMapper.writeValue(filePath.toFile(), records);
        } catch (IOException e) {
            throw new EmployeeStorageException("Failed to save employees to " + filePath, e);
        }
    }
}
