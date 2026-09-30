package org.example.payroll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class PayrollServiceBlackBoxTest {
    @Test
    void employeeCanBeAddedUpdatedFoundListedAndRemoved() {
        PayrollService service = new PayrollService();
        service.addEmployee(employee("E2", "B", "Analyst", "2000"));
        service.addEmployee(employee("E1", "A", "Developer", "1000"));

        assertEquals("E1", service.listEmployees().get(0).id());
        service.updateEmployee(employee("E1", "A", "Lead", "1500"));
        assertEquals("Lead", service.findEmployee("E1").role());
        service.removeEmployee("E2");
        assertEquals(1, service.listEmployees().size());
    }

    @Test
    void duplicateAndMissingEmployeesAreRejected() {
        PayrollService service = new PayrollService();
        service.addEmployee(employee("E1", "A", "Developer", "1000"));

        assertThrows(IllegalArgumentException.class,
                () -> service.addEmployee(employee("E1", "A", "Developer", "1000")));
        assertThrows(IllegalArgumentException.class, () -> service.findEmployee("missing"));
        assertThrows(IllegalArgumentException.class, () -> service.updateEmployee(employee("missing", "A", "Developer", "1000")));
        assertThrows(IllegalArgumentException.class, () -> service.removeEmployee("missing"));
    }

        @Test
        void invalidEmployeeFieldsAndSalaryAreRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> employee(" ", "A", "Developer", "1000"));
        assertThrows(IllegalArgumentException.class,
            () -> employee("E1", " ", "Developer", "1000"));
        assertThrows(IllegalArgumentException.class,
            () -> employee("E1", "A", " ", "1000"));
        assertThrows(IllegalArgumentException.class,
            () -> employee("E1", "A", "Developer", "-1"));
        assertThrows(IllegalArgumentException.class,
            () -> new Employee("E1", "A", "Developer", null));
        }

    @Test
    void paymentAndReportReturnExpectedAmounts() {
        PayrollService service = new PayrollService();
        service.addEmployee(employee("E1", "A", "Developer", "2500"));
        service.addEmployee(employee("E2", "B", "Analyst", "900"));

        PayrollRecord payment = service.processPayment("E1");
        assertEquals(new BigDecimal("150.00"), payment.tax());
        assertEquals(new BigDecimal("2350.00"), payment.netSalary());

        PayrollReport report = service.generateReport();
        assertEquals(new BigDecimal("3400.00"), report.totalGross());
        assertEquals(new BigDecimal("150.00"), report.totalTax());
        assertEquals(new BigDecimal("3250.00"), report.totalNet());
    }

    private static Employee employee(String id, String name, String role, String salary) {
        return new Employee(id, name, role, new BigDecimal(salary));
    }
}
