package controller;

import model.list.SupplierList;

import java.util.NoSuchElementException;

import model.Supplier;
import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

import database.DAO.SupplierDAO;

public class SupplierController {
    private final SupplierList sList;
    private final SupplierDAO sDao;

    public SupplierController(SupplierList sList, SupplierDAO sDao){
        this.sList = sList;
        this.sDao = sDao;
    }

    public Supplier registerSupplier(String sName, String sCNPJ, String sMail, String sPhone){
        try {
            CNPJ cnpj = new CNPJ(sCNPJ);
            Mail mail = new Mail(sMail);
            Phone phone = new Phone(sPhone);
    
            Supplier supplier = new Supplier(sName, cnpj, mail, phone);

            sDao.insert(supplier);
            sList.addSupplier(supplier);
    
            return supplier;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void showAllSuppliers(){
        try {
            for (Supplier supplier : sList.getAllSuppliers()) {
                System.out.println(supplier.toString());
            }
        } catch (NoSuchElementException e) {
            System.out.println("No suppliers registered");
        }
    }

    public Supplier updateSupplier(int sId, String sName, String sCNPJ, String sMail, String sPhone){
        try {
            Supplier supplier = sList.getSupplierById(sId);
            CNPJ CNPJ = new CNPJ(sCNPJ);
            Mail mail = new Mail(sMail);
            Phone phone = new Phone(sPhone);
    
            supplier.setName(sName);
            supplier.setCNPJ(CNPJ);
            supplier.setMail(mail);
            supplier.setPhone(phone);

            sDao.update(sId, supplier);
    
            return supplier;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void removeSupplier(int sId){
        try {
            sDao.delete(sId);
            sList.removeSupplierById(sId);   
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}
