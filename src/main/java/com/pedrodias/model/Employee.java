package com.pedrodias.model;

import com.pedrodias.exception.InvalidEmployeeDataException;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Employee {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final int id;
    private final String name;
    private final String email;
    private final String position;
    private final String department;
    private final double salary;
    private final LocalDate hireDate;

    public Employee(int id, String name, String email, String position, String department, double salary, LocalDate hireDate) {
        this.id = id;
        this.name = requireNonBlank(name, "Name");
        this.email = requireValidEmail(email);
        this.position = requireNonBlank(position, "Position");
        this.department = requireNonBlank(department, "Department");
        this.salary = requireNonNegative(salary);
        this.hireDate = hireDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidEmployeeDataException(fieldName + " cannot be null or blank");
        }
        return value;
    }

    private static String requireValidEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmployeeDataException("Email is invalid");
        }
        return email;
    }

    private static double requireNonNegative(double salary) {
        if (salary < 0) {
            throw new InvalidEmployeeDataException("Salary cannot be negative");
        }
        return salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                '}';
    }
}
