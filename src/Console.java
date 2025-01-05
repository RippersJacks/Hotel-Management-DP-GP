import Hotel.controller.HotelController;
import Hotel.HotelRegistrationSystem;
import Hotel.model.*;
import Hotel.repository.DBRepository.*;
import Hotel.repository.FileRepository;
import Hotel.repository.InMemoryRepository;
import Hotel.repository.Repository;
import Hotel.service.HotelService;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Console {

    private final HotelController hotelController;

    public Console(HotelController hotelController) {
        this.hotelController = hotelController;
    }

    /**
     * Contains the User Interface of the User when he logs in and when he uses the project's functionality.
    */

    private int takeUsersChoice() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
    }

    private boolean upToDate(){
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return hotelController.getDate().equals(LocalDate.now().format(formatter1));
    }


    public void run(int attemptCount) throws ParseException {
        HotelRegistrationSystem system = new HotelRegistrationSystem(hotelController);
        Scanner sc = new Scanner(System.in);
       if (!upToDate()){
           hotelController.makeAllCurrentReservedRoomsDirtyAndUpdateDate();

       }

        System.out.println("What do you want to login as?");
        System.out.println("1. Customer");
        System.out.println("2. Personnel");
        int loginTypeChoice = takeUsersChoice();

        if (loginTypeChoice == 2){
            System.out.println("Please enter your id: ");
            int id = sc.nextInt();
            sc.nextLine();
            if (id < 0)
                throw new IllegalArgumentException("Invalid hotel id: can't be negative");
            System.out.println("Please enter your password: ");
            String password = sc.nextLine();
            if (password.isEmpty())
                throw new IllegalArgumentException("Invalid hotel password: can't be empty");
            String role = system.login(id, password);

            while (true) {
                if (role.equalsIgnoreCase("Receptionist")) {
                    System.out.println("You are a " + role);
                    System.out.println("""
                            Choose what you want to do
                            1. Add a new customer
                            2. Remove a customer
                            3. Update a customer
                            4. See all customers
                            5. See all available rooms
                            6. See in which order the current customers will check out
                            7. See all rooms of a customer
                            8. Stop
                            0. Logout
                            """);

                    int choice = takeUsersChoice();
                    if (choice == 0) {
                        run(0);
                        break;
                    }
                    if (choice == 8) break;
                    switch (choice) {
                        case 1:
                            hotelController.createClientWithReservation();
                            break;
                        case 2:
                            hotelController.deleteClientValidate();
                            break;
                        case 3:
                            hotelController.updateClientValidate();
                            break;
                        case 4:
                            hotelController.showAllCustomers();
                            break;
                        case 5:
                            hotelController.showAllAvailableRooms();
                            break;
                        case 6:
                            hotelController.showInOrderUntilWhenCustomersStayInRoom();
                            break;
                        case 7:
                            hotelController.showAllRoomsOfACustomer();
                            break;
                    }
                } else if (role.equalsIgnoreCase("Manager")) {
                    System.out.println("You are a " + role);
                    System.out.println("""
                            Choose what you want to do
                            1. Add a new employee
                            2. Remove an employee
                            3. Update an employee
                            4. Add a new department
                            5. Update a department
                            6. Delete a department
                            7. Show all employees
                            8. Show all departments
                            9. Show all employees sorted by salary
                            10. Show departments with a salary average bigger than a given number
                            11. Stop
                            0. Logout
                            """);


                    int choice = takeUsersChoice();
                    if (choice == 0) {
                        run(0);
                        break;
                    }
                    if (choice == 11) break;
                    switch (choice) {
                        case 1:
                            hotelController.createEmployee(id);
                            break;
                        case 2:
                            hotelController.deleteEmployee(id);
                            break;
                        case 3:
                            hotelController.updateEmployee(id);
                            break;
                        case 4:
                            hotelController.createDepartment(id);
                            break;
                        case 5:
                            hotelController.updateDepartament(id);
                            break;
                        case 6:
                            hotelController.deleteDepartment(id);
                            break;
                        case 7:
                            hotelController.showAllEmployeesOnScreen();
                            break;
                        case 8:
                            hotelController.showAllDepartmentsScreen();
                            break;
                        case 9:
                            hotelController.showEmployeesSortedBySalary(id);
                            break;
                        case 10:
                            hotelController.showDepartmentsFilteredByAverageSalaryOverGivenNumber(id);
                            break;
                    }
                } else if (role.equalsIgnoreCase("Cleaner")) {
                    System.out.println("You are a " + role);
                    System.out.println("""
                            Choose what you want to do
                            1. See all the dirty rooms
                            2. Clean a room
                            3. Stop
                            0. Logout
                            """);

                    int choice = takeUsersChoice();
                    if (choice == 0) {
                        run(0);
                        break;
                    }
                    if (choice == 3) break;
                    switch (choice) {
                        case 1:
                            hotelController.checkDirtyRoomsValidate();
                            break;
                        case 2:
                            System.out.println("Write the id of the cleaned room:");
                            int roomId = sc.nextInt();
                            sc.nextLine();
                            if (roomId < 0)
                                throw new IllegalArgumentException("Invalid room ID: cannot be negative");
                            hotelController.cleanRoomValidate(id, roomId);
                            break;
                    }
                } else {
                    System.out.println("The information you entered can't be found in the database.");
                    attemptCount++;
                    if (attemptCount >= 3)
                        throw new RuntimeException("Reached 3 attempts, please wait before logging in again.");
                    run(attemptCount);
                    break;
                }
            }
        }else if(loginTypeChoice == 1){
            int loggedInCustomersId = -1;
            boolean notLoggedIn = true;
            while (notLoggedIn) {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("0. Exit");
                int loginOrRegisterChoice = takeUsersChoice();

                if (loginOrRegisterChoice == 1) {
                        System.out.println("Please enter your email: ");
                        String email = sc.nextLine();
                        System.out.println("Please enter your password: ");
                        String password = sc.nextLine();
                        Customer customer = system.clientLogin(email, password);
                        if (customer != null){
                            loggedInCustomersId = hotelController.getCustomerByEmail(email).getId();
                            notLoggedIn = false;
                        }else {
                            System.out.println("Invalid email or password");

                        }
                }else if (loginOrRegisterChoice == 2) {
                    hotelController.createAccount();

                }else if (loginOrRegisterChoice == 0){
                    break;
                }
            }
            boolean loggedIn = true;
            while (loggedIn) {
                System.out.println("""
                        Choose what you want to do
                        1. Reserve a room
                        2. See all future, present and past reservations
                        3. Cencel a reservation(Only Available if check-in is minimum 48 hours away)
                        4. See all available rooms by type, for a specific time interval
                        5. See all room types we offer
                        0. Logout
                        """);
                int choice = takeUsersChoice();

                if (choice == 0){
                    run(0);
                    break;
                }
                switch (choice){
                    case 1:
                        String datePattern = "^(\\d{4})-(\\d{2})-(\\d{2})$";
                        System.out.println("Enter check-in date (Format: YYYY-MM-DD): ");
                        String checkInDate = sc.nextLine();
                        if (!checkInDate.matches(datePattern))
                            throw new IllegalArgumentException("Check-in date must be in the format YYYY-MM-DD");

                        System.out.println("Enter check-out date: ");
                        String checkOutDate = sc.nextLine();
                        if (!checkOutDate.matches(datePattern))
                            throw new IllegalArgumentException("Check-out date must be in the format YYYY-MM-DD");

                        List<Room> roomsByType = hotelController.availableRoomsbyDate(checkInDate, checkOutDate);
                        for (int i = 0; i < roomsByType.size(); i++){
                            System.out.println(i + ". " + roomsByType.get(i));
                        }

                        System.out.println("Choose which room you want to reserve");
                        int roomChoice = takeUsersChoice();

                        hotelController.createReservation(loggedInCustomersId, roomsByType.get(roomChoice).getId(), checkInDate, checkOutDate);
                        break;
                    case 2:
                        hotelController.showCustomerAllReservations(loggedInCustomersId);
                        break;

                    case 3:
                        hotelController.deleteReservationWithCondition(loggedInCustomersId);

                    case 4:
                        datePattern = "^(\\d{4})-(\\d{2})-(\\d{2})$";
                        System.out.println("Enter check-in date (Format: YYYY-MM-DD): ");
                        checkInDate = sc.nextLine();
                        if (!checkInDate.matches(datePattern))
                            throw new IllegalArgumentException("Check-in date must be in the format YYYY-MM-DD");

                        System.out.println("Enter check-out date: ");
                        checkOutDate = sc.nextLine();
                        if (!checkOutDate.matches(datePattern))
                            throw new IllegalArgumentException("Check-out date must be in the format YYYY-MM-DD");

                        roomsByType = hotelController.availableRoomsbyDate(checkInDate, checkOutDate);
                        for (int i = 0; i < roomsByType.size(); i++){
                            System.out.println(i + ". " + roomsByType.get(i));
                        }
                        break;

                    case 5:
                        System.out.println("""
                                1. Single Room
                                2. Twin Room
                                3. King Room
                                4. Queen Room
                                5. Suite
                                """);
                        break;
                }
            }
        }
    }


    public static void main(String[] args) throws ParseException {

        // Initialize the DB repositories

        //TODO: Select repositoryType UI + Actions (override InFile with InMemory etc.)
        String repositoryType = "Local";

        HotelService hotelService = createHotelServiceObject(repositoryType);
        HotelController hotelcontroller = new HotelController(hotelService);
        Console console = new Console(hotelcontroller);
        console.run(0);
    }



    public static HotelService createHotelServiceObject(String repositoryType){
        switch (repositoryType) {
            case "Online" -> {
                ReceptionistDBRepository receptionistDBRepository = new ReceptionistDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                ReservationDBRepository reservationDBRepository = new ReservationDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                ManagerDBRepository managerDBRepository = new ManagerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                DepartmentDBRepository departmentDBRepository = new DepartmentDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                CustomerDBRepository customerDBRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                CleanerDBRepository cleanerDBRepository = new CleanerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                RoomDBRepository roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                RoomCleanerDBRepository roomCleanerDBRepository = new RoomCleanerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
                TimeDBRepository timeDBRepository = new TimeDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
                return new HotelService(receptionistDBRepository, reservationDBRepository, managerDBRepository, departmentDBRepository, customerDBRepository, cleanerDBRepository, roomDBRepository, roomCleanerDBRepository, timeDBRepository);
            }
            case "Local" -> {
                Repository<Receptionist> receptionistRepository = new FileRepository<Receptionist>("../../InFileRepository/receptionists.db");
                Repository<Reservation> reservationRepository = new FileRepository<Reservation>("../../InFileRepository/reservations.db");
                Repository<Manager> managerRepository = new FileRepository<Manager>("../../InFileRepository/managers.db");
                Repository<Department> departmentRepository = new FileRepository<Department>("../../InFileRepository/departments.db");
                Repository<Customer> customerRepository = new FileRepository<Customer>("../../InFileRepository/customers.db");
                Repository<Cleaner> cleanerRepository = new FileRepository<Cleaner>("../../InFileRepository/cleaners.db");
                Repository<Room> roomRepository = new FileRepository<Room>("../../InFileRepository/rooms.db");
                Repository<RoomCleaner> roomCleanerRepository = new FileRepository<RoomCleaner>("../../InFileRepository/roomCleaners.db");
                TimeDBRepository timeDBRepository = new TimeDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
                return new HotelService(receptionistRepository, reservationRepository, managerRepository, departmentRepository, customerRepository, cleanerRepository, roomRepository, roomCleanerRepository, timeDBRepository);
            }
            case "Testing" -> {
            }
        }

        throw new RuntimeException("Error at selecting the Repository Type");
    }


    //------REPOSITORY ACTIONS------


}

