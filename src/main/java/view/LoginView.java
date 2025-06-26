package view;

import java.util.Scanner;
import java.util.TreeMap;

import view.Menu.Operation;
import view.Menu.Menu;

public class LoginView {
    private final Scanner sc = new Scanner(System.in);
    private final ProductView pView;
    private final SupplierView sView;

    public LoginView(ProductView pView, SupplierView sView){
        this.pView = pView;
        this.sView = sView;
    }

    public void showMainMenu(){
        TreeMap<Integer, Operation> mDict = new TreeMap<>();
        
        mDict.put(1, new Operation("Manage Products", () -> pView.showProductMenu()));
        mDict.put(2, new Operation("Manage Suppliers", () -> sView.showSuppliersMenu()));
        mDict.put(3, new Operation("Exit", () -> System.out.println("Finishing program!")));

        Menu mMenu = new Menu(mDict, sc);
        mMenu.runMenu();
    }
}
