package Comportamentales.ChainOfResponsibility.modelo;

public class SolicitudDescuento {
    private String clienteId;
    private double montoTotal;
    private int cantidad;
    private int numeroCompras;
    private String codigoCupon;
    private String categoria;
    
    public SolicitudDescuento(String clienteId, double montoTotal, int cantidad, 
                              int numeroCompras, String codigoCupon, String categoria) {
        this.clienteId = clienteId;
        this.montoTotal = montoTotal;
        this.cantidad = cantidad;
        this.numeroCompras = numeroCompras;
        this.codigoCupon = codigoCupon;
        this.categoria = categoria;
    }
    
    // Constructor simplificado
    public SolicitudDescuento(String clienteId, double montoTotal, int cantidad) {
        this(clienteId, montoTotal, cantidad, 0, null, "GENERAL");
    }
    
    // Getters
    public String getClienteId() {
        return clienteId;
    }
    
    public double getMontoTotal() {
        return montoTotal;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public int getNumeroCompras() {
        return numeroCompras;
    }
    
    public String getCodigoCupon() {
        return codigoCupon;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    // Setters
    public void setNumeroCompras(int numeroCompras) {
        this.numeroCompras = numeroCompras;
    }
    
    public void setCodigoCupon(String codigoCupon) {
        this.codigoCupon = codigoCupon;
    }
    
    @Override
    public String toString() {
        return String.format("Solicitud[Cliente: %s, Monto: $%.2f, Cantidad: %d, Compras: %d, Cupón: %s]",
            clienteId, montoTotal, cantidad, numeroCompras, 
            codigoCupon != null ? codigoCupon : "ninguno");
    }
}