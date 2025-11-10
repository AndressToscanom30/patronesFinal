package Comportamentales.State.modelo;

public class ConfirmadoState implements EstadoPedido {
    
    @Override
    public void procesarPedido(Pedido pedido) {
        System.out.println("✗ El pedido ya fue procesado y confirmado");
    }
    
    @Override
    public void cancelarPedido(Pedido pedido) {
        System.out.println("Cancelando pedido confirmado...");
        System.out.println("Procesando reembolso...");
        pedido.setEstado(new CanceladoState());
        System.out.println("✓ Pedido cancelado y reembolso iniciado");
    }
    
    @Override
    public void confirmarPago(Pedido pedido) {
        System.out.println("✗ El pago ya fue confirmado");
    }
    
    @Override
    public void enviarPedido(Pedido pedido) {
        System.out.println("Preparando pedido para envío...");
        System.out.println("Empacando productos...");
        System.out.println("Generando guía de envío...");
        pedido.setEstado(new EnviadoState());
        System.out.println("✓ Pedido ahora en estado: ENVIADO");
    }
    
    @Override
    public void entregarPedido(Pedido pedido) {
        System.out.println("✗ No se puede entregar. El pedido debe estar en camino");
    }
    
    @Override
    public String obtenerEstado() {
        return "CONFIRMADO";
    }
}