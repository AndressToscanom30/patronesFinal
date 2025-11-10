package Comportamentales.State.modelo;

public interface EstadoPedido {
    void procesarPedido(Pedido pedido);
    void cancelarPedido(Pedido pedido);
    void confirmarPago(Pedido pedido);
    void enviarPedido(Pedido pedido);
    void entregarPedido(Pedido pedido);
    String obtenerEstado();
}