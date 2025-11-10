package Comportamentales.Command.modelo;

import java.util.ArrayList;
import java.util.List;

public class CarritoCompras {
    private List<ItemCarrito> items;
    private double descuento;
    
    public CarritoCompras() {
        this.items = new ArrayList<>();
        this.descuento = 0;
    }
    
    public void agregarProducto(Producto producto, int cantidad) {
        // Buscar si el producto ya existe
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo().equals(producto.getCodigo())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        // Si no existe, agregar nuevo item
        items.add(new ItemCarrito(producto, cantidad));
    }
    
    public void eliminarProducto(Producto producto, int cantidad) {
        ItemCarrito itemAEliminar = null;
        
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo().equals(producto.getCodigo())) {
                int nuevaCantidad = item.getCantidad() - cantidad;
                if (nuevaCantidad <= 0) {
                    itemAEliminar = item;
                } else {
                    item.setCantidad(nuevaCantidad);
                }
                break;
            }
        }
        
        if (itemAEliminar != null) {
            items.remove(itemAEliminar);
        }
    }
    
    public void aplicarDescuento(double porcentaje) {
        this.descuento = porcentaje;
    }
    
    public void vaciar() {
        items.clear();
        descuento = 0;
    }
    
    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.getSubtotal();
        }
        return Math.round(subtotal * 100.0) / 100.0;
    }
    
    public double calcularDescuento() {
        // Si el descuento es 0, no hay descuento que calcular
        if (descuento == 0) return 0;
        // Calculamos el subtotal primero
        double subtotal = calcularSubtotal();
        // Calculamos el descuento exacto y lo redondeamos
        return Math.round(subtotal * (descuento / 100.0) * 100.0) / 100.0;
    }
    
    public double calcularTotal() {
        // Calculamos el subtotal primero
        double subtotal = calcularSubtotal();
        // Calculamos el descuento usando calcularDescuento()
        double montoDescuento = calcularDescuento();
        // Restamos el descuento y redondeamos
        return Math.round((subtotal - montoDescuento) * 100.0) / 100.0;
    }
    
    public List<ItemCarrito> getItems() {
        return new ArrayList<>(items);
    }
    
    public double getDescuento() {
        return descuento;
    }
    
    public int getCantidadItems() {
        return items.size();
    }
    
    public boolean estaVacio() {
        return items.isEmpty();
    }
}