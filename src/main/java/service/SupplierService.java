package service;

import model.list.SupplierList;
import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;
import model.Supplier;
import database.DAO.DAO;

public class SupplierService {
    private final DAO<Supplier, SupplierList> sDao;

    public SupplierService(DAO<Supplier, SupplierList> sDao) {
        this.sDao = sDao;
    }

    public Supplier registerSupplier(String sName, String sCNPJ, String sMail, String sPhone){
        CNPJ cnpj = new CNPJ(sCNPJ);
        Mail mail = new Mail(sMail);
        Phone phone = new Phone(sPhone);

        Supplier supplier = new Supplier(sName, cnpj, mail, phone);
        sDao.insert(supplier);

        return supplier;
    }

    public Supplier updateSupplier(int sId, String sName, String sCNPJ, String sMail, String sPhone){
        Supplier supplier = sDao.getById(sId);

        if(supplier == null) {
            throw new IllegalArgumentException("Supplier not found");
        }

        CNPJ cnpj = new CNPJ(sCNPJ);
        Mail mail = new Mail(sMail);
        Phone phone = new Phone(sPhone);
        
        supplier.setName(sName);
        supplier.setCNPJ(cnpj);
        supplier.setMail(mail);
        supplier.setPhone(phone);

        sDao.update(sId, supplier);

        return supplier;
    }

    public void deleteSupplier(int sId) {
        Supplier supplier = sDao.getById(sId);
        
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier not found");
        }

        sDao.delete(sId);
    }

    public void showAllSuppliers() {
        SupplierList sList = sDao.get();

        if (sList.get().isEmpty()) {
            System.out.println("No Suppliers found");
            return;
        } 

        for (Supplier supplier : sList.get()) {
            System.out.println(supplier.toString());
        }
    }
}
