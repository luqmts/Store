package service;

import database.DAO.DAO;
import model.Product;
import model.Supplier;
import model.list.ProductList;
import model.list.SupplierList;

public class ProductService {
    private final DAO<Product, ProductList> pDao;
    private final DAO<Supplier, SupplierList> sDao;

    public ProductService(DAO<Product, ProductList> pDao, DAO<Supplier, SupplierList> sDao) {
        this.pDao = pDao;
        this.sDao = sDao;
    }

    public Product registerProduct(String pSku, String pName, String pDescription, int sId) {
        Supplier supplier = sDao.getById(sId);

        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found");
        }

        Product product = new Product(pSku, pName, pDescription, sId);
        pDao.insert(product);

        return product;
    }

    public Product updateProduct(int pId, String pSku, String pName, String pDescription, int sId) {
        Supplier supplier = sDao.getById(sId);

        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found");
        }

        Product product = new Product(pSku, pName, pDescription, sId);
        pDao.update(pId, product);

        return product;
    }

    public void deleteProduct(int pId) {
        Product product = pDao.getById(pId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        pDao.delete(pId);
    }

    public void showAllProducts() {
        ProductList pList = pDao.get();
        
        for (Product product : pList.get()) {
            System.out.println(product.toString());
        }
    }
}
