package Hotel;

import Hotel.model.Customer;
import Hotel.model.Room;

import java.util.List;
import java.util.Scanner;

public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService cleanerService) {
        this.hotelService = cleanerService;
    }

    //-------------CLEANER SECTION--------------
    public void checkDirtyRoomsValidate(){
        //get the list of all dirty rooms
        List<Room> roomList;
        roomList = hotelService.checkDirtyRooms();

        //show all dirty rooms on the screen
        System.out.println("Dirty rooms: ");
        for (Room room: roomList)
            System.out.println(room.getId() + "(nr. " + room.getNumber() + ", floor " + room.getFloor() + ")");
    }

    public void cleanRoomValidate(int roomID){
        //the cleanRoom function cleans the room and also returns true if this room has been found; else false
        boolean roomExists = hotelService.cleanRoom(roomID);

        if (roomExists) System.out.println("Room " + roomID + " cleaned");
            else System.out.println("Room " + roomID + " not found");
    }
    //-------------------------------------------



    //-----------RECEPTIONIST SECTION------------
    public void createClientValidate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer name: ");
        String name = sc.nextLine();

        hotelService.createClient(name);
    }

    public void deleteClientValidate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer id to be deleted: ");
        Integer id = sc.nextInt();

        hotelService.deleteClient(id);
    }

    public void updateClientValidate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer id to be updated: ");
        Integer id = sc.nextInt();

        System.out.println("New customer name: ");
        String name = sc.nextLine();

        Customer customer = new Customer(id, name);
        hotelService.updateClient(customer);
    }
    //-------------------------------------------



    //-------------MANAGER SECTION---------------


    //-------------------------------------------
}
