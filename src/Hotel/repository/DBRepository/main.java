package Hotel.repository.DBRepository;

public class main {
    public static void main(String[] args) {


        CustomerDBRepository customerBDRepository = new CustomerDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        RoomDBRepository roomDBRepository = new RoomDBRepository("jdbc:postgresql://localhost:5432/HotelManagement", "postgres", "User");
        CleanerDBRepository cleanerDBRepository = new CleanerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
        ReceptionistDBRepository receptionistDBRepository = new ReceptionistDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
        ManagerDBRepository managerDBRepository = new ManagerDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
        ReservationDBRepository reservationDBRepository = new ReservationDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");
        TimeDBRepository timeDBRepository = new TimeDBRepository("jdbc:postgresql://localhost/HotelManagement", "postgres", "User");

        System.out.println(timeDBRepository.getDate());

    }
}
