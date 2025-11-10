package Comportamentales.State.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private String numeroPedido;
    private String cliente;
    private List<ItemPedido> items;
    private double total;
    private EstadoPedido estado;
    private Date fechaCreacion;
    private List<String> historialEstados;
    
    public Pedido(String numeroPedido, String cliente) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.total = 0;
        this.estado = new PendienteState();
        this.fechaCreacion = new Date();
        this.historialEstados = new ArrayList<>();
        registrarCambioEstado("PENDIENTE");
    }
    
    public void agregarItem(ItemPedido item) {
        items.add(item);
        calcularTotal();
    }
    
    private void calcularTotal() {
        total = 0;
        for (ItemPedido item : items) {
            total += item.getSubtotal();
        }
    }
    
    public void setEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
        registrarCambioEstado(nuevoEstado.obtenerEstado());
    }
    
    private void registrarCambioEstado(String nombreEstado) {
        String registro = new Date() + " - " + nombreEstado;
        historialEstados.add(registro);
    }
    
    // Métodos que delegan al estado actual
    public void procesar() {
        estado.procesarPedido(this);
    }
    
    public void cancelar() {
        estado.cancelarPedido(this);
    }
    
    public void confirmarPago() {
        estado.confirmarPago(this);
    }
    
    public void enviar() {
        estado.enviarPedido(this);
    }
    
    public void entregar() {
        estado.entregarPedido(this);
    }
    
    // Getters
    public String getNumeroPedido() {
        return numeroPedido;
    }
    
    public String getCliente() {
        return cliente;
    }
    
    public List<ItemPedido> getItems() {
        return items;
    }
    
    public double getTotal() {
        return total;
    }
    
    public String getEstadoActual() {
        return estado.obtenerEstado();
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public List<String> getHistorialEstados() {
        return new ArrayList<>(historialEstados);
    }
    
    @Override
    public String toString() {
        return String.format("Pedido #%s - Cliente: %s - Estado: %s - Total: $%.2f",
            numeroPedido, cliente, getEstadoActual(), total);
    }
}