package Comportamentales.State.modelo;

public class ProcesandoState implements EstadoPedido {
    
    @Override
    public void procesarPedido(Pedido pedido) {
        System.out.println("✗ El pedido ya está siendo procesado");
    }
    
    @Override
    public void cancelarPedido(Pedido pedido) {
        System.out.println("Cancelando pedido en proceso...");
        System.out.println("Liberando productos reservados...");
        pedido.setEstado(new CanceladoState());
        System.out.println("✓ Pedido cancelado");
    }
    
    @Override
    public void confirmarPago(Pedido pedido) {
        System.out.println("Confirmando pago del pedido...");
        System.out.println("Verificando transacción...");
        System.out.println("Pago aprobado...");
        pedido.setEstado(new ConfirmadoState());
        System.out.println("✓ Pedido ahora en estado: CONFIRMADO");
    }
    
    @Override
    public void enviarPedido(Pedido pedido) {
        System.out.println("✗ No se puede enviar. Debe confirmar el pago primero");
    }
    
    @Override
    public void entregarPedido(Pedido pedido) {
        System.out.println("✗ No se puede entregar. El pedido debe estar en camino");
    }
    
    @Override
    public String obtenerEstado() {
        return "PROCESANDO";
    }
}