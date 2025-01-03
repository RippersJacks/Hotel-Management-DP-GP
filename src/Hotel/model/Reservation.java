package Hotel.model;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class Reservation implements HasId, Comparable<Reservation> {
    private Integer id;
    private int roomId;
    private int customerId;
    private Date CheckIn;
    private Date CheckOut;

    public Reservation(Integer id, int roomId, int customerId, Date CheckIn, Date CheckOut) {
        this.id = id;
        this.roomId = roomId;
        this.customerId = customerId;
        this.CheckIn = CheckIn;
        this.CheckOut = CheckOut;
    }


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "RoomCustomer{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", customerId=" + customerId +
                ", fromDate=" + CheckIn +
                ", untilDate=" + CheckOut +
                '}';
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

    public Date getCheckIn() {
        return CheckIn;
    }

    public void setCheckIn(Date date) {
        this.CheckIn = date;
    }

    public Date getCheckOut() {return CheckOut;}

    public void setCheckOut(Date checkOut) {this.CheckOut = checkOut;}

    @Override
    public int compareTo(Reservation other) {
        // Convert both dates to LocalDate
        LocalDate thisUntilDate = this.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate otherUntilDate = other.getCheckOut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Compare the dates directly (compares entire date, not just the day)
        return thisUntilDate.compareTo(otherUntilDate);
    }
}
