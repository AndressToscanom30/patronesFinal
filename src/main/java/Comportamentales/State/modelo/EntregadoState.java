package Comportamentales.State.modelo;

public class EntregadoState implements EstadoPedido {
    
    @Override
    public void procesarPedido(Pedido pedido) {
        System.out.println("✗ El pedido ya fue completado y entregado");
    }
    
    @Override
    public void cancelarPedido(Pedido pedido) {
        System.out.println("✗ No se puede cancelar. El pedido ya fue entregado");
        System.out.println("Puede solicitar una devolución");
    }
    
    @Override
    public void confirmarPago(Pedido pedido) {
        System.out.println("✗ El pedido ya fue pagado y entregado");
    }
    
    @Override
    public void enviarPedido(Pedido pedido) {
        System.out.println("✗ El pedido ya fue enviado y entregado");
    }
    
    @Override
    public void entregarPedido(Pedido pedido) {
        System.out.println("✗ El pedido ya fue entregado");
    }
    
    @Override
    public String obtenerEstado() {
        return "ENTREGADO";
    }
}