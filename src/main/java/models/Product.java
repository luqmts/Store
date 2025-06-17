package models;

public class Product {
    private int pid;
    private String sku;
    private String name;
    private String description;

    public Product(int pid, String sku, String name, String description) {
        this.pid = pid;
        this.sku = sku;
        this.name = name;
        this.description = description;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
