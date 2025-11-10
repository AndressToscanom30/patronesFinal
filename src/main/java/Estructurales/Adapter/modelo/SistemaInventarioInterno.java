package Estructurales.Adapter.modelo;

import java.util.*;

public class SistemaInventarioInterno {
    private Map<String, Producto> inventario;

    public SistemaInventarioInterno() {
        this.inventario = new HashMap<>();
        inicializarInventario();
    }

    private void inicializarInventario() {
        agregarProducto(new Producto("P001", "Arroz Diana 1kg", 3500, "ALIMENTOS", 100));
        agregarProducto(new Producto("P002", "Aceite Gourmet 1L", 8500, "ALIMENTOS", 50));
        agregarProducto(new Producto("P003", "Leche Alpina 1L", 3800, "LACTEOS", 80));
        agregarProducto(new Producto("P004", "Pan Tajado", 4500, "PANADERIA", 60));
        agregarProducto(new Producto("P005", "Huevos x30", 12000, "GRANJA", 40));
    }

    public void agregarProducto(Producto producto) {
        inventario.put(producto.getCodigo(), producto);
    }

    public Producto buscarProducto(String codigo) {
        return inventario.get(codigo);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return new ArrayList<>(inventario.values());
    }

    public boolean actualizarStock(String codigo, int nuevoStock) {
        Producto producto = inventario.get(codigo);
        if (producto != null) {
            producto.setStock(nuevoStock);
            return true;
        }
        return false;
    }

    public boolean verificarDisponibilidad(String codigo, int cantidad) {
        Producto producto = inventario.get(codigo);
        return producto != null && producto.getStock() >= cantidad;
    }
}