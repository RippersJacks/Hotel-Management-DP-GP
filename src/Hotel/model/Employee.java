package Hotel.model;

public abstract class Employee implements HasId, Comparable<Employee> {
    private Integer id;
    private String name;
    private int salary;
    private String password;
    private int DepartmentId;

    public Employee(Integer id, String name, int salary, String password, Integer DepartmentId) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.password = password;
        this.DepartmentId = DepartmentId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(int departmentId) {
        DepartmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", password='" + password + '\'' +
                '}';
    }


    @Override
    public int compareTo(Employee employee) {
        return Integer.compare(this.getSalary(), employee.getSalary());
    }
}
