package Hotel.ControllerLayer;

import Hotel.ServiceLayer.CleanerService;
import Hotel.model.Room;

import java.util.ArrayList;
import java.util.List;

public class CleanerController {

    public  void checkDirtyRoomsValidate(){
        //no validations necessary (?)

        CleanerService cleanerService = new CleanerService();
        cleanerService.checkDirtyRooms();
    }

    public void cleanRoomValidate(int roomID){
        RoomRepository roomRepo = new RoomRepository();
        List<Room> roomList = roomRepo.getAll();

        CleanerService cleanerService = new CleanerService();

        //--check if room exists--
        boolean roomExists = false;
        for (Room room : roomList) {
            if (room.getId() == roomID)
                { roomExists = true; break;}
        }

        if (roomExists)
            cleanerService.cleanRoom(roomID);
    }
}
