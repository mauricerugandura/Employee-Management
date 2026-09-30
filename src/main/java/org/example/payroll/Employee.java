package org.example.payroll;

import java.math.BigDecimal;

public record Employee(String id, String name, String role, BigDecimal grossSalary) {
    public Employee {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Employee id is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Employee name is required");
        if (role == null || role.isBlank()) throw new IllegalArgumentException("Employee role is required");
        if (grossSalary == null || grossSalary.signum() < 0) {
            throw new IllegalArgumentException("Gross salary must be non-negative");
        }
        grossSalary = grossSalary.setScale(2);
    }
}
