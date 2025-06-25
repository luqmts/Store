package model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

public class SupplierListTest {
    Supplier s1, s2, s3, s4;
    SupplierList sList;

    @BeforeEach
    public void setUp(){
        s1 = new Supplier(
            1, "Sony Brasil LTDA.", new CNPJ("43.447.044/0004-10"), new Mail("sony@mail.com"), new Phone("11000001111")
        );
        s2 = new Supplier(
            2, "Microsoft Brasil LTDA.", new CNPJ("33.652.161/0001-19"), new Mail("microsoft@mail.com"), new Phone("21985855858")
        );
        s3 = new Supplier(
            3, "8BitDO LTDA.", new CNPJ("37.835.617/0001-37"), new Mail("8bitdo@mail.com"), new Phone("85990909090")
        );

        sList = new SupplierList();

        sList.addSupplier(s1);
        sList.addSupplier(s2);
        sList.addSupplier(s3);
    }
    
    @Test
    @DisplayName("Assert all suppliers created are being returned in getAllSuppliers method")
    public void testGetAllSuppliers(){
        assertAll(
            () -> assertNotNull(sList.getAllSuppliers()),
            () -> assertEquals(3, sList.getAllSuppliers().size())
        );
    }
    
    @Test
    @DisplayName("Assert supplier returned on method getSupplierById is being returned correctly")
    public void testGetSupplierById(){
        Supplier supplier = sList.getSupplierById(2);
        assertAll(
            () -> assertInstanceOf(Supplier.class, supplier),
            () -> assertEquals(2, supplier.getsId()),
            () -> assertEquals("Microsoft Brasil LTDA.", supplier.getName()),
            () -> assertEquals("33.652.161/0001-19", supplier.getCNPJ().toString()),
            () -> assertEquals("microsoft@mail.com", supplier.getMail().toString()),
            () -> assertEquals("21985855858", supplier.getPhone().toString())
        );
    }

    @Test
    @DisplayName("Assert supplier returned on method getSupplierByCNPJ is being returned correctly")
    public void testGetSupplierByCNPJ(){
        Supplier supplier = sList.getSupplierByCNPJ("33.652.161/0001-19");
        assertAll(
            () -> assertInstanceOf(Supplier.class, supplier),
            () -> assertEquals(2, supplier.getsId()),
            () -> assertEquals("Microsoft Brasil LTDA.", supplier.getName()),
            () -> assertEquals("33.652.161/0001-19", supplier.getCNPJ().toString()),
            () -> assertEquals("microsoft@mail.com", supplier.getMail().toString()),
            () -> assertEquals("21985855858", supplier.getPhone().toString())
        );
    }
    
    @Test
    @DisplayName("Assert if no supplier are found in sList return NoSuchElementException")
    public void testGetAllSuppliersNoItemFound(){
        sList.removeSupplierById(1);
        sList.removeSupplierById(2);
        sList.removeSupplierById(3);

        assertThrows(NoSuchElementException.class, () -> sList.getAllSuppliers());
    }

    @Test
    @DisplayName("For a id that is not in Supplier List must return NoSuchElementException")
    public void testGetSupplierByIdNoItemFound(){
        assertThrows(NoSuchElementException.class, () -> sList.getSupplierById(10));
    }

    @Test
    @DisplayName("For a CNPJ that is not in Supplier List must return NoSuchElementException")
    public void testGetSupplierByCNPJNoItemFound(){
        assertThrows(NoSuchElementException.class, () -> sList.getSupplierByCNPJ("01.062.519/0001-67"));
    }

    @Test
    @DisplayName("Assert sList not null and new supplier was added succesfully")
    public void testProductsAddedCorrectly(){
        s4 = new Supplier(
            "Nintendo", new CNPJ("08.592.899/0001-90"), new Mail("nintendo@mail.com"), new Phone("85943527697")
        );
        sList.addSupplier(s4);

        assertAll(
            () -> assertNotNull(sList.getAllSuppliers()),
            () -> assertEquals(4, sList.getAllSuppliers().size())
        );
    }

    @Test
    @DisplayName("Assert supplier is correctly being removed by supplier's index")
    public void testSupplierRemovedByIndexCorrectly(){
        sList.removeSupplierByIndex(1);
        
        assertAll(
            () -> assertEquals(2, sList.getAllSuppliers().size()),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> sList.getAllSuppliers().get(2))
        );
    }

    @Test
    @DisplayName("Assert supplier is correctly being removed by supplier's id and get by id is working")
    public void testSupplierRemovedByIdCorrectly(){
        sList.removeSupplierById(2);
        
        assertAll(
            () -> assertEquals(2, sList.getAllSuppliers().size()),
            () -> assertThrows(NoSuchElementException.class, () -> sList.getSupplierById(2))
        );
    }
}
 