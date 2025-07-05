package com.luq.storevs.model.list;

import java.util.NoSuchElementException;

import com.luq.storevs.model.Supplier;

public class SupplierList extends List<Supplier> {
    public Supplier getBycnpj(String cnpjString) {
        for (Supplier supplier : items) {
            if (cnpjString == supplier.getCnpj().toString()) return supplier;
        }

        throw new NoSuchElementException("No supplier found with cnpj: " + cnpjString);
    }
}
