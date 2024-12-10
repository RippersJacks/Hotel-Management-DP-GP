package Hotel.service;

import Hotel.model.*;
import Hotel.repository.DBRepository.*;
import Hotel.repository.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

public class HotelService{
    //private final Repository<Room> roomRepository;
    //private final Repository<Employee> employeeRepository;
    //private final Repository<Customer> customerRepository;
    //private final Repository<Department> departmentRepository;
    //private final Repository<RoomCustomer> roomCustomerRepository;
    private final ReceptionistDBRepository receptionistDBRepository;
    private final RoomCustomerDBRepository roomCustomerDBRepository;
    private final ManagerDBRepository managerDBRepository;
    private final DepartmentDBRepository departmentDBRepository;
    private final CustomerDBRepository customerDBRepository;
    private final CleanerDBRepository cleanerDBRepository;
    private final RoomDBRepository roomDBRepository;

    /**
     * Contains the functionality of the project and works with the repositories.
     * @param roomDBRepository repository with objects of type Room
     * @param cleanerDBRepository repository with objects of type Employee
     * @param customerDBRepository repository with objects of type Customer
     * @param departmentDBRepository repository with objects of type Department
     * @param roomCustomerDBRepository repository with objects of type RoomCustomer (crossing table for Room & Customer)
     */
    public HotelService( ReceptionistDBRepository receptionistDBRepository, RoomCustomerDBRepository roomCustomerDBRepository, ManagerDBRepository managerDBRepository, DepartmentDBRepository departmentDBRepository, CustomerDBRepository customerDBRepository, CleanerDBRepository cleanerDBRepository, RoomDBRepository roomDBRepository) {
        //this.roomRepository = roomRepository;
        //this.employeeRepository = employeeRepository;
        //this.customerRepository = customerRepository;
        //this.departmentRepository = departmentRepository;
        //this.roomCustomerRepository = roomCustomerRepository;
        this.receptionistDBRepository = receptionistDBRepository;
        this.roomCustomerDBRepository = roomCustomerDBRepository;
        this.managerDBRepository = managerDBRepository;
        this.departmentDBRepository = departmentDBRepository;
        this.customerDBRepository = customerDBRepository;
        this.cleanerDBRepository = cleanerDBRepository;
        this.roomDBRepository = roomDBRepository;
    }


    //---------------CLEANER SECTION---------------

    /**
     * Returns a list of all rooms that are of availability Dirty in the database.
     * @return list with objects of type Room, with availability Dirty
     */
    public List<Room> checkDirtyRooms(){
        List<Room> roomList = roomDBRepository.getAll();
        if (roomList.isEmpty()){throw new NullPointerException("No rooms exist");}
        List<Room> dirtyRoomList = new ArrayList<>();

        Cleaner cleanerFunction = new Cleaner(0,"",0,"",0,0); //just for the use of the checkRoom function which is located in the Cleaner class

        for (Room room : roomList) {
            if (cleanerFunction.checkRoom(room))
                dirtyRoomList.add(room);
        }
        return dirtyRoomList;
    }

    /**
     * Uses a given ID parameter to find a room and change its availability from "Dirty" to "Available".
     * <p>
     * If the given room has been found and can be cleaned, true is returned.
     * If it doesnt exist, or exists but is of availability Available or Unavailable, an exception is thrown.
     * <p>
     * Availability of given room must be "Dirty" in order for it to be cleaned.
     *
     * @param roomID Integer object representing the ID of the room to be cleaned
     * @return true or false
     */
    public boolean cleanRoom(Integer roomID){
        List<Room> roomList = roomDBRepository.getAll();

        for (Room room : roomList) {
            if (room.getId().equals(roomID))
            {
                if (room.getAvailability().equals("Dirty"))
                {
                    room.setAvailability("Available");
                    roomDBRepository.update(room);
                    return true;
                }
                else
                    throw new IllegalArgumentException("Room is currently occupied, cannot clean");
            }
        }
        throw new IllegalArgumentException("Room does not exist");
    }
    //--------------------------------------------------



    //---------------RECEPTIONIST SECTION---------------

