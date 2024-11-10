package Hotel;

import Hotel.model.Cleaner;
import Hotel.model.Employee;
import Hotel.model.Manager;
import Hotel.model.Receptionist;
import Hotel.repository.Repository;

import java.util.Scanner;

public class HotelRegistrationSystem {

    private final Repository<Employee> employeeRepo;

    public HotelRegistrationSystem(Repository<Employee> employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public String login() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your id: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.println("Please enter your password: ");
        String password = sc.nextLine();

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
