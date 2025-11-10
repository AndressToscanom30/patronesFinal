package Comportamentales.State.modelo;

public class CanceladoState implements EstadoPedido {
    
    @Override
    public void procesarPedido(Pedido pedido) {
        System.out.println("✗ No se puede procesar. El pedido fue cancelado");
    }
    
    @Override
    public void cancelarPedido(Pedido pedido) {
        System.out.println("✗ El pedido ya está cancelado");
    }
    
    @Override
    public void confirmarPago(Pedido pedido) {
        System.out.println("✗ No se puede confirmar pago. El pedido fue cancelado");
    }
    
    @Override
    public void enviarPedido(Pedido pedido) {
        System.out.println("✗ No se puede enviar. El pedido fue cancelado");
    }
    
    @Override
    public void entregarPedido(Pedido pedido) {
        System.out.println("✗ No se puede entregar. El pedido fue cancelado");
    }
    
    @Override
    public String obtenerEstado() {
        return "CANCELADO";
    }
}