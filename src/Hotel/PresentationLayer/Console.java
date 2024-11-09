package Hotel.PresentationLayer;

import java.util.Scanner;

public class Console {
    public void run() throws InterruptedException {
        System.out.println("Booting up system...");
        wait(1);

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your id: ");
        int id = sc.nextInt();
        System.out.println("Please enter your password: ");
        String password = sc.nextLine();

        RegistrationSystem system = new RegistrationSystem(id, password);
        system.login(id,password);
    }
}
