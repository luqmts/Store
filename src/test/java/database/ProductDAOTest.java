package database;

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
import org.junit.jupiter.api.Test;

import database.DAO.ProductDAO;
import model.Product;
import model.Supplier;
import model.list.ProductList;
import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

public class ProductDAOTest {
    Connection conn;
    PreparedStatement stmt;
    ResultSet result;
    ProductDAO pDao;
    Supplier fakeSupplier;
    Product fakeProduct1, fakeProduct2;

    @BeforeEach
    void setUp() throws SQLException{
        conn = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        result = mock(ResultSet.class);

        fakeSupplier = new Supplier(
            1,
            "Sony Brasil LTDA.", 
            new CNPJ("43.447.044/0004-10"), 
            new Mail("sony@mail.com"), 
            new Phone("11000001111")
        );
        fakeProduct1 = new Product(1, "PS4", "Playstation 4", "Video Game Console by Sony.", fakeSupplier.getId());    

        pDao = new ProductDAO(conn);
    }
        
    @Test
    void testInsert() throws SQLException {
        when(conn.prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.getGeneratedKeys()).thenReturn(result);
        when(result.next()).thenReturn(true);
        when(result.getInt(1)).thenReturn(fakeProduct1.getId());

        pDao.insert(fakeProduct1);

        assertAll(
            () -> verify(conn).prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS)),
            () -> verify(stmt).setString(1, fakeProduct1.getSku()),
            () -> verify(stmt).setString(2, fakeProduct1.getName()),
            () -> verify(stmt).setString(3, fakeProduct1.getDescription()),
            () -> verify(stmt).setInt(4, fakeProduct1.getSupplierId()),
            () -> verify(stmt).executeUpdate(),
            () -> verify(stmt).getGeneratedKeys(),
            () -> verify(result).next(),
            () -> assertEquals(1, fakeProduct1.getId())
        );
    }

    @Test
    void testInsertNoIdGenerated() throws SQLException {
        when(conn.prepareStatement(any(String.class), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(stmt);
        when(stmt.getGeneratedKeys()).thenReturn(result);
        when(result.next()).thenReturn(false);
        
        assertThrows(
            SQLException.class, 
            () -> pDao.insert(fakeProduct1),
            "No id generated for inserted product"
        );
    }

    @Test
    void testUpdate() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(stmt.executeUpdate()).thenReturn(1);

        fakeProduct2 = new Product(1, "PS4", "Playstation 4", "Video Game Console by Sony.", fakeSupplier.getId());
        boolean productUpdated = pDao.update(1, fakeProduct2);

        assertAll(
            () -> verify(conn).prepareStatement(any(String.class)),
            () -> verify(stmt).setString(1, fakeProduct2.getSku()),
            () -> verify(stmt).setString(2, fakeProduct2.getName()),
            () -> verify(stmt).setString(3, fakeProduct2.getDescription()),
            () -> verify(stmt).setInt(4, fakeProduct2.getSupplierId()),
            () -> verify(stmt).setInt(5, 1),
            () -> verify(stmt).executeUpdate(),
            () -> assertTrue(productUpdated)
        );
    }

    @Test
    void testDelete() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);

        int idProduct = pDao.delete(1);
        
        assertAll(
            () -> verify(conn).prepareStatement(any(String.class)),
            () -> verify(stmt).setInt(1, fakeProduct1.getId()),
            () -> verify(stmt).executeUpdate(),
            () -> assertEquals(fakeProduct1.getId(), idProduct)
        );
    }

    @Test
    void testGet() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(result);
        when(result.next()).thenReturn(true, false);
        when(result.getInt("pid")).thenReturn(fakeProduct1.getId());
        when(result.getString("sku")).thenReturn(fakeProduct1.getSku());
        when(result.getString("name")).thenReturn(fakeProduct1.getName());
        when(result.getString("description")).thenReturn(fakeProduct1.getDescription());
        when(result.getInt("supplier_id")).thenReturn(fakeProduct1.getId());

        ProductList pList = pDao.get();

        assertAll(
            () -> verify(conn).prepareStatement(any(String.class)),
            () -> verify(stmt).executeQuery(),
            () -> verify(result, times(2)).next(),
            () -> assertEquals(1, pList.get().size())
        );
    }
    
    @Test
    void testGetById() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(result);
        when(result.next()).thenReturn(true);
        when(result.getInt("pid")).thenReturn(fakeProduct1.getId());
        when(result.getString("sku")).thenReturn(fakeProduct1.getSku());
        when(result.getString("name")).thenReturn(fakeProduct1.getName());
        when(result.getString("description")).thenReturn(fakeProduct1.getDescription());
        when(result.getInt("supplier_id")).thenReturn(fakeProduct1.getId());

        fakeProduct2 = pDao.getById(1);

        assertAll(
            () -> verify(conn).prepareStatement(any(String.class)),
            () -> verify(stmt).executeQuery(),
            () -> verify(result).next(),
            () -> assertEquals(fakeProduct1.getId(), fakeProduct2.getId()),
            () -> assertEquals(fakeProduct1.getSku(), fakeProduct2.getSku()),
            () -> assertEquals(fakeProduct1.getName(), fakeProduct2.getName()),
            () -> assertEquals(fakeProduct1.getDescription(), fakeProduct2.getDescription()),
            () -> assertEquals(fakeProduct1.getSupplierId(), fakeProduct2.getSupplierId())
        );
    }

    @Test
    void testGetByIdNoProductFound() throws SQLException {
        when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(result);
        when(result.next()).thenReturn(false);
        
        assertThrows(
            SQLException.class, 
            () -> pDao.getById(1),
            "No id generated for inserted product"
        );
    }

}

