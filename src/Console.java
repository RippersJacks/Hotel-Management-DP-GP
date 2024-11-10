import Hotel.PresentationLayer.RegistrationSystem;

import java.util.Scanner;

public class Console {
    public void run(){

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your id: ");
        int id = sc.nextInt();
        System.out.println("Please enter your password: ");
        String password = sc.nextLine();

        RegistrationSystem system = new RegistrationSystem(id, password);
        system.login(id,password);
    }


    public static void main(String[] args) throws InterruptedException {
        Console console = new Console();
        console.run();
    }
}
