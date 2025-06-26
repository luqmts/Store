package model.list;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import model.Supplier;

public class SupplierList {
    private ArrayList<Supplier> sList;
    
    public SupplierList(){
        sList = new ArrayList<Supplier>();
    }

    public void addSupplier(Supplier supplier) {
        sList.add(supplier);
    }

    public void removeSupplierByIndex(int sIndex) {
        try {
            sList.remove(sIndex);
        } catch (IndexOutOfBoundsException e ) {
            System.out.println(String.format("Supplier with index '%d' not found.", sIndex));
        }
    }

    public void removeSupplierById(int sId) {
        try {
            Supplier supplier = getSupplierById(sId);
            sList.remove(supplier);
        } catch (NoSuchElementException e){
            System.out.println(String.format("Supplier with id '%d' not found.", sId));
        }
    }

    public ArrayList<Supplier> getAllSuppliers(){
        if (sList.isEmpty()) throw new NoSuchElementException();
        else return sList;
    }
    
    public Supplier getSupplierById(int supplierId) {
        for (Supplier supplier : sList) {
            if (supplierId == supplier.getsId()) return supplier;
        }

        throw new NoSuchElementException();
    }

    public Supplier getSupplierByCNPJ(String CNPJString) {
        for (Supplier supplier : sList) {
            if (CNPJString == supplier.getCNPJ().toString()) return supplier;
        }

        throw new NoSuchElementException();
    }
}
