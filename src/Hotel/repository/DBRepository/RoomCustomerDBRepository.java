package Hotel.repository.DBRepository;

import Hotel.model.Cleaner;
import Hotel.model.RoomCustomer;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomCustomerDBRepository extends DBRepository<RoomCustomer> {
    public RoomCustomerDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(RoomCustomer roomCustomer) {
        String sql = "Insert INTO \"Reservation\" (\"Id\", \"RoomId\", \"CustomerId\", \"CheckIn\", \"CheckOut\") VALUES (?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomCustomer.getId());
            statement.setInt(2, roomCustomer.getRoomId());
            statement.setInt(3, roomCustomer.getCustomerId());
            statement.setDate(4, new java.sql.Date(roomCustomer.getFromDate().getTime()));
            statement.setDate(5, new java.sql.Date(roomCustomer.getUntilDate().getTime()));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public RoomCustomer get(Integer id) {
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
    public void update(RoomCustomer roomCustomer) {
        String sql = "UPDATE \"Reservation\" SET \"RoomId\"=?,\"CustomerId\"=?,\"CheckIn\"=?,\"CheckOut\"=? WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, roomCustomer.getRoomId());
            statement.setInt(2, roomCustomer.getCustomerId());
            statement.setDate(3, new java.sql.Date(roomCustomer.getFromDate().getTime()));
            statement.setDate(4, new java.sql.Date(roomCustomer.getUntilDate().getTime()));
            statement.setInt(5, roomCustomer.getId());

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
    public List<RoomCustomer> getAll() {
        String sql = "SELECT * FROM \"Reservation\"";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            List<RoomCustomer> reservations = new ArrayList<>();

            while(resultSet.next()){
                reservations.add(extractFromResultSet(resultSet));
            }

            return reservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static RoomCustomer extractFromResultSet(ResultSet resultSet) throws SQLException {
        java.sql.Date sqlCheckIn = resultSet.getDate("CheckIn");
        java.util.Date utilCheckIn = new java.util.Date(sqlCheckIn.getTime());
        java.sql.Date sqlCheckOut = resultSet.getDate("CheckOut");
        java.util.Date utilCheckOut = new java.util.Date(sqlCheckOut.getTime());
        return new RoomCustomer(
                resultSet.getInt("Id"),
                resultSet.getInt("RoomId"),
                resultSet.getInt("CustomerId"),
                utilCheckIn,
                utilCheckOut
        );
    }

}
