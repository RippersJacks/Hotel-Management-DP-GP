package Hotel.model;

public class Cleaner extends Employee implements CheckRoom {
    private int floor;

    public Cleaner(Integer id, String name, int salary, String password, int floor) {
        super(id, name, salary, password);
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }


    @Override
    public boolean checkRoom(Room room) {
        return room.getAvailability().equals("Dirty");
    }
}
