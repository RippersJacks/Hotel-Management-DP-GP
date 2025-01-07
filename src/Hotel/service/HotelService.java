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
    private final Repository<Receptionist> receptionistRepository;
    private final Repository<Reservation> reservationRepository;
    private final Repository<Manager> managerRepository;
    private final Repository<Department> departmentRepository;
    private final Repository<Customer> customerRepository;
    private final Repository<Cleaner> cleanerRepository;
    private final Repository<Room> roomRepository;
    private final Repository<RoomCleaner> roomCleanerRepository;
    private final TimeDBRepository timeDBRepository;

    /**
     * Contains the functionality of the project and works with the repositories.
     * @param roomRepository repository with objects of type Room
     * @param cleanerRepository repository with objects of type Employee
     * @param customerRepository repository with objects of type Customer
     * @param departmentRepository repository with objects of type Department
     * @param reservationRepository repository with objects of type RoomCustomer (crossing table for Room & Customer)
     */
    public HotelService(Repository<Receptionist> receptionistRepository, Repository<Reservation> reservationRepository, Repository<Manager> managerRepository, Repository<Department> departmentRepository, Repository<Customer> customerRepository, Repository<Cleaner> cleanerRepository, Repository<Room> roomRepository, Repository<RoomCleaner> roomCleanerRepository, TimeDBRepository timeDBRepository) {
        //this.roomRepository = roomRepository;
        //this.employeeRepository = employeeRepository;
        //this.customerRepository = customerRepository;
        //this.departmentRepository = departmentRepository;
        //this.roomCustomerRepository = roomCustomerRepository;
        this.receptionistRepository = receptionistRepository;
        this.reservationRepository = reservationRepository;
        this.managerRepository = managerRepository;
        this.departmentRepository = departmentRepository;
        this.customerRepository = customerRepository;
        this.cleanerRepository = cleanerRepository;
        this.roomRepository = roomRepository;
        this.roomCleanerRepository = roomCleanerRepository;
        this.timeDBRepository = timeDBRepository;
    }




    //---------------CLEANER SECTION---------------

    /**
     * Returns a list of all rooms that are of availability Dirty in the database.
     * @return list with objects of type Room, with availability Dirty
     */
    public List<Room> checkDirtyRooms(){
        try{
            List<Room> roomList = roomRepository.getAll();
            if (roomList.isEmpty()){throw new NullPointerException("No rooms exist");}
            List<Room> dirtyRoomList = new ArrayList<>();

            Cleaner cleanerFunction = new Cleaner(0,"",0,"",0,0); //just for the use of the checkRoom function which is located in the Cleaner class

            for (Room room : roomList) {
                if (cleanerFunction.checkRoom(room))
                    dirtyRoomList.add(room);
            }
            return dirtyRoomList;
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
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
    public boolean cleanRoom(int cleanerId, Integer roomID){
        List<Room> roomList = roomRepository.getAll();

        int ok = 0;
        for (RoomCleaner roomCleaner: roomCleanerRepository.getAll())
            if (roomCleaner.getId() == cleanerId)
                if (roomCleaner.getRoomId() == roomID)
                    ok = 1;
        if (ok == 0)
            throw new RuntimeException("You are not assigned to this floor as a cleaner");

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
                    throw new IllegalArgumentException("Room is currently occupied, cannot clean");
            }
        }
        throw new IllegalArgumentException("Room does not exist");
    }
    //--------------------------------------------------


    //---------------ROOM CLEANER SECTION--------------

    public List<RoomCleaner> getAllRoomCleaners(){
        if (roomCleanerRepository.getAll().isEmpty())
            throw new RuntimeException("No RoomCleaner relations exist");
        return roomCleanerRepository.getAll();
    }

    public void createRoomCleaner(int cleanerId, int floor){
        for (Room room: getAllRooms())
            if (room.getFloor() == floor)
            {
                int id = 0;
                if (!roomCleanerRepository.getAll().isEmpty())
                    for (RoomCleaner roomCleaner: getAllRoomCleaners())
                    {
                        if (id < roomCleaner.getId())
                            id = roomCleaner.getId();
                    }
                id += 1;

                roomCleanerRepository.create(new RoomCleaner(id,room.getId(),cleanerId));
            }
    }

    //-------------------------------------------------


    //---------------RECEPTIONIST SECTION---------------

    /**
     * Adds a new customer to the Customer section of the database.
     * His ID is determined automatically.
     * It also passes parameters to he createRoomCustomer function to create simultaneosly a roomCustomer*
     * @param name name of the customer to be created
     * @param email customers email adress
     * @param password customers password
     */
    public void createClient(String name, String email, String password) {
        //id of customers autoincrements (searches for maximum id and then +1)
        Integer id = 0;
        List<Customer> customerList = customerRepository.getAll();
        for (Customer customer : customerList) {if (id < customer.getId()) id = customer.getId();}
        id += 1;

        Customer client = new Customer(id, email, password, name);
        customerRepository.create(client);
    }

    /**
     * Deletes a customer from the database.
     * @param clientID id of the customer to be deleted
     */
    public void deleteClient(Integer clientID) {

        //Search for the room in which the customer stayed in order to change its availability; used crossing table for this
        List<Reservation> reservationList = reservationRepository.getAll();

        for (Reservation reservation : reservationList) {
            if (clientID.equals(reservation.getCustomerId()))
            {
                roomRepository.delete(reservation.getId());
            }
        }

        customerRepository.delete(clientID);
    }

    /**
     * Updates a customer in the customer database.
     * @param client object of type Customer, a.k.a. the customer to be updated
     */
    public void updateClient(Customer client) {
        customerRepository.update(client);
    }

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
        Receptionist receptionistFunction = new Receptionist(0,"",0,"",0, null);
        //lambda function inheriting the checkRoom functionality of the Receptionist Class
        Predicate<Room> checkIfRoomIsAvailable = receptionistFunction::checkRoom;

        return roomRepository.getAll().stream().filter(checkIfRoomIsAvailable).toList();
    }

    /**
     * Sorts a copy of the roomCustomerRepository filtering by the end of each customers stay.
     * @return the sorted list of RoomCustomer objects
     */
    public List<Reservation> sortRoomCustomerByUntilDate(){
        List<Reservation> reservationList = new ArrayList<>(reservationRepository.getAll());
        if (reservationList.isEmpty())
            throw new IllegalArgumentException("No rooms exist, nothing to sort");
        Collections.sort(reservationList);
        return reservationList;
    }


    /**
     * Returns a list containing all rooms reserved by a specific customer.
     * @param customerID the ID of the searched customer
     * @return list of rooms
     */
    public List<Room> getAllRoomsOfACustomer(int customerID){

        List<Room> roomList = new ArrayList<>();

        for (Reservation reservation : reservationRepository.getAll()) {
            if (reservation.getCustomerId() == customerID)
                roomList.add(roomRepository.get(reservation.getRoomId()));
        }

        return roomList;
    }

    //--------------------------------------------------


    //-----------------MANAGER SECTION------------------

    public List<Employee>getAllEmployees(){
        List<Employee> employeeList = new ArrayList<>();
        List<Cleaner> cleanerList = cleanerRepository.getAll();
        List<Manager> managerList = managerRepository.getAll();
        List<Receptionist> receptionistList = receptionistRepository.getAll();
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
    public void createEmployee(String type, String name, int salary, String password){
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

            Cleaner employee = new Cleaner(id, name, salary, password, 2, floor);
            createRoomCleaner(id,floor);

            cleanerRepository.create(employee);
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
            Receptionist employee = new Receptionist(id, name, salary, password, 1, languages);
            receptionistRepository.create(employee);
        }
        else if (type.equalsIgnoreCase("Manager")){
            System.out.println("Enter department id: ");
            int managerdepartmentId = sc.nextInt();
            sc.nextLine();

            Manager employee = new Manager(id, name, salary, password, 3,managerdepartmentId);
            managerRepository.create(employee);
        }
    }

    /**
     * Removes an employee from the database.
     * @param id ID of the employee to be removed
     */
    public void deleteEmployee(Integer id){
        managerRepository.delete(id);
        receptionistRepository.delete(id);
        cleanerRepository.delete(id);
    }


    /**
     * Changes an employees values by receiving new data as a parameter and
     * replacing the employee object in the database with given data.
     * @param employee employee object with same ID, new data (salary,password)
     */
    public void updateEmployee(Employee employee){
        managerRepository.update((Manager) employee);
        receptionistRepository.update((Receptionist) employee);
        cleanerRepository.update((Cleaner) employee);

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


    //public List<Employee> showAllEmployees(){
        //return employeeDBRepository.getAll();
    //}
    public List<Department> showAllDepartments(){
        return departmentRepository.getAll();
    }

    /**
     * Creates a copy of the repository (list of type Employee), sorts it and then returns it.
     */
    public List<Employee> sortEmployeesBySalary(){
        List<Employee> sortedEmployeeList = new ArrayList<>(getAllEmployees());
        if (sortedEmployeeList.isEmpty())
            throw new IllegalArgumentException("List of employees is empty, cannot sort");

        Collections.sort(sortedEmployeeList);
        return sortedEmployeeList;
    }
    //--------------------------------------------------


    //-----------------DEPARTMENT SECTION------------------
    public void createDepartment(String name) {
        Integer id = 0;
        List<Department> departmentList = departmentRepository.getAll();
        for (Department department : departmentList) {if (id < department.getId()) id = department.getId();}
        id += 1;

        Department newDepartment = new Department(id, name);
        departmentRepository.create(newDepartment);
    }


    public void deleteDepartment(int id) {
        departmentRepository.delete(id);
    }

    public void updateDepartment(Department department) {
        departmentRepository.update(department);}

    public List<Department> getAllDepartments(){
        return departmentRepository.getAll();
    }

    public List<? extends Employee> getDepartmentEmployees(int id) {
        if (id == managerRepository.getAll().getFirst().getId()){
            return managerRepository.getAll();
        }else if (id == cleanerRepository.getAll().getFirst().getId()){
            return cleanerRepository.getAll();
        }else return receptionistRepository.getAll();
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

        List<Department> departmentList = departmentRepository.getAll();
        return departmentList.stream().filter(avgSalaryOverGivenNr).toList();
    }


    //--------------------------------------------------


    //-----------------RESERVATION SECTION------------------
    public void createReservation(int customerId, int roomId, String checkInDate, String checkOutDate) {
        Integer id = 0;
        List<Reservation> reservationList = reservationRepository.getAll();
        for (Reservation reservation : reservationList) {if (id < reservation.getId()) id = reservation.getId();}
        id += 1;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateFormatCheckInDate = formatter.parse(checkInDate);
            Date dateFormatCheckOutDate = formatter.parse(checkOutDate);
            Reservation newReservation = new Reservation(id, roomId, customerId, dateFormatCheckInDate, dateFormatCheckOutDate);
            reservationRepository.create(newReservation);

        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAll();
    }

    public List<Reservation> searchReservationByCustomer(int id){
        ArrayList<Reservation>reservationsList = new ArrayList<>();
        for (Reservation reservation : reservationRepository.getAll()){
            if (reservation.getCustomerId() == id) reservationsList.add(reservation);
        }
        return reservationsList;
    }


    public void deleteSpecificReservation(int customerid, int roomId, String checkInDate, String checkOutDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Reservation reservation : searchReservationByCustomer(customerid))
            if (reservation.getRoomId() == roomId && reservation.getCheckIn().equals(formatter.parse(checkInDate)) && reservation.getCheckOut().equals(formatter.parse(checkOutDate))) {
                reservationRepository.delete(reservation.getId());

            }
    }


    public void deleteReservation(int reservationId) {
        reservationRepository.delete(reservationId);
    }

    public void updateReservation(Reservation reservation) {
        reservationRepository.update(reservation);}


    public List<Reservation> searchReservationsByRoom(int id){
        ArrayList<Reservation>reservationsList = new ArrayList<>();
        for (Reservation reservation : reservationRepository.getAll()){
            if (reservation.getRoomId() == id) reservationsList.add(reservation);
        }
        return reservationsList;
    }


    public boolean checkIfDatesIntersect(Date checkInDate1, Date checkOutDate1, Date checkInDate2, Date checkOutDate2) {
        if ((checkInDate2.after(checkInDate1) && checkInDate2.before(checkOutDate1)) || checkInDate2.equals(checkOutDate1) || checkInDate2.equals(checkInDate1)) {
            return true;
        }
        else return (checkOutDate2.after(checkInDate1) && checkOutDate2.before(checkOutDate1)) || checkOutDate2.equals(checkOutDate1) || checkOutDate2.equals(checkInDate1);
    }

    //--------------------------------------------------



    //------ROOM MANAGEMENT------
    //--> only used for tests

    public void createRoom(int id, int floor, int number, String type, int pricePerNight, String availability){
        roomRepository.create(new Room(id, floor, number, type, pricePerNight, availability));
    }

    public void deleteRoom(int id) {
        roomRepository.delete(id);
    }

    public void updateRoom(int id, int floor, int number, String type, int pricePerNight, String availability) {
        roomRepository.update(new Room(id, floor, number, type, pricePerNight, availability));
    }

    public Room getRoom(int id){
        return roomRepository.get(id);
    }

    public List<Room> getAllRooms(){
        return roomRepository.getAll();
    }

    //--------Time Section-------
    public void updateDate(){
        timeDBRepository.updateDate();
    }


    public String getDate(){
        return timeDBRepository.getDate();
    }

    //---------------------------
}


