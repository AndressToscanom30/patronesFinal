package Estructurales.Adapter.controlador;

import Estructurales.Adapter.modelo.*;
import java.util.*;

public class AdapterControlador {
    private SistemaInventarioInterno sistemaInterno;
    private SistemaProveedorExterno sistemaExterno;
    private InventarioAdapter adapter;

    public AdapterControlador() {
        this.sistemaInterno = new SistemaInventarioInterno();
        this.sistemaExterno = new SistemaProveedorExterno();
        this.adapter = new InventarioAdapter(sistemaExterno);
    }

    public Producto buscarProductoInterno(String codigo) {
        return sistemaInterno.buscarProducto(codigo);
    }

    public Producto buscarProductoExterno(String codigo) {
        return adapter.buscarProducto(codigo);
    }

    public List<Producto> obtenerProductosInternos() {
        return sistemaInterno.obtenerTodosLosProductos();
    }

    public List<Producto> obtenerProductosExternos() {
        return adapter.obtenerTodosLosProductos();
    }

    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> todos = new ArrayList<>();
        todos.addAll(sistemaInterno.obtenerTodosLosProductos());
        todos.addAll(adapter.obtenerTodosLosProductos());
        return todos;
    }

    public boolean verificarDisponibilidadInterno(String codigo, int cantidad) {
        return sistemaInterno.verificarDisponibilidad(codigo, cantidad);
    }

    public boolean verificarDisponibilidadExterno(String codigo, int cantidad) {
        return adapter.verificarDisponibilidad(codigo, cantidad);
    }

    public boolean verificarDisponibilidadGeneral(String codigo, int cantidad) {
        if (codigo.startsWith("EXT-")) {
            return verificarDisponibilidadExterno(codigo, cantidad);
        } else {
            return verificarDisponibilidadInterno(codigo, cantidad);
        }
    }

    public boolean actualizarStockInterno(String codigo, int nuevoStock) {
        return sistemaInterno.actualizarStock(codigo, nuevoStock);
    }

    public boolean actualizarStockExterno(String codigo, int nuevoStock) {
        return adapter.actualizarStock(codigo, nuevoStock);
    }

    public List<Producto> buscarPorProveedor(String codigoProveedor) {
        return adapter.buscarPorProveedor(codigoProveedor);
    }

    public void agregarProductoInterno(Producto producto) {
        sistemaInterno.agregarProducto(producto);
    }

    public void agregarProductoExterno(Producto producto) {
        adapter.agregarProducto(producto);
    }

    public Map<String, Integer> obtenerEstadisticasInventario() {
        Map<String, Integer> stats = new HashMap<>();
        
        int totalInterno = sistemaInterno.obtenerTodosLosProductos().size();
        int totalExterno = adapter.obtenerTodosLosProductos().size();
        
        stats.put("INTERNOS", totalInterno);
        stats.put("EXTERNOS", totalExterno);
        stats.put("TOTAL", totalInterno + totalExterno);
        
        return stats;
    }

    public SistemaInventarioInterno getSistemaInterno() {
        return sistemaInterno;
    }

    public InventarioAdapter getAdapter() {
        return adapter;
    }
}
