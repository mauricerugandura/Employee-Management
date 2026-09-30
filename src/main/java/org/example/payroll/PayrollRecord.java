package org.example.payroll;

import java.math.BigDecimal;

public record PayrollRecord(String employeeId, String employeeName, BigDecimal grossSalary,
                            BigDecimal tax, BigDecimal netSalary) {
}
