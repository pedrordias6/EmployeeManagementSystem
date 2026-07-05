package com.pedrodias.repository;

import com.pedrodias.exception.DuplicateEmployeeException;
import com.pedrodias.exception.EmployeeNotFoundException;
import com.pedrodias.exception.EmployeeStorageException;
import com.pedrodias.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileEmployeeRepositoryTest {

    private static final LocalDate HIRE_DATE = LocalDate.of(2023, 1, 15);

    @TempDir
    private Path tempDir;

    private Path filePath;
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        filePath = tempDir.resolve("employees.json");
        repository = new FileEmployeeRepository(filePath);
    }

    private static Employee employee(int id, String email) {
        return new Employee(id, "Employee " + id, email, "Developer", "Engineering", 5000.0, HIRE_DATE);
    }

    @Test
    void startsEmptyWhenNoFileExistsYet() {
        assertFalse(Files.exists(filePath));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void addEmployeeCreatesTheFileAndPersistsIt() {
        repository.addEmployee(employee(1, "one@example.com"));

        assertTrue(Files.exists(filePath));

        EmployeeRepository reloaded = new FileEmployeeRepository(filePath);
        Employee found = reloaded.findById(1).orElseThrow();
        assertEquals("Employee 1", found.getName());
        assertEquals("one@example.com", found.getEmail());
        assertEquals(HIRE_DATE, found.getHireDate());
    }

    @Test
    void addEmployeeRejectsDuplicateId() {
        repository.addEmployee(employee(1, "one@example.com"));

        assertThrows(DuplicateEmployeeException.class, () -> repository.addEmployee(employee(1, "other@example.com")));
    }

    @Test
    void dataSurvivesAcrossRepositoryInstances() {
        repository.addEmployee(employee(1, "one@example.com"));
        repository.addEmployee(employee(2, "two@example.com"));

        EmployeeRepository reloaded = new FileEmployeeRepository(filePath);

        List<Employee> all = reloaded.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void removeEmployeePersistsTheDeletion() {
        repository.addEmployee(employee(1, "one@example.com"));

        assertTrue(repository.removeEmployee(1));

        EmployeeRepository reloaded = new FileEmployeeRepository(filePath);
        assertTrue(reloaded.findAll().isEmpty());
    }

    @Test
    void removeEmployeeReturnsFalseAndDoesNotTouchTheFileWhenNotFound() throws IOException {
        assertFalse(repository.removeEmployee(404));
        assertFalse(Files.exists(filePath));
    }

    @Test
    void updateEmployeePersistsTheNewData() {
        repository.addEmployee(employee(1, "one@example.com"));
        Employee updated = new Employee(1, "Updated Name", "updated@example.com", "Manager", "Sales", 9000.0, HIRE_DATE);

        repository.updateEmployee(updated);

        EmployeeRepository reloaded = new FileEmployeeRepository(filePath);
        Employee found = reloaded.findById(1).orElseThrow();
        assertEquals("Updated Name", found.getName());
        assertEquals("updated@example.com", found.getEmail());
        assertEquals(9000.0, found.getSalary());
    }

    @Test
    void updateEmployeeRejectsUnknownId() {
        assertThrows(EmployeeNotFoundException.class, () -> repository.updateEmployee(employee(404, "ghost@example.com")));
    }

    @Test
    void findAllReturnsAnUnmodifiableList() {
        repository.addEmployee(employee(1, "one@example.com"));

        List<Employee> all = repository.findAll();

        assertThrows(UnsupportedOperationException.class, () -> all.add(employee(2, "two@example.com")));
    }

    @Test
    void loadingACorruptFileFailsFast() throws IOException {
        Files.writeString(filePath, "{ not valid json ]");

        assertThrows(EmployeeStorageException.class, () -> new FileEmployeeRepository(filePath));
    }
}
