package Estructurales.Adapter.modelo;

public class ProductoExterno {
    private String productId;
    private String productName;
    private double unitPrice;
    private String categoryCode;
    private int availableQuantity;
    private String supplierCode;

    public ProductoExterno(String productId, String productName, double unitPrice, 
                          String categoryCode, int availableQuantity, String supplierCode) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.categoryCode = categoryCode;
        this.availableQuantity = availableQuantity;
        this.supplierCode = supplierCode;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getUnitPrice() { return unitPrice; }
    public String getCategoryCode() { return categoryCode; }
    public int getAvailableQuantity() { return availableQuantity; }
    public String getSupplierCode() { return supplierCode; }

    public void setProductId(String productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }

    @Override
    public String toString() {
        return String.format("ProductoExterno[%s, %s, $%.2f, Qty: %d, Supplier: %s]", 
            productId, productName, unitPrice, availableQuantity, supplierCode);
    }
}
