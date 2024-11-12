package Hotel.model;

public interface CheckRoom {
    /**
     * An interface used by the Cleaner Class and the Receptionist Class for checking specific properties of a room.
     * @param room object of type Room
     * @return true or false
     */
    boolean checkRoom(Room room);
}
