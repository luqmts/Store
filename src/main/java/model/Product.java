package model;

public class Product implements Identifiable {
    private int id;
    private String sku;
    private String name;
    private String description;
    private int sid;

    public Product(int id, String sku, String name, String description, int supplier_id) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.sid = supplier_id;
    }

    public Product(String sku, String name, String description, int supplier_id) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.sid = supplier_id;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSupplierId() {
        return sid;
    }

    public void setSupplierId(int supplier_id) {
        this.sid = supplier_id;
    }

    @Override
    public String toString() {
        return String.format("ID: %d - [%s] %s", getId(), getSku(), getName());
    }
}
