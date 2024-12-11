package Hotel.tests;

import Hotel.model.*;
import Hotel.repository.DBRepository.*;
import Hotel.service.HotelService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HotelServiceTest {

    HotelService hotelService;
    ReceptionistDBRepository receptionistDBRepository;
    RoomCustomerDBRepository roomCustomerDBRepository;
    ManagerDBRepository managerDBRepository;
    DepartmentDBRepository departmentDBRepository;
    CustomerDBRepository customerDBRepository;
    CleanerDBRepository cleanerDBRepository;
    RoomDBRepository roomDBRepository;
    RoomCleanerDBRepository roomCleanerDBRepository;

    public HotelServiceTest() {
        receptionistDBRepository = new ReceptionistDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        roomCustomerDBRepository = new RoomCustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        managerDBRepository = new ManagerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        departmentDBRepository = new DepartmentDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        customerDBRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        cleanerDBRepository = new CleanerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        roomCleanerDBRepository = new RoomCleanerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");

        hotelService = new HotelService(receptionistDBRepository, roomCustomerDBRepository, managerDBRepository, departmentDBRepository, customerDBRepository, cleanerDBRepository, roomDBRepository, roomCleanerDBRepository);
    }

    @Test
    void checkDirtyRooms() {
        hotelService.createRoom(-1,-1,-1,"Single Room", -1, "Dirty");

        int ok = 0;
        for (Room room: hotelService.checkDirtyRooms())
            if (room.getId() == -1)
                ok = 1;

        assertEquals(ok, 1);
        hotelService.deleteRoom(-1);
    }

    @Test
    void cleanRoom() {
        hotelService.createRoom(-1,4,-1,"Single Room", -1, "Dirty");
        hotelService.createRoomCleaner(6972,4);

        //---check that cleanRoom works
        hotelService.cleanRoom(6972,-1);

        int ok = 1;
        for (Room room: hotelService.checkDirtyRooms())
            if (room.getId() == -1)
                ok = 0;
        hotelService.deleteRoom(-1);

        int id = 0;
        for (RoomCleaner roomCleaner: roomCleanerDBRepository.getAll())
            if (id < roomCleaner.getId())
                id = roomCleaner.getId();
        roomCleanerDBRepository.delete(id);

        assertEquals(ok, 1);


        //---check that cleanRoom throws exception for wrong assigned floor
        hotelService.createRoom(-1,3,-1,"Single Room", -1, "Dirty"); //create a room on floor 3
        hotelService.createRoomCleaner(6972,4);  //assign all rooms of floor 4 to Cleaner
        assertThrows(RuntimeException.class, () -> hotelService.cleanRoom(4,-1));  //tries to clean a floor different than 4

        hotelService.deleteRoom(-1);
        roomCleanerDBRepository.delete(id);
    }

    @Test
    void getAllRoomCleaners(){
        ArrayList<RoomCleaner> oldList = new ArrayList<>(roomCleanerDBRepository.getAll());

        for (RoomCleaner roomCleaner: roomCleanerDBRepository.getAll())
            roomCleanerDBRepository.delete(roomCleaner.getId());

        assertThrows(RuntimeException.class, () -> hotelService.getAllRoomCleaners());

        for (RoomCleaner roomCleaner: oldList)
            roomCleanerDBRepository.create(roomCleaner);
    }

    @Test
    void createRoomCleaner() {
        roomDBRepository.create(new Room(-1,-50,-1,"Single Room", -1, "Available"));
        cleanerDBRepository.create(new Cleaner(-2,"Papa",20,"papspdwpapwd",2,-50));
        hotelService.createRoomCleaner(-2,-50);

        int ok = 0;
        int id=0;
        for (RoomCleaner roomCleaner: roomCleanerDBRepository.getAll())
            if (roomCleaner.getCleanerId() == -2 && roomCleaner.getRoomId() == -1)
            {
                ok = 1;
                id = roomCleaner.getId();
                break;
            }

        roomCleanerDBRepository.delete(id);
        roomDBRepository.delete(-1);
        cleanerDBRepository.delete(-2);

        assertEquals(ok, 1);
    }

    @Test
    void createClient() {
        hotelService.createRoom(1973,-1,-1,"Single Room",-1,"Available");
        hotelService.createClient("TestSubject123",1973,"2024-11-21","2024-11-28");

        int ok = 0;
        int id = 0;
        for (Customer customer: hotelService.getAllCustomers())
            if (customer.getName().equals("TestSubject123")) {
                ok = 1;
                id = customer.getId();
                break;
            }

        hotelService.deleteClient(id);
        hotelService.deleteRoom(1973);
        int idRoomCustomer = hotelService.searchRoomCustomerByCustomer(id).getFirst();
        hotelService.deleteRoomCustomer(idRoomCustomer);

        assertEquals(ok, 1);
    }

    @Test
    void deleteClient() {
        hotelService.createRoom(1973,-1,-1,"Single Room",-1,"Available");
        hotelService.createClient("TestSubject123",1973,"2024-11-21","2024-11-28");

        int ok = 0;
        int id = 0;
        for (Customer customer: hotelService.getAllCustomers())
            if (customer.getName().equals("TestSubject123")) {
                ok = 1;
                id = customer.getId();
                break;
            }
        assertEquals(ok, 1);

        hotelService.deleteClient(id);
        hotelService.deleteRoom(1973);

        for (Customer customer: hotelService.getAllCustomers())
            if (customer.getName().equals("TestSubject123")) {
                ok = 0;
                break;
            }
        assertEquals(ok, 1);
    }

    @Test
    void updateClient() {
        hotelService.createRoom(1973,-1,-1,"Single Room",-1,"Available");
        hotelService.createClient("TestSubject123",1973,"2024-11-21","2024-11-28");

        int ok = 0;
        int id = 0;
        for (Customer customer: hotelService.getAllCustomers())
            if (customer.getName().equals("TestSubject123")) {
                ok = 1;
                id = customer.getId();
                break;
            }
        assertEquals(ok, 1);

        Customer client = new Customer(id,"TestedDeadSubject123");
        hotelService.updateClient(client);

        for (Customer customer: hotelService.getAllCustomers())
            if (customer.getName().equals("TestedDeadSubject123")){
                ok = 2;
                break;
            }

        hotelService.deleteClient(id);
        hotelService.deleteRoom(1973);

        assertEquals(ok, 2);
    }

    @Test
    void getAllCustomers() {
        CustomerDBRepository customerDBRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        //assertEquals(hotelService.getAllCustomers(),customerDBRepository.getAll());  - nu merge pt ca sunt liste diferite

        for (Customer customer: hotelService.getAllCustomers())
        {
            int ok=0;
            for (Customer customer1: customerDBRepository.getAll())
                if (Objects.equals(customer.getId(), customer1.getId())) {
                    ok = 1;
                    break;
                }
            if (ok == 0)
                assert(false);
        }
         assert(true);
    }

    @Test
    void getAvailableRooms() {
        for (Room room: hotelService.getAvailableRooms())
            assert room.getAvailability().equals("Available");
    }

    @Test
    void sortRoomCustomerByUntilDate(){
        ArrayList<RoomCustomer> sortedList = new ArrayList<>(hotelService.sortRoomCustomerByUntilDate());

        for (int i=0;i<sortedList.size()-1;i++)
            for (int j=i+1;j<sortedList.size();j++)
                assert sortedList.get(i).getUntilDate().compareTo(sortedList.get(j).getUntilDate()) <= 0;
    }

    @Test
    void getAllRoomsOfACustomer() {
        hotelService.createRoom(1973,-1,-1,"Single Room",-1,"Available");
        hotelService.createClient("TestSubject123",1973,"2024-11-21","2024-11-28");

        int id = -1;
        for (Customer customer: hotelService.getAllCustomers())
            if (customer.getName().equals("TestSubject123")) {id=customer.getId(); break;}

        List<Room> roomList = hotelService.getAllRoomsOfACustomer(id);

        hotelService.deleteClient(id);
        hotelService.deleteRoom(1973);

        assertEquals(roomList.getFirst().getId(), 1973);
    }

    @Test
    void getEmployeeTypeString() {
        assertEquals(hotelService.getEmployeeTypeString(200),"Receptionist");
    }


    @Test
    void getManagersManagedDepartmentType() {
        assertEquals(hotelService.getManagersManagedDepartmentType(6969),"Cleaner");
    }

    @Test
    void showAllDepartments() {
        for (Department department: hotelService.showAllDepartments())
            if (!department.getName().equals("Cleaning Department") && !department.getName().equals("Receptionist Department")
            && !department.getName().equals("Manager Department") && !department.getName().equals("Structural Department"))
                assert(false);
        assert(true);
    }

    @Test
    void createDepartment() {
        hotelService.createDepartment("Security Department");
        int ok=0;
        int id=-1;
        for (Department department: hotelService.getAllDepartments())
            if (department.getName().equals("Security Department")) {
                ok = 1;
                id = department.getId();
                break;
            }

        hotelService.deleteDepartment(id);
        assertEquals(ok,1);
    }

    @Test
    void deleteDepartment() {
        hotelService.createDepartment("Security Department");
        int ok=0;
        int id=-1;
        for (Department department: hotelService.getAllDepartments())
            if (department.getName().equals("Security Department")) {
                ok = 1;
                id = department.getId();
                break;
            }

        hotelService.deleteDepartment(id);
        assertEquals(ok,1);

        for (Department department: hotelService.getAllDepartments())
            if (department.getName().equals("Security Department")) {
                ok = 0;
                break;
            }
        assertEquals(ok,1);
    }

    @Test
    void updateDepartment() {
        hotelService.createDepartment("Security Department");
        int ok=0;
        int id=-1;
        for (Department department: hotelService.getAllDepartments())
            if (department.getName().equals("Security Department")) {
                ok = 1;
                id = department.getId();
                break;
            }

        Department department1 = new Department(id,"Insecurity Department");
        hotelService.updateDepartment(department1);
        assertEquals(ok,1);

        for (Department department: hotelService.getAllDepartments())
            if (department.getName().equals("Insecurity Department")) {
                ok = 2;
                break;
            }
        hotelService.deleteDepartment(id);
        assertEquals(ok,2);
    }

    @Test
    void createRoomCustomer() {
        hotelService.createRoom(1973,-1,-1,"Single Room",-1,"Available");
        hotelService.createClient("TestSubject955",1973,"2024-11-21","2024-11-28");
        int id=-1;
        for (Customer customer: hotelService.getAllCustomers())
            if (customer.getName().equals("TestSubject955")) {id=customer.getId(); break;}
        hotelService.createRoomCustomer(id,1973,"2024-11-21","2024-11-28");
    }
}