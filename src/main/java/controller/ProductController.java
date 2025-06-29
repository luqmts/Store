package controller;

import model.Product;
import service.ProductService;

public class ProductController {
    private final ProductService pService;

    public ProductController(ProductService pService) {
        this.pService = pService;
    }

    public Product registerProduct(String pSku, String pName, String pDescription, int sId){
        return pService.registerProduct(pSku, pName, pDescription, sId);
    }

    public void showAllProducts(){
       pService.showAllProducts();
    }

    public Product updateProduct(int pId, String pSku, String pName, String pDescription, int sId){
        return pService.updateProduct(pId, pSku, pName, pDescription, sId);
    }

    public void removeProduct(int pId){
        pService.deleteProduct(pId);
    }
}