    /**
     * Adds a new customer to the Customer section of the database.
     * His ID is determined automatically.
     * It also passes parameters to he createRoomCustomer function to create simultaneosly a roomCustomer*
     * @param name name of the customer to be created
     * @param roomId the room id that will be used to create the roomCustomer
     * @param checkInDate also used to create the roomCustomer
     * @param checkOutDate also used to create the roomCustomer
     */
    public void createClient(String name, int roomId, String checkInDate, String checkOutDate) {
        //id of customers autoincrements (searches for maximum id and then +1)
        Integer id = 0;
        List<Customer> customerList = customerDBRepository.getAll();
        for (Customer customer : customerList) {if (id < customer.getId()) id = customer.getId();}
        id += 1;

        if (roomId < 0)
            throw new NullPointerException("Room id cant be negative");


        Customer client = new Customer(id, name);
        this.createRoomCustomer(id, roomId, checkInDate, checkOutDate);
        customerDBRepository.create(client);
    }

    /**
     * Deletes a customer from the database.
     * @param clientID id of the customer to be deleted
     */
    public void deleteClient(Integer clientID) {

        //Search for the room in which the customer stayed in order to change its availability; used crossing table for this
        List<RoomCustomer> roomCustomerList = roomCustomerDBRepository.getAll();
        Integer roomID = null;

        for (RoomCustomer roomCustomer : roomCustomerList) {
            if (clientID.equals(roomCustomer.getCustomerId()))
            {
                roomID = roomCustomer.getRoomId();
            }
        }
        Room changedRoom = roomDBRepository.get(roomID);
        changedRoom.setAvailability("Dirty");
        roomDBRepository.update(changedRoom);

        customerDBRepository.delete(clientID);
    }

    /**
     * Updates a customer in the customer database.
     * @param client object of type Customer, a.k.a. the customer to be updated
     */
    public void updateClient(Customer client) {
        customerDBRepository.update(client);
    }

    /**
     * Returns a list containing all customers.
     * @return list of customers
     */
    public List<Customer>getAllCustomers(){
        return customerDBRepository.getAll();
    }

    /**
     * Returns a list containing all rooms of availability "Available".
     * @return list of rooms
     */
    public List<Room> getAvailableRooms(){
        Receptionist receptionistFunction = new Receptionist(0,"",0,"",0, null);
        //lambda function inheriting the checkRoom functionality of the Receptionist Class
        Predicate<Room> checkIfRoomIsAvailable = receptionistFunction::checkRoom;

        return roomDBRepository.getAll().stream().filter(checkIfRoomIsAvailable).toList();
    }

    /**
     * Sorts a copy of the roomCustomerRepository filtering by the end of each customers stay.
     * @return the sorted list of RoomCustomer objects
     */
    public List<RoomCustomer> sortRoomCustomerByUntilDate(){
        List<RoomCustomer> roomCustomerList = new ArrayList<>(roomCustomerDBRepository.getAll());
        Collections.sort(roomCustomerList);
        return roomCustomerList;
    }


    /**
     * Returns a list containing all rooms reserved by a specific customer.
     * @param customerID the ID of the searched customer
     * @return list of rooms
     */
    public List<Room> getAllRoomsOfACustomer(int customerID){

        List<Room> roomList = new ArrayList<>();

        for (RoomCustomer roomCustomer : roomCustomerDBRepository.getAll()) {
            if (roomCustomer.getCustomerId() == customerID)
                roomList.add(roomDBRepository.get(roomCustomer.getRoomId()));
        }

        return roomList;
    }

    //--------------------------------------------------


    //-----------------MANAGER SECTION------------------

    public List<Employee>getAllEmployees(){
        List<Employee> employeeList = new ArrayList<>();
        List<Cleaner> cleanerList = cleanerDBRepository.getAll();
        List<Manager> managerList = managerDBRepository.getAll();
        List<Receptionist> receptionistList = receptionistDBRepository.getAll();
        employeeList.addAll(cleanerList);
        employeeList.addAll(managerList);
        employeeList.addAll(receptionistList);
        return employeeList;
    }


