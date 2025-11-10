package Estructurales.Facade.modelo;

public class ItemVenta {
    private String codigoProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    
    public ItemVenta(String codigoProducto, String nombreProducto, int cantidad, double precioUnitario) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }
    
    public String getCodigoProducto() {
        return codigoProducto;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
}