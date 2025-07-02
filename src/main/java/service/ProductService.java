package service;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import database.DAO.DAO;
import model.Product;
import model.Supplier;
import model.list.ProductList;
import model.list.SupplierList;

@Service
public class ProductService {
    private DAO<Product, ProductList> pDao;
    private DAO<Supplier, SupplierList> sDao;

    public ProductService(DAO<Product, ProductList> pDao, DAO<Supplier, SupplierList> sDao) {
        this.pDao = pDao;
        this.sDao = sDao;
    }

    public Product registerProduct(String pSku, String pName, String pDescription, int sId) {
        try {
            Supplier supplier = sDao.getById(sId);
    
            if (supplier == null) {
                throw new IllegalArgumentException("Supplier not found");
            }
    
            Product product = new Product(pSku, pName, pDescription, sId);
            pDao.insert(product);
    
            return product;
        } catch (SQLException e) {
            return null;
        }
    }

    public Product updateProduct(int pId, String pSku, String pName, String pDescription, int sId) {
        try {
            Product product = pDao.getById(pId);
            Supplier supplier = sDao.getById(sId);
    
            if (product == null) {
                throw new IllegalArgumentException("Product not found");
            }
    
            if (supplier == null) {
                throw new IllegalArgumentException("Supplier not found");
            }
    
            product = new Product(pSku, pName, pDescription, sId);
            pDao.update(pId, product);
    
            return product;
        } catch (SQLException e) {
            return null;
        }
    }

    public int deleteProduct(int pId) {
        try {
            Product product = pDao.getById(pId);
    
            if (product == null) {
                throw new IllegalArgumentException("Product not found");
            }
    
            pDao.delete(pId);
    
            return pId;
        } catch (SQLException e) {
            return -1;
        }
    }

    public String showAllProducts() {
        try {
            ProductList pList = pDao.get();
        
            if (pList.get().isEmpty()) 
                throw new NoSuchElementException("No items registered.");
    
            return pList.get().stream()
            .map(Product::toString)
            .collect(java.util.stream.Collectors.joining("\n"));
        } catch (SQLException e) {
            return "Due to an database problem couldn't be possible to show all products, please try again";
        }
    }
}
