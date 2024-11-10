package Hotel.model;

import java.util.List;

public class Receptionist extends Employee implements CheckRoom{
    private List<String> languages;

    public Receptionist(Integer id, String name, int salary, String password, List<String> languages) {
        super(id, name, salary, password);
        this.languages = languages;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    @Override   //Interface
    public boolean checkRoom(Room room) {  //only available rooms will have return value True
        return room.getAvailability().equals("Avaible");
    }
}
