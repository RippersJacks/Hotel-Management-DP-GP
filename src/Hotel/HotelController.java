package Hotel;

import Hotel.model.*;
import Hotel.repository.Repository;

import java.util.ArrayList;
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
        sc.nextLine();

        System.out.println("New customer name: ");
        String name = sc.nextLine();

        Customer customer = new Customer(id, name);
        hotelService.updateClient(customer);
    }

    public void showAllCustomers(){
        System.out.println("\nCurrent list of customers:");
        for (Customer customer:hotelService.getAllCustomers()){
            System.out.println(customer.toString());
        }
        System.out.println();
    }

    public void showAllAvailableRooms(){
        System.out.println("\nCurrently available rooms:");
        for (Room room : hotelService.getAvailableRooms())
            System.out.println(room.toString());
        System.out.println();
    }
    //-------------------------------------------



    //-------------MANAGER SECTION---------------
    public void createEmployee(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter employee name: ");
        String name = sc.nextLine();
        System.out.println("Enter employee salary: ");
        int salary = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter employee password: ");
        String password = sc.nextLine();
        System.out.println("Enter employee role: ");
        String role = sc.nextLine();
        hotelService.createEmployee(role, name, salary, password);
    }


    public void deleteEmployee(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer id to be deleted: ");
        Integer id = sc.nextInt();

        hotelService.deleteEmployee(id);
    }


    public void updateEmployee(int managerId) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter employee id to be updated: ");
        Integer id = sc.nextInt();
        sc.nextLine();

        String employeeType = hotelService.getEmployeeType(id);
        System.out.println("mere");
        String managerType = hotelService.getManagersManagedDepartmentType(managerId);

        if (employeeType.equals(managerType)) {
            System.out.println("Enter employee name: ");
            String newName = sc.nextLine();
            System.out.println("Enter employee salary: ");
            int newSalary = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter employee password: ");
            String newPassword = sc.nextLine();

            if (employeeType.equalsIgnoreCase("Manager")) {
                System.out.println("Enter managed department Id:");
                Integer newDepartmentId = sc.nextInt();
                Employee employee = new Manager(id, newName, newSalary, newPassword, newDepartmentId);
                hotelService.updateEmployee(employee);
            } else if (employeeType.equalsIgnoreCase("Cleaner")) {
                System.out.println("Enter floor number:");
                int newFloorNumber = sc.nextInt();
                Employee employee = new Manager(id, newName, newSalary, newPassword, newFloorNumber);
                hotelService.updateEmployee(employee);
            } else if (employeeType.equalsIgnoreCase("Receptionist")) {
                ArrayList<String> newLanguages = new ArrayList<>();
                String language = " ";
                while (!language.equalsIgnoreCase("stop")) {         //when the user types "stop" the language adding stops
                    System.out.println("Enter a language: ");
                    language = sc.nextLine();
                    newLanguages.add(language);
                }
                Employee employee = new Receptionist(id, newName, newSalary, newPassword, newLanguages);
                hotelService.updateEmployee(employee);
            }

        }else System.out.println("Wrong department");
    }

    //-------------------------------------------
}
