package org.example.payroll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class PayrollServiceWhiteBoxTest {
    @Test
    void taxBranchForTaxFreeSalary() {
        assertEquals(new BigDecimal("0.00"), PayrollService.calculateTax(new BigDecimal("1000.00")));
    }

    @Test
    void taxBranchForSecondBandSalary() {
        assertEquals(new BigDecimal("200.00"), PayrollService.calculateTax(new BigDecimal("3000.00")));
    }

    @Test
    void taxBranchForTopBandSalary() {
        assertEquals(new BigDecimal("400.00"), PayrollService.calculateTax(new BigDecimal("4000.00")));
    }

    @Test
    void emptyReportUsesZeroTotals() {
        PayrollReport report = new PayrollService().generateReport();
        assertEquals(new BigDecimal("0.00"), report.totalGross());
        assertEquals(new BigDecimal("0.00"), report.totalTax());
        assertEquals(new BigDecimal("0.00"), report.totalNet());
    }
}
