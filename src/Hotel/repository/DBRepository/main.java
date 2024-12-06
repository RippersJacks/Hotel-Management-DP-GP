package Hotel.repository.DBRepository;

import Hotel.model.Room;

public class main {
    public static void main(String[] args) {
        Room room = new Room(2, 10, 10, "Suite", 200, "Available");
        RoomDBRepository roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        roomDBRepository.create(room);
    }
}