    public int getNextAvailableId(){
        int id = 0;
        for (Employee employee : getAllEmployees()) {if (id < employee.getId()) id = employee.getId();}
        id += 1;

        return id;
    }
    /**
     * Creates a new employee in the database. This includes giving the type, name, salary and password as input.
     * @param type type (role) of the employee (ex. Receptionist)
     * @param name name of the employee
     * @param salary salary of the employee
     * @param password password of the employee
     */
    public void createEmployee(String type, String name, int salary, int departmentId, String password){
        //id of customers autoincrements (searches for maximum id and then +1)
        Integer id = getNextAvailableId();
        //List<Employee> employeeList = employeeRepository.getAll();
        //for (Employee employee : employeeList) {if (id < employee.getId()) id = employee.getId();}
        //id += 1;

        Scanner sc = new Scanner(System.in);
        //TODO: Conditia daca nu ii bun type-u
        if (type.equalsIgnoreCase("Cleaner")) {             //Checks what type of employee we add ignoring the case
            System.out.println("Enter employee floor: ");
            int floor = sc.nextInt();
            sc.nextLine();

            Cleaner employee = new Cleaner(id, name, salary, password, departmentId, floor);
            cleanerDBRepository.create(employee);
        }
        else if (type.equalsIgnoreCase("Receptionist")){
            ArrayList<String> languages = new ArrayList<>();
            String language = " ";

            while (!language.equalsIgnoreCase("stop")){
                System.out.println("Enter a language: ");
                language = sc.nextLine();
                if (language.isEmpty())
                    throw new NullPointerException("Language cannot be empty");
                languages.add(language);
            }
            Receptionist employee = new Receptionist(id, name, salary, password, departmentId, languages);
            receptionistDBRepository.create(employee);
        }
        else if (type.equalsIgnoreCase("Manager")){
            System.out.println("Enter department id: ");
            int managerdepartmentId = sc.nextInt();
            sc.nextLine();
            if (departmentId < 0)
                throw new NullPointerException("Department id cannot be empty");

            Manager employee = new Manager(id, name, salary, password, departmentId ,managerdepartmentId);
            managerDBRepository.create(employee);
        }
    }

    /**
     * Removes an employee from the database.
     * @param id ID of the employee to be removed
     */
    public void deleteEmployee(Integer id){
        managerDBRepository.delete(id);
        receptionistDBRepository.delete(id);
        cleanerDBRepository.delete(id);
    }


    /**
     * Changes an employees values by receiving new data as a parameter and
     * replacing the employee object in the database with given data.
     * @param employee employee object with same ID, new data (salary,password)
     */
    public void updateEmployee(Employee employee){
        managerDBRepository.update((Manager) employee);
        receptionistDBRepository.update((Receptionist) employee);
        cleanerDBRepository.update((Cleaner) employee);

    }


