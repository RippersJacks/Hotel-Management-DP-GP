package Hotel.service;

import Hotel.model.*;
import Hotel.repository.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

public class HotelService{
    private final Repository<Room> roomRepository;
    private final Repository<Employee> employeeRepository;
    private final Repository<Customer> customerRepository;
    private final Repository<Department> departmentRepository;
    private final Repository<RoomCustomer> roomCustomerRepository;


    /**
     * Contains the functionality of the project and works with the repositories.
     * @param roomRepository repository with objects of type Room
     * @param employeeRepository repository with objects of type Employee
     * @param customerRepository repository with objects of type Customer
     * @param departmentRepository repository with objects of type Department
     * @param roomCustomerRepository repository with objects of type RoomCustomer (crossing table for Room & Customer)
     */
    public HotelService(Repository<Room> roomRepository, Repository<Employee> employeeRepository, Repository<Customer> customerRepository, Repository<Department>departmentRepository, Repository<RoomCustomer>roomCustomerRepository) {
        this.roomRepository = roomRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.departmentRepository = departmentRepository;
        this.roomCustomerRepository = roomCustomerRepository;
    }


    //---------------CLEANER SECTION---------------

    /**
     * Returns a list of all rooms that are of availability Dirty in the database.
     * @return list with objects of type Room, with availability Dirty
     */
    public List<Room> checkDirtyRooms(){
        List<Room> roomList = roomRepository.getAll();
        List<Room> dirtyRoomList = new ArrayList<>();

        Cleaner cleanerFunction = new Cleaner(0,"",0,"",0); //just for the use of the checkRoom function which is located in the Cleaner class

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
     * If it doesnt exist, or exists but is of availability Available or Unavailable, false is returned.
     * <p>
     * Availability of given room must be "Dirty" in order for it to be cleaned.
     *
     * @param roomID Integer object representing the ID of the room to be cleaned
     * @return true or false
     */
    public boolean cleanRoom(Integer roomID){  //TO-DO: update repo with new value
        List<Room> roomList = roomRepository.getAll();
        Room targetRoom = null;

        for (Room room : roomList) {
            if (room.getId().equals(roomID))
            {
                if (room.getAvailability().equals("Dirty"))
                {
                    room.setAvailability("Available");
                    roomRepository.update(room);
                    return true;
                }
                else
                    return false;
            }
        }
        return false;
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
        List<Customer> customerList = customerRepository.getAll();
        for (Customer customer : customerList) {if (id < customer.getId()) id = customer.getId();}
        id += 1;

        Customer client = new Customer(id, name);
        this.createRoomCustomer(id, roomId, checkInDate, checkOutDate);
        customerRepository.create(client);
    }

    /**
     * Deletes a customer from the database.
     * @param clientID id of the customer to be deleted
     */
    public void deleteClient(Integer clientID) {

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

    /**
     * Updates a customer in the customer database.
     * @param client object of type Customer, a.k.a. the customer to be updated
     */
    public void updateClient(Customer client) {customerRepository.update(client);}

    /**
     * Returns a list containing all customers.
     * @return list of customers
     */
    public List<Customer>getAllCustomers(){
        return customerRepository.getAll();
    }

    /**
     * Returns a list containing all rooms of availability "Available".
     * @return list of rooms
     */
    public List<Room> getAvailableRooms(){
        Receptionist receptionistFunction = new Receptionist(0,"",0,"",null);
        //lambda function inheriting the checkRoom functionality of the Receptionist Class
        Predicate<Room> checkIfRoomIsAvailable = receptionistFunction::checkRoom;

        return roomRepository.getAll().stream().filter(checkIfRoomIsAvailable).toList();
    }

    /**
     * Sorts a copy of the roomCustomerRepository filtering by the end of each customers stay.
     * @return the sorted list of RoomCustomer objects
     */
    public List<RoomCustomer> sortRoomCustomerByUntilDate(){
        List<RoomCustomer> roomCustomerList = new ArrayList<>(roomCustomerRepository.getAll());
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

        for (RoomCustomer roomCustomer : roomCustomerRepository.getAll()) {
            if (roomCustomer.getCustomerId() == customerID)
                roomList.add(roomRepository.get(roomCustomer.getRoomId()));
        }

        return roomList;
    }

    //--------------------------------------------------


    //-----------------MANAGER SECTION------------------

    /**
     * Creates a new employee in the database. This includes giving the type, name, salary and password as input.
     * @param type type (role) of the employee (ex. Receptionist)
     * @param name name of the employee
     * @param salary salary of the employee
     * @param password password of the employee
     */
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
            System.out.println("Enter department id: ");
            int departmentId = sc.nextInt();
            Employee employee = new Cleaner(id, name, salary, password, departmentId);
            employeeRepository.create(employee);
        }
    }

