package Hotel;

import Hotel.RepositoryLayer.Repository;
import Hotel.model.CheckRoom;
import Hotel.model.Room;

import java.util.ArrayList;
import java.util.List;

public class HotelService implements CheckRoom {
    private final Repository<Room> roomRepository;

    public HotelService(Repository<Room> roomRepository) {
        this.roomRepository = roomRepository;
    }



    public List<Room> checkDirtyRooms(){
        List<Room> roomList = roomRepository.getAll();
        List<Room> dirtyRoomList = new ArrayList<>();

        for (Room room : roomList) {
            if (checkRoom(room))
                dirtyRoomList.add(room);
        }
        return dirtyRoomList;
    }

    public boolean cleanRoom(Integer roomID){  //TO-DO: update repo with new value
        List<Room> roomList = roomRepository.getAll();
        Room targetRoom = null;

        for (Room room : roomList) {
            if (room.getId().equals(roomID))
            {
                room.setAvailability("Available");
                targetRoom = room;
                break;
            }
        }
        if (targetRoom != null)
        {
            roomRepository.update(targetRoom);
            return true;
        }
        else return false;
    }
}
