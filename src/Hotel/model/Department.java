package Hotel.model;
import java.util.ArrayList;
import java.util.List;

public class Department implements HasId {

    private Integer id;
    private String name;
    private List<Employee> employees;

    /**
     * Class: Department; contains an ID, a name and a list of all employees that belong to the department
     * @param id The ID of the department.
     * @param name The name of the department.
     * @param employees The list with all employees of the department.
     */
    public Department(Integer id, String name, List<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
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

    /**
     * Returns all employees of the department.
     * @return arraylist of employees of the department
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * Replaces the current employee list of the department with a new one.
     * @param employees arraylist of employees
     */
    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    /**
     * Returns the total amount of employees in the department.
     * @return number of type int
     */
    public int countEmployees(){
        return employees.size();
    }
}
