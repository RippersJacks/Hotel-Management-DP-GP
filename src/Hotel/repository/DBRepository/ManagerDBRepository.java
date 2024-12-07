package Hotel.repository.DBRepository;

import Hotel.model.Cleaner;
import Hotel.model.Manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagerDBRepository extends DBRepository<Manager> {
    public ManagerDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(Manager manager) {
        String sql = "Insert INTO \"Manager\" (\"Id\", \"Name\", \"Salary\", \"Password\", \"DepartmentId\", \"ManagedDepartmentId\") VALUES (?,?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, manager.getId());
            statement.setString(2, manager.getName());
            statement.setInt(3, manager.getSalary());
            statement.setString(4, manager.getPassword());
            statement.setInt(5, manager.getDepartmentId());
            statement.setInt(6, manager.getManagedDepartmentID());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Manager get(Integer id) {
        String sql = "SELECT * FROM \"Manager\" WHERE \"Id\" = ?";

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
    public void update(Manager manager) {
        String sql = "UPDATE \"Manager\" SET \"Name\"=?,\"Salary\"=?,\"Password\"=?,\"DepartmentId\"=?,\"ManagedDepartmentId\"=? WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, manager.getName());
            statement.setInt(2, manager.getSalary());
            statement.setString(3, manager.getPassword());
            statement.setInt(4, manager.getDepartmentId());
            statement.setInt(5, manager.getManagedDepartmentID());
            statement.setInt(6, manager.getId());

            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM \"Manager\" WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Manager> getAll() {
        String sql = "SELECT * FROM \"Manager\"";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            List<Manager> managers = new ArrayList<>();

            while(resultSet.next()){
                managers.add(extractFromResultSet(resultSet));
            }

            return managers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Manager extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Manager(
                resultSet.getInt("Id"),
                resultSet.getString("Name"),
                resultSet.getInt("Salary"),
                resultSet.getString("Password"),
                resultSet.getInt("DepartmentId"),
                resultSet.getInt("ManagedDepartmentId")
        );
    }
}
