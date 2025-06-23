package models;

import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

public class Supplier {
    private String name;
    private CNPJ cnpj;
    private Mail mail;
    private Phone phone;

    public Supplier(String name, CNPJ cnpj, Mail mail, Phone phone) {
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
    public Phone getPhone() {
        return phone;
    }
    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
