package Hotel.PresentationLayer;

import Hotel.model.Cleaner;
import Hotel.model.Employee;
import Hotel.model.Manager;
import Hotel.model.Receptionist;

public class RegistrationSystem {
    private int id;
    private String password;

    public RegistrationSystem(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Login is written from the UI/controller
    public void login(int id,String password) {
        EmployeeRepo employeeRepo = new EmployeeRepo();
        String role = ""; //wird die Rolle des Employees beinhalten

        for (Employee employee: employeeRepo.getEmployees)
            if (employee.getId() == id && employee.getPassword().equals(password)) //user found
            {
                //wir wollen wissen, was der User ist
                switch (employee) {
                    case Receptionist receptionist -> role = "Receptionist";
                    case Manager manager -> role = "Manager";
                    case Cleaner cleaner -> role = "Cleaner";
                    default -> { role = "N/A";
                    }
                }
                break;
            }

        //the role determines which controller will be called
        if (role.equals("Receptionist"))
            return;   ///TO-DO: call UI/Controller of Receptionist
        else if (role.equals("Manager"))
            return;
        else if (role.equals("Cleaner"))
            return;
    }
}
