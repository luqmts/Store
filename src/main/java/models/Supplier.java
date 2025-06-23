package models;

import valueobjects.Mail;
import valueobjects.CNPJ;
import valueobjects.Phone;

public class Supplier {
    private String name;
    private CNPJ cnpj;
    private Mail mail;
    private String phone;

    public Supplier(String name, CNPJ cnpj, Mail mail, String phone) {
        this.name = name;
        this.cnpj = cnpj;
        this.mail = mail;
        this.phone = phone;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public CNPJ getCnpj() {
        return cnpj;
    }
    public void setCnpj(CNPJ cnpj) {
        this.cnpj = cnpj;
    }
    public Mail getMail() {
        return mail;
    }
    public void setMail(Mail mail) {
        this.mail = mail;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
