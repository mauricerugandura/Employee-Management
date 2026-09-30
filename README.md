# Employee Payroll System

Java 17 implementation for the homework scenario: employee management, payment processing, and reporting.

## Run

```text
mvn package
java -cp target/classes org.example.payroll.PayrollApplication
```

The normal application run asks you to enter the number of employees, then each employee's ID, name, role, and gross salary. Salary values use `BigDecimal` and are rounded to two decimal places.

For a presentation, enter values in this order:

```text
How many employees do you want to enter? 2
ID: E101
Name: Sara Ali
Role: Developer
Gross salary: 1000
ID: E102
Name: John Doe
Role: Manager
Gross salary: 4000
```

The program then prints each employee's gross salary, tax, and net salary, followed by report totals. To run the original sample data without entering values, use:

```text
java -cp target/classes org.example.payroll.PayrollApplication --demo
```

## Run Tests

The test programs use plain Java checks and do not require JUnit or another testing framework. Compile them and run each test program with:

```text
mvn test-compile
java -cp "target/classes;target/test-classes" org.example.payroll.PayrollServiceBlackBoxTest
java -cp "target/classes;target/test-classes" org.example.payroll.PayrollServiceWhiteBoxTest
```

Each program prints a PASS or FAIL line for every case and a final count.

## Functional requirements

1. The system shall add an employee with an id, name, role, and gross salary.
2. The system shall update, find, list, and remove employees.
3. The system shall reject duplicate ids, missing employees, invalid required fields, and negative salaries.
4. The system shall process an employee payment and return gross salary, tax, and net salary.
5. The system shall generate a payroll report containing each payment and gross, tax, and net totals.
6. The system shall calculate tax progressively: 0% through 1,000; 10% from 1,000 to 3,000; 20% above 3,000.
