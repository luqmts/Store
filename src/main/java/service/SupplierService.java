package service;

import model.list.SupplierList;
import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;
import model.Supplier;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import database.DAO.DAO;

public class SupplierService {
    private final DAO<Supplier, SupplierList> sDao;

    public SupplierService(DAO<Supplier, SupplierList> sDao) {
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
        } catch (SQLException e) {
            return null;
        }
    }

    public Supplier updateSupplier(int sId, String sName, String sCNPJ, String sMail, String sPhone){
        try {
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
        } catch (SQLException e) {
            return null;
        }
    }

    public int deleteSupplier(int sId) {
        try {
            Supplier supplier = sDao.getById(sId);
            
            if (supplier == null) {
                throw new IllegalArgumentException("Supplier not found");
            }
    
            sDao.delete(sId);
    
            return sId;
        } catch (SQLException e) {
            return -1;
        }
    }

    public String showAllSuppliers() {
        try {
            SupplierList sList = sDao.get();
    
            if (sList.get().isEmpty()) {
                throw new NoSuchElementException("No items registered.");
            } 
    
            return sList.get().stream()
            .map(Supplier::toString)
            .collect(java.util.stream.Collectors.joining("\n"));
        } catch (SQLException e) {
            return "Due to an database problem couldn't be possible to show all suppliers, please try again";
        }
    }
}
