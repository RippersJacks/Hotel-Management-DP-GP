package Hotel.model;

public class Manager extends Employee{
    private Integer managedDepartmentID;

    public Manager(Integer id, String name, int salary, String password, int departmentId, Integer managedDepartment) {
        super(id, name, salary, password, departmentId);
        this.managedDepartmentID = managedDepartment;
    }

    public Integer getManagedDepartmentID() {
        return managedDepartmentID;
    }

    public void setManagedDepartmentID(Integer managedDepartmentID) {
        this.managedDepartmentID = managedDepartmentID;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "managedDepartmentID=" + managedDepartmentID +
                '}';
    }
}
