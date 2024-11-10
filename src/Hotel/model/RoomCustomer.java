package Hotel.model;
import java.util.Date;


public class RoomCustomer implements HasId {
    private Integer id;
    private int roomId;
    private int customerId;
    private Date date;

    public RoomCustomer(Integer id, int roomId, int customerId, Date date) {
        this.id = id;
        this.roomId = roomId;
        this.customerId = customerId;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
