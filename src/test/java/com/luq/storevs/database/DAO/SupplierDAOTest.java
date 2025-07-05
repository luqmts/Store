package com.luq.storevs.database.DAO;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.storevs.model.Supplier;
import com.luq.storevs.model.list.SupplierList;
import com.luq.storevs.valueobjects.Cnpj;
import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;

public class SupplierDAOTest {
    Connection conn;
    PreparedStatement stmt;
    ResultSet result;
    SupplierDAO sDao;
    Supplier fakeSupplier1, fakeSupplier2;

    @BeforeEach
    void setUp() throws SQLException{
        conn = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        result = mock(ResultSet.class);

        fakeSupplier1 = new Supplier(
            1,
            "Sony Brasil LTDA.", 
            new Cnpj("43.447.044/0004-10"), 
            new Mail("sony@mail.com"), 
            new Phone("11000001111")
        );

        sDao = new SupplierDAO(conn);
    }
        
    @Test
    @DisplayName("Test if supplier is being inserted in database")
    void testInsert() throws SQLException {
        when(conn.prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.getGeneratedKeys()).thenReturn(result);
        when(result.next()).thenReturn(true);
        when(result.getInt(1)).thenReturn(fakeSupplier1.getId());

        sDao.insert(fakeSupplier1);

        assertAll(
            () -> verify(conn).prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS)),
            () -> verify(stmt).setString(1, fakeSupplier1.getName()),
            () -> verify(stmt).setString(2, fakeSupplier1.getCnpj().toString()),
            () -> verify(stmt).setString(3, fakeSupplier1.getMail().toString()),
            () -> verify(stmt).setString(4, fakeSupplier1.getPhone().toString()),
            () -> verify(stmt).executeUpdate(),
            () -> verify(stmt).getGeneratedKeys(),
            () -> verify(result).next(),
            () -> assertEquals(1, fakeSupplier1.getId())
        );
    }

    @Test
    @DisplayName("Test if trying to insert a supplier and some SQLException occurs is throwed for user")
    void testInsertNoIdGenerated() throws SQLException {
        when(conn.prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.getGeneratedKeys()).thenReturn(result);
        when(result.next()).thenReturn(false);
        
        assertThrows(
            SQLException.class, 
            () -> sDao.insert(fakeSupplier1),
            "No id generated for inserted supplier"
        );
    }

    @Test
    @DisplayName("Test if a supplier is being updated correctly")
    void testUpdate() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);

        fakeSupplier2 = new Supplier(
            1,
            "Microsoft Brasil LTDA.", 
            new Cnpj("43.447.044/0004-10"), 
            new Mail("microsoft@mail.com"), 
            new Phone("11000001111")
        );        
        boolean supplierUpdated = sDao.update(1, fakeSupplier2);

        assertAll(
            () -> verify(conn).prepareStatement(any(String.class)),
            () -> verify(stmt).setString(1, fakeSupplier2.getName()),
            () -> verify(stmt).setString(2, fakeSupplier2.getCnpj().toString()),
            () -> verify(stmt).setString(3, fakeSupplier2.getMail().toString()),
            () -> verify(stmt).setString(4, fakeSupplier2.getPhone().toString()),
            () -> verify(stmt).executeUpdate(),
            () -> assertTrue(supplierUpdated)
        );
    }

    @Test
    @DisplayName("Test if a supplier is being deleted correctly")
    void testDelete() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);

        int idSupplier = sDao.delete(1);
        
        assertAll(
            () -> verify(conn).prepareStatement(any(String.class)),
            () -> verify(stmt).setInt(1, fakeSupplier1.getId()),
            () -> verify(stmt).executeUpdate(),
            () -> assertEquals(fakeSupplier1.getId(), idSupplier)
        );
    }

    @Test
    @DisplayName("Test if suppliers are being returned on get() method")
    void testGet() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(result);
        when(result.next()).thenReturn(true, false);
        when(result.getInt("sid")).thenReturn(fakeSupplier1.getId());
        when(result.getString("name")).thenReturn(fakeSupplier1.getName());
        when(result.getString("cnpj")).thenReturn(fakeSupplier1.getCnpj().toString());
        when(result.getString("mail")).thenReturn(fakeSupplier1.getMail().toString());
        when(result.getString("phone")).thenReturn(fakeSupplier1.getPhone().toString());

        SupplierList sList = sDao.get();

        assertAll(
            () -> verify(conn).prepareStatement(any(String.class)),
            () -> verify(stmt).executeQuery(),
            () -> verify(result, times(2)).next(),
            () -> assertEquals(1, sList.get().size())
        );
    }
    
    @Test
    @DisplayName("Test if a supplier is being returned on trying to get by id")
    void testGetById() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(result);
        when(result.next()).thenReturn(true);
        when(result.getInt("sid")).thenReturn(fakeSupplier1.getId());
        when(result.getString("name")).thenReturn(fakeSupplier1.getName());
        when(result.getString("cnpj")).thenReturn(fakeSupplier1.getCnpj().toString());
        when(result.getString("mail")).thenReturn(fakeSupplier1.getMail().toString());
        when(result.getString("phone")).thenReturn(fakeSupplier1.getPhone().toString());

        fakeSupplier2 = sDao.getById(1);

        assertAll(
            () -> verify(conn).prepareStatement(any(String.class)),
            () -> verify(stmt).executeQuery(),
            () -> verify(result).next(),
            () -> assertEquals(fakeSupplier1.getId(), fakeSupplier2.getId()),
            () -> assertEquals(fakeSupplier1.getName(), fakeSupplier2.getName()),
            () -> assertEquals(fakeSupplier1.getCnpj().toString(), fakeSupplier2.getCnpj().toString()),
            () -> assertEquals(fakeSupplier1.getMail().toString(), fakeSupplier2.getMail().toString()),
            () -> assertEquals(fakeSupplier1.getPhone().toString(), fakeSupplier2.getPhone().toString())
        );
    }

    @Test
    @DisplayName("Test if no supplier is being returned on invalid supplier's id and a exception is being throwed")
    void testGetByIdNoProductFound() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(result);
        when(result.next()).thenReturn(false);
        
        assertThrows(
            SQLException.class, 
            () -> sDao.getById(1),
            "No id generated for inserted supplier"
        );
    }

}

