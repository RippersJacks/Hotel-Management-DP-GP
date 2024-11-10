package Hotel.PresentationLayer;

import Hotel.HotelController;
import java.util.Scanner;

public class CleanerUI {

    void run(){
        System.out.println("Logged in as: Cleaner");
        System.out.println("Options:\n" +
                "  1. Check which rooms are dirty\n" +
                "  2. Clean a room\n" +
                "   --> ");
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();

        HotelController controller = new HotelController();

        if (option == 1){
            controller.checkDirtyRoomsValidate();
        }
        else if (option == 2){
            System.out.println("Enter room id: ");
            option = sc.nextInt();
            controller.cleanRoomValidate(option);
        }
    }

}
