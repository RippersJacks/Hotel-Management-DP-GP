package Hotel.repository.DBRepository;

import Hotel.model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDBRepository extends DBRepository<Customer> {
    public CustomerDBRepository(String dbUrl, String dbUser, String dbPass) {
        super(dbUrl, dbUser, dbPass);
    }

    @Override
    public void create(Customer customer) {
        String sql = "Insert INTO \"Customer\" (\"Id\", \"Name\") VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customer.getId());
            statement.setString(2, customer.getName());
            statement.execute();
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Customer customer) {
        String sql = "Update \"Customer\" SET \"Name\" = ? WHERE \"Id\" = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, customer.getName());
            statement.setInt(2, customer.getId());
            statement.execute();
    }catch (SQLException e){
        throw new RuntimeException(e);}
    }

    @Override
    public Customer get(Integer id){
        String sql = "SELECT * FROM \"Customer\" WHERE \"Id\" = ?";

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
    public void delete(Integer id) {
        String sql = "DELETE FROM \"Customer\" WHERE \"Id\" = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.execute();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<Customer>getAll(){
        String sql = "SELECT * FROM \"Customer\"";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            List<Customer> Customers = new ArrayList<>();

            while(resultSet.next()){
                Customers.add(extractFromResultSet(resultSet));
            }

            return Customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static Customer extractFromResultSet(ResultSet resultSet) throws SQLException{
        return new Customer(resultSet.getInt("Id"), resultSet.getString("Name"));
    }

}
