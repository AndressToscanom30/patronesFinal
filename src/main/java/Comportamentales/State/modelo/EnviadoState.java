package Comportamentales.State.modelo;

public class EnviadoState implements EstadoPedido {
    
    @Override
    public void procesarPedido(Pedido pedido) {
        System.out.println("✗ El pedido ya fue procesado y está en camino");
    }
    
    @Override
    public void cancelarPedido(Pedido pedido) {
        System.out.println("✗ No se puede cancelar. El pedido ya fue enviado");
        System.out.println("Por favor contacte al servicio al cliente");
    }
    
    @Override
    public void confirmarPago(Pedido pedido) {
        System.out.println("✗ El pago ya fue confirmado anteriormente");
    }
    
    @Override
    public void enviarPedido(Pedido pedido) {
        System.out.println("✗ El pedido ya fue enviado");
    }
    
    @Override
    public void entregarPedido(Pedido pedido) {
        System.out.println("Entregando pedido al cliente...");
        System.out.println("Confirmando recepción...");
        System.out.println("Obteniendo firma...");
        pedido.setEstado(new EntregadoState());
        System.out.println("✓ Pedido ahora en estado: ENTREGADO");
    }
    
    @Override
    public String obtenerEstado() {
        return "ENVIADO";
    }
}