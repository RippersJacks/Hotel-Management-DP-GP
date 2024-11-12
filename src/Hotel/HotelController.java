package Hotel;

import Hotel.model.*;
import Hotel.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HotelController {
    private final HotelService hotelService;

    /**
     * Contains some UI as a layer for the functionality of the project, some input management and little functionality itself.
     *
     * @param hotelService an object of type HotelService which contains the functions with the functionality of the project
     */
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    //-------------CLEANER SECTION--------------

    /**
     * Shows all rooms on the screen with availability "Dirty".
     * <p>
     * Format: ID (number of the room, floor of the room)
     */
    public void checkDirtyRoomsValidate(){
        //get the list of all dirty rooms
        List<Room> roomList;
        roomList = hotelService.checkDirtyRooms();

        //show all dirty rooms on the screen
        System.out.println("Dirty rooms: ");
        for (Room room: roomList)
            System.out.println(room.getId() + " (nr. " + room.getNumber() + ", floor " + room.getFloor() + ")");
    }

    /**
     * Checks if the room with given ID parameter exists, and changes its availability to "Available" if it was "Dirty" before.
     * @param roomID ID of the room to be cleaned.
     */
    public void cleanRoomValidate(int roomID){
        //the cleanRoom function cleans the room and also returns true if this room has been found; else false
        boolean roomExists = hotelService.cleanRoom(roomID);

        if (roomExists) System.out.println("Room " + roomID + " cleaned");
            else System.out.println("Room " + roomID + " uncleanable or doesnt exist");
    }
    //-------------------------------------------


    //-----------RECEPTIONIST SECTION------------

    /**
     * Creates a customer in the database with an automatically chosen ID and a name given trough input.
     */
    public void createClientValidate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer name: ");
        String name = sc.nextLine();

        hotelService.createClient(name);
    }

    /**
     * Deletes a customer from the database; which customer is to be deleted, is decided by giving his ID as input
     */
    public void deleteClientValidate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer id to be deleted: ");
        Integer id = sc.nextInt();

        hotelService.deleteClient(id);
    }

    /**
     * Alters a customers data; the ID of the customer to be changed is given (as input) and a new name will be given (as input).
     */
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

    /**
     * Shows all current customers of the hotel on the screen.
     */
    public void showAllCustomers(){
        System.out.println("\nCurrent list of customers:");
        for (Customer customer:hotelService.getAllCustomers()){
            System.out.println(customer.toString());
        }
        System.out.println();
    }

    /**
     * Shows all currently avaible rooms on the screen.
     */
    public void showAllAvailableRooms(){
        System.out.println("\nCurrently available rooms:");
        for (Room room : hotelService.getAvailableRooms())
            System.out.println(room.toString());
        System.out.println();
    }
    //-------------------------------------------



    //-------------MANAGER SECTION---------------

    /**
     * Creates a new employee, receiving the data as input; ID is decided automatically.
     * <p>
     * A manager may only use this function to create employees of his managed department's type. (ex. Receptionist)
     */
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

    /**
     * Deletes an employee.
     * <p>
     * A manager may only use this function to delete employees of his managed department's type. (ex. Receptionist)
     */
    public void deleteEmployee(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer id to be deleted: ");
        Integer id = sc.nextInt();

        hotelService.deleteEmployee(id);
    }

    /**
     * Updates employee, receiving the data as input; ID is also given as input.
     * <p>
     * A manager may only use this function to create employees of his managed department's type. (ex. Receptionist)
     * @param managerId ID of the manager who wants to use this function
     */
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
