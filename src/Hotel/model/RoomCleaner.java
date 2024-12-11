package Hotel.model;

public class RoomCleaner implements HasId {
    private int id;
    private int roomId;
    private int cleanerId;

    public RoomCleaner(int id, int roomId, int cleanerId) {
        this.id = id;
        this.roomId = roomId;
        this.cleanerId = cleanerId;
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

    public int getCleanerId() {
        return cleanerId;
    }

    public void setCleanerId(int cleanerId) {
        this.cleanerId = cleanerId;
    }
}
