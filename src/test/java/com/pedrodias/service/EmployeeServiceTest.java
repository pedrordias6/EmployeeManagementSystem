package com.pedrodias.service;

import com.pedrodias.exception.DuplicateEmployeeException;
import com.pedrodias.exception.EmployeeNotFoundException;
import com.pedrodias.model.Employee;
import com.pedrodias.repository.EmployeeRepository;
import com.pedrodias.repository.FileEmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeServiceTest {

    private static final LocalDate HIRE_DATE = LocalDate.of(2023, 1, 15);

    @TempDir
    private Path tempDir;

    private EmployeeRepository repository;
    private EmployeeService service;

    @BeforeEach
    void setUp() {
        repository = new FileEmployeeRepository(tempDir.resolve("employees.json"));
        service = new EmployeeService(repository);
    }

    private static Employee employee(int id, String email) {
        return new Employee(id, "Employee " + id, email, "Developer", "Engineering", 5000.0, HIRE_DATE);
    }

    @Test
    void constructorRejectsNullRepository() {
        assertThrows(NullPointerException.class, () -> new EmployeeService(null));
    }

    @Test
    void addEmployeePersistsEmployeeThroughRepository() {
        Employee employee = employee(1, "one@example.com");

        service.addEmployee(employee);

        assertSame(employee, service.findById(1));
    }

    @Test
    void addEmployeeRejectsNull() {
        assertThrows(NullPointerException.class, () -> service.addEmployee(null));
    }

    @Test
    void addEmployeeRejectsDuplicateEmailCaseInsensitively() {
        service.addEmployee(employee(1, "one@example.com"));
        Employee duplicate = employee(2, "ONE@EXAMPLE.COM");

        assertThrows(DuplicateEmployeeException.class, () -> service.addEmployee(duplicate));
        assertTrue(repository.findById(2).isEmpty());
    }

    @Test
    void addEmployeePropagatesDuplicateIdFromRepository() {
        service.addEmployee(employee(1, "one@example.com"));

        assertThrows(DuplicateEmployeeException.class, () -> service.addEmployee(employee(1, "two@example.com")));
    }

    @Test
    void updateEmployeeReplacesExistingRecord() {
        service.addEmployee(employee(1, "one@example.com"));
        Employee updated = new Employee(1, "Updated Name", "updated@example.com", "Manager", "Sales", 9000.0, HIRE_DATE);

        service.updateEmployee(updated);

        Employee found = service.findById(1);
        assertEquals("Updated Name", found.getName());
        assertEquals("updated@example.com", found.getEmail());
        assertEquals(9000.0, found.getSalary());
    }

    @Test
    void updateEmployeeAllowsKeepingItsOwnEmail() {
        service.addEmployee(employee(1, "one@example.com"));
        Employee updated = new Employee(1, "New Name", "one@example.com", "Manager", "Sales", 6000.0, HIRE_DATE);

        assertDoesNotThrow(() -> service.updateEmployee(updated));
        assertEquals("New Name", service.findById(1).getName());
    }

    @Test
    void updateEmployeeRejectsUnknownId() {
        Employee unknown = employee(404, "ghost@example.com");

        assertThrows(EmployeeNotFoundException.class, () -> service.updateEmployee(unknown));
    }

    @Test
    void updateEmployeeRejectsEmailAlreadyUsedByAnotherEmployee() {
        service.addEmployee(employee(1, "one@example.com"));
        service.addEmployee(employee(2, "two@example.com"));
        Employee conflicting = new Employee(2, "Employee 2", "one@example.com", "Developer", "Engineering", 5000.0, HIRE_DATE);

        assertThrows(DuplicateEmployeeException.class, () -> service.updateEmployee(conflicting));
        assertEquals("two@example.com", service.findById(2).getEmail());
    }

    @Test
    void updateEmployeeRejectsNull() {
        assertThrows(NullPointerException.class, () -> service.updateEmployee(null));
    }

    @Test
    void removeEmployeeDeletesExistingEmployee() {
        service.addEmployee(employee(1, "one@example.com"));

        service.removeEmployee(1);

        assertThrows(EmployeeNotFoundException.class, () -> service.findById(1));
    }

    @Test
    void removeEmployeeRejectsUnknownId() {
        assertThrows(EmployeeNotFoundException.class, () -> service.removeEmployee(404));
    }

    @Test
    void findByIdReturnsStoredEmployee() {
        Employee employee = employee(1, "one@example.com");
        service.addEmployee(employee);

        assertSame(employee, service.findById(1));
    }

    @Test
    void findByIdThrowsWhenMissing() {
        assertThrows(EmployeeNotFoundException.class, () -> service.findById(404));
    }

    @Test
    void findAllReturnsEmptyListWhenNoEmployeesExist() {
        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void findAllReturnsEveryStoredEmployee() {
        service.addEmployee(employee(1, "one@example.com"));
        service.addEmployee(employee(2, "two@example.com"));

        List<Employee> all = service.findAll();

        assertEquals(2, all.size());
    }
}
