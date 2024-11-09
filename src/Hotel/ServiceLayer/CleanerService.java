package Hotel.ServiceLayer;

import Hotel.model.CheckRoom;
import Hotel.model.Room;

import java.util.ArrayList;
import java.util.List;

public class CleanerService implements CheckRoom {
    @Override
    public boolean checkRoom(Room room) { //only dirty rooms will have return value True
        return room.getAvailability().equals("Dirty") ;
    }

    public void checkDirtyRooms(){
        RoomRepository roomRepo = new RoomRepository();
        List<Room> roomList = roomRepo.getAll();
        List<Room> dirtyRoomList = new ArrayList<>();

        CleanerService cleanerService = new CleanerService();
        for (Room room : roomList) {
            if (cleanerService.checkRoom(room))
                dirtyRoomList.add(room);
        }
        System.out.println("Dirty rooms: ");
        for (Room room: dirtyRoomList)
            System.out.println(room.getId() + "(nr. " + room.getNumber() + ", floor " + room.getFloor() + ")");
    }

    public void cleanRoom(int roomID){  //TO-DO: update repo with new value
        RoomRepository roomRepo = new RoomRepository();
        List<Room> roomList = roomRepo.getAll();

        for (Room room : roomList) {
            if (room.getId() == roomID)
            {
                room.setAvailability("Available");  //? se actualizeaza si lista? in python nu era cazul
                break;
            }
        }
        roomRepo.save(roomList);  //!!!
    }
}
