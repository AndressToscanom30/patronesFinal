package unit.State;

import Comportamentales.State.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DisplayName("State Pattern - Unit Tests")
public class StateUnitTest {
    
    private Pedido pedido;
    
    @BeforeEach
    public void setUp() {
        pedido = new Pedido("PED-001", "Cliente Test");
    }
    
    // ==================== ItemPedido Tests ====================
    
    @Test
    @DisplayName("ItemPedido - Crear item")
    public void testCrearItem() {
        ItemPedido item = new ItemPedido("Laptop", 1, 2500000);
        assertEquals("Laptop", item.getProducto());
        assertEquals(1, item.getCantidad());
        assertEquals(2500000, item.getPrecioUnitario(), 0.01);
    }
    
    @Test
    @DisplayName("ItemPedido - Calcular subtotal")
    public void testCalcularSubtotal() {
        ItemPedido item = new ItemPedido("Mouse", 3, 50000);
        assertEquals(150000, item.getSubtotal(), 0.01);
    }
    
    @Test
    @DisplayName("ItemPedido - ToString")
    public void testItemToString() {
        ItemPedido item = new ItemPedido("Teclado", 2, 75000);
        String resultado = item.toString();
        assertTrue(resultado.contains("Teclado"));
        assertTrue(resultado.contains("2"));
    }
    
    // ==================== Pedido Tests ====================
    
    @Test
    @DisplayName("Pedido - Crear pedido")
    public void testCrearPedido() {
        Pedido p = new Pedido("PED-002", "Juan Pérez");
        assertEquals("PED-002", p.getNumeroPedido());
        assertEquals("Juan Pérez", p.getCliente());
        assertEquals("PENDIENTE", p.getEstadoActual());
    }
    
