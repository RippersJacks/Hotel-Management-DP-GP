package Hotel.repository.DBRepository;
import Hotel.model.Receptionist;
import Hotel.model.RoomCleaner;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomCleanerDBRepository extends DBRepository<RoomCleaner>{
    public RoomCleanerDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(RoomCleaner roomCleaner) {
        String sql = "Insert INTO \"RoomCleaner\" (\"Id\", \"RoomId\", \"CleanerId\") VALUES (?,?,?)";


        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomCleaner.getId());
            statement.setInt(2, roomCleaner.getRoomId());
            statement.setInt(3, roomCleaner.getCleanerId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoomCleaner get(Integer id) {
        String sql = "SELECT * FROM \"RoomCleaner\" WHERE \"Id\" = ?";

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
    public void update(RoomCleaner roomCleaner) {
        String sql = "UPDATE \"RoomCleaner\" SET \"RoomId\"=?,\"CleanerId\"=? WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, roomCleaner.getRoomId());
            statement.setInt(2, roomCleaner.getCleanerId());
            statement.setInt(3, roomCleaner.getId());

            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM \"RoomCleaner\" WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RoomCleaner> getAll() {
        String sql = "SELECT * FROM \"RoomCleaner\"";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            List<RoomCleaner> roomCleaners = new ArrayList<>();

            while(resultSet.next()){
                roomCleaners.add(extractFromResultSet(resultSet));
            }

            return roomCleaners;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RoomCleaner extractFromResultSet(ResultSet resultSet) throws SQLException {

        return new RoomCleaner
                (resultSet.getInt("Id"),
                        resultSet.getInt("RoomId"),
                        resultSet.getInt("CleanerId"));
    }

}