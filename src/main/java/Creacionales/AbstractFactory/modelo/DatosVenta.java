package Creacionales.AbstractFactory.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatosVenta {
    private String numeroVenta;
    private String clienteId;
    private String clienteNombre;
    private LocalDate fecha;
    private List<ItemVenta> items;
    private double subtotal;
    private double impuestos;
    private double total;
    private String metodoPago;
    private String direccionEnvio;

    public DatosVenta(String numeroVenta, String clienteId, String clienteNombre) {
        this.numeroVenta = numeroVenta;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.fecha = LocalDate.now();
        this.items = new ArrayList<>();
    }

    public void agregarItem(ItemVenta item) {
        items.add(item);
        recalcularTotales();
    }

    private void recalcularTotales() {
        this.subtotal = items.stream()
            .mapToDouble(ItemVenta::getSubtotal)
            .sum();
        this.impuestos = subtotal * 0.19;
        this.total = subtotal + impuestos;
    }

    // Getters y Setters
    public String getNumeroVenta() { return numeroVenta; }
    public String getClienteId() { return clienteId; }
    public String getClienteNombre() { return clienteNombre; }
    public LocalDate getFecha() { return fecha; }
    public List<ItemVenta> getItems() { return java.util.Collections.unmodifiableList(items); }
    public double getSubtotal() { return subtotal; }
    public double getImpuestos() { return impuestos; }
    public double getTotal() { return total; }
    public String getMetodoPago() { return metodoPago; }
    public String getDireccionEnvio() { return direccionEnvio; }

    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public void setDireccionEnvio(String direccion) { this.direccionEnvio = direccion; }
}