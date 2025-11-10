package Estructurales.Adapter.modelo;

import java.util.*;

public class InventarioAdapter {
    private SistemaProveedorExterno sistemaExterno;

    public InventarioAdapter(SistemaProveedorExterno sistemaExterno) {
        this.sistemaExterno = sistemaExterno;
    }

    public Producto buscarProducto(String codigo) {
        ProductoExterno externo = sistemaExterno.findProductById(codigo);
        if (externo == null) {
            return null;
        }
        return convertirProductoExternoAInterno(externo);
    }

    public List<Producto> obtenerTodosLosProductos() {
        List<ProductoExterno> externos = sistemaExterno.getAllProducts();
        List<Producto> internos = new ArrayList<>();
        
        for (ProductoExterno externo : externos) {
            internos.add(convertirProductoExternoAInterno(externo));
        }
        
        return internos;
    }

    public boolean actualizarStock(String codigo, int nuevoStock) {
        return sistemaExterno.updateQuantity(codigo, nuevoStock);
    }

    public boolean verificarDisponibilidad(String codigo, int cantidad) {
        return sistemaExterno.checkAvailability(codigo, cantidad);
    }

    public List<Producto> buscarPorProveedor(String codigoProveedor) {
        List<ProductoExterno> externos = sistemaExterno.searchBySupplier(codigoProveedor);
        List<Producto> internos = new ArrayList<>();
        
        for (ProductoExterno externo : externos) {
            internos.add(convertirProductoExternoAInterno(externo));
        }
        
        return internos;
    }

    private Producto convertirProductoExternoAInterno(ProductoExterno externo) {
        String categoria = mapearCategoria(externo.getCategoryCode());
        return new Producto(
            externo.getProductId(),
            externo.getProductName(),
            externo.getUnitPrice(),
            categoria,
            externo.getAvailableQuantity()
        );
    }

    private String mapearCategoria(String categoryCode) {
        return switch (categoryCode) {
            case "FOOD" -> "ALIMENTOS";
            case "DAIRY" -> "LACTEOS";
            case "BAKERY" -> "PANADERIA";
            case "BEVERAGE" -> "BEBIDAS";
            default -> "OTROS";
        };
    }

    public void agregarProducto(Producto producto) {
        ProductoExterno externo = convertirProductoInternoAExterno(producto);
        sistemaExterno.addProduct(externo);
    }

    private ProductoExterno convertirProductoInternoAExterno(Producto interno) {
        String categoryCode = mapearCategoriaInversa(interno.getCategoria());
        return new ProductoExterno(
            interno.getCodigo(),
            interno.getNombre(),
            interno.getPrecio(),
            categoryCode,
            interno.getStock(),
            "SUP-INTERNAL"
        );
    }

    private String mapearCategoriaInversa(String categoria) {
        return switch (categoria) {
            case "ALIMENTOS" -> "FOOD";
            case "LACTEOS" -> "DAIRY";
            case "PANADERIA" -> "BAKERY";
            case "BEBIDAS" -> "BEVERAGE";
            default -> "OTHER";
        };
    }
}
