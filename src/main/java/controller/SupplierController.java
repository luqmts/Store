package controller;

import model.list.SupplierList;

import java.util.NoSuchElementException;

import model.Supplier;
import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

import database.DAO.SupplierDAO;

public class SupplierController {
    private final SupplierDAO sDao;

    public SupplierController(SupplierDAO sDao){
        this.sDao = sDao;
    }

    public Supplier registerSupplier(String sName, String sCNPJ, String sMail, String sPhone){
        try {
            CNPJ cnpj = new CNPJ(sCNPJ);
            Mail mail = new Mail(sMail);
            Phone phone = new Phone(sPhone);
    
            Supplier supplier = new Supplier(sName, cnpj, mail, phone);

            sDao.insert(supplier);    
            return supplier;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void showAllSuppliers(){
        SupplierList sList = sDao.get();

        try {
            for (Supplier supplier : sList.get()) {
                System.out.println(supplier.toString());
            }
        } catch (NoSuchElementException e) {
            System.out.println("No suppliers registered");
        }
    }

    public Supplier updateSupplier(int sId, String sName, String sCNPJ, String sMail, String sPhone){
        try {
            Supplier supplier = sDao.getById(sId);
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
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}
