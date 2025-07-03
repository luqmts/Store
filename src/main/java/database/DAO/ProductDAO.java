package database.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Product;
import model.list.ProductList;

public class ProductDAO implements DAO<Product, ProductList> {
    private final Connection conn;

    public ProductDAO(Connection conn) { this.conn = conn; }

    public int insert(Product product) throws SQLException {
        String sql = """
            INSERT INTO products 
            (sku, name, description, supplier_id) 
            VALUES (?, ?, ?, ?);""";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, product.getSupplier_id());

            stmt.executeUpdate();

            try(ResultSet result = stmt.getGeneratedKeys()) {
                if(result.next()) {
                    int idProduct = result.getInt(1);
                    product.setId(idProduct);
                    return idProduct;
                } else {
                    throw new SQLException("No id generated for inserted product");
                }
            }
        } catch (SQLException e){
            throw new SQLException("Error inserting items on products database: " + e);
        }
    }

    public boolean update(int pId, Product product) throws SQLException{
        String sql = """
            UPDATE products 
            SET sku=?, name=?, description=?, supplier_id=? 
            WHERE pid=?;""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, product.getSupplier_id());
            stmt.setInt(5, pId);

            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Error updating items on products database: " + e);
        }
    }

    public int delete(int pId) throws SQLException{
        String sql = """
            DELETE FROM products 
            WHERE pid=?;""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pId);
            
            stmt.executeUpdate();

            return pId;
        } catch(SQLException e) {
            throw new SQLException("Error deleting items on products database: " + e);
        }
    }

    public ProductList get() throws SQLException{
        String sql = """
            SELECT * 
            FROM products
            ORDER BY pid """;

        ProductList pList = new ProductList();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                int pid = result.getInt("pid");
                String pSku = result.getString("sku").trim();
                String pName = result.getString("name").trim();
                String pDescrition = result.getString("description").trim();
                int sId = result.getInt("supplier_id");

                Product product = new Product(pid, pSku, pName, pDescrition, sId);
                pList.add(product);
            }

            return pList;
        } catch (SQLException e){
            throw new SQLException("Error geting items from suppliers database: " + e);
        }
    }

    public Product getById(int pId) throws SQLException{
            String sql = """
                SELECT * 
                FROM products
                WHERE pid=?;""";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, pId);
                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    String pSku = result.getString("sku").trim();
                    String pName = result.getString("name").trim();
                    String pDescription = result.getString("description").trim();
                    int sId = result.getInt("supplier_id");

                    return new Product(pId, pSku, pName, pDescription, sId);
                } else {
                    throw new SQLException("No product found with inserted id");
                }
            } catch (SQLException e) {
                throw new SQLException("Error getting product by id: " + e);
            }
        }
}
