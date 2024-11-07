package Hotel.model;
import java.util.ArrayList;

public class Department {
    private int id;
    private String name;
    private ArrayList<Employee> employees;

    public Department(int id, String name, ArrayList<Employee> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public int getId() {
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

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public int countEmployees(){
        return employees.size();
    }
}
