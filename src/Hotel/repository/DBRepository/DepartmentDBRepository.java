package Hotel.repository.DBRepository;

import Hotel.model.Department;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDBRepository extends DBRepository<Department> {
    public DepartmentDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(Department department) {
        String sql = "Insert INTO \"Department\" (\"Id\", \"Name\") VALUES (?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, department.getId());
            statement.setString(2, department.getName());
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Department get(Integer id) {
        String sql = "Select * from \"Department\" where \"Id\" = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractFromResultSet(resultSet);
            } else return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Department department) {
        String sql = "Update \"Department\" set \"Name\" = ? where \"Id\" = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, department.getName());
            statement.setInt(2, department.getId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "Delete from \"Department\" where \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Department> getAll(){
        String sql = "SELECT * FROM \"Department\"";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            List<Department> Departments = new ArrayList<>();

            while(resultSet.next()){
                Departments.add(extractFromResultSet(resultSet));
            }

            return Departments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Department extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Department(resultSet.getInt("Id"), resultSet.getString("Name"));
    }

}
