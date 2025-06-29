package model.list;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import model.Product;
import model.Supplier;


public class ProductList extends List<Product> {
    private ArrayList<Product> pList;
    
    public ProductList(){
        pList = new ArrayList<Product>();
    }

    public Product getBySku(String productSku) {
        for (Product product : pList) {
            if (productSku == product.getSku()) return product;
        }

        throw new NoSuchElementException();
    }

    public ArrayList<Product> getBySupplier(Supplier supplier) {
        ArrayList<Product> pListBySupplier = new ArrayList<Product>();

        for (Product product : pList) {
            if (supplier.getId() == product.getSupplierId())
                pListBySupplier.add(product);
        }

        if (pListBySupplier.isEmpty()) 
            throw new NoSuchElementException();
        else return pListBySupplier;
    }
}
