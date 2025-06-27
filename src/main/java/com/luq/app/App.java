package com.luq.app;

import java.sql.Connection;

import controller.ProductController;
import controller.SupplierController;

import database.PostgresConnection;
import database.DAO.ProductDAO;
import database.DAO.SupplierDAO;

import view.LoginView;
import view.ProductView;
import view.SupplierView;

public class App {
    public static void main(String[] args) {
        Connection conn = PostgresConnection.connect();

        if (conn == null) {
            System.out.println("Error connecting to the database");
            return;
        }

        SupplierDAO sDao = new SupplierDAO(conn);
        ProductDAO pDao = new ProductDAO(conn);
  
        SupplierController sController = new SupplierController(sDao);
        ProductController pController = new ProductController(sDao, pDao);

        SupplierView sView = new SupplierView(sController);
        ProductView pView = new ProductView(pController, sController);

        LoginView lView = new LoginView(pView, sView);
        
        lView.showMainMenu();
    }
}
