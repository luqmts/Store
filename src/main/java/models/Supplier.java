package models;

public class Supplier {
    private String name;
    private String cnpj;
    private String mail;
    private String phone;

    public Supplier(String name, String cnpj, String mail, String phone) {
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
    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
