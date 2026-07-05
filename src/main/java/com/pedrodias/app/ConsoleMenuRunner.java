package com.pedrodias.app;

import com.pedrodias.exception.EmployeeManagementException;
import com.pedrodias.model.Employee;
import com.pedrodias.service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleMenuRunner implements CommandLineRunner {

    private final EmployeeService employeeService;

    public ConsoleMenuRunner(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void run(String... args) {
        try (Scanner scanner = new Scanner(System.in)) {
            runMenuLoop(scanner);
        }
    }

    private void runMenuLoop(Scanner scanner) {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addEmployee(scanner);
                    case "2" -> listEmployees();
                    case "3" -> searchEmployee(scanner);
                    case "4" -> updateEmployee(scanner);
                    case "5" -> removeEmployee(scanner);
                    case "6" -> running = false;
                    default -> System.out.println("Invalid option. Please choose a number between 1 and 6.");
                }
            } catch (EmployeeManagementException | NumberFormatException | DateTimeParseException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n=== Employee Management System ===");
        System.out.println("1. Add Employee");
        System.out.println("2. List Employees");
        System.out.println("3. Search Employee");
        System.out.println("4. Update Employee");
        System.out.println("5. Remove Employee");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private void addEmployee(Scanner scanner) {
        int id = readInt(scanner, "Id: ");
        Employee employee = readEmployeeData(scanner, id);
        employeeService.addEmployee(employee);
        System.out.println("Employee added successfully.");
    }

    private void listEmployees() {
        List<Employee> employees = employeeService.findAll();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        employees.forEach(System.out::println);
    }

    private void searchEmployee(Scanner scanner) {
        int id = readInt(scanner, "Id: ");
        System.out.println(employeeService.findById(id));
    }

    private void updateEmployee(Scanner scanner) {
        int id = readInt(scanner, "Id of the employee to update: ");
        employeeService.findById(id);
        Employee updated = readEmployeeData(scanner, id);
        employeeService.updateEmployee(updated);
        System.out.println("Employee updated successfully.");
    }

    private void removeEmployee(Scanner scanner) {
        int id = readInt(scanner, "Id of the employee to remove: ");
        employeeService.removeEmployee(id);
        System.out.println("Employee removed successfully.");
    }

    private Employee readEmployeeData(Scanner scanner, int id) {
        String name = readString(scanner, "Name: ");
        String email = readString(scanner, "Email: ");
        String position = readString(scanner, "Position: ");
        String department = readString(scanner, "Department: ");
        double salary = readDouble(scanner, "Salary: ");
        LocalDate hireDate = LocalDate.parse(readString(scanner, "Hire date (yyyy-MM-dd): "));
        return new Employee(id, name, email, position, department, salary, hireDate);
    }

    private String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(scanner.nextLine().trim());
    }
}
