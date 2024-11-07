package Hotel.model;

public class Cleaner extends Employee {
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
}