    /**
     * Returns the type of an employee. (ex. Receptionist)
     * @param id ID of the employee
     * @return String object with name of the type ("Receptionist", "Cleaner" or "Manager")
     */
    public String getEmployeeTypeString(int id){
        for (Employee employee : getAllEmployees()){
            if (employee.getId() == id){
                switch (employee) {
                    case Receptionist receptionist -> {
                        return "Receptionist";
                    }
                    case Cleaner cleaner -> {
                        return "Cleaner";
                    }
                    case Manager manager -> {
                        return "Mananger";
                    }
                    default -> {
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns what department type a manager has access to. (The type of employee is returned, ex. Receptionist)
     * <p>
     * ex. of use: A manager managing the Receptionist department may only alter info of employees of type Receptionist.
     * @param id ID of the manager
     * @return String object in format: "Receptionist", "Cleaner" or "Manager"
     */
    public String getManagersManagedDepartmentType(int id){
        Manager manager = null;
        for (Employee employee : getAllEmployees()){
            if (employee.getId() == id){
                manager = (Manager)employee;
                break;
            }
        }
        for (Department department : departmentDBRepository.getAll()){
            if (manager.getManagedDepartmentID().equals(department.getId())){
                switch (department.getName()){
                    case "Receptionist Department" : return "Receptionist";
                    case "Cleaning Department" : return "Cleaner";
                    case "Manager Department" : return "Manager";
                    case "Structural Department": return "Department";
                }
            }
        }
        return null;
    }


    //public List<Employee> showAllEmployees(){
        //return employeeDBRepository.getAll();
    //}
    public List<Department> showAllDepartments(){
        return departmentDBRepository.getAll();
    }

    /**
     * Creates a copy of the repository (list of type Employee), sorts it and then returns it.
     */
    public List<Employee> sortEmployeesBySalary(){
        List<Employee> sortedEmployeeList = new ArrayList<>(getAllEmployees());

        Collections.sort(sortedEmployeeList);
        return sortedEmployeeList;
    }
    //--------------------------------------------------


    //-----------------DEPARTMENT SECTION------------------
    public void createDepartment(String name) {
        Integer id = 0;
        List<Department> departmentList = departmentDBRepository.getAll();
        for (Department department : departmentList) {if (id < department.getId()) id = department.getId();}
        id += 1;

        Department newDepartment = new Department(id, name);
        departmentDBRepository.create(newDepartment);
    }


    public void deleteDepartment(int id) {
        departmentDBRepository.delete(id);
    }

    public void updateDepartment(Department department) {departmentDBRepository.update(department);}

    public List<Department> getAllDepartments(){
        return departmentDBRepository.getAll();
    }

    public List<? extends Employee> getDepartmentEmployees(int id) {
        if (id == managerDBRepository.getAll().getFirst().getId()){
            return managerDBRepository.getAll();
        }else if (id == cleanerDBRepository.getAll().getFirst().getId()){
            return cleanerDBRepository.getAll();
        }else return receptionistDBRepository.getAll();
    }

    /**
     * Makes a sum of each employee's salary (of given department) and divides it by the employee count (of given department).
     * @param department object of type Department
     * @return int value containing the average employee salary
     */
    public int getAvgSalaryOfADepartment(Department department) {
        int sum = 0;
        int nrEmployees = 0;
        for (Employee employee : getDepartmentEmployees(department.getId())) {
            sum += employee.getSalary();
            nrEmployees++;
        }
        return sum / nrEmployees;
    }

    /**
     * Filters the department repository and returns all departments with an average salary of over an input given number.
     * <p>
     * Average salary is calculated by adding all employee salaries and then dividing it by the employee count.
     * @return list of departments (filtered)
     */
    public List<Department> getDepartmentsWithOverGivenNumberAvgSalary(int inputNumber) {

        //lambda function that gets the average salary of a given department
        //and then asks if the average salary of the department is higher than the input number
        Predicate<Department> avgSalaryOverGivenNr = department -> {
            int sum = getAvgSalaryOfADepartment(department);
            return sum > inputNumber;
        };

        List<Department> departmentList = departmentDBRepository.getAll();
        return departmentList.stream().filter(avgSalaryOverGivenNr).toList();
    }


    //--------------------------------------------------


    //-----------------ROOMCUSTOMER SECTION------------------
    public void createRoomCustomer(int customerId, int roomId, String checkInDate, String checkOutDate) {
        Integer id = 0;
        List<RoomCustomer> roomCustomerList = roomCustomerDBRepository.getAll();
        for (RoomCustomer roomCustomer : roomCustomerList) {if (id < roomCustomer.getId()) id = roomCustomer.getId();}
        id += 1;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateFormatCheckInDate = formatter.parse(checkInDate);
            Date dateFormatCheckOutDate = formatter.parse(checkOutDate);
            RoomCustomer newRoomCustomer = new RoomCustomer(id, roomId, customerId, dateFormatCheckInDate, dateFormatCheckOutDate);
            roomCustomerDBRepository.create(newRoomCustomer);

        } catch (ParseException e) {
            e.printStackTrace();

        }
    }


    public List<Integer> searchRoomCustomerByCustomer(int id){
        ArrayList<Integer>idsList = new ArrayList<>();
        for (RoomCustomer roomCustomer : roomCustomerDBRepository.getAll()){
            if (roomCustomer.getCustomerId() == id) idsList.add( roomCustomer.getId());
        }
        return idsList;
    }


    public void deleteRoomCustomer(int id) {
        for (int roomCustomerId : searchRoomCustomerByCustomer(id))
            roomCustomerDBRepository.delete(roomCustomerId);}


    public void updateRoomCustomer(RoomCustomer roomCustomer) {roomCustomerDBRepository.update(roomCustomer);}
    //--------------------------------------------------



    //------ROOM MANAGEMENT------
    //--> only used for tests

    public void createRoom(int id, int floor, int number, String type, int pricePerNight, String availability){
        roomDBRepository.create(new Room(id, floor, number, type, pricePerNight, availability));
    }

    public void deleteRoom(int id) {
        roomDBRepository.delete(id);
    }

    public void updateRoom(int id, int floor, int number, String type, int pricePerNight, String availability) {
        roomDBRepository.update(new Room(id, floor, number, type, pricePerNight, availability));
    }

    public Room getRoom(int id){
        return roomDBRepository.get(id);
    }

    public List<Room> getAllRooms(){
        return roomDBRepository.getAll();
    }
}


