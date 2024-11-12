package Hotel;

import Hotel.model.Cleaner;
import Hotel.model.Employee;
import Hotel.model.Manager;
import Hotel.model.Receptionist;
import Hotel.repository.Repository;

import java.util.Scanner;

public class HotelRegistrationSystem {

    private final Repository<Employee> employeeRepo;

    /**
     * Manages the verification of the User's credentials; if he is found in the database, his role as an employee is returned.
     * If not, N/A is returned; wrong credentials have been introduced.
     * @param employeeRepo repository containing all employees
     */
    public HotelRegistrationSystem(Repository<Employee> employeeRepo) {
        this.employeeRepo = employeeRepo;
    }


    /**
     * Returns the type of the User (ex. Receptionist).
     * His credentials must be known already and be given as a parameter.
     * <p>
     * This receives the id and password, searches after the employee data in the database
     * which has the given credentials and returns a String based off what type the employee is.
     * The String is in the format "Receptionist", "Manager" or "Cleaner".
     *
     * @param  id the ID of the User that logs in
     * @param  password the password of the User that logs in
     * @return      the type of the Employee, as a String object
     */
    public String login(int id, String password) {



        for (Employee employee: employeeRepo.getAll())
            if (employee.getId() == id && employee.getPassword().equals(password)) //user found
            {
                switch (employee) {
                    case Receptionist receptionist -> {
                        return "Receptionist";
                    }
                    case Manager manager -> {
                        return "Manager";
                    }
                    case Cleaner cleaner -> {
                        return "Cleaner";
                    }
                    default -> {
                    }
                }
                break;
            }
        return "N/A";
    }
}
