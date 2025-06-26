package controller;

import java.util.NoSuchElementException;

import model.Product;
import model.Supplier;
import model.list.ProductList;
import model.list.SupplierList;

public class ProductController {
    private final ProductList pList;
    private final SupplierList sList;

    public ProductController(ProductList pList, SupplierList sList) {
        this.pList = pList;
        this.sList = sList;
    }

    public Product registerProduct(String pSku, String pName, String pDescription, int sId){
        try {
            Supplier supplier = sList.getSupplierById(sId);
            Product product = new Product(pSku, pName, pDescription, supplier);
    
            pList.addProduct(product);

            return product;
        } catch (NoSuchElementException e) {
            System.out.println("No supplier found with inserted id.");
        }

        return null;
    }

    public void showAllProducts(){
        try {
            for (Product product : pList.getAllProducts()) {
                System.out.println(product.toString());
            }
        } catch (NoSuchElementException e) {
            System.out.println("No products registered");
        }
    }

    public void showAllSuppliers(){
        try {
            for (Supplier supplier : sList.getAllSuppliers()) {
                System.out.println(supplier.toString());
            }
        } catch (NoSuchElementException e) {
            System.out.println("No suppliers registered");
        }
    }

    public Product updateProduct(int pId, String pSku, String pName, String pDescription, int sId){
        try {
            Product product = pList.getProductById(pId);
            Supplier supplier = sList.getSupplierById(sId);
    
            product.setSku(pSku);
            product.setName(pName);
            product.setDescription(pDescription);
            product.setSupplier(supplier);
            
            return product;
        } catch (Exception e) {
            System.out.println("No product/supplier found with inserted id");
        }

        return null;
    }

    public void removeProduct(int pId){
        try {
            pList.removeProductById(pId);
        } catch (NoSuchElementException e) {
            System.out.println("No product found with inserted id");
        }
    }
}
