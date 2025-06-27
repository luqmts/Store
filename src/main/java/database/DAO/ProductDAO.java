package database.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Product;
import model.list.ProductList;

public class ProductDAO {
    private final Connection conn;

    public ProductDAO(Connection conn) { this.conn = conn; }

    public void insert(Product product) {
        String sql = """
            INSERT INTO products 
            (sku, name, description, supplier_id) 
            VALUES (?, ?, ?, ?);""";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, product.getSupplierId());

            stmt.executeUpdate();

            try(ResultSet result = stmt.getGeneratedKeys()) {
                if(result.next()) {
                    product.setPid(result.getInt(1));
                    System.out.println("Product succesfully added to database.");
                }
            }
        } catch (SQLException e){
            System.out.println("Error inserting items on products database: " + e);
        }
    }

    public void update(int pId, Product product) {
        String sql = """
            UPDATE products 
            SET sku=?, name=?, description=?, supplier_id=? 
            WHERE pid=?;""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getSku());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, product.getSupplierId());
            stmt.setInt(5, pId);

            stmt.executeUpdate();
            
            System.out.println("Product succesfully updated on products database.");
        } catch (SQLException e) {
            System.out.println("Error updating items on products database: " + e);
        }
    }

    public void delete(int pId) {
        String sql = """
            DELETE FROM products 
            WHERE pid=?;""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pId);
            
            stmt.executeUpdate();

            System.out.println("Product successfully deleted on products database.");
        } catch(SQLException e) {
            System.out.println("Error deleting items on products database: " + e);
        }
    }

        public ProductList get(SupplierDAO sDAO) {
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
                    pList.addProduct(product);
                }
            } catch (SQLException e){
                System.out.println("Error geting items from suppliers database");
            }

            return pList;
        }

        public Product getById(int pId) {
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
                }
            } catch (SQLException e) {
                System.out.println("Error getting product by id: " + e);
            }

            return null;
        }
}
