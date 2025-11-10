package Comportamentales.State.modelo;

public class ItemPedido {
    private String producto;
    private int cantidad;
    private double precioUnitario;
    
    public ItemPedido(String producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
    
    public String getProducto() {
        return producto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    @Override
    public String toString() {
        return String.format("%d x %s @ $%.2f = $%.2f",
            cantidad, producto, precioUnitario, getSubtotal());
    }
}