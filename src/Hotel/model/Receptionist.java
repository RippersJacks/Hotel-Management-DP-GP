package Hotel.model;

import java.util.List;

public class Receptionist extends Employee {
    private List<String> languages;

    public Receptionist(int id, String name, int salary, String password, List<String> languages) {
        super(id, name, salary, password);
        this.languages = languages;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
