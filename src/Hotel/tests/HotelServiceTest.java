package Hotel.tests;

import Hotel.model.*;
import Hotel.repository.DBRepository.*;
import Hotel.service.HotelService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HotelServiceTest {

    HotelService hotelService;

    public HotelServiceTest() {
        ReceptionistDBRepository receptionistDBRepository = new ReceptionistDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        RoomCustomerDBRepository roomCustomerDBRepository = new RoomCustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        ManagerDBRepository managerDBRepository = new ManagerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        DepartmentDBRepository departmentDBRepository = new DepartmentDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        CustomerDBRepository customerDBRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        CleanerDBRepository cleanerDBRepository = new CleanerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        RoomDBRepository roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");

        hotelService = new HotelService(receptionistDBRepository, roomCustomerDBRepository, managerDBRepository, departmentDBRepository, customerDBRepository, cleanerDBRepository, roomDBRepository);
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
        hotelService.createRoom(-1,-1,-1,"Single Room", -1, "Dirty");

        int ok = 0;
        for (Room room: hotelService.checkDirtyRooms())
            if (room.getId() == -1)
                ok = 1;

        assertEquals(ok, 1);


        hotelService.cleanRoom(-1);
        for (Room room: hotelService.checkDirtyRooms())
            if (room.getId() == -1)
                ok = 0;

        hotelService.deleteRoom(-1);
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

//    @Test
//    void updateClient() {    //nu merge updateClient (problema in repository)
//        hotelService.createRoom(1973,-1,-1,"Single Room",-1,"Available");
//        hotelService.createClient("TestSubject123",1973,"2024-11-21","2024-11-28");
//
//        int ok = 0;
//        int id = 0;
//        for (Customer customer: hotelService.getAllCustomers())
//            if (customer.getName().equals("TestSubject123")) {
//                ok = 1;
//                id = customer.getId();
//                break;
//            }
//        assertEquals(ok, 1);
//
//        Customer client = new Customer(id,"TestedDeadSubject123");
//        hotelService.updateClient(client);
//        System.out.println(hotelService.getAllCustomers());
//
//        for (Customer customer: hotelService.getAllCustomers())
//            if (customer.getName().equals("TestedDeadSubject123")){
//                ok = 2;
//                break;
//            }
//
//        hotelService.deleteClient(id);
//        hotelService.deleteRoom(1973);
//
//        assertEquals(ok, 2);
//    }

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