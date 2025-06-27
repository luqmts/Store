package model.list;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import model.Product;
import model.Supplier;

public class ProductList implements List<Product> {
    private ArrayList<Product> pList;
    
    public ProductList(){
        pList = new ArrayList<Product>();
    }

    public void add(Product product) {
        pList.add(product);
    }

    public void removeByIndex(int pIndex) {
        try {
            pList.remove(pIndex);
        } catch (IndexOutOfBoundsException e ) {
            System.out.println(String.format("Product with index '%d' not found.", pIndex));
        }
    }

    public void removeById(int pId) {
        try {
            Product product = getById(pId);
            pList.remove(product);
        } catch (NoSuchElementException e){
            System.out.println(String.format("Product with id '%d' not found.", pId));
        }
    }

    public ArrayList<Product> get(){
        if (pList.isEmpty()) throw new NoSuchElementException();
        else return pList;
    }
    
    public Product getById(int productId) {
        for (Product product : pList) {
            if (productId == product.getPid()) return product;
        }

        throw new NoSuchElementException();
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
            if (supplier.getsId() == product.getSupplierId())
                pListBySupplier.add(product);
        }

        if (pListBySupplier.isEmpty()) 
            throw new NoSuchElementException();
        else return pListBySupplier;
    }
}
