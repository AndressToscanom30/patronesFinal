package Estructurales.Facade.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Venta {
    private String id;
    private String clienteId;
    private String clienteNombre;
    private String clienteEmail;
    private List<ItemVenta> items;
    private double subtotal;
    private double impuestos;
    private double total;
    private String metodoPago;
    private Date fecha;
    private String numeroFactura;
    private String comprobantePago;
    
    public Venta() {
        this.items = new ArrayList<>();
        this.fecha = new Date();
        this.id = "VENTA-" + System.currentTimeMillis();
    }
    
    public void agregarItem(String codigoProducto, String nombreProducto, int cantidad, double precioUnitario) {
        ItemVenta item = new ItemVenta(codigoProducto, nombreProducto, cantidad, precioUnitario);
        items.add(item);
        calcularTotales();
    }
    
    private void calcularTotales() {
        subtotal = 0;
        for (ItemVenta item : items) {
            subtotal += item.getSubtotal();
        }
        impuestos = subtotal * 0.19;
        total = subtotal + impuestos;
    }
    
    public String getId() {
        return id;
    }
    
    public String getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getClienteNombre() {
        return clienteNombre;
    }
    
    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }
    
    public String getClienteEmail() {
        return clienteEmail;
    }
    
    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }
    
    public List<ItemVenta> getItems() {
        return items;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public double getImpuestos() {
        return impuestos;
    }
    
    public double getTotal() {
        return total;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public String getNumeroFactura() {
        return numeroFactura;
    }
    
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }
    
    public String getComprobantePago() {
        return comprobantePago;
    }
    
    public void setComprobantePago(String comprobantePago) {
        this.comprobantePago = comprobantePago;
    }
}