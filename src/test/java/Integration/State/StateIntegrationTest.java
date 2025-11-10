package Integration.State;

import Comportamentales.State.modelo.*;
import Comportamentales.State.controlador.StateController;
import Comportamentales.State.vista.ConsolaState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DisplayName("State Pattern - Integration Tests")
public class StateIntegrationTest {
    
    private StateController controller;
    private ConsolaState vista;
    
    @BeforeEach
    public void setUp() {
        vista = new ConsolaState();
        controller = new StateController(vista);
    }
    
    // ==================== MVC Integration Tests ====================
    
    @Test
    @DisplayName("MVC - Controller crea pedido")
    public void testControllerCreaPedido() {
        Pedido pedido = controller.crearPedido("Cliente Test");
        
        assertNotNull(pedido);
        assertEquals("Cliente Test", pedido.getCliente());
        assertEquals("PENDIENTE", pedido.getEstadoActual());
        assertTrue(pedido.getNumeroPedido().startsWith("PED-"));
    }
    
    @Test
    @DisplayName("MVC - Controller agrega items")
    public void testControllerAgregaItems() {
        Pedido pedido = controller.crearPedido("Cliente Test");
        
        controller.agregarItemPedido(pedido, "Laptop", 1, 2500000);
        controller.agregarItemPedido(pedido, "Mouse", 2, 50000);
        
        assertEquals(2, pedido.getItems().size());
        assertEquals(2600000, pedido.getTotal(), 0.01);
    }
    
