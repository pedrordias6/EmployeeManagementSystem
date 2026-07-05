package com.pedrodias.model;

import com.pedrodias.exception.InvalidEmployeeDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeTest {

    private static final LocalDate HIRE_DATE = LocalDate.of(2023, 1, 15);

    private static Employee validEmployee() {
        return new Employee(1, "Ana Silva", "ana@example.com", "Developer", "Engineering", 5000.0, HIRE_DATE);
    }

    @Test
    void constructorStoresAllFields() {
        Employee employee = validEmployee();

        assertEquals(1, employee.getId());
        assertEquals("Ana Silva", employee.getName());
        assertEquals("ana@example.com", employee.getEmail());
        assertEquals("Developer", employee.getPosition());
        assertEquals("Engineering", employee.getDepartment());
        assertEquals(5000.0, employee.getSalary());
        assertEquals(HIRE_DATE, employee.getHireDate());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void constructorRejectsBlankName(String invalidName) {
        assertThrows(InvalidEmployeeDataException.class,
                () -> new Employee(1, invalidName, "ana@example.com", "Developer", "Engineering", 5000.0, HIRE_DATE));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void constructorRejectsBlankPosition(String invalidPosition) {
        assertThrows(InvalidEmployeeDataException.class,
                () -> new Employee(1, "Ana Silva", "ana@example.com", invalidPosition, "Engineering", 5000.0, HIRE_DATE));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void constructorRejectsBlankDepartment(String invalidDepartment) {
        assertThrows(InvalidEmployeeDataException.class,
                () -> new Employee(1, "Ana Silva", "ana@example.com", "Developer", invalidDepartment, 5000.0, HIRE_DATE));
    }

    @Test
    void constructorRejectsNullEmail() {
        assertThrows(InvalidEmployeeDataException.class,
                () -> new Employee(1, "Ana Silva", null, "Developer", "Engineering", 5000.0, HIRE_DATE));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "plainaddress", "@no-local-part.com", "no-domain@", "with space@example.com", "missing-dot@examplecom"})
    void constructorRejectsInvalidEmailFormat(String invalidEmail) {
        assertThrows(InvalidEmployeeDataException.class,
                () -> new Employee(1, "Ana Silva", invalidEmail, "Developer", "Engineering", 5000.0, HIRE_DATE));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -1, -100.5})
    void constructorRejectsNegativeSalary(double invalidSalary) {
        assertThrows(InvalidEmployeeDataException.class,
                () -> new Employee(1, "Ana Silva", "ana@example.com", "Developer", "Engineering", invalidSalary, HIRE_DATE));
    }

    @Test
    void constructorAcceptsZeroSalary() {
        Employee employee = new Employee(1, "Ana Silva", "ana@example.com", "Developer", "Engineering", 0.0, HIRE_DATE);

        assertEquals(0.0, employee.getSalary());
    }

    @Test
    void equalsAndHashCodeAreBasedOnIdOnly() {
        Employee employee1 = new Employee(1, "Ana Silva", "ana@example.com", "Developer", "Engineering", 5000.0, HIRE_DATE);
        Employee employee2 = new Employee(1, "Different Name", "different@example.com", "Manager", "Sales", 9000.0, HIRE_DATE);
        Employee employee3 = new Employee(2, "Ana Silva", "ana@example.com", "Developer", "Engineering", 5000.0, HIRE_DATE);

        assertEquals(employee1, employee2);
        assertEquals(employee1.hashCode(), employee2.hashCode());
        assertNotEquals(employee1, employee3);
    }

    @Test
    void equalsHandlesSelfNullAndOtherTypes() {
        Employee employee = validEmployee();

        assertEquals(employee, employee);
        assertNotEquals(null, employee);
        assertNotEquals("not an employee", employee);
    }

    @Test
    void toStringContainsAllFieldValues() {
        String text = validEmployee().toString();

        assertTrue(text.contains("id=1"));
        assertTrue(text.contains("Ana Silva"));
        assertTrue(text.contains("ana@example.com"));
        assertTrue(text.contains("Developer"));
        assertTrue(text.contains("Engineering"));
        assertTrue(text.contains("5000.0"));
        assertTrue(text.contains("2023-01-15"));
    }
}
