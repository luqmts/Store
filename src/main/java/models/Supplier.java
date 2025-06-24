package models;

import valueobjects.CNPJ;
import valueobjects.Mail;
import valueobjects.Phone;

public class Supplier {
    private String name;
    private CNPJ CNPJ;
    private Mail mail;
    private Phone phone;

    public Supplier(String name, CNPJ CNPJ, Mail mail, Phone phone) {
        this.name = name;
        this.CNPJ = CNPJ;
        this.mail = mail;
        this.phone = phone;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public CNPJ getCNPJ() {
        return CNPJ;
    }
    public void setCNPJ(CNPJ CNPJ) {
        this.CNPJ = CNPJ;
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

    @Override
    public String toString() {
        return String.format("%s, CNPJ: %s", getName(), getCNPJ());
    }
}
