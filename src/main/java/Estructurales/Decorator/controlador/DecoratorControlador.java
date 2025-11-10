package Estructurales.Decorator.controlador;

import Estructurales.Decorator.modelo.*;
import java.util.*;

public class DecoratorControlador {
    private Map<String, ProductoBase> catalogo;
    private List<ProductoVendible> productosConfigurados;

    public DecoratorControlador() {
        this.catalogo = new HashMap<>();
        this.productosConfigurados = new ArrayList<>();
        inicializarCatalogo();
    }

    private void inicializarCatalogo() {
        catalogo.put("PROD-001", new ProductoBase("PROD-001", "Laptop Dell", "Laptop Dell Inspiron 15", 1500000, 2.5));
        catalogo.put("PROD-002", new ProductoBase("PROD-002", "Mouse Logitech", "Mouse inalámbrico", 80000, 0.2));
        catalogo.put("PROD-003", new ProductoBase("PROD-003", "Teclado Mecánico", "Teclado RGB", 250000, 1.0));
        catalogo.put("PROD-004", new ProductoBase("PROD-004", "Monitor LG 24", "Monitor Full HD", 600000, 4.5));
        catalogo.put("PROD-005", new ProductoBase("PROD-005", "Audífonos Sony", "Audífonos Bluetooth", 350000, 0.3));
    }

    public ProductoBase obtenerProductoBase(String codigo) {
        return catalogo.get(codigo);
    }

    public List<ProductoBase> obtenerCatalogo() {
        return new ArrayList<>(catalogo.values());
    }

    public ProductoVendible agregarEmpaqueRegalo(ProductoVendible producto, String tipoEmpaque, boolean incluyeTarjeta) {
        return new EmpaqueRegalo(producto, tipoEmpaque, incluyeTarjeta);
    }

    public ProductoVendible agregarGarantiaExtendida(ProductoVendible producto, int meses, String cobertura) {
        GarantiaExtendida garantia = new GarantiaExtendida(producto, meses, cobertura);
        if (!garantia.validarAplicabilidad()) {
            throw new IllegalArgumentException("Producto no califica para garantía extendida (precio mínimo $50,000)");
        }
        return garantia;
    }

    public ProductoVendible agregarPersonalizacion(ProductoVendible producto, String texto, String tipo, String posicion) {
        Personalizacion personalizacion = new Personalizacion(producto, texto, tipo, posicion);
        if (!personalizacion.validarTexto()) {
            throw new IllegalArgumentException("Texto de personalización inválido");
        }
        return personalizacion;
    }

    public void guardarProductoConfigurado(ProductoVendible producto) {
        productosConfigurados.add(producto);
    }

    public List<ProductoVendible> obtenerProductosConfigurados() {
        return new ArrayList<>(productosConfigurados);
    }

    public double calcularTotalVentas() {
        return productosConfigurados.stream()
            .mapToDouble(ProductoVendible::obtenerPrecio)
            .sum();
    }

    public Map<String, Integer> obtenerEstadisticasDecoradores() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("EMPAQUE_REGALO", 0);
        stats.put("GARANTIA", 0);
        stats.put("PERSONALIZACION", 0);

        for (ProductoVendible producto : productosConfigurados) {
            if (producto instanceof EmpaqueRegalo) stats.put("EMPAQUE_REGALO", stats.get("EMPAQUE_REGALO") + 1);
            if (producto instanceof GarantiaExtendida) stats.put("GARANTIA", stats.get("GARANTIA") + 1);
            if (producto instanceof Personalizacion) stats.put("PERSONALIZACION", stats.get("PERSONALIZACION") + 1);
        }

        return stats;
    }

    public ProductoVendible construirProductoCompleto(String codigoBase, boolean empaque, String tipoEmpaque,
                                                      boolean tarjeta, boolean garantia, int mesesGarantia,
                                                      String cobertura, boolean personalizado, String texto,
                                                      String tipoPersonalizacion, String posicion) {
        ProductoBase base = obtenerProductoBase(codigoBase);
        if (base == null) {
            throw new IllegalArgumentException("Producto base no encontrado");
        }

        ProductoVendible producto = base;

        if (empaque) {
            producto = agregarEmpaqueRegalo(producto, tipoEmpaque, tarjeta);
        }

        if (garantia) {
            producto = agregarGarantiaExtendida(producto, mesesGarantia, cobertura);
        }

        if (personalizado) {
            producto = agregarPersonalizacion(producto, texto, tipoPersonalizacion, posicion);
        }

        return producto;
    }

    public void agregarProductoAlCatalogo(ProductoBase producto) {
        catalogo.put(producto.getCodigo(), producto);
    }
}