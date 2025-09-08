// Employee : id, name, department, role, city, yearsExp, salary, constructor(), toString()
// FilterCriteria : nameContains, dept, role, city, minYears, maxYears, minSalary, maxSalary, constructor(), matches(employee)
// EmployeeDirectory : employees, addEmployee(), filter(criteria), sortBy(field), print(list)

import java.util.*;

// Employee
class Employee {
    int id;
    String name;
    String department;
    String role;
    String city;
    int yearsExp;
    double salary;

    public Employee(int id, String name, String department, String role,
                    String city, int yearsExp, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.role = role;
        this.city = city;
        this.yearsExp = yearsExp;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + department + " " + role + " " +
               city + " " + yearsExp + "yrs $" + salary;
    }
}

// FilterCriteria
class FilterCriteria {
    String nameContains;
    String dept;
    String role;
    String city;
    Integer minYears;
    Integer maxYears;
    Double minSalary;
    Double maxSalary;

    public FilterCriteria(String nameContains, String dept, String role, String city,
                          Integer minYears, Integer maxYears,
                          Double minSalary, Double maxSalary) {
        this.nameContains = nameContains;
        this.dept = dept;
        this.role = role;
        this.city = city;
        this.minYears = minYears;
        this.maxYears = maxYears;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    public boolean matches(Employee e) {
        if (nameContains != null && !e.name.toLowerCase().contains(nameContains.toLowerCase())) return false;
        if (dept != null && !dept.equalsIgnoreCase(e.department)) return false;
        if (role != null && !role.equalsIgnoreCase(e.role)) return false;
        if (city != null && !city.equalsIgnoreCase(e.city)) return false;
        if (minYears != null && e.yearsExp < minYears) return false;
        if (maxYears != null && e.yearsExp > maxYears) return false;
        if (minSalary != null && e.salary < minSalary) return false;
        if (maxSalary != null && e.salary > maxSalary) return false;
        return true;
    }
}

// EmployeeDirectory
class EmployeeDirectory {
    List<Employee> employees = new ArrayList<>();

    public EmployeeDirectory addEmployee(Employee e) {
        employees.add(e);
        return this;
    }

    public List<Employee> filter(FilterCriteria c) {
        List<Employee> out = new ArrayList<>();
        for (Employee e : employees) {
            if (c == null || c.matches(e)) out.add(e);
        }
        return out;
    }

    // Ascending-only sort
    public void sortBy(List<Employee> list, String field) {
        Comparator<Employee> cmp;
        switch (field.toLowerCase()) {
            case "name":
                cmp = (e1, e2) -> e1.name.compareToIgnoreCase(e2.name); break;
            case "department":
                cmp = (e1, e2) -> e1.department.compareToIgnoreCase(e2.department); break;
            case "role":
                cmp = (e1, e2) -> e1.role.compareToIgnoreCase(e2.role); break;
            case "city":
                cmp = (e1, e2) -> e1.city.compareToIgnoreCase(e2.city); break;
            case "years":
                cmp = (e1, e2) -> Integer.compare(e1.yearsExp, e2.yearsExp); break;
            case "salary":
                cmp = (e1, e2) -> Double.compare(e1.salary, e2.salary); break;
            default:
                cmp = (e1, e2) -> Integer.compare(e1.id, e2.id);
        }
        list.sort(cmp);
    }

    public void print(List<Employee> list) {
        for (Employee e : list) System.out.println(e);
        System.out.println();
    }
}

// Demo
public class EmployeeListFilter {
    public static void main(String[] args) {
        EmployeeDirectory dir = new EmployeeDirectory()
            .addEmployee(new Employee(1, "Alice", "Engineering", "SDE2", "Seattle", 4, 155000))
            .addEmployee(new Employee(2, "Bob", "Engineering", "SDE1", "Austin", 2, 120000))
            .addEmployee(new Employee(3, "Carol", "Data", "DataEng", "NYC", 5, 165000))
            .addEmployee(new Employee(4, "Dave", "Data", "Analyst", "NYC", 3, 110000))
            .addEmployee(new Employee(5, "Eve", "Product", "PM", "Seattle", 6, 180000))
            .addEmployee(new Employee(6, "Frank", "Engineering", "SDE3", "Seattle", 8, 210000));

        // Filter: Engineering in Seattle with salary >= 150k
        FilterCriteria c1 = new FilterCriteria(null, "Engineering", null, "Seattle",
                                               null, null, 150000.0, null);
        List<Employee> r1 = dir.filter(c1);
        dir.sortBy(r1, "salary"); // ascending only
        dir.print(r1);
    }
}
