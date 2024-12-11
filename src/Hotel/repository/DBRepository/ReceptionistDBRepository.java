package Hotel.repository.DBRepository;
import Hotel.model.Receptionist;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReceptionistDBRepository extends DBRepository<Receptionist>{
    public ReceptionistDBRepository(String dbUrl, String dbUser, String dbPassword) {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(Receptionist receptionist) {
        String sql = "Insert INTO \"Receptionist\" (\"Id\", \"Name\", \"Salary\", \"Password\", \"DepartmentId\", \"Languages\") VALUES (?,?,?,?,?,?)";


        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, receptionist.getId());
            statement.setString(2, receptionist.getName());
            statement.setInt(3, receptionist.getSalary());
            statement.setString(4, receptionist.getPassword());
            statement.setInt(5, receptionist.getDepartmentId());
            statement.setArray(6, connection.createArrayOf("TEXT", receptionist.getLanguages().toArray()));

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Receptionist get(Integer id) {
        String sql = "SELECT * FROM \"Receptionist\" WHERE \"Id\" = ?";

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
    public void update(Receptionist receptionist) {
        String sql = "UPDATE \"Receptionist\" SET \"Name\"=?,\"Salary\"=?,\"Password\"=?,\"DepartmentId\"=?,\"Languages\"=? WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, receptionist.getName());
            statement.setInt(2, receptionist.getSalary());
            statement.setString(3, receptionist.getPassword());
            statement.setInt(4, receptionist.getDepartmentId());
            statement.setArray(5, connection.createArrayOf("TEXT", receptionist.getLanguages().toArray()));
            statement.setInt(6, receptionist.getId());

            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM \"Receptionist\" WHERE \"Id\" = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Receptionist> getAll() {
        String sql = "SELECT * FROM \"Receptionist\"";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            List<Receptionist> receptionists = new ArrayList<>();

            while(resultSet.next()){
                receptionists.add(extractFromResultSet(resultSet));
            }

            return receptionists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Receptionist extractFromResultSet(ResultSet resultSet) throws SQLException {
        String[] javaArray = (String[]) resultSet.getArray("Languages").getArray();
        List<String> languages = Arrays.asList(javaArray);

        return new Receptionist(
                resultSet.getInt("Id"),
                resultSet.getString("Name"),
                resultSet.getInt("Salary"),
                resultSet.getString("Password"),
                resultSet.getInt("DepartmentId"),
                languages

        );
    }

}