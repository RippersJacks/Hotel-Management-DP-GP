package Hotel.model;

public class Cleaner extends Employee implements CheckRoom{
    private int floor;

    public Cleaner(int id, String name, int salary, String password, int floor) {
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
    public boolean checkRoom(Room room) { //only dirty rooms will have return value True
        return room.getAvailability == "Dirty";
    }

    public void cleanRoom(Room room)  //can only be called for rooms that must be cleaned
    {
        room.setAvailability("Available");
    }
}