    /**
     * Removes an employee from the database.
     * @param id ID of the employee to be removed
     */
    public void deleteEmployee(Integer id){
        employeeRepository.delete(id);
    }


    /**
     * Changes an employees values by receiving new data as a parameter and
     * replacing the employee object in the database with given data.
     * @param employee employee object with same ID, new data (salary,password)
     */
    public void updateEmployee(Employee employee){
        employeeRepository.update(employee);
    }


    /**
     * Returns the type of an employee. (ex. Receptionist)
     * @param id ID of the employee
     * @return String object with name of the type ("Receptionist", "Cleaner" or "Manager")
     */
    public String getEmployeeTypeString(int id){
        for (Employee employee : employeeRepository.getAll()){
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
    public Employee getEmployeeTypeObject(int id){
        for (Employee employee : employeeRepository.getAll())
            if (employee.getId() == id)
                return employee;
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
        for (Employee employee : employeeRepository.getAll()){
            if (employee.getId() == id){
                manager = (Manager)employee;
                break;
            }
        }
        for (Department department : departmentRepository.getAll()){
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


    public List<Employee> showAllEmployees(){
        return employeeRepository.getAll();
    }
    public List<Department> showAllDepartments(){
        return departmentRepository.getAll();
    }

    /**
     * Creates a copy of the repository (list of type Employee), sorts it and then returns it.
     */
    public List<Employee> sortEmployeesBySalary(){
        List<Employee> sortedEmployeeList = new ArrayList<>(employeeRepository.getAll());

        Collections.sort(sortedEmployeeList);
        return sortedEmployeeList;
    }
    //--------------------------------------------------


    //-----------------DEPARTMENT SECTION------------------
    public void createDepartment(String name, List<Employee> employees) {
        Integer id = 0;
        List<Department> departmentList = departmentRepository.getAll();
        for (Department department : departmentList) {if (id < department.getId()) id = department.getId();}
        id += 1;

        Department newDepartment = new Department(id, name, employees);
        departmentRepository.create(newDepartment);
    }


    public void deleteDepartment(int id) {
        departmentRepository.delete(id);
    }

    public void updateDepartment(Department department) {departmentRepository.update(department);}


    /**
     * Makes a sum of each employee's salary (of given department) and divides it by the employee count (of given department).
     * @param department object of type Department
     * @return int value containing the average employee salary
     */
    public int getAvgSalaryOfADepartment(Department department) {
        int sum = 0;
        for (Employee employee : department.getEmployees()) {
            sum += employee.getSalary();
        }
        return sum / department.countEmployees();
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

        List<Department> departmentList = departmentRepository.getAll();
        return departmentList.stream().filter(avgSalaryOverGivenNr).toList();
    }


    //--------------------------------------------------


    //-----------------ROOMCUSTOMER SECTION------------------
    public void createRoomCustomer(int customerId, int roomId, String checkInDate, String checkOutDate) {
        Integer id = 0;
        List<RoomCustomer> roomCustomerList = roomCustomerRepository.getAll();
        for (RoomCustomer roomCustomer : roomCustomerList) {if (id < roomCustomer.getId()) id = roomCustomer.getId();}
        id += 1;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateFormatCheckInDate = formatter.parse(checkInDate);
            Date dateFormatCheckOutDate = formatter.parse(checkOutDate);
            RoomCustomer newRoomCustomer = new RoomCustomer(id, roomId, customerId, dateFormatCheckInDate, dateFormatCheckOutDate);
            roomCustomerRepository.create(newRoomCustomer);

        } catch (ParseException e) {
            e.printStackTrace();

        }
    }


    public List<Integer> searchRoomCustomerByCustomer(int id){
        ArrayList<Integer>idsList = new ArrayList<>();
        for (RoomCustomer roomCustomer : roomCustomerRepository.getAll()){
            if (roomCustomer.getCustomerId() == id) idsList.add( roomCustomer.getId());
        }
        return idsList;
    }


    public void deleteRoomCustomer(int id) {
        for (int roomCustomerId : searchRoomCustomerByCustomer(id))
            roomCustomerRepository.delete(roomCustomerId);}


    public void updateRoomCustomer(RoomCustomer roomCustomer) {roomCustomerRepository.update(roomCustomer);}
    //--------------------------------------------------
}
