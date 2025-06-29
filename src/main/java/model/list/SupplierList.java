package model.list;

import java.util.NoSuchElementException;

import model.Supplier;

public class SupplierList extends List<Supplier> {
    public Supplier getByCNPJ(String CNPJString) {
        for (Supplier supplier : items) {
            if (CNPJString == supplier.getCNPJ().toString()) return supplier;
        }

        throw new NoSuchElementException("No supplier found with CNPJ: " + CNPJString);
    }
}
