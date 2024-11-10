import Hotel.HotelController;
import Hotel.HotelRegistrationSystem;
import Hotel.HotelService;
import Hotel.model.*;
import Hotel.repository.InMemoryRepository;
import Hotel.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Console {

    private final HotelController hotelController;

    public Console(HotelController hotelController) {
        this.hotelController = hotelController;
    }

    public void run(){
        HotelRegistrationSystem system = new HotelRegistrationSystem();
        system.login();
    }


    public static void main(String[] args) throws InterruptedException {
        Repository<Employee> employeeRepository = createInMemoryEmployeeRepository();
        Repository<Department> departmentRepository = createInMemoryDepartmentRepository();
        Repository<Room> roomRepository = createInMemoryRoomRepository();
        Repository<Customer> customerRepository = createInMemoryCustomerRepository();
        Repository<RoomCustomer> roomCustomerRepository = createInMemoryRoomCustomerRepository();

        HotelService hotelService = new HotelService(roomRepository, employeeRepository,customerRepository,departmentRepository,roomCustomerRepository);
        HotelController hotelController = new HotelController(hotelService);

        Console console = new Console(hotelController);
        console.run();
    }


    /**
     * Creates an in-memory repository for employees and populates it with some initial data.
     *
     * @return The in-memory repository for employees.
     */
    private static Repository<Employee> createInMemoryEmployeeRepository() {
        Repository<Employee> employeeRepository = new InMemoryRepository<>();

        //Receptionists
        List<String> languageList = new ArrayList<>(); languageList.add("german"); languageList.add("english");
        employeeRepository.create(new Receptionist(100,"Mark",2500,"mark1525",languageList));
        languageList.clear(); languageList.add("ukrainian");
        employeeRepository.create(new Receptionist(101,"Zelensceta",2100,"password123",languageList));

        //Cleaners
        employeeRepository.create(new Cleaner(150,"Tina",1800,"tinytina",1));
        employeeRepository.create(new Cleaner(151,"Zack",1800,"123zack123",2));

        //Managers
        employeeRepository.create(new Manager(10,"James",3200,"james1973",9215));
        employeeRepository.create(new Manager(11,"Victor",4000,"1892WorchestershireSauce!?##Vice",8115));

        return employeeRepository;
    }

    /**
     * Creates an in-memory repository for departments and populates it with some initial data.
     *
     * @return The in-memory repository for departments.
     */
    private static Repository<Department> createInMemoryDepartmentRepository() {
        Repository<Department> departmentRepo = new InMemoryRepository<>();


        departmentRepo.create(new Department(9215, "Cleaning Department", );

        departmentRepo.getAll().forEach(System.out::println);
        return departmentRepo;
    }

    /**
     * Creates an in-memory repository for rooms and populates it with some initial data.
     *
     * @return The in-memory repository for rooms.
     */


    private static Repository<Room> createInMemoryRoomRepository(){
        Repository<Room> roomRepo = new InMemoryRepository<>();
        roomRepo.create(new Room(50, 2, 210, "Twin Room", 80, "Unavailable"));
        roomRepo.create(new Room(51, 2, 211, "Queen Room", 200, "Available"));
        roomRepo.create(new Room(58, 4, 440, "Suite", 450, "Unavailable"));
        return roomRepo;
    }

    /**
     * Creates an in-memory repository for customers and populates it with some initial data.
     *
     * @return The in-memory repository for customers.
     */

    private static Repository<Customer> createInMemoryCustomerRepository(){
        Repository<Customer> customerRepo = new InMemoryRepository<>();
        customerRepo.create(new Customer(1000, "Harry Bergenson"));
        customerRepo.create(new Customer(1002, "Tamara Smith"));
        customerRepo.create(new Customer(1001, "Julia Beta"));
        return customerRepo;
    }


    /**
     * Creates an in-memory repository for roomCustomers and populates it with some initial data.
     *
     * @return The in-memory repository for roomCustomers.
     */

    private static Repository<RoomCustomer> createInMemoryRoomCustomerRepository(){
        Repository<RoomCustomer> roomCustomerRepo = new InMemoryRepository<>();
        Calendar calendar = Calendar.getInstance();                                    //calendar is used for creating the date objects
        calendar.set(2024, Calendar.NOVEMBER, 16);
        Date fromDate = calendar.getTime();
        calendar.set(2024, Calendar.NOVEMBER, 22 );
        Date untilDate = calendar.getTime();
        roomCustomerRepo.create(new RoomCustomer(4, 50, 1000, fromDate, untilDate));

        calendar.set(2024, Calendar.OCTOBER, 10);
        fromDate = calendar.getTime();
        calendar.set(2024, Calendar.OCTOBER, 23 );
        untilDate = calendar.getTime();
        roomCustomerRepo.create(new RoomCustomer(5, 58, 1001, fromDate, untilDate));

        return roomCustomerRepo;
    }


}
