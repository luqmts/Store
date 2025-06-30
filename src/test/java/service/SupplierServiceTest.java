package service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import database.DAO.DAO;
import model.Supplier;
import model.list.SupplierList;
import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

public class SupplierServiceTest {
    DAO<Supplier, SupplierList> sDao;
    SupplierService sService;
    Supplier fakeSupplier;

    @BeforeEach
    public void setUp(){
        sDao = mock(DAO.class);
        sService = new SupplierService(sDao);

        fakeSupplier = new Supplier(
            1,
            "Microsoft Brasil LTDA.", 
            new CNPJ("43.447.044/0004-10"), 
            new Mail("microsoft@mail.com"), 
            new Phone("11000001111")
        );
    }
    
    @Test
    @DisplayName("Test if Supplier is being registerd correctly")
    public void testRegisterSupplier(){
        Supplier result = sService.registerSupplier("Microsoft Brasil LTDA.", "43.447.044/0004-10", "microsoft@mail.com", "11000001111");

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(Supplier.class, result),
            () -> assertEquals("Microsoft Brasil LTDA.", result.getName()),
            () -> assertEquals("43.447.044/0004-10", result.getCNPJ().toString()),
            () -> assertEquals("microsoft@mail.com", result.getMail().toString()),
            () -> assertEquals("11000001111", result.getPhone().toString())
        );
    }

    @Test
    @DisplayName("Test if Supplier is being updated correctly")
    public void testUpdateSupplier(){
        when(sDao.getById(1)).thenReturn(fakeSupplier);

        Supplier result = sService.updateSupplier(1, "Sony Brasil LTDA.", "04.542.534/0001-09", "sony@mail.com", "11222225555");
        
        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(Supplier.class, result),
            () -> assertEquals("Sony Brasil LTDA.", result.getName()),
            () -> assertEquals("04.542.534/0001-09", result.getCNPJ().toString()),
            () -> assertEquals("sony@mail.com", result.getMail().toString()),
            () -> assertEquals("11222225555", result.getPhone().toString())
        );
    }

    @Test
    @DisplayName("Test if Supplier is not being updated and throwing a exception for invalid supplier's id")
    public void testUpdateSupplierInvalidSupplier(){
        assertThrows(
            IllegalArgumentException.class, 
            () -> sService.updateSupplier(1, "Sony Brasil LTDA.", "04.542.534/0001-09", "sony@mail.com", "11222225555"),
            "Supplier not found"
        );
    }

    @Test
    @DisplayName("Test if Supplier is being deleted correctly")
    public void testDeleteSupplier(){
        when(sDao.getById(1)).thenReturn(fakeSupplier);
        int id = sService.deleteSupplier(1);

        assertAll(
            () -> assertNotNull(id),
            () -> assertEquals(1, id)
        );
    }

    @Test
    @DisplayName("Test if Supplier is not being deleting and throwing a exception for invalid supplier's id")
    public void testDeleteSupplierInvalidSupplier(){
        assertThrows(
            IllegalArgumentException.class, 
            () -> sService.deleteSupplier(1),
            "Supplier not found"
        );
    }

    @Test
    @DisplayName("Test if all Suppliers added to SupplierList are being returned on method showAllSuppliers()")
    public void testShowAllSuppliers(){
        SupplierList sList = new SupplierList();
        Supplier fakeSupplier2 = new Supplier(2, "Sony Brasil LTDA.", new CNPJ("04.542.534/0001-09"), new Mail("sony@mail.com"), new Phone("11222225555"));
        sList.add(fakeSupplier);
        sList.add(fakeSupplier2);

        when(sDao.get()).thenReturn(sList);
        String result = sService.showAllSuppliers();

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(fakeSupplier + "\n" +fakeSupplier2, result) 
        );
    }

    @Test
    @DisplayName("Test if a exception is being throwed when try to get a empty list on method showAllSuppliers()")
    public void testShowAllSuppliersNoItem(){
        SupplierList sList = new SupplierList();

        when(sDao.get()).thenReturn(sList);

        assertThrows(
            NoSuchElementException.class, 
            () -> sService.showAllSuppliers(),
            "No items registered"
        );
    }
}
