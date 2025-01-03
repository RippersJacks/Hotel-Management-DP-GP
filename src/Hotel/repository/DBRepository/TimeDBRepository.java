package Hotel.repository.DBRepository;

import Hotel.model.Employee;
import Hotel.model.HasId;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TimeDBRepository extends DBRepository {
    public TimeDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(HasId obj) {

    }

    @Override
    public HasId get(Integer id) {
        return null;
    }

    public String getDate(){
        String sql = "SELECT \"TodaysDate\" FROM \"Date\" WHERE \"Id\" = 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractFromResultSet(resultSet);
            } else return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDate(){
        String sql = "UPDATE \"Date\" SET \"TodaysDate\" = ? WHERE \"Id\" = 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(LocalDate.now()));
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


        @Override
    public void update(HasId obj) {

    }

        @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Employee> getAll() {
        return List.of();
    }

    public static String extractFromResultSet(ResultSet resultSet) throws SQLException{
        return resultSet.getString("TodaysDate");
    }

}

