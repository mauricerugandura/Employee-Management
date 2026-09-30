package org.example.payroll;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PayrollService {
    private static final BigDecimal TAX_FREE_LIMIT = new BigDecimal("1000.00");
    private static final BigDecimal SECOND_BAND_LIMIT = new BigDecimal("3000.00");
    private static final BigDecimal SECOND_BAND_RATE = new BigDecimal("0.10");
    private static final BigDecimal TOP_BAND_RATE = new BigDecimal("0.20");

    private final Map<String, Employee> employees = new LinkedHashMap<>();

    public void addEmployee(Employee employee) {
        if (employees.putIfAbsent(employee.id(), employee) != null) {
            throw new IllegalArgumentException("Employee already exists: " + employee.id());
        }
    }

    public void updateEmployee(Employee employee) {
        requireEmployee(employee.id());
        employees.put(employee.id(), employee);
    }

    public void removeEmployee(String employeeId) {
        if (employees.remove(employeeId) == null) {
            throw new IllegalArgumentException("Employee not found: " + employeeId);
        }
    }

    public Employee findEmployee(String employeeId) {
        return requireEmployee(employeeId);
    }

    public List<Employee> listEmployees() {
        return employees.values().stream()
                .sorted(Comparator.comparing(Employee::id))
                .toList();
    }

    public PayrollRecord processPayment(String employeeId) {
        Employee employee = requireEmployee(employeeId);
        BigDecimal gross = employee.grossSalary();
        BigDecimal tax = calculateTax(gross);
        return new PayrollRecord(employee.id(), employee.name(), gross, tax, gross.subtract(tax));
    }

    public PayrollReport generateReport() {
        List<PayrollRecord> records = new ArrayList<>();
        for (Employee employee : listEmployees()) {
            records.add(processPayment(employee.id()));
        }
        BigDecimal totalGross = sum(records, PayrollRecord::grossSalary);
        BigDecimal totalTax = sum(records, PayrollRecord::tax);
        BigDecimal totalNet = sum(records, PayrollRecord::netSalary);
        return new PayrollReport(records, totalGross, totalTax, totalNet);
    }

    static BigDecimal calculateTax(BigDecimal gross) {
        BigDecimal tax;
        if (gross.compareTo(TAX_FREE_LIMIT) <= 0) {
            tax = BigDecimal.ZERO;
        } else if (gross.compareTo(SECOND_BAND_LIMIT) <= 0) {
            tax = gross.subtract(TAX_FREE_LIMIT).multiply(SECOND_BAND_RATE);
        } else {
            tax = SECOND_BAND_LIMIT.subtract(TAX_FREE_LIMIT).multiply(SECOND_BAND_RATE)
                    .add(gross.subtract(SECOND_BAND_LIMIT).multiply(TOP_BAND_RATE));
        }
        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    private Employee requireEmployee(String employeeId) {
        Employee employee = employees.get(employeeId);
        if (employee == null) throw new IllegalArgumentException("Employee not found: " + employeeId);
        return employee;
    }

    private static BigDecimal sum(List<PayrollRecord> records,
                                  java.util.function.Function<PayrollRecord, BigDecimal> selector) {
        return records.stream().map(selector).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }
}
