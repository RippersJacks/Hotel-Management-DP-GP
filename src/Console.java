import Hotel.HotelController;
import Hotel.HotelRegistrationSystem;
import Hotel.HotelService;
import Hotel.model.*;
import Hotel.repository.InMemoryRepository;
import Hotel.repository.Repository;

import java.util.ArrayList;
import java.util.List;
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

        List<Employee> employees = new ArrayList<>();
        employees = createInMemoryEmployeeRepository().getAll();

        ArrayList<Employee> cleaners = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee instanceof Cleaner) {cleaners.add((Cleaner) employee);}
        }
        departmentRepo.create(new Department(9215, "Cleaning Department", cleaners));

        ArrayList<Employee> receptionists = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee instanceof Receptionist) {cleaners.add((Receptionist) employee);}
        }
        departmentRepo.create(new Department(9215, "Cleaning Department", receptionists));

        departmentRepo.getAll().forEach(System.out::println);
        return departmentRepo;
    }


    ///TO-DO: createInMemory pt Rooms, Customers si RoomCustomer sa extraga din acestea
}
