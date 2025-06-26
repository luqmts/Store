package view;

import java.util.Scanner;
import java.util.TreeMap;

import controller.ProductController;
import controller.SupplierController;
import model.Product;
import view.Menu.Menu;
import view.Menu.Operation;

public class ProductView {
    private final Scanner sc = new Scanner(System.in);
    private final ProductController pController;
    private final SupplierController sController;

    public ProductView(ProductController pController, SupplierController sController){
        this.pController = pController;
        this.sController = sController;
    }

    public void showProductMenu() {
        TreeMap<Integer, Operation> pDict = new TreeMap<>();

        pDict.put(1, new Operation("Insert new product", () -> promptRegisterProduct()));
        pDict.put(2, new Operation("Get all products", () -> promptGetAllProducts()));
        pDict.put(3, new Operation("Update product", () -> promptUpdateProduct()));
        pDict.put(4, new Operation("Exit", () -> System.out.println("Finishing program")));

        Menu pMenu = new Menu(pDict, sc);
        pMenu.runMenu();
    }

    public void promptRegisterProduct() {
        String pName, pSku, pDescription;
        int sId;
        Product product;

        System.out.println("Insert product's name: ");
        pName = sc.nextLine();
        System.out.println("Insert product's sku: ");
        pSku = sc.nextLine();
        System.out.println("Insert product's description");
        pDescription = sc.nextLine();
        System.out.println("Insert product's supplier Id");
        sController.showAllSuppliers();
        sId = Integer.parseInt(sc.nextLine());

        product = pController.registerProduct(pSku, pName, pDescription, sId);

        if (product == null) System.out.println("Something went wrong and your product couldn't be created");
        else System.out.println("Product created successfully!");
    }

    public void promptGetAllProducts() {
        pController.showAllProducts();
    }

    public void promptUpdateProduct(){
        String pName, pSku, pDescription;
        int sId, pId;

        System.out.println("Insert product's id that is going to be edited: ");
        pId = Integer.parseInt(sc.nextLine());
        System.out.println("Insert product's name: ");
        pName = sc.nextLine();
        System.out.println("Insert product's sku: ");
        pSku = sc.nextLine();
        System.out.println("Insert product's description");
        pDescription = sc.nextLine();
        System.out.println("Insert product's supplier Id");
        sId = Integer.parseInt(sc.nextLine());
        
        pController.updateProduct(pId, pSku, pName, pDescription, sId);
    }
}
