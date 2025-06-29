package model.list;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import model.Supplier;

public class SupplierList extends List<Supplier> {
    private ArrayList<Supplier> sList;
    
    public SupplierList(){
        sList = new ArrayList<Supplier>();
    }

    public void add(Supplier supplier) {
        sList.add(supplier);
    }

    public void removeByIndex(int sIndex) {
        try {
            sList.remove(sIndex);
        } catch (IndexOutOfBoundsException e ) {
            System.out.println(String.format("Supplier with index '%d' not found.", sIndex));
        }
    }

    public void removeById(int sId) {
        try {
            Supplier supplier = getById(sId);
            sList.remove(supplier);
        } catch (NoSuchElementException e){
            System.out.println(String.format("Supplier with id '%d' not found.", sId));
        }
    }

    public ArrayList<Supplier> get(){
        if (sList.isEmpty()) throw new NoSuchElementException();
        else return sList;
    }
    
    public Supplier getById(int supplierId) {
        for (Supplier supplier : sList) {
            if (supplierId == supplier.getId()) return supplier;
        }

        throw new NoSuchElementException();
    }

    public Supplier getByCNPJ(String CNPJString) {
        for (Supplier supplier : sList) {
            if (CNPJString == supplier.getCNPJ().toString()) return supplier;
        }

        throw new NoSuchElementException();
    }
}
