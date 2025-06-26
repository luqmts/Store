package model;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ProductList {
    private ArrayList<Product> pList;
    
    public ProductList(){
        pList = new ArrayList<Product>();
    }

    public void addProduct(Product product) {
        pList.add(product);
    }

    public void removeProductByIndex(int pIndex) {
        try {
            pList.remove(pIndex);
        } catch (IndexOutOfBoundsException e ) {
            System.out.println(String.format("Product with index '%d' not found.", pIndex));
        }
    }

    public void removeProductById(int pId) {
        try {
            Product product = getProductById(pId);
            pList.remove(product);
        } catch (NoSuchElementException e){
            System.out.println(String.format("Product with id '%d' not found.", pId));
        }
    }

    public ArrayList<Product> getAllProducts(){
        if (pList.isEmpty()) throw new NoSuchElementException();
        else return pList;
    }
    
    public Product getProductById(int productId) {
        for (Product product : pList) {
            if (productId == product.getPid()) return product;
        }

        throw new NoSuchElementException();
    }

    public Product getProductBySku(String productSku) {
        for (Product product : pList) {
            if (productSku == product.getSku()) return product;
        }

        throw new NoSuchElementException();
    }

    public ArrayList<Product> getProductsBySupplier(Supplier supplier) {
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
