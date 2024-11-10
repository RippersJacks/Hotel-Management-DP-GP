package Hotel.model;
import java.util.Date;


public class RoomCustomer implements HasId {
    private Integer id;
    private int roomId;
    private int customerId;
    private Date fromDate;
    private Date untilDate;

    public RoomCustomer(Integer id, int roomId, int customerId, Date fromDate, Date untilDate) {
        this.id = id;
        this.roomId = roomId;
        this.customerId = customerId;
        this.fromDate = fromDate;
        this.untilDate = untilDate;
    }


    @Override
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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date date) {
        this.fromDate = date;
    }

    public Date getUntilDate() {return untilDate;}

    public void setUntilDate(Date untilDate) {this.untilDate = untilDate;}

}
