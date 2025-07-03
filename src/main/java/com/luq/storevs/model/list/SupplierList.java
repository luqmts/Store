package com.luq.storevs.model.list;

import java.util.NoSuchElementException;

import com.luq.storevs.model.Supplier;

public class SupplierList extends List<Supplier> {
    public Supplier getByCNPJ(String CNPJString) {
        for (Supplier supplier : items) {
            if (CNPJString == supplier.getCNPJ().toString()) return supplier;
        }

        throw new NoSuchElementException("No supplier found with CNPJ: " + CNPJString);
    }
}
