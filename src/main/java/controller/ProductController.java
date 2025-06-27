package controller;

import java.util.NoSuchElementException;

import model.Product;
import model.Supplier;
import model.list.ProductList;

import database.DAO.ProductDAO;
import database.DAO.SupplierDAO;

public class ProductController {
    private final SupplierDAO sDao;
    private final ProductDAO pDao; 

    public ProductController(SupplierDAO sDao, ProductDAO pDao) {
        this.sDao = sDao;
        this.pDao = pDao;
    }

    public Product registerProduct(String pSku, String pName, String pDescription, int sId){
        try {
            Supplier supplier = sDao.getById(sId);
            if (supplier == null) {
                throw new NoSuchElementException("No supplier found with the given id.");
            }
            Product product = new Product(pSku, pName, pDescription, sId);
            
            pDao.insert(product);

            return product;
        } catch (NoSuchElementException e) {
            System.out.println("No supplier found with inserted id.");
        }

        return null;
    }

    public void showAllProducts(){
        ProductList pList = pDao.get(sDao);
        try {
            for (Product product : pList.get()) {
                System.out.println(product.toString());
            }
        } catch (NoSuchElementException e) {
            System.out.println("No products registered");
        }
    }

    public Product updateProduct(int pId, String pSku, String pName, String pDescription, int sId){
        try {
            Product product = pDao.getById(pId);
    
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
        } catch (NoSuchElementException e) {
            System.out.println("No product found with inserted id");
        }
    }
}
