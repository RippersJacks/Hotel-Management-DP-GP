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
        System.out.println("3. Administrator");
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
            boolean loggedIn = false;
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
                            loggedIn = true;
                        }else {
                            System.out.println("Invalid email or password");

                        }
                }else if (loginOrRegisterChoice == 2) {
                    hotelController.createAccount();

                }else if (loginOrRegisterChoice == 0){
                    run(0);
                    break;
                }
            }

            while (loggedIn) {
                System.out.println("""
                        Choose what you want to do
                        1. Reserve a room
                        2. See all future, present and past reservations
                        3. Cancel a reservation (Only Available if check-in is minimum 48 hours away)
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
                        System.out.println("Enter start date (Format: YYYY-MM-DD): ");
                        checkInDate = sc.nextLine();
                        if (!checkInDate.matches(datePattern))
                            throw new IllegalArgumentException("Check-in date must be in the format YYYY-MM-DD");

                        System.out.println("Enter end date (Format: YYYY-MM-DD): ");
                        checkOutDate = sc.nextLine();
                        if (!checkOutDate.matches(datePattern))
                            throw new IllegalArgumentException("Check-out date must be in the format YYYY-MM-DD");

                        roomsByType = hotelController.availableRoomsbyDate(checkInDate, checkOutDate);
                        System.out.println("The following rooms are available between "+checkInDate+" and "+checkOutDate+":");
                        for (int i = 0; i < roomsByType.size(); i++){
                            System.out.println(i + ". " + roomsByType.get(i).toStringForCustomers());
                        }
                        System.out.println();
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
        else if (loginTypeChoice == 3)
        {
            System.out.println("Please enter the administrator password: ");
            String password = sc.nextLine();

            if (password.equals("HEXA123F!"))
            {
                boolean loggedIn = true;
                while (loggedIn)
                {
                    System.out.println("""
                        1. Change repository type
                        2. Override one repository over the other
                        0. Logout
                        """);
                    int choice = takeUsersChoice();

                    if (choice == 1){
                        System.out.println("""
                            Change to:
                              1. Testing zone (InMemoryRepository, changes will NOT be applied)
                              2. Local saves  (InFileRepository, changes will only be saved locally
                              3. Online saves (InDataBaseRepository, changes will be visible to everyone else\s""");
                        int choice1 = takeUsersChoice();
                        String repositoryType = "";
                        if (choice1 == 1)
                            repositoryType = "Memory";
                        else if (choice1 == 2)
                            repositoryType = "File";
                        else if (choice1 == 3)
                            repositoryType = "Database";
                        HotelService hotelService = createHotelServiceObject(repositoryType);
                        HotelController hotelcontroller = new HotelController(hotelService);
                        Console console = new Console(hotelcontroller);
                        console.run(0);
                        throw new RuntimeException("Exitted application");  //to break recursion
                    }
                    else if (choice == 2){
                        String origin = "", target = "";
                        System.out.println("""
                            Choose which repository will be overridden:\s
                            1. Local data will be replaced by Online data
                            2. Online data will be replaced by Local data
                            """);
                        int choice2 = takeUsersChoice();
                        if (choice2 == 1) {origin = "Database"; target = "File"; }
                        else if (choice2 == 2) {origin = "File"; target = "Database";}

                        System.out.println("Are you sure you want the data in the "+origin+" repository to overwrite the data of the "+target+" repository? (Y/N)");
                        Scanner scanner = new Scanner(System.in);
                        String choice3 = scanner.nextLine();
                        if (choice3.equals("Y") || choice3.equals("y"))
                        {
                            if (target.equals("Database"))
                            {
                                System.out.println("Please reintroduce the administrator password: ");
                                password = sc.nextLine();

                                if (password.equals("HEXA123F!"))
                                    overrrideRepository(origin, target);
                            }
                            else
                                overrrideRepository(origin, target);
                        }
                        else if (choice3.equals("N") || choice3.equals("n"))
                        {
                            System.out.println("\n\nCancelled Override\n");
                        }
                        else System.out.println("Invalid input");
                    }
                    else if (choice == 0)
                    {
                        run(0);
                        break;
                    }
                }


            }
            else throw new IllegalArgumentException("Invalid password, aborting...");
        }
    }

    public static void main(String[] args) throws ParseException {
        String repositoryType = "Database";

        HotelService hotelService = createHotelServiceObject(repositoryType);
        HotelController hotelcontroller = new HotelController(hotelService);
        Console console = new Console(hotelcontroller);
        console.run(0);
    }

    void RepositoryActions(){

    }


    public static HotelService createHotelServiceObject(String repositoryType){
        switch (repositoryType) {
            case "Database" -> {
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
            case "File" -> {
                Repository<Receptionist> receptionistRepository = new FileRepository<Receptionist>("InFileRepository/receptionists.db");
                Repository<Reservation> reservationRepository = new FileRepository<Reservation>("InFileRepository/reservations.db");
                Repository<Manager> managerRepository = new FileRepository<Manager>("InFileRepository/managers.db");
                Repository<Department> departmentRepository = new FileRepository<Department>("InFileRepository/departments.db");
                Repository<Customer> customerRepository = new FileRepository<Customer>("InFileRepository/customers.db");
                Repository<Cleaner> cleanerRepository = new FileRepository<Cleaner>("InFileRepository/cleaners.db");
                Repository<Room> roomRepository = new FileRepository<Room>("InFileRepository/rooms.db");
                Repository<RoomCleaner> roomCleanerRepository = new FileRepository<RoomCleaner>("InFileRepository/roomCleaners.db");
                TimeDBRepository timeDBRepository = new TimeDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
                return new HotelService(receptionistRepository, reservationRepository, managerRepository, departmentRepository, customerRepository, cleanerRepository, roomRepository, roomCleanerRepository, timeDBRepository);
            }
            case "Memory" -> {
                System.out.println("""
                            Choose which repository to take data from:
                            1. Local
                            2. Online
                        """);
                Scanner sc = new Scanner(System.in);
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    Repository<Receptionist> receptionistFileRepository = new FileRepository<Receptionist>("InFileRepository/receptionists.db");
                    Repository<Reservation> reservationFileRepository = new FileRepository<Reservation>("InFileRepository/reservations.db");
                    Repository<Manager> managerFileRepository = new FileRepository<Manager>("InFileRepository/managers.db");
                    Repository<Department> departmentFileRepository = new FileRepository<Department>("InFileRepository/departments.db");
                    Repository<Customer> customerFileRepository = new FileRepository<Customer>("InFileRepository/customers.db");
                    Repository<Cleaner> cleanerFileRepository = new FileRepository<Cleaner>("InFileRepository/cleaners.db");
                    Repository<Room> roomFileRepository = new FileRepository<Room>("InFileRepository/rooms.db");
                    Repository<RoomCleaner> roomCleanerFileRepository = new FileRepository<RoomCleaner>("InFileRepository/roomCleaners.db");
                    TimeDBRepository timeDBRepository = new TimeDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");

                    Repository<Receptionist> receptionistRepository = new InMemoryRepository<>();
                    Repository<Reservation> reservationRepository = new InMemoryRepository<>();
                    Repository<Manager> managerRepository = new InMemoryRepository<>();
                    Repository<Department> departmentRepository = new InMemoryRepository<>();
                    Repository<Customer> customerRepository = new InMemoryRepository<>();
                    Repository<Cleaner> cleanerRepository = new InMemoryRepository<>();
                    Repository<Room> roomRepository = new InMemoryRepository<>();
                    Repository<RoomCleaner> roomCleanerRepository = new InMemoryRepository<>();

                    for (Receptionist receptionist: receptionistFileRepository.getAll())
                        receptionistRepository.create(receptionist);
                    for (Reservation reservation: reservationFileRepository.getAll())
                        reservationRepository.create(reservation);
                    for (Manager manager: managerFileRepository.getAll())
                        managerRepository.create(manager);
                    for (Department department: departmentFileRepository.getAll())
                        departmentRepository.create(department);
                    for (Customer customer: customerFileRepository.getAll())
                        customerRepository.create(customer);
                    for (Cleaner cleaner: cleanerFileRepository.getAll())
                        cleanerRepository.create(cleaner);
                    for (Room room: roomFileRepository.getAll())
                        roomRepository.create(room);
                    for (RoomCleaner roomCleaner: roomCleanerFileRepository.getAll())
                        roomCleanerRepository.create(roomCleaner);

                    return new HotelService(receptionistRepository, reservationRepository, managerRepository, departmentRepository, customerRepository, cleanerRepository, roomRepository, roomCleanerRepository, timeDBRepository);
                }
                else if (choice == 2) {
                    ReceptionistDBRepository receptionistDBRepository = new ReceptionistDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                    ReservationDBRepository reservationDBRepository = new ReservationDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                    ManagerDBRepository managerDBRepository = new ManagerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                    DepartmentDBRepository departmentDBRepository = new DepartmentDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                    CustomerDBRepository customerDBRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                    CleanerDBRepository cleanerDBRepository = new CleanerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                    RoomDBRepository roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
                    RoomCleanerDBRepository roomCleanerDBRepository = new RoomCleanerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
                    TimeDBRepository timeDBRepository = new TimeDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");

                    Repository<Receptionist> receptionistRepository = new InMemoryRepository<>();
                    Repository<Reservation> reservationRepository = new InMemoryRepository<>();
                    Repository<Manager> managerRepository = new InMemoryRepository<>();
                    Repository<Department> departmentRepository = new InMemoryRepository<>();
                    Repository<Customer> customerRepository = new InMemoryRepository<>();
                    Repository<Cleaner> cleanerRepository = new InMemoryRepository<>();
                    Repository<Room> roomRepository = new InMemoryRepository<>();
                    Repository<RoomCleaner> roomCleanerRepository = new InMemoryRepository<>();

                    for (Receptionist receptionist: receptionistDBRepository.getAll())
                        receptionistRepository.create(receptionist);
                    for (Reservation reservation: reservationDBRepository.getAll())
                        reservationRepository.create(reservation);
                    for (Manager manager: managerDBRepository.getAll())
                        managerRepository.create(manager);
                    for (Department department: departmentDBRepository.getAll())
                        departmentRepository.create(department);
                    for (Customer customer: customerDBRepository.getAll())
                        customerRepository.create(customer);
                    for (Cleaner cleaner: cleanerDBRepository.getAll())
                        cleanerRepository.create(cleaner);
                    for (Room room: roomDBRepository.getAll())
                        roomRepository.create(room);
                    for (RoomCleaner roomCleaner: roomCleanerDBRepository.getAll())
                        roomCleanerRepository.create(roomCleaner);

                    return new HotelService(receptionistRepository, reservationRepository, managerRepository, departmentRepository, customerRepository, cleanerRepository, roomRepository, roomCleanerRepository, timeDBRepository);

                }
            }
        }

        throw new RuntimeException("Error at selecting the Repository Type");
    }


    //------REPOSITORY ACTIONS------

    /**
     * Overrides the "target" repository with all the data in the "origin" one.
     * @param origin repository from where data will be taken
     * @param target repository which will be changed
     */
    void overrrideRepository(String origin, String target)
    {
        if (origin.equals("File") && target.equals("Database"))
        {
            ReceptionistDBRepository receptionistDBRepository = new ReceptionistDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            ReservationDBRepository reservationDBRepository = new ReservationDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            ManagerDBRepository managerDBRepository = new ManagerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            DepartmentDBRepository departmentDBRepository = new DepartmentDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            CustomerDBRepository customerDBRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            CleanerDBRepository cleanerDBRepository = new CleanerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            RoomDBRepository roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            RoomCleanerDBRepository roomCleanerDBRepository = new RoomCleanerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");

            for (Receptionist receptionist: receptionistDBRepository.getAll())
                receptionistDBRepository.delete(receptionist.getId());
            for (Reservation reservation: reservationDBRepository.getAll())
                reservationDBRepository.delete(reservation.getId());
            for (Manager manager: managerDBRepository.getAll())
                managerDBRepository.delete(manager.getId());
            for (Department department: departmentDBRepository.getAll())
                departmentDBRepository.delete(department.getId());
            for (Customer customer: customerDBRepository.getAll())
                customerDBRepository.delete(customer.getId());
            for (Cleaner cleaner: cleanerDBRepository.getAll())
                cleanerDBRepository.delete(cleaner.getId());
            for (Room room: roomDBRepository.getAll())
                roomDBRepository.delete(room.getId());
            for (RoomCleaner roomCleaner: roomCleanerDBRepository.getAll())
                roomCleanerDBRepository.delete(roomCleaner.getId());

            Repository<Receptionist> receptionistFileRepository = new FileRepository<Receptionist>("InFileRepository/receptionists.db");
            Repository<Reservation> reservationFileRepository = new FileRepository<Reservation>("InFileRepository/reservations.db");
            Repository<Manager> managerFileRepository = new FileRepository<Manager>("InFileRepository/managers.db");
            Repository<Department> departmentFileRepository = new FileRepository<Department>("InFileRepository/departments.db");
            Repository<Customer> customerFileRepository = new FileRepository<Customer>("InFileRepository/customers.db");
            Repository<Cleaner> cleanerFileRepository = new FileRepository<Cleaner>("InFileRepository/cleaners.db");
            Repository<Room> roomFileRepository = new FileRepository<Room>("InFileRepository/rooms.db");
            Repository<RoomCleaner> roomCleanerFileRepository = new FileRepository<RoomCleaner>("InFileRepository/roomCleaners.db");

            for (Receptionist receptionist: receptionistFileRepository.getAll())
                receptionistDBRepository.create(receptionist);
            for (Reservation reservation: reservationFileRepository.getAll())
                reservationDBRepository.create(reservation);
            for (Manager manager: managerFileRepository.getAll())
                managerDBRepository.create(manager);
            for (Department department: departmentFileRepository.getAll())
                departmentDBRepository.create(department);
            for (Customer customer: customerFileRepository.getAll())
                customerDBRepository.create(customer);
            for (Cleaner cleaner: cleanerFileRepository.getAll())
                cleanerDBRepository.create(cleaner);
            for (Room room: roomFileRepository.getAll())
                roomDBRepository.create(room);
            for (RoomCleaner roomCleaner: roomCleanerFileRepository.getAll())
                roomCleanerDBRepository.create(roomCleaner);
        }
        else if (origin.equals("Database") && target.equals("File"))
        {
            Repository<Receptionist> receptionistFileRepository = new FileRepository<Receptionist>("InFileRepository/receptionists.db");
            Repository<Reservation> reservationFileRepository = new FileRepository<Reservation>("InFileRepository/reservations.db");
            Repository<Manager> managerFileRepository = new FileRepository<Manager>("InFileRepository/managers.db");
            Repository<Department> departmentFileRepository = new FileRepository<Department>("InFileRepository/departments.db");
            Repository<Customer> customerFileRepository = new FileRepository<Customer>("InFileRepository/customers.db");
            Repository<Cleaner> cleanerFileRepository = new FileRepository<Cleaner>("InFileRepository/cleaners.db");
            Repository<Room> roomFileRepository = new FileRepository<Room>("InFileRepository/rooms.db");
            Repository<RoomCleaner> roomCleanerFileRepository = new FileRepository<RoomCleaner>("InFileRepository/roomCleaners.db");

            for (Receptionist receptionist: receptionistFileRepository.getAll())
                receptionistFileRepository.delete(receptionist.getId());
            for (Reservation reservation: reservationFileRepository.getAll())
                reservationFileRepository.delete(reservation.getId());
            for (Manager manager: managerFileRepository.getAll())
                managerFileRepository.delete(manager.getId());
            for (Department department: departmentFileRepository.getAll())
                departmentFileRepository.delete(department.getId());
            for (Customer customer: customerFileRepository.getAll())
                customerFileRepository.delete(customer.getId());
            for (Cleaner cleaner: cleanerFileRepository.getAll())
                cleanerFileRepository.delete(cleaner.getId());
            for (Room room: roomFileRepository.getAll())
                roomFileRepository.delete(room.getId());
            for (RoomCleaner roomCleaner: roomCleanerFileRepository.getAll())
                roomCleanerFileRepository.delete(roomCleaner.getId());

            ReceptionistDBRepository receptionistDBRepository = new ReceptionistDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            ReservationDBRepository reservationDBRepository = new ReservationDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            ManagerDBRepository managerDBRepository = new ManagerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            DepartmentDBRepository departmentDBRepository = new DepartmentDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            CustomerDBRepository customerDBRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            CleanerDBRepository cleanerDBRepository = new CleanerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            RoomDBRepository roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
            RoomCleanerDBRepository roomCleanerDBRepository = new RoomCleanerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");

            for (Receptionist receptionist: receptionistDBRepository.getAll())
                receptionistFileRepository.create(receptionist);
            for (Reservation reservation: reservationDBRepository.getAll())
                reservationFileRepository.create(reservation);
            for (Manager manager: managerDBRepository.getAll())
                managerFileRepository.create(manager);
            for (Department department: departmentDBRepository.getAll())
                departmentFileRepository.create(department);
            for (Customer customer: customerDBRepository.getAll())
                customerFileRepository.create(customer);
            for (Cleaner cleaner: cleanerDBRepository.getAll())
                cleanerFileRepository.create(cleaner);
            for (Room room: roomDBRepository.getAll())
                roomFileRepository.create(room);
            for (RoomCleaner roomCleaner: roomCleanerDBRepository.getAll())
                roomCleanerFileRepository.create(roomCleaner);
        }
    }

}

