package com.luq.storevs.model.list;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.luq.storevs.model.Supplier;
import com.luq.storevs.valueobjects.Cnpj;
import com.luq.storevs.valueobjects.Mail;
import com.luq.storevs.valueobjects.Phone;

public class SupplierListTest {
    Supplier s1, s2, s3, s4;
    SupplierList sList;

    @BeforeEach
    public void setUp(){
        s1 = new Supplier(
            1, "Sony Brasil LTDA.", new Cnpj("43.447.044/0004-10"), new Mail("sony@mail.com"), new Phone("11000001111")
        );
        s2 = new Supplier(
            2, "Microsoft Brasil LTDA.", new Cnpj("33.652.161/0001-19"), new Mail("microsoft@mail.com"), new Phone("21985855858")
        );
        s3 = new Supplier(
            3, "8BitDO LTDA.", new Cnpj("37.835.617/0001-37"), new Mail("8bitdo@mail.com"), new Phone("85990909090")
        );

        sList = new SupplierList();

        sList.add(s1);
        sList.add(s2);
        sList.add(s3);
    }
    
    @Test
    @DisplayName("Assert all suppliers created are being returned in getAllSuppliers method")
    public void testGetAllSuppliers(){
        assertAll(
            () -> assertNotNull(sList.get()),
            () -> assertEquals(3, sList.get().size())
        );
    }
    
    @Test
    @DisplayName("Assert supplier returned on method getSupplierById is being returned correctly")
    public void testGetSupplierById(){
        Supplier supplier = sList.getById(2);
        assertAll(
            () -> assertInstanceOf(Supplier.class, supplier),
            () -> assertEquals(2, supplier.getId()),
            () -> assertEquals("Microsoft Brasil LTDA.", supplier.getName()),
            () -> assertEquals("33.652.161/0001-19", supplier.getCnpj().toString()),
            () -> assertEquals("microsoft@mail.com", supplier.getMail().toString()),
            () -> assertEquals("21985855858", supplier.getPhone().toString())
        );
    }

    @Test
    @DisplayName("Assert supplier returned on method getSupplierBycnpj is being returned correctly")
    public void testGetSupplierBycnpj(){
        Supplier supplier = sList.getBycnpj("33.652.161/0001-19");
        assertAll(
            () -> assertInstanceOf(Supplier.class, supplier),
            () -> assertEquals(2, supplier.getId()),
            () -> assertEquals("Microsoft Brasil LTDA.", supplier.getName()),
            () -> assertEquals("33.652.161/0001-19", supplier.getCnpj().toString()),
            () -> assertEquals("microsoft@mail.com", supplier.getMail().toString()),
            () -> assertEquals("21985855858", supplier.getPhone().toString())
        );
    }
    
    @Test
    @DisplayName("Assert if no supplier are found in sList return NoSuchElementException")
    public void testGetAllSuppliersNoItemFound(){
        sList.removeById(1);
        sList.removeById(2);
        sList.removeById(3);

        assertThrows(NoSuchElementException.class, () -> sList.get());
    }

    @Test
    @DisplayName("For a id that is not in Supplier List must return NoSuchElementException")
    public void testGetSupplierByIdNoItemFound(){
        assertThrows(NoSuchElementException.class, () -> sList.getById(10));
    }

    @Test
    @DisplayName("For a cnpj that is not in Supplier List must return NoSuchElementException")
    public void testGetSupplierBycnpjNoItemFound(){
        assertThrows(NoSuchElementException.class, () -> sList.getBycnpj("01.062.519/0001-67"));
    }

    @Test
    @DisplayName("Assert sList not null and new supplier was added succesfully")
    public void testProductsAddedCorrectly(){
        s4 = new Supplier(
            "Nintendo", new Cnpj("08.592.899/0001-90"), new Mail("nintendo@mail.com"), new Phone("85943527697")
        );
        sList.add(s4);

        assertAll(
            () -> assertNotNull(sList.get()),
            () -> assertEquals(4, sList.get().size())
        );
    }

    @Test
    @DisplayName("Assert supplier is correctly being removed by supplier's index")
    public void testSupplierRemovedByIndexCorrectly(){
        sList.removeById(1);
        
        assertAll(
            () -> assertEquals(2, sList.get().size()),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> sList.get().get(2))
        );
    }

    @Test
    @DisplayName("Assert supplier is correctly being removed by supplier's id and get by id is working")
    public void testSupplierRemovedByIdCorrectly(){
        sList.removeById(2);
        
        assertAll(
            () -> assertEquals(2, sList.get().size()),
            () -> assertThrows(NoSuchElementException.class, () -> sList.getById(2))
        );
    }
}
 