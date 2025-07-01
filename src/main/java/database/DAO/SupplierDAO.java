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

public class SupplierDAO implements DAO<Supplier, SupplierList> {
    private final Connection conn;

    public SupplierDAO(Connection conn) { this.conn = conn; }

    public int insert(Supplier supplier) throws SQLException {
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
                    int idSupplier = result.getInt(1);
                    supplier.setId(idSupplier);
                    return idSupplier;
                } else {
                    throw new SQLException("No id generated for inserted supplier");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error inserting items on suppliers database" + e.getMessage());
        }
    }

    public boolean update(int sId, Supplier supplier) throws SQLException {
        String sql = """
            UPDATE suppliers 
            SET name=?, cnpj=?, mail=?, phone=? 
            WHERE sid=?;""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getCNPJ().toString());
            stmt.setString(3, supplier.getMail().toString());
            stmt.setString(4, supplier.getPhone().toString());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Error updating items on suppliers database: " + e);
        }
    }

    public int delete(int sId) throws SQLException {
        String sql = """
            DELETE FROM suppliers 
            WHERE sid=?;""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, sId);

            stmt.executeUpdate();

            return sId;
        } catch (Exception e) {
            throw new SQLException("Error deleting items on suppliers database");
        }
    }

    public SupplierList get() throws SQLException {
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
                sList.add(supplier);
            }
        } catch (SQLException e){
            throw new SQLException("Error geting items from suppliers database");
        }

        return sList;
    }

    public Supplier getById(int sId) throws SQLException {
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

                return new Supplier(sid, sName, sCNPJ, sMail, sPhone);
            } else {
                throw new SQLException("No supplier found with inserted id");
            }
        } catch (SQLException e){
            throw new SQLException("Error geting supplier with id inserted from suppliers database");
        }
    }
}