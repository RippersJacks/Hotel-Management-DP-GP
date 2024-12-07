package Hotel.model;

import java.util.List;

public class Receptionist extends Employee implements CheckRoom{
    private List<String> languages;

    public Receptionist(Integer id, String name, int salary, String password, int departmentId, List<String> languages) {
        super(id, name, salary, password, departmentId);
        this.languages = languages;
    }

    /**
     * Gets all languages known by the Receptionist.
     * @return list of Strings
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * Changes the list of known languages to a new list for the Receptionist.
     * @param languages list of Strings
     */
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    /**
     * Receives a Room object as a parameter and returns true if it is available.
     * @param room a room object that is either available, unavailable or dirty
     * @return true or false
     */
    @Override   //Interface
    public boolean checkRoom(Room room) {  //only available rooms will have return value True
        return room.getAvailability().equals("Available");
    }

    @Override
    public String toString() {
        return "Receptionist{" +
                "languages=" + languages +
                '}';
    }
}
