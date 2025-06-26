package com.luq.app;


import controller.ProductController;
import model.Supplier;
import model.list.ProductList;
import model.list.SupplierList;
import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;
import view.ProductView;

public class App {
    public static void main(String[] args) {
        ProductList pList = new ProductList();
        SupplierList sList = new SupplierList();
        Supplier s1 = new Supplier(
            1, "Sony Brasil LTDA.", new CNPJ("43.447.044/0004-10"), new Mail("sony@mail.com"), new Phone("11000001111")
        );
        sList.addSupplier(s1);
        
        ProductController pController = new ProductController(pList, sList);
        ProductView pView = new ProductView(pController);
        pView.showProductMenu();
    }
}
