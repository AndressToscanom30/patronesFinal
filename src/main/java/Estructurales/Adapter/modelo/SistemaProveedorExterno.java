package Estructurales.Adapter.modelo;

import java.util.*;

public class SistemaProveedorExterno {
    private Map<String, ProductoExterno> catalogo;

    public SistemaProveedorExterno() {
        this.catalogo = new HashMap<>();
        inicializarCatalogo();
    }

    private void inicializarCatalogo() {
        addProduct(new ProductoExterno("EXT-001", "Rice Premium 1kg", 3200, "FOOD", 500, "SUP-A"));
        addProduct(new ProductoExterno("EXT-002", "Olive Oil 1L", 25000, "FOOD", 200, "SUP-B"));
        addProduct(new ProductoExterno("EXT-003", "Fresh Milk 1L", 3500, "DAIRY", 300, "SUP-A"));
        addProduct(new ProductoExterno("EXT-004", "Sliced Bread", 4000, "BAKERY", 150, "SUP-C"));
        addProduct(new ProductoExterno("EXT-005", "Premium Coffee 500g", 18000, "BEVERAGE", 100, "SUP-B"));
    }

    public void addProduct(ProductoExterno producto) {
        catalogo.put(producto.getProductId(), producto);
    }

    public ProductoExterno findProductById(String productId) {
        return catalogo.get(productId);
    }

    public List<ProductoExterno> getAllProducts() {
        return new ArrayList<>(catalogo.values());
    }

    public boolean updateQuantity(String productId, int newQuantity) {
        ProductoExterno producto = catalogo.get(productId);
        if (producto != null) {
            producto.setAvailableQuantity(newQuantity);
            return true;
        }
        return false;
    }

    public boolean checkAvailability(String productId, int requiredQuantity) {
        ProductoExterno producto = catalogo.get(productId);
        return producto != null && producto.getAvailableQuantity() >= requiredQuantity;
    }

    public List<ProductoExterno> searchBySupplier(String supplierCode) {
        return catalogo.values().stream()
            .filter(p -> p.getSupplierCode().equals(supplierCode))
            .toList();
    }
}