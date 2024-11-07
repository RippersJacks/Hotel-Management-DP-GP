package Hotel.model;

import java.util.List;

public class Receptionist extends Employee implements CheckRoom{
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

    @Override   //Interface
    public boolean checkRoom(Room room) {  //only available rooms will have return value True
        return room.getAvailability() == "Avaible";
    }

    ///TO-DO: checkout (once a client is removed, the room becomes available)
    ///TO-DO: adapt code by what will be happening in the repository
    void addClient(Client client) {
        customerRepo.add(client);
    }
    void removeClient(Client client) {
        customerRepo.remove(client);
    }
    void editClient(Client client) {
        customerRepo.update(client);
    }
}
