package Creacionales.FactoryMethod.controlador;

import Creacionales.FactoryMethod.modelo.*;
import java.util.*;

public class ProductoControlador {

    private final Map<String, ProductoFactory> factories;
    private final List<Producto> productos;

    public ProductoControlador() {
        this.factories = new HashMap<>();
        this.productos = new ArrayList<>();
        inicializarFactories();
    }

    private void inicializarFactories() {
        factories.put("SIMPLE", new ProductoSimpleFactory());
        factories.put("PESO", new ProductoPesoFactory());
    }

    public Producto crearProducto(String tipo, String codigo, String nombre,
                                  double precio, String categoria) {
        ProductoFactory factory = factories.get(tipo.toUpperCase());

        if (factory == null) {
            return null;
        }

        Producto producto = factory.crearProducto(codigo, nombre, precio, categoria);
        productos.add(producto);
        return producto;
    }

    public List<Producto> obtenerTodosLosProductos() {
        return new ArrayList<>(productos);
    }

    public Producto buscarPorCodigo(String codigo) {
        return productos.stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    public Set<String> obtenerTiposDisponibles() {
        return factories.keySet();
    }

}
