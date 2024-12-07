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
     */
    public Department(Integer id, String name) {
        this.id = id;
        this.name = name;
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



    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employees=" + employees +
                '}';
    }
}
