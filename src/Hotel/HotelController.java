package Hotel;

import Hotel.model.Room;

import java.util.List;

public class HotelController {
    private final HotelService cleanerService;

    public HotelController(HotelService cleanerService) {
        this.cleanerService = cleanerService;
    }

    public void checkDirtyRoomsValidate(){
        //no validations necessary (?)
        List<Room> roomList;
        roomList = cleanerService.checkDirtyRooms();

        System.out.println("Dirty rooms: ");
        for (Room room: roomList)
            System.out.println(room.getId() + "(nr. " + room.getNumber() + ", floor " + room.getFloor() + ")");
    }

    public void cleanRoomValidate(int roomID){
        boolean roomExists = cleanerService.cleanRoom(roomID);
        if (roomExists) System.out.println("Room " + roomID + " cleaned");
        else System.out.println("Room " + roomID + " not found");
    }
}
