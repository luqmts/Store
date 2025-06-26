package controller;

import java.util.NoSuchElementException;

import model.Product;
import model.list.ProductList;

public class ProductController {
    private final ProductList pList;

    public ProductController(ProductList pList) {
        this.pList = pList;
    }

    public Product registerProduct(String pSku, String pName, String pDescription, int sId){
        try {
            Product product = new Product(pSku, pName, pDescription, sId);
    
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

    public Product updateProduct(int pId, String pSku, String pName, String pDescription, int sId){
        try {
            Product product = pList.getProductById(pId);
    
            product.setSku(pSku);
            product.setName(pName);
            product.setDescription(pDescription);
            product.setSupplierId(sId);
            
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
