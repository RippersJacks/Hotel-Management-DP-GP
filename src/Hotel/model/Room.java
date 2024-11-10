package Hotel.model;

public class Room implements HasId {
    private int id;
    private int floor;
    private int number;
    private String type;
    private int pricePerNight;
    private String availability;

    public Room(int id, int floor, int number, String type, int pricePerNight, String availability) {
        this.id = id;
        this.floor = floor;
        this.number = number;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public int getFloor() {
        return floor;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPricePerNight(int pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}


