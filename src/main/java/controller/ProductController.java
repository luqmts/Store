package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.Product;
import service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService pService;

    @Autowired
    public ProductController(ProductService pService) {
        this.pService = pService;
    }

    @PostMapping
    public Product registerProduct(String pSku, String pName, String pDescription, int sId){
        return pService.registerProduct(pSku, pName, pDescription, sId);
    }

    @GetMapping
    public String showAllProducts(){
       return pService.showAllProducts();
    }

    @PutMapping
    public Product updateProduct(int pId, String pSku, String pName, String pDescription, int sId){
        return pService.updateProduct(pId, pSku, pName, pDescription, sId);
    }

    @DeleteMapping
    public int removeProduct(int pId){
        return pService.deleteProduct(pId);
    }
}
