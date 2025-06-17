package database.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Product;

public class ProductDAO {
    private final Connection conn;

    public ProductDAO(Connection conn) { this.conn = conn;}

    public void insert(Product product){
        String sql = "INSERT INTO products (sku, name, description) VALUES (?, ?, ?);";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());

            stmt.executeUpdate();

            try(ResultSet result = stmt.getGeneratedKeys()) {
                if(result.next()) product.setPid(result.getInt(1));
                System.out.println("Product succesfully added to database.");
            }
        } catch (SQLException e){
            System.out.println("Error inserting items on products database: " + e);
        }
    }

    public void update(int pId, Product product){
        String sql = "UPDATE products SET sku=?, name=?, description=? where pid=?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, pId);

            stmt.executeUpdate();
            
            System.out.println("Product succesfully updated on products database.");
        } catch (SQLException e) {
            System.out.println("Error updating items on products database: " + e);
        }
    }

    public void delete(int pId){
        String sql = "DELETE FROM products WHERE pid=?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pId);
            
            stmt.executeUpdate();

            System.out.println("Product successfully deleted on products database.");
        } catch(SQLException e) {
            System.out.println("Error deleting items on products database: " + e);
        }
    }
}
