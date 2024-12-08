import Hotel.controller.HotelController;
import Hotel.HotelRegistrationSystem;
import Hotel.repository.DBRepository.*;
import Hotel.repository.FileRepository;
import Hotel.service.HotelService;
import Hotel.repository.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
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


    public void run() {
        HotelRegistrationSystem system = new HotelRegistrationSystem(hotelController);

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your id: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.println("Please enter your password: ");
        String password = sc.nextLine();

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
                    run();
                    break;
                }
                if (choice == 8) break;
                switch (choice) {
                    case 1:
                        hotelController.createClientValidate();
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
                    run();
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
                    run();
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
                        hotelController.cleanRoomValidate(roomId);
                        break;
                }
            } else {
                System.out.println("The information you entered can't be found in the database.");
                run();
                break;
            }

        }
    }


    public static void main(String[] args) {

        // Initialize the DB repositories
        ReceptionistDBRepository receptionistDBRepository = new ReceptionistDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        RoomCustomerDBRepository roomCustomerDBRepository = new RoomCustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        ManagerDBRepository managerDBRepository = new ManagerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        DepartmentDBRepository departmentDBRepository = new DepartmentDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        CustomerDBRepository customerDBRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        CleanerDBRepository cleanerDBRepository = new CleanerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        RoomDBRepository roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");

        HotelService hotelService = new HotelService(receptionistDBRepository, roomCustomerDBRepository, managerDBRepository, departmentDBRepository, customerDBRepository, cleanerDBRepository, roomDBRepository);
        HotelController hotelcontroller = new HotelController(hotelService);
        Console console = new Console(hotelcontroller);
        console.run();
    }


    /*private static Console getConsole(Repository<Employee> employeeRepository) {
        HotelService hotelService = new HotelService(createRoomFileRepository(), employeeRepository, createCustomerInFileRepository(), createDepartmentInFileRepository(), createRoomCustomerInFileRepository());
        HotelController hotelController = new HotelController(hotelService);

        return new Console(hotelController);
    }

         */


    /**
     * Creates an in-memory repository for employees and populates it with some initial data.
     *
     * @return The in-memory repository for employees.
    */


    /*private static Repository<Employee> createEmployeeFileRepository() {
        Repository<Employee> employeeRepository = new FileRepository<>("employees.db");
        //Receptionists
        List<String> languageList = new ArrayList<>();
        languageList.add("german");
        languageList.add("english");
        employeeRepository.create(new Receptionist(100, "Mark", 2500, "mark1525", languageList));
        List<String> languageList2 = new ArrayList<>();
        languageList2.add("ukrainian");
        employeeRepository.create(new Receptionist(101, "Zelensceta", 2100, "password123", languageList2));

        //Cleaners
        employeeRepository.create(new Cleaner(150, "Tina", 1800, "tinytina", 1));
        employeeRepository.create(new Cleaner(151, "Zack", 1800, "123zack123", 2));

        //Managers
        employeeRepository.create(new Manager(10, "James", 3200, "james1973", 9215));
        employeeRepository.create(new Manager(11, "Victor", 4000, "1892WorchestershireSauce!?##Vice", 9216));
        employeeRepository.create(new Manager(12, "Morna", 4500, "victoriasSecret", 9217));
        employeeRepository.create(new Manager(13, "Herbert", 2500, "nerfMiner", 9218));

        return employeeRepository;
    }


    private static Repository<Room> createRoomFileRepository() {
        Repository<Room> roomRepo = new FileRepository<>("rooms.db");
        roomRepo.create(new Room(50, 2, 210, "Twin Room", 80, "Unavailable"));
        roomRepo.create(new Room(51, 2, 211, "Queen Room", 200, "Dirty"));
        roomRepo.create(new Room(58, 4, 440, "Suite", 450, "Unavailable"));
        roomRepo.create(new Room(60, 2, 212, "Single Room", 60, "Available"));
        return roomRepo;
    }


    private static Repository<Department> createDepartmentInFileRepository() {
        Repository<Department> departmentRepository = new FileRepository<>("departments.db");
        List<Employee> employees;
        employees = createEmployeeFileRepository().getAll();

        ArrayList<Employee> cleaners = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee instanceof Cleaner) {
                cleaners.add(employee);
            }
        }
        departmentRepository.create(new Department(9215, "Cleaning Department", cleaners));

        ArrayList<Employee> receptionists = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee instanceof Receptionist) {
                receptionists.add(employee);
            }
        }
        departmentRepository.create(new Department(9216, "Receptionist Department", receptionists));

        ArrayList<Employee> managers = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee instanceof Manager) {
                managers.add(employee);
            }
        }
        departmentRepository.create(new Department(9217, "Manager Department", managers));

        ArrayList<Employee> structuralManagers = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee instanceof Manager && ((Manager) employee).getManagedDepartmentID() == 9218) {
                structuralManagers.add(employee);
            }
        }
        departmentRepository.create(new Department(9218, "Structural Department", structuralManagers));
        return departmentRepository;
    }


    private static Repository<Customer> createCustomerInFileRepository() {
        Repository<Customer> customerRepo = new FileRepository<>("customers.db");
        customerRepo.create(new Customer(1000, "Harry Bergenson"));
        customerRepo.create(new Customer(1002, "Tamara Smith"));
        customerRepo.create(new Customer(1001, "Julia Beta"));
        return customerRepo;
    }


    private static Repository<RoomCustomer> createRoomCustomerInFileRepository() {
        Repository<RoomCustomer> roomCustomerRepo = new FileRepository<>("roomCustomers.db");
        Calendar calendar = Calendar.getInstance();                                    //calendar is used for creating the date objects
        calendar.set(2024, Calendar.NOVEMBER, 16);
        Date fromDate = calendar.getTime();
        calendar.set(2024, Calendar.NOVEMBER, 22);
        Date untilDate = calendar.getTime();
        roomCustomerRepo.create(new RoomCustomer(4, 50, 1000, fromDate, untilDate));

        calendar.set(2024, Calendar.OCTOBER, 10);
        fromDate = calendar.getTime();
        calendar.set(2024, Calendar.OCTOBER, 23);
        untilDate = calendar.getTime();
        roomCustomerRepo.create(new RoomCustomer(5, 58, 1001, fromDate, untilDate));

        return roomCustomerRepo;
    }
     */
}

