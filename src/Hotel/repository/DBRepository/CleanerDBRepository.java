package Hotel.repository.DBRepository;

import Hotel.model.Cleaner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CleanerDBRepository extends DBRepository<Cleaner> {
    public CleanerDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(Cleaner cleaner) {
        String sql = "Insert INTO \"Cleaner\" (\"Id\", \"Name\", \"Salary\", \"Password\", \"DepartmentId\", \"Floor\") VALUES (?,?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cleaner.getId());
            statement.setString(2, cleaner.getName());
            statement.setInt(3, cleaner.getSalary());
            statement.setString(4, cleaner.getPassword());
            statement.setInt(5, cleaner.getDepartmentId());
            statement.setInt(6, cleaner.getFloor());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Cleaner get(Integer id) {
        String sql = "SELECT * FROM \"Cleaner\" WHERE \"Id\" = ?";

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
    public void update(Cleaner cleaner) {
        String sql = "UPDATE \"Cleaner\" SET \"Name\"=?,\"Salary\"=?,\"Password\"=?,\"DepartmentId\"=?,\"Floor\"=? WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, cleaner.getName());
            statement.setInt(2, cleaner.getSalary());
            statement.setString(3, cleaner.getPassword());
            statement.setInt(4, cleaner.getDepartmentId());
            statement.setInt(5, cleaner.getFloor());
            statement.setInt(6, cleaner.getId());

            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM \"Cleaner\" WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public  List<Cleaner> getAll() {
        String sql = "SELECT * FROM \"Cleaner\"";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            List<Cleaner> cleaners = new ArrayList<>();

            while(resultSet.next()){
                cleaners.add(extractFromResultSet(resultSet));
            }

            return cleaners;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static Cleaner extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Cleaner(
                resultSet.getInt("Id"),
                resultSet.getString("Name"),
                resultSet.getInt("Salary"),
                resultSet.getString("Password"),
                resultSet.getInt("DepartmentId"),
                resultSet.getInt("Floor")
        );
    }

}
