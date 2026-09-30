package org.example.payroll;

import java.math.BigDecimal;
import java.util.List;

public record PayrollReport(List<PayrollRecord> records, BigDecimal totalGross,
                            BigDecimal totalTax, BigDecimal totalNet) {
    public PayrollReport {
        records = List.copyOf(records);
    }
}
