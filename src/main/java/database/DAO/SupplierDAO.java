package database.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Supplier;
import model.list.SupplierList;

import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

public class SupplierDAO {
    private final Connection conn;

    public SupplierDAO(Connection conn) { this.conn = conn; }

    public void insert(Supplier supplier) {
        String sql = """
            INSERT INTO suppliers 
            (name, cnpj, mail, phone) 
            VALUES (?, ?, ?, ?);""";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getCNPJ().toString());
            stmt.setString(3, supplier.getMail().toString());
            stmt.setString(4, supplier.getPhone().toString());

            stmt.executeUpdate();

            try (ResultSet result = stmt.getGeneratedKeys()) {
                if(result.next()) {
                    supplier.setsId(result.getInt(1));
                    System.out.println("Supplier succesfully added to database.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting items on suppliers database" + e.getMessage());
        }
    }

    public void update(int sId, Supplier supplier) {
        String sql = """
            UPDATE suppliers 
            SET name=?, cnpj=?, mail=?, phone=? 
            WHERE sid=?;""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getCNPJ().toString());
            stmt.setString(3, supplier.getMail().toString());
            stmt.setString(4, supplier.getPhone().toString());

            stmt.executeUpdate();

            System.out.println("Supplier successfully uptaded on suppliers database.");
        } catch (SQLException e) {
            System.out.println("Error updating items on suppliers database: " + e);
        }
    }

    public void delete(int sId) {
        String sql = """
            DELETE FROM suppliers 
            WHERE sid=?;""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, sId);

            stmt.executeUpdate();

            System.out.println("Supplier successfully deleted on product database");
        } catch (Exception e) {
            System.out.println("Error deleting items on suppliers database");
        }
    }

    public SupplierList get() {
        String sql = """
            SELECT * FROM suppliers 
            ORDER BY sid""";
        SupplierList sList = new SupplierList();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                int sid = result.getInt("sid");
                String sName = result.getString("name").trim();
                CNPJ sCNPJ = new CNPJ(result.getString("cnpj").trim());
                Mail sMail = new Mail(result.getString("mail").trim());
                Phone sPhone = new Phone(result.getString("phone").trim());

                Supplier supplier = new Supplier(sid, sName, sCNPJ, sMail, sPhone);
                sList.addSupplier(supplier);
            }
        } catch (SQLException e){
            System.out.println("Error geting items from suppliers database");
        }

        return sList;
    }


    public Supplier getById(int sId) {
        String sql = """
            SELECT * FROM suppliers 
            WHERE sid = ?""";
        Supplier supplier = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sId);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int sid = result.getInt("sid");
                String sName = result.getString("name").trim();
                CNPJ sCNPJ = new CNPJ(result.getString("cnpj").trim());
                Mail sMail = new Mail(result.getString("mail").trim());
                Phone sPhone = new Phone(result.getString("phone").trim());

                supplier = new Supplier(sid, sName, sCNPJ, sMail, sPhone);

            }
        } catch (SQLException e){
            System.out.println("Error geting supplier with id inserted from suppliers database");
        }

        return supplier;
    }
}