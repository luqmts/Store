package controller;

import service.SupplierService;

import model.Supplier;

public class SupplierController {
    protected final SupplierService sService;
    
    public SupplierController(SupplierService sService){
        this.sService = sService;
    }

    public Supplier registerSupplier(String sName, String sCNPJ, String sMail, String sPhone){
        return sService.registerSupplier(sName, sCNPJ, sMail, sPhone);
    }

    public void showAllSuppliers(){
        sService.showAllSuppliers();
    }

    public Supplier updateSupplier(int sId, String sName, String sCNPJ, String sMail, String sPhone){
        return sService.updateSupplier(sId, sName, sCNPJ, sMail, sPhone);
    }

    public void removeSupplier(int sId){
        sService.deleteSupplier(sId);
    }
}
