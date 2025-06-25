package model;

public class Product {
    private int pid;
    private String sku;
    private String name;
    private String description;
    private Supplier supplier;

    public Product(int pid, String sku, String name, String description, Supplier supplier) {
        this.pid = pid;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.supplier = supplier;
    }

    public Product(String sku, String name, String description, Supplier supplier) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.supplier = supplier;
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", getSku(), getName());
    }
}
