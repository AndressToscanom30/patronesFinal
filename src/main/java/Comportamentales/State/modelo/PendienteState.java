package Comportamentales.State.modelo;

public class PendienteState implements EstadoPedido {
    
    @Override
    public void procesarPedido(Pedido pedido) {
        System.out.println("Procesando pedido pendiente...");
        System.out.println("Verificando inventario...");
        System.out.println("Reservando productos...");
        pedido.setEstado(new ProcesandoState());
        System.out.println("✓ Pedido ahora en estado: PROCESANDO");
    }
    
    @Override
    public void cancelarPedido(Pedido pedido) {
        System.out.println("Cancelando pedido pendiente...");
        pedido.setEstado(new CanceladoState());
        System.out.println("✓ Pedido cancelado exitosamente");
    }
    
    @Override
    public void confirmarPago(Pedido pedido) {
        System.out.println("✗ No se puede confirmar pago. El pedido aún no está procesado");
    }
    
    @Override
    public void enviarPedido(Pedido pedido) {
        System.out.println("✗ No se puede enviar. El pedido debe estar procesado primero");
    }
    
    @Override
    public void entregarPedido(Pedido pedido) {
        System.out.println("✗ No se puede entregar. El pedido debe estar en camino");
    }
    
    @Override
    public String obtenerEstado() {
        return "PENDIENTE";
    }
}