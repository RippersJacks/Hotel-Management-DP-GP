package Hotel.controller;

import Hotel.model.*;
import Hotel.service.HotelService;

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
     * It also saves a roomCustomer in the database.
     * Everything happens after it checks, if the clients desired room type exists and is available.
     */
    public void createClientValidate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer name: ");
        String name = sc.nextLine();
        System.out.println("Enter desired room type: ");
        String roomType = sc.nextLine();
        System.out.println("Enter check-in date: ");
        String checkInDate = sc.nextLine();
        System.out.println("Enter check-out date: ");
        String checkOutDate = sc.nextLine();

        for (Room room: hotelService.getAvailableRooms()){
            if (room.getType().equals(roomType)){
                int roomId = room.getId();
                room.setAvailability("Unavailable");
                hotelService.createClient(name, roomId, checkInDate, checkOutDate);
                break;
            }
            System.out.println("No available rooms left of this type");
        }

    }

    /**
     * Deletes a customer from the database; which customer is to be deleted, is decided by giving his ID as input
     */
    public void deleteClientValidate(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer id to be deleted: ");
        int id = sc.nextInt();
        hotelService.deleteClient(id);
        hotelService.deleteRoomCustomer(id);
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

    /**
     * Shows a sorted list of RoomCustomer objects (sorted by hotelService)
     */
    public void showInOrderUntilWhenCustomersStayInRoom(){
        System.out.println();
        for (RoomCustomer roomCustomer : hotelService.sortRoomCustomerByUntilDate())
        {
            System.out.println("Customer " + roomCustomer.getCustomerId() + " stays in room " + roomCustomer.getRoomId() + " until " + roomCustomer.getUntilDate());
        }
        System.out.println();
    }
    //-------------------------------------------



    //-------------MANAGER SECTION---------------

    /**
     * Creates a new employee, receiving the data as input; ID is decided automatically.
     * <p>
     * A manager may only use this function to create employees of his managed department's type. (ex. Receptionist)
     */
    public void createEmployee(int managerId){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter employee name: ");
        String name = sc.nextLine();
        System.out.println("Enter employee salary: ");
        int salary = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter employee password: ");
        String password = sc.nextLine();
        String role = hotelService.getManagersManagedDepartmentType(managerId);
        hotelService.createEmployee(role, name, salary, password);
    }

    /**
     * Deletes an employee.
     * <p>
     * A manager may only use this function to delete employees of his managed department's type. (ex. Receptionist)
     */
    public void deleteEmployee(int managerId){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter employee id to be deleted: ");
        int id = sc.nextInt();
        if (hotelService.getEmployeeTypeString(id).equals(hotelService.getManagersManagedDepartmentType(managerId))) {
            hotelService.deleteEmployee(id);
            System.out.println("Employee successfully deleted");
        }
        else System.out.println("The given id is not valid for your department");
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

        String employeeType = hotelService.getEmployeeTypeString(id);
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

    public void showAllEmployeesOnScreen(){
        System.out.println("\nCurrent list of employees:");
        for (Employee employee : hotelService.showAllEmployees())
            System.out.println(employee.getId() + " " + employee.getName() + " " + employee.getSalary() + " " + employee.getPassword() + " " + employee.toString());
    }

    public void showEmployeesSortedBySalary(Integer managerId){
        String managerType = hotelService.getManagersManagedDepartmentType(managerId);
        Employee type = (managerType.equals("Receptionist")) ? new Receptionist(-1,"",0,"",null):
                (managerType.equals("Cleaner")) ? new Cleaner(-1,"",0,"",-1):
                        (managerType.equals("Manager")) ? new Manager(-1,"",0,"",null):
                                null;  //daca e de tip department, vom sti ca type e null (nu putem asigna un object de tip Department la type pt ca type e de tip Employee

        System.out.println("\n\nEmployees sorted by salary:");
        switch (type) {
            case Manager manager -> {   //show all managers
                for (Employee employee : hotelService.sortEmployeesBySalary())
                    if (employee instanceof Manager)
                        System.out.println(employee.getId() + " " + employee.getName() + " " + employee.getSalary() + " " + employee.getPassword() + " " + employee.toString());
            }
            case Cleaner cleaner -> {   //show all cleaners
                for (Employee employee : hotelService.sortEmployeesBySalary())
                    if (employee instanceof Cleaner)
                        System.out.println(employee.getId() + " " + employee.getName() + " " + employee.getSalary() + " " + employee.getPassword() + " " + employee.toString());
            }
            case Receptionist receptionist -> {  //show all receptionists
                for (Employee employee : hotelService.sortEmployeesBySalary())
                    if (employee instanceof Receptionist)
                        System.out.println(employee.getId() + " " + employee.getName() + " " + employee.getSalary() + " " + employee.getPassword() + " " + employee.toString());
            }
            case null -> {  //cant sort departments by salary
                System.out.println("\nCant sort departments by salary\n");
            }
            default -> {
            }
        }
        System.out.println("\n");
    }


    //-------DEPARTMENT SECTION (still belongs to Manager section; one manager manages the department structure)--------
    public void showAllDepartmentsScreen(){
        System.out.println("\nCurrent list of departments:");
        for (Department department : hotelService.showAllDepartments())
            System.out.println(department.toString());
    }

    /**
     * The function checks if a specific manager is the manager of all the departments
     *
     * @param id is the id of a manager
     * @return true or false
     */
    private boolean managerOverDepartments(int id){
        return hotelService.getManagersManagedDepartmentType(id).equals("Department");
    }


    public void createDepartment(int id){
        if (managerOverDepartments(id)){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter a new department name: ");
            String departmentName = sc.nextLine();
            List<Employee> employees = new ArrayList<>();
            hotelService.createDepartment(departmentName, employees);

        }else System.out.println("You are not authorised to do this");
    }


    public void deleteDepartment(int id){
        if (managerOverDepartments(id)){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the id of the department you want to delete: ");
            hotelService.deleteDepartment(sc.nextInt());

        }else System.out.println("You are not authorised to do this");
    }

    /**
     * Updates a department. Only the manager of the department structure may use this function.
     * @param id id of the manager currently logged in
     */
    public void updateDepartament(int id){
        if (managerOverDepartments(id)){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter a department id to be updated: ");
            int departmentId = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter new department name: ");
            String departmentName = sc.nextLine();
            List<Employee> emptyEmployeeList = new ArrayList<>();

            Department updatedDepartment =new Department(departmentId, departmentName, emptyEmployeeList);
            hotelService.updateDepartment(updatedDepartment);

        }else System.out.println("You are not authorised to do this");
    }

    /**
     * Shows all departments where the employee average salary is over an input given number. Only the manager of the department structure may use this function.
     * @param id id of the manager currently logged in
     */
    public void showDepartmentsFilteredByAverageSalaryOverGivenNumber(int id){
        System.out.println("Enter the minimum average salary to be searched by: ");
        Scanner sc = new Scanner(System.in);
        int minAvgSalary = sc.nextInt();
        sc.nextLine();

        System.out.println("\nDepartments with an average salary of over " + minAvgSalary +":");

        List<Department> departmentList = hotelService.getDepartmentsWithOverGivenNumberAvgSalary(minAvgSalary);
        if (managerOverDepartments(id)){
            for (Department department : departmentList)
                System.out.println(department.toString());
        }
        else
            System.out.println("You do not have access to this functionality");
    }

    //-------------------------------------------

}
