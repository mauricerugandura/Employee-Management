package org.example.payroll;

import java.math.BigDecimal;
import java.util.Objects;

public final class PayrollServiceWhiteBoxTest {
    private static int passed;
    private static int failed;

    private PayrollServiceWhiteBoxTest() {
    }

    public static void main(String[] args) {
        run("WB-01", "tax-free branch", () -> checkEquals(
                new BigDecimal("0.00"), PayrollService.calculateTax(new BigDecimal("1000.00"))));
        run("WB-02", "second tax-band branch", () -> checkEquals(
                new BigDecimal("200.00"), PayrollService.calculateTax(new BigDecimal("3000.00"))));
        run("WB-03", "top tax-band branch", () -> checkEquals(
                new BigDecimal("400.00"), PayrollService.calculateTax(new BigDecimal("4000.00"))));
        run("WB-04", "empty report uses zero totals", PayrollServiceWhiteBoxTest::emptyReportUsesZeroTotals);
        printSummary();
    }

    private static void emptyReportUsesZeroTotals() {
        PayrollReport report = new PayrollService().generateReport();
        checkEquals(new BigDecimal("0.00"), report.totalGross());
        checkEquals(new BigDecimal("0.00"), report.totalTax());
        checkEquals(new BigDecimal("0.00"), report.totalNet());
    }

    private static void checkEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private static void run(String id, String description, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS " + id + ": " + description);
        } catch (RuntimeException | AssertionError error) {
            failed++;
            System.out.println("FAIL " + id + ": " + description + " (" + error.getMessage() + ")");
        }
    }

    private static void printSummary() {
        System.out.println("White-box result: " + passed + " passed, " + failed + " failed.");
        if (failed > 0) {
            throw new AssertionError("White-box tests failed");
        }
    }
}
