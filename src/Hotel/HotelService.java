package Hotel;

import Hotel.model.*;
import Hotel.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class HotelService{
    private final Repository<Room> roomRepository;
    private final Repository<Employee> employeeRepository;
    private final Repository<Customer> customerRepository;
    private final Repository<Department> departmentRepository;
    private final Repository<RoomCustomer> roomCustomerRepository;

    public HotelService(Repository<Room> roomRepository, Repository<Employee> employeeRepository, Repository<Customer> customerRepository, Repository<Department>departmentRepository, Repository<RoomCustomer>roomCustomerRepository) {
        this.roomRepository = roomRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.departmentRepository = departmentRepository;
        this.roomCustomerRepository = roomCustomerRepository;
    }


    //---------------CLEANER SECTION---------------
    public List<Room> checkDirtyRooms(){
        List<Room> roomList = roomRepository.getAll();
        List<Room> dirtyRoomList = new ArrayList<>();

        Cleaner cleanerFunction = new Cleaner(0,"",0,"",0); //just for the use of the checkRoom function

        for (Room room : roomList) {
            if (cleanerFunction.checkRoom(room))
                dirtyRoomList.add(room);
        }
        return dirtyRoomList;
    }

    public boolean cleanRoom(Integer roomID){  //TO-DO: update repo with new value
        List<Room> roomList = roomRepository.getAll();
        Room targetRoom = null;

        for (Room room : roomList) {
            if (room.getId().equals(roomID))
            {
                room.setAvailability("Available");
                targetRoom = room;
                break;
            }
        }
        if (targetRoom != null)
        {
            roomRepository.update(targetRoom);
            return true;
        }
        else return false;
    }
    //--------------------------------------------------



    //---------------RECEPTIONIST SECTION---------------
    ///TO-DO: checkout (once a client is removed, the room becomes dirty(no longer unavailable))
    void addClient(Customer client) {
        customerRepository.create(client);
    }

    void removeClient(Customer client) {

        //Search for the room in which the customer stayed in order to change its availability; used crossing table for this
        List<RoomCustomer> roomCustomerList = roomCustomerRepository.getAll();
        Integer roomID = null;

        for (RoomCustomer roomCustomer : roomCustomerList) {
            if (client.getId().equals(roomCustomer.getCustomerId()))
            {
                roomID = roomCustomer.getRoomId();
            }
        }

        Room changedRoom = roomRepository.get(roomID);
        changedRoom.setAvailability("Dirty");
        roomRepository.update(changedRoom);

        customerRepository.delete(client.getId());
    }

    void editClient(Customer client) {
        customerRepository.update(client);
    }
    //--------------------------------------------------



    //-----------------MANAGER SECTION------------------
    public void addEmployee(Employee employee){
        employeeRepository.create(employee);
    }
    public void fireEmployee(Employee employee){
        employeeRepository.delete(employee.getId());
    }
    public void editEmployee(Employee employee){
        employeeRepository.update(employee);
    }
    //--------------------------------------------------


    //-----------------DEPARTMENT SECTION------------------
    public void addDepartment(Department department) {departmentRepository.create(department);}
    public void deleteDepartment(Department department) {departmentRepository.delete(department.getId());}
    public void editDepartment(Department department) {departmentRepository.update(department);}
    //--------------------------------------------------

    //-----------------ROOMCUSTOMER SECTION------------------
    public void addRoomCustomer(RoomCustomer roomCustomer) {roomCustomerRepository.create(roomCustomer);}
    public void deleteRoomCustomer(RoomCustomer roomCustomer) {roomCustomerRepository.delete(roomCustomer.getId());}
    public void updateRoomCustomer(RoomCustomer roomCustomer) {roomCustomerRepository.update(roomCustomer);}
    //--------------------------------------------------
}