    @Test
    @DisplayName("MVC - Controller procesa pedido")
    public void testControllerProcesaPedido() {
        Pedido pedido = controller.crearPedido("Cliente Test");
        
        controller.procesarPedido(pedido);
        
        assertEquals("PROCESANDO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("MVC - Controller confirma pago")
    public void testControllerConfirmaPago() {
        Pedido pedido = controller.crearPedido("Cliente Test");
        controller.procesarPedido(pedido);
        
        controller.confirmarPago(pedido);
        
        assertEquals("CONFIRMADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("MVC - Controller envía pedido")
    public void testControllerEnviaPedido() {
        Pedido pedido = controller.crearPedido("Cliente Test");
        controller.procesarPedido(pedido);
        controller.confirmarPago(pedido);
        
        controller.enviarPedido(pedido);
        
        assertEquals("ENVIADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("MVC - Controller entrega pedido")
    public void testControllerEntregaPedido() {
        Pedido pedido = controller.crearPedido("Cliente Test");
        controller.procesarPedido(pedido);
        controller.confirmarPago(pedido);
        controller.enviarPedido(pedido);
        
        controller.entregarPedido(pedido);
        
        assertEquals("ENTREGADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("MVC - Controller cancela pedido")
    public void testControllerCancelaPedido() {
        Pedido pedido = controller.crearPedido("Cliente Test");
        
        controller.cancelarPedido(pedido);
        
        assertEquals("CANCELADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("MVC - Controller busca pedido")
    public void testControllerBuscaPedido() {
        Pedido pedido1 = controller.crearPedido("Cliente 1");
        Pedido pedido2 = controller.crearPedido("Cliente 2");
        
        Pedido encontrado = controller.buscarPedido(pedido1.getNumeroPedido());
        
        assertNotNull(encontrado);
        assertEquals(pedido1.getNumeroPedido(), encontrado.getNumeroPedido());
    }
    
    @Test
    @DisplayName("MVC - Controller obtiene pedidos por estado")
    public void testControllerObtienePorEstado() {
        Pedido pedido1 = controller.crearPedido("Cliente 1");
        Pedido pedido2 = controller.crearPedido("Cliente 2");
        Pedido pedido3 = controller.crearPedido("Cliente 3");
        
        controller.procesarPedido(pedido1);
        controller.procesarPedido(pedido2);
        
        List<Pedido> procesando = controller.obtenerPedidosPorEstado("PROCESANDO");
        List<Pedido> pendientes = controller.obtenerPedidosPorEstado("PENDIENTE");
        
        assertEquals(2, procesando.size());
        assertEquals(1, pendientes.size());
    }
    
    // ==================== Integración Estado-Pedido ====================
    
    @Test
    @DisplayName("Integración - Flujo completo con items")
    public void testFlujoCompletoConItems() {
        Pedido pedido = controller.crearPedido("Juan Pérez");
        
        controller.agregarItemPedido(pedido, "Producto A", 2, 100000);
        controller.agregarItemPedido(pedido, "Producto B", 1, 50000);
        
        double totalInicial = pedido.getTotal();
        assertEquals(250000, totalInicial, 0.01);
        
        controller.procesarPedido(pedido);
        controller.confirmarPago(pedido);
        controller.enviarPedido(pedido);
        controller.entregarPedido(pedido);
        
        assertEquals("ENTREGADO", pedido.getEstadoActual());
        assertEquals(totalInicial, pedido.getTotal(), 0.01); // Total no cambia
    }
    
    @Test
    @DisplayName("Integración - Múltiples pedidos independientes")
    public void testMultiplesPedidosIndependientes() {
        Pedido pedido1 = controller.crearPedido("Cliente 1");
        Pedido pedido2 = controller.crearPedido("Cliente 2");
        Pedido pedido3 = controller.crearPedido("Cliente 3");
        
        controller.procesarPedido(pedido1);
        controller.procesarPedido(pedido2);
        controller.cancelarPedido(pedido3);
        
        assertEquals("PROCESANDO", pedido1.getEstadoActual());
        assertEquals("PROCESANDO", pedido2.getEstadoActual());
        assertEquals("CANCELADO", pedido3.getEstadoActual());
    }
    
    @Test
    @DisplayName("Integración - Historial persiste entre operaciones")
    public void testHistorialPersiste() {
        Pedido pedido = controller.crearPedido("Cliente Test");
        
        controller.procesarPedido(pedido);
        controller.confirmarPago(pedido);
        controller.enviarPedido(pedido);
        
        List<String> historial = pedido.getHistorialEstados();
        
        assertEquals(4, historial.size());
        assertTrue(historial.get(0).contains("PENDIENTE"));
        assertTrue(historial.get(1).contains("PROCESANDO"));
        assertTrue(historial.get(2).contains("CONFIRMADO"));
        assertTrue(historial.get(3).contains("ENVIADO"));
    }
    
    // ==================== Escenarios de Negocio ====================
    
    @Test
    @DisplayName("Integración - Cliente cancela antes de envío")
    public void testClienteCancelaAntesDeEnvio() {
        Pedido pedido = controller.crearPedido("Cliente Indeciso");
        controller.agregarItemPedido(pedido, "Laptop", 1, 3000000);
        
        controller.procesarPedido(pedido);
        controller.confirmarPago(pedido);
        
        // Cliente se arrepiente antes del envío
        controller.cancelarPedido(pedido);
        
        assertEquals("CANCELADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("Integración - No puede cancelar después de envío")
    public void testNoPuedeCancelarDespuesEnvio() {
        Pedido pedido = controller.crearPedido("Cliente Test");
        
        controller.procesarPedido(pedido);
        controller.confirmarPago(pedido);
        controller.enviarPedido(pedido);
        
        String estadoAntes = pedido.getEstadoActual();
        controller.cancelarPedido(pedido);
        
        assertEquals(estadoAntes, pedido.getEstadoActual()); // No cambió
        assertEquals("ENVIADO", pedido.getEstadoActual());
    }
    
    @Test
    @DisplayName("Integración - Múltiples transiciones rápidas")
    public void testMultiplesTransicionesRapidas() {
        Pedido pedido = controller.crearPedido("Express Delivery");
        controller.agregarItemPedido(pedido, "Producto Urgente", 1, 100000);
        
        // Transiciones rápidas
        controller.procesarPedido(pedido);
        assertEquals("PROCESANDO", pedido.getEstadoActual());
        
        controller.confirmarPago(pedido);
        assertEquals("CONFIRMADO", pedido.getEstadoActual());
        
        controller.enviarPedido(pedido);
        assertEquals("ENVIADO", pedido.getEstadoActual());
        
        controller.entregarPedido(pedido);
        assertEquals("ENTREGADO", pedido.getEstadoActual());
        
        // Verificar integridad del historial
        assertEquals(5, pedido.getHistorialEstados().size());
    }
}