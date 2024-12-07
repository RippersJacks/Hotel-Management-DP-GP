package Hotel.model;

public class Cleaner extends Employee implements CheckRoom {
    private int floor;

    public Cleaner(Integer id, String name, int salary, String password, int departmentId, int floor) {
        super(id, name, salary,password, departmentId);
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * Returns true if the room given as a parameter is of availability "Dirty".
     * @param room object of type Room
     * @return true or false
     */
    @Override
    public boolean checkRoom(Room room) {
        return room.getAvailability().equals("Dirty");
    }

    @Override
    public String toString() {
        return "Cleaner{" +
                "floor=" + floor +
                '}';
    }
}
