package Hotel.repository.DBRepository;

import Hotel.model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class main {
    public static void main(String[] args) {
        Room room = new Room(2, 10, 10, "Suite", 200, "Available");
        Customer customer = new Customer(4, "John Cena");
        Cleaner cleaner = new Cleaner(4, "Iona", 23000, "1234", 2, 2);
        List<String> languages = new ArrayList<>();
        languages.add("English");
        Receptionist receptionist = new Receptionist(200,"Big Ph", 100, "ahhh", 2, languages);
        Manager manager = new Manager(6969, "Paul", 2000, "Eu", 1, 2);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.NOVEMBER, 16);
        Date fromDate = calendar.getTime();
        calendar.set(2024, Calendar.NOVEMBER, 22);
        Date untilDate = calendar.getTime();
        RoomCustomer roomCustomer = new RoomCustomer(22, 2, 4, fromDate, untilDate);

        CustomerDBRepository customerBDRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        RoomDBRepository roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        CleanerDBRepository cleanerDBRepository = new CleanerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
        ReceptionistDBRepository receptionistDBRepository = new ReceptionistDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
        ManagerDBRepository managerDBRepository = new ManagerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
        RoomCustomerDBRepository roomCustomerDBRepository = new RoomCustomerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");

        roomDBRepository.create(room);
        customerBDRepository.create(customer);
        cleanerDBRepository.create(cleaner);
        receptionistDBRepository.create(receptionist);
        managerDBRepository.create(manager);
        roomCustomerDBRepository.create(roomCustomer);
    }
}
