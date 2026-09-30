package org.example.payroll;

import java.math.BigDecimal;
import java.util.Objects;

public final class PayrollServiceBlackBoxTest {
    private static int passed;
    private static int failed;

    private PayrollServiceBlackBoxTest() {
    }

    public static void main(String[] args) {
        run("BB-01", "employee management", PayrollServiceBlackBoxTest::employeeManagementWorks);
        run("BB-02", "duplicate employee is rejected", PayrollServiceBlackBoxTest::duplicateEmployeeIsRejected);
        run("BB-03", "missing employees are rejected", PayrollServiceBlackBoxTest::missingEmployeesAreRejected);
        run("BB-04", "invalid employee data is rejected", PayrollServiceBlackBoxTest::invalidEmployeeDataIsRejected);
        run("BB-05", "payment amounts are correct", PayrollServiceBlackBoxTest::paymentAmountsAreCorrect);
        run("BB-06", "payroll report totals are correct", PayrollServiceBlackBoxTest::reportTotalsAreCorrect);
        printSummary("Black-box");
    }

    private static void employeeManagementWorks() {
        PayrollService service = new PayrollService();
        service.addEmployee(employee("E2", "B", "Analyst", "2000"));
        service.addEmployee(employee("E1", "A", "Developer", "1000"));

        checkEquals("E1", service.listEmployees().get(0).id());
        service.updateEmployee(employee("E1", "A", "Lead", "1500"));
        checkEquals("Lead", service.findEmployee("E1").role());
        service.removeEmployee("E2");
        checkEquals(1, service.listEmployees().size());
    }

    private static void duplicateEmployeeIsRejected() {
        PayrollService service = new PayrollService();
        service.addEmployee(employee("E1", "A", "Developer", "1000"));
        expectIllegalArgument(() -> service.addEmployee(employee("E1", "A", "Developer", "1000")));
    }

    private static void missingEmployeesAreRejected() {
        PayrollService service = new PayrollService();
        expectIllegalArgument(() -> service.findEmployee("missing"));
        expectIllegalArgument(() -> service.updateEmployee(employee("missing", "A", "Developer", "1000")));
        expectIllegalArgument(() -> service.removeEmployee("missing"));
    }

    private static void invalidEmployeeDataIsRejected() {
        expectIllegalArgument(() -> employee(" ", "A", "Developer", "1000"));
        expectIllegalArgument(() -> employee("E1", " ", "Developer", "1000"));
        expectIllegalArgument(() -> employee("E1", "A", " ", "1000"));
        expectIllegalArgument(() -> employee("E1", "A", "Developer", "-1"));
        expectIllegalArgument(() -> new Employee("E1", "A", "Developer", null));
    }

    private static void paymentAmountsAreCorrect() {
        PayrollService service = new PayrollService();
        service.addEmployee(employee("E1", "A", "Developer", "2500"));

        PayrollRecord payment = service.processPayment("E1");
        checkEquals(new BigDecimal("150.00"), payment.tax());
        checkEquals(new BigDecimal("2350.00"), payment.netSalary());
    }

    private static void reportTotalsAreCorrect() {
        PayrollService service = new PayrollService();
        service.addEmployee(employee("E1", "A", "Developer", "2500"));
        service.addEmployee(employee("E2", "B", "Analyst", "900"));

        PayrollReport report = service.generateReport();
        checkEquals(new BigDecimal("3400.00"), report.totalGross());
        checkEquals(new BigDecimal("150.00"), report.totalTax());
        checkEquals(new BigDecimal("3250.00"), report.totalNet());
    }

    private static Employee employee(String id, String name, String role, String salary) {
        return new Employee(id, name, role, new BigDecimal(salary));
    }

    private static void expectIllegalArgument(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected IllegalArgumentException");
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

    private static void printSummary(String category) {
        System.out.println(category + " result: " + passed + " passed, " + failed + " failed.");
        if (failed > 0) {
            throw new AssertionError(category + " tests failed");
        }
    }
}
