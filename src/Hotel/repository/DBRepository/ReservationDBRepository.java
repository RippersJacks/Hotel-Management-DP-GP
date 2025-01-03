package Hotel.repository.DBRepository;

import Hotel.model.Reservation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDBRepository extends DBRepository<Reservation> {
    public ReservationDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(Reservation reservation) {
        String sql = "Insert INTO \"Reservation\" (\"Id\", \"RoomId\", \"CustomerId\", \"CheckIn\", \"CheckOut\") VALUES (?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservation.getId());
            statement.setInt(2, reservation.getRoomId());
            statement.setInt(3, reservation.getCustomerId());
            statement.setDate(4, new java.sql.Date(reservation.getCheckIn().getTime()));
            statement.setDate(5, new java.sql.Date(reservation.getCheckOut().getTime()));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Reservation get(Integer id) {
        String sql = "SELECT * FROM \"Reservation\" WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractFromResultSet(resultSet);
            } else return null;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Reservation reservation) {
        String sql = "UPDATE \"Reservation\" SET \"RoomId\"=?,\"CustomerId\"=?,\"CheckIn\"=?,\"CheckOut\"=? WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, reservation.getRoomId());
            statement.setInt(2, reservation.getCustomerId());
            statement.setDate(3, new java.sql.Date(reservation.getCheckIn().getTime()));
            statement.setDate(4, new java.sql.Date(reservation.getCheckOut().getTime()));
            statement.setInt(5, reservation.getId());

            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM \"Reservation\" WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reservation> getAll() {
        String sql = "SELECT * FROM \"Reservation\"";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            List<Reservation> reservations = new ArrayList<>();

            while(resultSet.next()){
                reservations.add(extractFromResultSet(resultSet));
            }

            return reservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static Reservation extractFromResultSet(ResultSet resultSet) throws SQLException {
        java.sql.Date sqlCheckIn = resultSet.getDate("CheckIn");
        java.util.Date utilCheckIn = new java.util.Date(sqlCheckIn.getTime());
        java.sql.Date sqlCheckOut = resultSet.getDate("CheckOut");
        java.util.Date utilCheckOut = new java.util.Date(sqlCheckOut.getTime());
        return new Reservation(
                resultSet.getInt("Id"),
                resultSet.getInt("RoomId"),
                resultSet.getInt("CustomerId"),
                utilCheckIn,
                utilCheckOut
        );
    }

}
