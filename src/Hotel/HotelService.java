package Hotel;

import Hotel.model.Customer;
import Hotel.model.Employee;
import Hotel.repository.Repository;
import Hotel.model.Cleaner;
import Hotel.model.Room;

import java.util.ArrayList;
import java.util.List;

public class HotelService{
    private final Repository<Room> roomRepository;
    private final Repository<Employee> employeeRepository;
    private final Repository<Customer> customerRepository;

    public HotelService(Repository<Room> roomRepository, Repository<Employee> employeeRepository, Repository<Customer> customerRepository) {
        this.roomRepository = roomRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
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
    ///TO-DO: checkout (once a client is removed, the room becomes available)
    void addClient(Customer client) {
        customerRepository.create(client);
    }
    void removeClient(Customer client) {
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

}
