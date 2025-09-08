// Employee : id, name, role, managerId, constructor(), toString()
// EmployeeDirectory : employees, addEmployee(), getManager(employeeId), getDirectReports(managerId), printAll()

import java.util.*;

// Employee
class Employee {
    int id;
    String name;
    String role;
    Integer managerId; // null if top-level manager/CEO

    public Employee(int id, String name, String role, Integer managerId) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return id + " " + name + " (" + role + ") manager=" + (managerId == null ? "None" : managerId);
    }
}

// EmployeeDirectory
class EmployeeDirectory {
    Map<Integer, Employee> employees = new HashMap<>();

    public EmployeeDirectory addEmployee(Employee e) {
        employees.put(e.id, e);
        return this;
    }

    public Employee getManager(int empId) {
        Employee e = employees.get(empId);
        if (e == null || e.managerId == null) return null;
        return employees.get(e.managerId);
    }

    public List<Employee> getDirectReports(int managerId) {
        List<Employee> out = new ArrayList<>();
        for (Employee e : employees.values()) {
            if (e.managerId != null && e.managerId == managerId) {
                out.add(e);
            }
        }
        return out;
    }

    public void printAll() {
        for (Employee e : employees.values()) {
            System.out.println(e);
        }
        System.out.println();
    }
}

// Demo
public class EmployeeManagerFinder {
    public static void main(String[] args) {
        EmployeeDirectory dir = new EmployeeDirectory()
            .addEmployee(new Employee(1, "Alice", "CEO", null))
            .addEmployee(new Employee(2, "Bob", "CTO", 1))
            .addEmployee(new Employee(3, "Carol", "CFO", 1))
            .addEmployee(new Employee(4, "Dave", "Engineer", 2))
            .addEmployee(new Employee(5, "Eve", "Engineer", 2))
            .addEmployee(new Employee(6, "Frank", "Accountant", 3));

        System.out.println("All employees:");
        dir.printAll();

        System.out.println("Manager of Dave (id=4):");
        System.out.println(dir.getManager(4));

        System.out.println("Direct reports of Bob (id=2):");
        List<Employee> reports = dir.getDirectReports(2);
        for (Employee r : reports) System.out.println(r);
    }
}
