package com.luq.storevs.view;

import java.util.Scanner;
import java.util.TreeMap;

import com.luq.storevs.controller.SupplierController;
import com.luq.storevs.view.Menu.Menu;
import com.luq.storevs.view.Menu.Operation;
import com.luq.storevs.model.Supplier;

public class SupplierView{
    private final Scanner sc = new Scanner(System.in);
    private final SupplierController sController;

    public SupplierView(SupplierController sController){
        this.sController = sController;
    }

    public void showSuppliersMenu(){
        TreeMap<Integer, Operation> sDict = new TreeMap<>();

        sDict.put(1, new Operation("Insert new Supplier", () -> promptRegisterSupplier()));
        sDict.put(2, new Operation("Get all Suppliers", () -> promptGettAllSuppliers()));
        sDict.put(3, new Operation("Update Product", () -> promptUpdateSupplier()));
        sDict.put(4, new Operation("Remove a Supplier", () -> promptRemoveSupplier()));
        sDict.put(5, new Operation("Exit", () -> System.out.println("Finishing supplier management")));

        Menu sMenu = new Menu(sDict, sc);
        sMenu.runMenu();
    }

    public void promptRegisterSupplier(){
        String sName, sCNPJ, sMail, sPhone;

        System.out.println("Insert Supplier's name: ");
        sName = sc.nextLine();
        System.out.println("Insert Supplier's CNPJ: ");
        sCNPJ = sc.nextLine();
        System.out.println("Insert Supplier's mail: ");
        sMail = sc.nextLine();
        System.out.println("Insert Supplier's phone: ");
        sPhone = sc.nextLine();

        Supplier supplier = sController.registerSupplier(sName, sCNPJ, sMail, sPhone);

        if (supplier == null) System.out.println("Something went wrong and your supplier couldn't be registered");
        else System.out.println("Supplier registered successfully!");
    }

    public void promptGettAllSuppliers(){
        sController.getSuppliers();
    }

    public void promptUpdateSupplier(){
        String sName, sCNPJ, sMail, sPhone;
        int sId;

        System.out.println("Insert Supplier's id: ");
        sId = Integer.parseInt(sc.nextLine());
        System.out.println("Insert Supplier's name: ");
        sName = sc.nextLine();
        System.out.println("Insert Supplier's CNPJ: ");
        sCNPJ = sc.nextLine();
        System.out.println("Insert Supplier's mail: ");
        sMail = sc.nextLine();
        System.out.println("Insert Supplier's phone: ");
        sPhone = sc.nextLine();

        //sController.updateSupplier(sId, sName, sCNPJ, sMail, sPhone);
    }

    public void promptRemoveSupplier(){
        int sId;

        System.out.println("Insert Supplier's id: ");
        sId = Integer.parseInt(sc.nextLine());

        sController.removeSupplier(sId);
    }
}