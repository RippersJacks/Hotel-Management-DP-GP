package Hotel.model;

public class Manager extends Employee{
    private Department managedDepartment;

    public Manager(Integer id, String name, int salary, String password, Department managedDepartment) {
        super(id, name, salary, password);
        this.managedDepartment = managedDepartment;
    }

    public Department getManagedDepartment() {
        return managedDepartment;
    }

    public void setManagedDepartment(Department managedDepartment) {
        this.managedDepartment = managedDepartment;
    }
}
