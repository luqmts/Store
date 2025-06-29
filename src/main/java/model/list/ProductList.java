package model.list;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import model.Product;
import model.Supplier;


public class ProductList extends List<Product> {
    public Product getBySku(String productSku) {
        for (Product product : items) {
            if (productSku == product.getSku()) return product;
        }

        throw new NoSuchElementException("No Product found with SKU: " + productSku);
    }

    public ArrayList<Product> getBySupplier(Supplier supplier) {
        ArrayList<Product> pListBySupplier = new ArrayList<Product>();

        for (Product product : items) {
            if (supplier.getId() == product.getSupplierId())
                pListBySupplier.add(product);
        }

        if (pListBySupplier.isEmpty()) 
            throw new NoSuchElementException("No Product found with Supplier: " + supplier.getName()); 
        
        return pListBySupplier;
    }
}