    @Test
    @DisplayName("Pedido - Estado inicial es PENDIENTE")
    public void testEstadoInicialPendiente() {
        assertEquals("PENDIENTE", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("Pedido - Agregar items")
    public void testAgregarItems() {
        ItemPedido item1 = new ItemPedido("Producto A", 2, 10000);
        ItemPedido item2 = new ItemPedido("Producto B", 1, 5000);
        
        pedido.agregarItem(item1);
        pedido.agregarItem(item2);
        
        assertEquals(2, pedido.getItems().size());
        assertEquals(25000, pedido.getTotal(), 0.01);
    }
    
    @Test
    @DisplayName("Pedido - Calcular total automáticamente")
    public void testCalcularTotalAutomatico() {
        pedido.agregarItem(new ItemPedido("Item 1", 1, 10000));
        assertEquals(10000, pedido.getTotal(), 0.01);
        
        pedido.agregarItem(new ItemPedido("Item 2", 2, 5000));
        assertEquals(20000, pedido.getTotal(), 0.01);
    }
    
    @Test
    @DisplayName("Pedido - Historial registra estado inicial")
    public void testHistorialEstadoInicial() {
        List<String> historial = pedido.getHistorialEstados();
        assertEquals(1, historial.size());
        assertTrue(historial.get(0).contains("PENDIENTE"));
    }
    
    // ==================== PendienteState Tests ====================
    
    @Test
    @DisplayName("PendienteState - Procesar pedido cambia a PROCESANDO")
    public void testPendienteAProcesando() {
        pedido.procesar();
        assertEquals("PROCESANDO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("PendienteState - Cancelar pedido cambia a CANCELADO")
    public void testPendienteACancelado() {
        pedido.cancelar();
        assertEquals("CANCELADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("PendienteState - No puede confirmar pago")
    public void testPendienteNoConfirmarPago() {
        String estadoAntes = pedido.getEstadoActual();
        pedido.confirmarPago();
        assertEquals(estadoAntes, pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("PendienteState - No puede enviar")
    public void testPendienteNoEnviar() {
        String estadoAntes = pedido.getEstadoActual();
        pedido.enviar();
        assertEquals(estadoAntes, pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("PendienteState - No puede entregar")
    public void testPendienteNoEntregar() {
        String estadoAntes = pedido.getEstadoActual();
        pedido.entregar();
        assertEquals(estadoAntes, pedido.getEstadoActual());
    }
    
    // ==================== ProcesandoState Tests ====================
    
    @Test
    @DisplayName("ProcesandoState - Confirmar pago cambia a CONFIRMADO")
    public void testProcesandoAConfirmado() {
        pedido.procesar();
        pedido.confirmarPago();
        assertEquals("CONFIRMADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("ProcesandoState - Puede cancelar")
    public void testProcesandoPuedeCancelar() {
        pedido.procesar();
        pedido.cancelar();
        assertEquals("CANCELADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("ProcesandoState - No puede procesar de nuevo")
    public void testProcesandoNoProcesarDeNuevo() {
        pedido.procesar();
        String estadoAntes = pedido.getEstadoActual();
        pedido.procesar();
        assertEquals(estadoAntes, pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("ProcesandoState - No puede enviar sin confirmar pago")
    public void testProcesandoNoEnviarSinPago() {
        pedido.procesar();
        String estadoAntes = pedido.getEstadoActual();
        pedido.enviar();
        assertEquals(estadoAntes, pedido.getEstadoActual());
    }
    
    // ==================== ConfirmadoState Tests ====================
    
    @Test
    @DisplayName("ConfirmadoState - Enviar cambia a ENVIADO")
    public void testConfirmadoAEnviado() {
        pedido.procesar();
        pedido.confirmarPago();
        pedido.enviar();
        assertEquals("ENVIADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("ConfirmadoState - Puede cancelar con reembolso")
    public void testConfirmadoPuedeCancelar() {
        pedido.procesar();
        pedido.confirmarPago();
        pedido.cancelar();
        assertEquals("CANCELADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("ConfirmadoState - No puede confirmar pago de nuevo")
    public void testConfirmadoNoConfirmarDeNuevo() {
        pedido.procesar();
        pedido.confirmarPago();
        String estadoAntes = pedido.getEstadoActual();
        pedido.confirmarPago();
        assertEquals(estadoAntes, pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("ConfirmadoState - No puede entregar sin enviar")
    public void testConfirmadoNoEntregar() {
        pedido.procesar();
        pedido.confirmarPago();
        String estadoAntes = pedido.getEstadoActual();
        pedido.entregar();
        assertEquals(estadoAntes, pedido.getEstadoActual());
    }
    
    // ==================== EnviadoState Tests ====================
    
    @Test
    @DisplayName("EnviadoState - Entregar cambia a ENTREGADO")
    public void testEnviadoAEntregado() {
        pedido.procesar();
        pedido.confirmarPago();
        pedido.enviar();
        pedido.entregar();
        assertEquals("ENTREGADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("EnviadoState - No puede cancelar")
    public void testEnviadoNoPuedeCancelar() {
        pedido.procesar();
        pedido.confirmarPago();
        pedido.enviar();
        String estadoAntes = pedido.getEstadoActual();
        pedido.cancelar();
        assertEquals(estadoAntes, pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("EnviadoState - No puede enviar de nuevo")
    public void testEnviadoNoEnviarDeNuevo() {
        pedido.procesar();
        pedido.confirmarPago();
        pedido.enviar();
        String estadoAntes = pedido.getEstadoActual();
        pedido.enviar();
        assertEquals(estadoAntes, pedido.getEstadoActual());
    }
    
    // ==================== EntregadoState Tests ====================
    
    @Test
    @DisplayName("EntregadoState - No permite ninguna operación")
    public void testEntregadoNoPermiteOperaciones() {
        pedido.procesar();
        pedido.confirmarPago();
        pedido.enviar();
        pedido.entregar();
        
        String estadoFinal = pedido.getEstadoActual();
        
        pedido.procesar();
        assertEquals(estadoFinal, pedido.getEstadoActual());
        
        pedido.cancelar();
        assertEquals(estadoFinal, pedido.getEstadoActual());
        
        pedido.enviar();
        assertEquals(estadoFinal, pedido.getEstadoActual());
        
        pedido.entregar();
        assertEquals(estadoFinal, pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("EntregadoState - Es estado terminal")
    public void testEntregadoEstadoTerminal() {
        pedido.procesar();
        pedido.confirmarPago();
        pedido.enviar();
        pedido.entregar();
        
        assertEquals("ENTREGADO", pedido.getEstadoActual());
    }
    
    // ==================== CanceladoState Tests ====================
    
    @Test
    @DisplayName("CanceladoState - No permite ninguna operación")
    public void testCanceladoNoPermiteOperaciones() {
        pedido.cancelar();
        
        String estadoCancelado = pedido.getEstadoActual();
        
        pedido.procesar();
        assertEquals(estadoCancelado, pedido.getEstadoActual());
        
        pedido.confirmarPago();
        assertEquals(estadoCancelado, pedido.getEstadoActual());
        
        pedido.enviar();
        assertEquals(estadoCancelado, pedido.getEstadoActual());
        
        pedido.entregar();
        assertEquals(estadoCancelado, pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("CanceladoState - Es estado terminal")
    public void testCanceladoEstadoTerminal() {
        pedido.cancelar();
        assertEquals("CANCELADO", pedido.getEstadoActual());
    }
    
    // ==================== Transiciones de Estado Tests ====================
    
    @Test
    @DisplayName("Transición - Flujo completo exitoso")
    public void testFlujoCompletoExitoso() {
        assertEquals("PENDIENTE", pedido.getEstadoActual());
        
        pedido.procesar();
        assertEquals("PROCESANDO", pedido.getEstadoActual());
        
        pedido.confirmarPago();
        assertEquals("CONFIRMADO", pedido.getEstadoActual());
        
        pedido.enviar();
        assertEquals("ENVIADO", pedido.getEstadoActual());
        
        pedido.entregar();
        assertEquals("ENTREGADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("Transición - Cancelación desde PENDIENTE")
    public void testCancelacionDesdePendiente() {
        pedido.cancelar();
        assertEquals("CANCELADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("Transición - Cancelación desde PROCESANDO")
    public void testCancelacionDesdeProcesando() {
        pedido.procesar();
        pedido.cancelar();
        assertEquals("CANCELADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("Transición - Cancelación desde CONFIRMADO")
    public void testCancelacionDesdeConfirmado() {
        pedido.procesar();
        pedido.confirmarPago();
        pedido.cancelar();
        assertEquals("CANCELADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("Historial - Registra todas las transiciones")
    public void testHistorialRegistraTransiciones() {
        pedido.procesar();
        pedido.confirmarPago();
        pedido.enviar();
        
        List<String> historial = pedido.getHistorialEstados();
        assertEquals(4, historial.size()); // PENDIENTE + PROCESANDO + CONFIRMADO + ENVIADO
    }
}