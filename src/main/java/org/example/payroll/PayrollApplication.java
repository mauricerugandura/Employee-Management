package org.example.payroll;

import java.math.BigDecimal;
import java.util.Scanner;

public final class PayrollApplication {
    private PayrollApplication() {
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("--demo")) {
            runDemo();
            return;
        }

        PayrollService service = new PayrollService();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Employee Payroll System");
            System.out.print("How many employees do you want to enter? ");
            int employeeCount = Integer.parseInt(scanner.nextLine().trim());

            for (int index = 1; index <= employeeCount; index++) {
                System.out.printf("%nEmployee %d%n", index);
                while (true) {
                    try {
                        System.out.print("ID: ");
                        String id = scanner.nextLine().trim();
                        System.out.print("Name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Role: ");
                        String role = scanner.nextLine().trim();
                        System.out.print("Gross salary: ");
                        BigDecimal salary = new BigDecimal(scanner.nextLine().trim());
                        service.addEmployee(new Employee(id, name, role, salary));
                        break;
                    } catch (IllegalArgumentException exception) {
                        System.out.println("Employee was not added: " + exception.getMessage());
                        System.out.println("Please enter this employee again.");
                    }
                }
            }

            printReport(service.generateReport());
        } catch (IllegalArgumentException exception) {
            System.out.println("Input error: " + exception.getMessage());
        }
    }

    private static void runDemo() {
        PayrollService service = new PayrollService();
        service.addEmployee(new Employee("E001", "Amina Yusuf", "Developer", new BigDecimal("2500.00")));
        service.addEmployee(new Employee("E002", "Daniel Mensah", "Analyst", new BigDecimal("900.00")));
        printReport(service.generateReport());
    }

    private static void printReport(PayrollReport report) {
        System.out.println("\nPayroll Report");
        for (PayrollRecord record : report.records()) {
            System.out.printf("%s - %s: Gross=%s, Tax=%s, Net=%s%n",
                    record.employeeId(), record.employeeName(), record.grossSalary(),
                    record.tax(), record.netSalary());
        }
        System.out.printf("Employees: %d%nGross total: %s%nTax total: %s%nNet total: %s%n",
            report.records().size(), report.totalGross(), report.totalTax(), report.totalNet());
    }
}
