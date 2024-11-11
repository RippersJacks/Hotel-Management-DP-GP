package Hotel;

import Hotel.model.*;
import Hotel.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    void createClient(String name) {
        //id of customers autoincrements (searches for maximum id and then +1)
        Integer id = 0;
        List<Customer> customerList = customerRepository.getAll();
        for (Customer customer : customerList) {if (id < customer.getId()) id = customer.getId();}
        id += 1;

        Customer client = new Customer(id,name);
        customerRepository.create(client);
    }

    void deleteClient(Integer clientID) {

        //Search for the room in which the customer stayed in order to change its availability; used crossing table for this
        List<RoomCustomer> roomCustomerList = roomCustomerRepository.getAll();
        Integer roomID = null;

        for (RoomCustomer roomCustomer : roomCustomerList) {
            if (clientID.equals(roomCustomer.getCustomerId()))
            {
                roomID = roomCustomer.getRoomId();
            }
        }

        Room changedRoom = roomRepository.get(roomID);
        changedRoom.setAvailability("Dirty");
        roomRepository.update(changedRoom);

        customerRepository.delete(clientID);
    }

    void updateClient(Customer client) {customerRepository.update(client);}

    List<Customer>getAllCustomers(){
        return customerRepository.getAll();
    }

    List<Room> getAvailableRooms(){
        List<Room> roomList = roomRepository.getAll();
        List<Room> availableRoomList = new ArrayList<>();

        Receptionist receptionistFunction = new Receptionist(0,"",0,"",null);

        for (Room room : roomList) {
            if (receptionistFunction.checkRoom(room))
                availableRoomList.add(room);
        }

        return availableRoomList;
    }
    //--------------------------------------------------


    //-----------------MANAGER SECTION------------------
    public void createEmployee(String type, String name, int salary, String password){
        //id of customers autoincrements (searches for maximum id and then +1)
        Integer id = 0;
        List<Employee> employeeList = employeeRepository.getAll();
        for (Employee employee : employeeList) {if (id < employee.getId()) id = employee.getId();}
        id += 1;

        Scanner sc = new Scanner(System.in);
        //TODO: Conditia daca nu ii bun type-u
        if (type.equalsIgnoreCase("Cleaner")) {             //Checks what type of employee we add ignoring the case
            System.out.println("Enter employee floor: ");
            int floor = sc.nextInt();

            Employee employee = new Cleaner(id, name, salary, password, floor);
            employeeRepository.create(employee);
        }
        else if (type.equalsIgnoreCase("Receptionist")){
            ArrayList<String> languages = new ArrayList<>();
            String language = " ";

            while (!language.equalsIgnoreCase("stop")){
                System.out.println("Enter a language: ");
                language = sc.nextLine();
                languages.add(language);
            }
            Employee employee = new Receptionist(id, name, salary, password, languages);
            employeeRepository.create(employee);
        }
        else if (type.equalsIgnoreCase("Manager")){
            System.out.println("Enter departmentId: ");
            int departmentId = sc.nextInt();
            Employee employee = new Cleaner(id, name, salary, password, departmentId);
            employeeRepository.create(employee);
        }
    }


    public void deleteEmployee(Integer id){
        employeeRepository.delete(id);
    }


    public void updateEmployee(Employee employee){
        employeeRepository.update(employee);
    }


    public String getEmployeeType(int id){
        for (Employee employee : employeeRepository.getAll()){
            if (employee.getId() == id){
                if (employee instanceof Receptionist) return "Receptionist";
                else if (employee instanceof Cleaner) return "Cleaner";
                else if (employee instanceof Manager) return "Mananger";
            }
        }
        return null;
    }

    public String getManagersManagedDepartmentType(int id){
        Manager manager = null;
        for (Employee employee : employeeRepository.getAll()){
            if (employee.getId() == id){
                manager = (Manager)employee;
            }
        }
        for (Department department : departmentRepository.getAll()){
            if (manager.getManagedDepartmentID().equals(department.getId())){
                switch (department.getName()){
                    case "Receptionist Department" : return "Receptionist";
                    case "Cleaning Department" : return "Cleaner";
                    case "Manager Department" : return "Manager";
                }
            }
        }
        return null;
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
