package controller;

import java.util.NoSuchElementException;

import model.Product;
import model.list.ProductList;
import database.DAO.ProductDAO;;;

public class ProductController {
    private final ProductList pList;
    private final ProductDAO pDao; 

    public ProductController(ProductList pList, ProductDAO pDao) {
        this.pList = pList;
        this.pDao = pDao;
    }

    public Product registerProduct(String pSku, String pName, String pDescription, int sId){
        try {
            Product product = new Product(pSku, pName, pDescription, sId);
            
            pDao.insert(product);
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
            
            pDao.update(pId, product);

            return product;
        } catch (Exception e) {
            System.out.println("No product/supplier found with inserted id");
        }

        return null;
    }

    public void removeProduct(int pId){
        try {
            pDao.delete(pId);
            pList.removeProductById(pId);
        } catch (NoSuchElementException e) {
            System.out.println("No product found with inserted id");
        }
    }
}
