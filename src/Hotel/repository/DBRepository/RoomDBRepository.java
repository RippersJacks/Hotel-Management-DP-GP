package Hotel.repository.DBRepository;

import Hotel.model.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDBRepository extends DBRepository<Room> {

    public RoomDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);

    }
    @Override
    public void create(Room room){
        String sql = "Insert INTO \"Room\" (\"Id\", \"Floor\", \"Number\", \"Type\", \"PricePerNight\", \"Availability\") VALUES (?,?,?,?,?,?)";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,room.getId());
            statement.setInt(2,room.getFloor());
            statement.setInt(3,room.getNumber());
            statement.setString(4,room.getType());
            statement.setInt(5,room.getPricePerNight());
            statement.setString(6,room.getAvailability());
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Room get(Integer id){
        String sql = "SELECT * FROM \"Room\" WHERE \"Id\" = ?";

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
    public void update(Room room){
        String sql = "UPDATE \"Room\" SET \"Floor\"=?,\"Number\"=?,\"Type\"=?,\"PricePerNight\"=?,\"Availability\"=? WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, room.getFloor());
            statement.setInt(2, room.getNumber());
            statement.setString(3, room.getType());
            statement.setInt(4, room.getPricePerNight());
            statement.setString(5, room.getAvailability());
            statement.setInt(6, room.getId());

            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }



    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM \"Room\" WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.execute();
    }catch (SQLException e){
        throw new RuntimeException(e);
        }
    }

    @Override
    public List<Room> getAll(){
        String sql = "SELECT * FROM \"Room\"";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            List<Room> rooms = new ArrayList<>();

            while(resultSet.next()){
                rooms.add(extractFromResultSet(resultSet));
            }

            return rooms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Room extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Room(
                resultSet.getInt("Id"),
                resultSet.getInt("Floor"),
                resultSet.getInt("Number"),
                resultSet.getString("Type"),
                resultSet.getInt("PricePerNight"),
                resultSet.getString("Availability")
        );
    }

}
