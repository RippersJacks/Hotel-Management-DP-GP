package Hotel.model;

public abstract class Employee implements HasId{
    private Integer id;
    private String name;
    private int salary;
    private String password;

    public Employee(Integer id, String name, int salary, String password) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.password = password;
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
}
