/*package controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Supplier;
import service.SupplierService;
import valueobjects.cnpj;
import valueobjects.Mail;
import valueobjects.Phone;

public class SupplierControllerTest {
    SupplierController sController;
    SupplierService sService;

    @BeforeEach
    public void setUp() {
        sService = mock(SupplierService.class);
        sController = new SupplierController(sService);
    }

    @Test
    @DisplayName("Test if Supplier is being registered correctly")
    @Disabled
    public void testRegisterSupplier(){
        Supplier fakeSupplier = new Supplier(
            "Sony Brasil LTDA.", 
            new cnpj("43.447.044/0004-10"), 
            new Mail("sony@mail.com"), 
            new Phone("11000001111")
        );

        when(sService.registerSupplier(
            "Sony Brasil LTDA.", 
            "43.447.044/0004-10", 
            "sony@mail.com", 
            "11000001111")).thenReturn(fakeSupplier);
        Supplier supplier = sController.registerSupplier(
            "Sony Brasil LTDA.", 
            "43.447.044/0004-10", 
            "sony@mail.com", 
            "11000001111");
   
        assertAll(
            () -> assertNotNull(supplier),
            () -> assertEquals(fakeSupplier.getName(), supplier.getName()),
            () -> assertInstanceOf(cnpj.class, supplier.getcnpj()),
            () -> assertEquals(fakeSupplier.getcnpj(), supplier.getcnpj()),
            () -> assertInstanceOf(Mail.class, supplier.getMail()),
            () -> assertEquals(fakeSupplier.getMail(), supplier.getMail()),
            () -> assertInstanceOf(Phone.class, supplier.getPhone()),
            () -> assertEquals(fakeSupplier.getPhone(), supplier.getPhone())
        );
    }

    @Test
    @DisplayName("Test if Supplier is being updated correctly")
    @Disabled
    public void testUpdateSupplier(){
        Supplier fakeSupplier = new Supplier(
            1,
            "Sony Brasil LTDA.", 
            new cnpj("43.447.044/0004-10"), 
            new Mail("sony@mail.com"), 
            new Phone("11000001111")
        );

        sController.registerSupplier(
            "Microsoft Brasil LTDA.", 
            "19.573.576/0001-76", 
            "microsoft@mail.com", 
            "85111110000"
        );

        when(sService.updateSupplier(
            1, 
            "Sony Brasil LTDA.", 
           "43.447.044/0004-10", 
            "sony@mail.com", 
            "11000001111"
        )).thenReturn(fakeSupplier);
        Supplier supplier = sController.updateSupplier(
            1, 
            "Sony Brasil LTDA.", 
           "43.447.044/0004-10", 
            "sony@mail.com", 
            "11000001111"
        );

        assertAll(
            () -> assertNotNull(fakeSupplier),
            () -> assertEquals(fakeSupplier.getName(), supplier.getName()),
            () -> assertInstanceOf(cnpj.class, supplier.getcnpj()),
            () -> assertEquals(fakeSupplier.getcnpj(), supplier.getcnpj()),
            () -> assertInstanceOf(Mail.class, supplier.getMail()),
            () -> assertEquals(fakeSupplier.getMail(), supplier.getMail()),
            () -> assertInstanceOf(Phone.class, supplier.getPhone()),
            () -> assertEquals(fakeSupplier.getPhone(), supplier.getPhone())
        );
    }

    @Test
    @DisplayName("Test if invalid Supplier is not being updated and throwed a exception for")
    @Disabled
    public void testUpdateInvalidSuppler(){
        when(sService.updateSupplier(1, "Sony Brasil LTDA.", "43.447.044/0004-10", "sony@mail.com", "11000001111"))
            .thenThrow(new IllegalArgumentException("Supplier not found"));

        assertThrows(
            IllegalArgumentException.class,
            () -> sController.updateSupplier(1, "Sony Brasil LTDA.", "43.447.044/0004-10", "sony@mail.com", "11000001111"),
            "Supplier not found"
        );
    }

    @Test
    @DisplayName("Test if Supplier is being removed correctly")
    @Disabled
    public void testRemoveSupplier(){
         Supplier fakeSupplier = new Supplier(
            1,
            "Sony Brasil LTDA.", 
            new cnpj("43.447.044/0004-10"), 
            new Mail("sony@mail.com"), 
            new Phone("11000001111")
        );
        
        sController.registerSupplier("Sony Brasil LTDA.", "43.447.044/0004-10", "sony@mail.com", "11000001111");
        when(sService.deleteSupplier(1)).thenReturn(fakeSupplier.getId());

        int id = sController.removeSupplier(1);

        assertAll(
            () -> assertNotNull(id),
            () -> assertEquals(fakeSupplier.getId(), id)
        );
    }

    @Test
    @DisplayName("Test if invalid Supplier is not being removed and throwed a exception for")
    @Disabled
    public void testRemoveInvalidSupplier(){
        when(sService.deleteSupplier(1)).thenThrow(new IllegalArgumentException("Supplier not found"));
        assertThrows(
            IllegalArgumentException.class,
            () -> sController.removeSupplier(1),
            "Supplier not found"
        );
    }

    @Test
    @DisplayName("Test if all suppliers are being returned on showAllSuppliers methods")
    @Disabled
    public void testShowAllSuppliers(){
        Supplier fakeSupplier = new Supplier(
            1,
            "Sony Brasil LTDA.", 
            new cnpj("43.447.044/0004-10"), 
            new Mail("sony@mail.com"), 
            new Phone("11000001111")
        );    

        when(sService.showAllSuppliers()).thenReturn(fakeSupplier.toString());
        String result = sController.showAllSuppliers();

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(fakeSupplier.toString(), result)
        );
    }
}
*/