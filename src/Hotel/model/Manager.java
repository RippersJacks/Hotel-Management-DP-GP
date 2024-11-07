package Hotel.model;

public class Manager extends Employee{
    private Department managedDepartment;

    public Manager(int id, String name, int salary, String password, Department managedDepartment) {
        super(id, name, salary, password);
        this.managedDepartment = managedDepartment;
    }

    public Department getManagedDepartment() {
        return managedDepartment;
    }

    public void setManagedDepartment(Department managedDepartment) {
        this.managedDepartment = managedDepartment;
    }

    ///TO-DO: adapt code by what will be happening in the repository
    public void addEmployee(Employee employee){
        employeeRepo.add(employee);
    }
    public void fireEmployee(Employee employee){
        employeeRepo.remove(employee);
    }
    public void editEmployee(Employee employee){
        employeeRepo.update(employee);
    }

}
