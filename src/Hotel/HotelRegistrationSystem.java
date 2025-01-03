package Hotel;

import Hotel.controller.HotelController;
import Hotel.model.*;
import Hotel.repository.Repository;

import java.util.Scanner;

public class HotelRegistrationSystem {

    private final HotelController controller;

    /**
     * Manages the verification of the User's credentials; if he is found in the database, his role as an employee is returned.
     * If not, N/A is returned; wrong credentials have been introduced.
     * @param controller repository containing all employees
     */
    public HotelRegistrationSystem(HotelController controller) {
        this.controller = controller;
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
        for (Employee employee: controller.getAllEmployees())
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

    public Customer clientLogin(String email, String password) {
        for (Customer customer: controller.getAllCustomers()){
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)){
                return customer;
            }
        }
        return null;
    }

}
