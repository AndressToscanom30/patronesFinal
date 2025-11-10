package e2e.State;

import Comportamentales.State.controlador.StateController;
import Comportamentales.State.modelo.*;
import Comportamentales.State.vista.ConsolaState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;


@DisplayName("State Pattern - End-to-End Tests")
public class StateE2ETest {
    private StateController controller;
    private ConsolaState vista;

    @BeforeEach
    public void setUp() {
        vista = new ConsolaState();
        controller = new StateController(vista);
    }

    @Test
    @DisplayName("E2E - Cliente realiza pedido exitoso completo")
    public void testPedidoExitosoCompleto() {
        System.out.println("\n=== ESCENARIO E2E: PEDIDO EXITOSO COMPLETO ===\n");
        
        // Paso 1: Cliente crea pedido
        System.out.println("═".repeat(50));
        System.out.println("PASO 1: CLIENTE CREA PEDIDO");
        System.out.println("═".repeat(50));
        
        Pedido pedido = controller.crearPedido("María González");
        System.out.println("Cliente: " + pedido.getCliente());
        System.out.println("Número de pedido: " + pedido.getNumeroPedido());
        System.out.println("Estado inicial: " + pedido.getEstadoActual());
        
        assertEquals("PENDIENTE", pedido.getEstadoActual());
        pausa();
        
        // Paso 2: Agregar productos
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 2: AGREGAR PRODUCTOS");
        System.out.println("═".repeat(50));
        
        controller.agregarItemPedido(pedido, "Smart TV 55\"", 1, 2500000);
        System.out.println("✓ Smart TV 55\" agregado");
        
        controller.agregarItemPedido(pedido, "Soundbar", 1, 500000);
        System.out.println("✓ Soundbar agregado");
        
        controller.agregarItemPedido(pedido, "Cable HDMI", 2, 25000);
        System.out.println("✓ 2x Cable HDMI agregado");
        
        System.out.printf("\nTotal del pedido: $%.2f%n", pedido.getTotal());
        assertEquals(3050000, pedido.getTotal(), 0.01);
        pausa();
        
        // Paso 3: Procesar pedido
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 3: SISTEMA PROCESA PEDIDO");
        System.out.println("═".repeat(50));
        
        controller.procesarPedido(pedido);
        assertEquals("PROCESANDO", pedido.getEstadoActual());
        pausa();
        
        // Paso 4: Cliente confirma pago
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 4: CONFIRMAR PAGO");
        System.out.println("═".repeat(50));
        
        controller.confirmarPago(pedido);
        assertEquals("CONFIRMADO", pedido.getEstadoActual());
        pausa();
        
        // Paso 5: Enviar pedido
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 5: ENVÍO DEL PEDIDO");
        System.out.println("═".repeat(50));
        
        controller.enviarPedido(pedido);
        assertEquals("ENVIADO", pedido.getEstadoActual());
        pausa();
        
        // Paso 6: Entregar pedido
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 6: ENTREGA AL CLIENTE");
        System.out.println("═".repeat(50));
        
        controller.entregarPedido(pedido);
        assertEquals("ENTREGADO", pedido.getEstadoActual());
        pausa();
        
        // Resumen final
        System.out.println("\n═".repeat(50));
        System.out.println("RESUMEN FINAL DEL PEDIDO");
        System.out.println("═".repeat(50));
        System.out.println("Pedido: " + pedido.getNumeroPedido());
        System.out.println("Cliente: " + pedido.getCliente());
        System.out.println("Estado: " + pedido.getEstadoActual());
        System.out.printf("Total: $%.2f%n", pedido.getTotal());
        System.out.println("Items: " + pedido.getItems().size());
        
        System.out.println("\nHistorial de estados:");
        for (String estado : pedido.getHistorialEstados()) {
            System.out.println("  " + estado);
        }
        
        System.out.println("\n✓ Pedido completado exitosamente");
    }

    @Test
    @DisplayName("E2E - Cliente cancela pedido antes de envío")
    public void testClienteCancelaPedido() {
        System.out.println("\n=== ESCENARIO E2E: CANCELACIÓN DE PEDIDO ===\n");
        
        System.out.println("Cliente crea pedido...");
        Pedido pedido = controller.crearPedido("Carlos Ruiz");
        
        controller.agregarItemPedido(pedido, "Laptop", 1, 3500000);
        controller.agregarItemPedido(pedido, "Mochila", 1, 150000);
        
        System.out.printf("Total: $%.2f%n", pedido.getTotal());
        pausa();
        
        System.out.println("\nSistema procesa el pedido...");
        controller.procesarPedido(pedido);
        System.out.println("Estado: " + pedido.getEstadoActual());
        pausa();
        
        System.out.println("\nCliente confirma el pago...");
        controller.confirmarPago(pedido);
        System.out.println("Estado: " + pedido.getEstadoActual());
        pausa();
        
        System.out.println("\n¡Cliente se arrepiente!");
        System.out.println("Solicitando cancelación...");
        controller.cancelarPedido(pedido);
        
        System.out.println("\n═".repeat(50));
        System.out.println("Estado final: " + pedido.getEstadoActual());
        System.out.println("Reembolso procesándose...");
        System.out.println("═".repeat(50));
        
        assertEquals("CANCELADO", pedido.getEstadoActual());
        
        System.out.println("\n✓ Cancelación procesada correctamente");
    }

    @Test
    @DisplayName("E2E - Intento de cancelación después de envío")
    public void testIntentoCancelacionDespuesEnvio() {
        System.out.println("\n=== ESCENARIO E2E: NO PUEDE CANCELAR DESPUÉS DE ENVÍO ===\n");
        
        Pedido pedido = controller.crearPedido("Ana Martínez");
        controller.agregarItemPedido(pedido, "Smartphone", 1, 1500000);
        
        System.out.println("Procesando pedido completo...");
        controller.procesarPedido(pedido);
        controller.confirmarPago(pedido);
        controller.enviarPedido(pedido);
        
        System.out.println("Estado actual: " + pedido.getEstadoActual());
        pausa();
        
        System.out.println("\nCliente intenta cancelar el pedido...");
        String estadoAntes = pedido.getEstadoActual();
        controller.cancelarPedido(pedido);
        String estadoDespues = pedido.getEstadoActual();
        
        System.out.println("\n═".repeat(50));
        System.out.println("Resultado: Cancelación NO permitida");
        System.out.println("Estado sigue siendo: " + estadoDespues);
        System.out.println("═".repeat(50));
        
        assertEquals(estadoAntes, estadoDespues);
        assertEquals("ENVIADO", pedido.getEstadoActual());
        
        System.out.println("\n✓ Sistema previno cancelación incorrecta");
    }

    @Test
    @DisplayName("E2E - Múltiples pedidos en paralelo")
    public void testMultiplesPedidosParalelo() {
        System.out.println("\n=== ESCENARIO E2E: MÚLTIPLES PEDIDOS SIMULTÁNEOS ===\n");
        
        System.out.println("Sistema de supermercado recibiendo múltiples pedidos...\n");
        
        // Pedido 1: Flujo completo
        System.out.println("─".repeat(50));
        System.out.println("PEDIDO 1: Cliente A - Compra normal");
        System.out.println("─".repeat(50));
        Pedido pedido1 = controller.crearPedido("Cliente A");
        controller.agregarItemPedido(pedido1, "Arroz", 2, 10000);
        controller.procesarPedido(pedido1);
        controller.confirmarPago(pedido1);
        controller.enviarPedido(pedido1);
        controller.entregarPedido(pedido1);
        System.out.println("Estado final: " + pedido1.getEstadoActual());
        pausa();
        
        // Pedido 2: Se cancela en procesamiento
        System.out.println("\n─".repeat(50));
        System.out.println("PEDIDO 2: Cliente B - Cancela en procesamiento");
        System.out.println("─".repeat(50));
        Pedido pedido2 = controller.crearPedido("Cliente B");
        controller.agregarItemPedido(pedido2, "Aceite", 1, 25000);
        controller.procesarPedido(pedido2);
        controller.cancelarPedido(pedido2);
        System.out.println("Estado final: " + pedido2.getEstadoActual());
        pausa();
        
        // Pedido 3: En tránsito
        System.out.println("\n─".repeat(50));
        System.out.println("PEDIDO 3: Cliente C - En tránsito");
        System.out.println("─".repeat(50));
        Pedido pedido3 = controller.crearPedido("Cliente C");
        controller.agregarItemPedido(pedido3, "Café", 3, 15000);
        controller.procesarPedido(pedido3);
        controller.confirmarPago(pedido3);
        controller.enviarPedido(pedido3);
        System.out.println("Estado final: " + pedido3.getEstadoActual());
        pausa();
        
        // Pedido 4: Recién creado
        System.out.println("\n─".repeat(50));
        System.out.println("PEDIDO 4: Cliente D - Recién creado");
        System.out.println("─".repeat(50));
        Pedido pedido4 = controller.crearPedido("Cliente D");
        controller.agregarItemPedido(pedido4, "Pan", 5, 5000);
        System.out.println("Estado final: " + pedido4.getEstadoActual());
        
        // Resumen
        System.out.println("\n═".repeat(50));
        System.out.println("RESUMEN DE PEDIDOS");
        System.out.println("═".repeat(50));
        System.out.println("Pedido 1: " + pedido1.getEstadoActual());
        System.out.println("Pedido 2: " + pedido2.getEstadoActual());
        System.out.println("Pedido 3: " + pedido3.getEstadoActual());
        System.out.println("Pedido 4: " + pedido4.getEstadoActual());
        
        assertEquals("ENTREGADO", pedido1.getEstadoActual());
        assertEquals("CANCELADO", pedido2.getEstadoActual());
        assertEquals("ENVIADO", pedido3.getEstadoActual());
        assertEquals("PENDIENTE", pedido4.getEstadoActual());
        
        System.out.println("\n✓ Sistema maneja múltiples pedidos correctamente");
    }

    @Test
    @DisplayName("E2E - Validación de transiciones inválidas")
    public void testValidacionTransicionesInvalidas() {
        System.out.println("\n=== ESCENARIO E2E: VALIDACIÓN DE TRANSICIONES ===\n");
        
        Pedido pedido = controller.crearPedido("Cliente Prueba");
        controller.agregarItemPedido(pedido, "Producto Test", 1, 50000);
        
        System.out.println("Estado inicial: " + pedido.getEstadoActual());
        
        // Intentar operaciones inválidas
        System.out.println("\n--- Intentando operaciones inválidas ---");
        
        System.out.println("\n1. Intentar confirmar pago sin procesar:");
        String estado1 = pedido.getEstadoActual();
        controller.confirmarPago(pedido);
        assertEquals(estado1, pedido.getEstadoActual());
        System.out.println("   ✓ Bloqueado correctamente");
        
        System.out.println("\n2. Intentar enviar sin procesar:");
        String estado2 = pedido.getEstadoActual();
        controller.enviarPedido(pedido);
        assertEquals(estado2, pedido.getEstadoActual());
        System.out.println("   ✓ Bloqueado correctamente");
        
        System.out.println("\n3. Intentar entregar sin enviar:");
        String estado3 = pedido.getEstadoActual();
        controller.entregarPedido(pedido);
        assertEquals(estado3, pedido.getEstadoActual());
        System.out.println("   ✓ Bloqueado correctamente");
        
        System.out.println("\n═".repeat(50));
        System.out.println("Sistema validó todas las transiciones inválidas");
        System.out.println("Estado final: " + pedido.getEstadoActual());
        System.out.println("═".repeat(50));
        
        assertEquals("PENDIENTE", pedido.getEstadoActual());
        
        System.out.println("\n✓ Validaciones de estado funcionando correctamente");
    }

    @Test
    @DisplayName("E2E - Seguimiento completo de pedido")
    public void testSeguimientoCompletoPedido() {
        System.out.println("\n=== ESCENARIO E2E: SEGUIMIENTO DE PEDIDO ===\n");
        
        System.out.println("Cliente hace un pedido y rastrea su estado...\n");
        
        Pedido pedido = controller.crearPedido("Pedro López");
        controller.agregarItemPedido(pedido, "Televisor", 1, 2000000);
        
        System.out.println("═".repeat(50));
        System.out.println("TRACKING DEL PEDIDO: " + pedido.getNumeroPedido());
        System.out.println("═".repeat(50));
        
        System.out.println("\n[DÍA 1] Pedido creado");
        System.out.println("Estado: " + pedido.getEstadoActual());
        pausa();
        
        controller.procesarPedido(pedido);
        System.out.println("\n[DÍA 1] Pedido en procesamiento");
        System.out.println("Estado: " + pedido.getEstadoActual());
        pausa();
        
        controller.confirmarPago(pedido);
        System.out.println("\n[DÍA 2] Pago confirmado");
        System.out.println("Estado: " + pedido.getEstadoActual());
        pausa();
        
        controller.enviarPedido(pedido);
        System.out.println("\n[DÍA 3] Pedido enviado");
        System.out.println("Estado: " + pedido.getEstadoActual());
        System.out.println("Tiempo estimado de entrega: 2-3 días");
        pausa();
        
        controller.entregarPedido(pedido);
        System.out.println("\n[DÍA 5] Pedido entregado");
        System.out.println("Estado: " + pedido.getEstadoActual());
        
        System.out.println("\n═".repeat(50));
        System.out.println("HISTORIAL COMPLETO DE ESTADOS");
        System.out.println("═".repeat(50));
        
        int paso = 1;
        for (String estado : pedido.getHistorialEstados()) {
            System.out.println(paso++ + ". " + estado);
        }
        
        assertEquals(5, pedido.getHistorialEstados().size());
        assertEquals("ENTREGADO", pedido.getEstadoActual());
        
        System.out.println("\n✓ Cliente puede rastrear todo el ciclo de vida del pedido");
    }

    @Test
    @DisplayName("E2E - Día completo en el supermercado")
    public void testDiaCompletoSupermercado() {
        System.out.println("\n=== ESCENARIO E2E: DÍA COMPLETO EN EL SUPERMERCADO ===\n");
        
        System.out.println("Simulando un día de operaciones...\n");
        
        System.out.println("═".repeat(50));
        System.out.println("9:00 AM - APERTURA");
        System.out.println("═".repeat(50));
        
        List<Pedido> pedidosDia = new ArrayList<>();
        
        // Pedidos matutinos
        System.out.println("\n--- Pedidos de la mañana ---");
        for (int i = 1; i <= 3; i++) {
            Pedido p = controller.crearPedido("Cliente Mañana " + i);
            controller.agregarItemPedido(p, "Desayuno", 1, 20000);
            controller.procesarPedido(p);
            controller.confirmarPago(p);
            pedidosDia.add(p);
            System.out.println("Pedido " + i + ": Procesado");
        }
        
        pausa();
        
        // Pedidos medio día
        System.out.println("\n--- Pedidos del mediodía ---");
        for (int i = 4; i <= 7; i++) {
            Pedido p = controller.crearPedido("Cliente Mediodía " + (i-3));
            controller.agregarItemPedido(p, "Almuerzo", 1, 35000);
            controller.procesarPedido(p);
            controller.confirmarPago(p);
            controller.enviarPedido(p);
            pedidosDia.add(p);
            System.out.println("Pedido " + i + ": Enviado");
        }
        
        pausa();
        
        // Pedidos tarde
        System.out.println("\n--- Pedidos de la tarde ---");
        for (int i = 8; i <= 10; i++) {
            Pedido p = controller.crearPedido("Cliente Tarde " + (i-7));
            controller.agregarItemPedido(p, "Cena", 1, 40000);
            controller.procesarPedido(p);
            controller.confirmarPago(p);
            controller.enviarPedido(p);
            controller.entregarPedido(p);
            pedidosDia.add(p);
            System.out.println("Pedido " + i + ": Entregado");
        }
        
        // Resumen del día
        System.out.println("\n═".repeat(50));
        System.out.println("CIERRE DEL DÍA - ESTADÍSTICAS");
        System.out.println("═".repeat(50));
        
        int confirmados = 0, enviados = 0, entregados = 0;
        double totalVentas = 0;
        
        for (Pedido p : pedidosDia) {
            totalVentas += p.getTotal();
            String estado = p.getEstadoActual();
            if (estado.equals("CONFIRMADO")) confirmados++;
            else if (estado.equals("ENVIADO")) enviados++;
            else if (estado.equals("ENTREGADO")) entregados++;
        }
        
        System.out.println("Total de pedidos: " + pedidosDia.size());
        System.out.println("Confirmados: " + confirmados);
        System.out.println("Enviados: " + enviados);
        System.out.println("Entregados: " + entregados);
        System.out.printf("Total de ventas: $%.2f%n", totalVentas);
        
        assertEquals(10, pedidosDia.size());
        assertTrue(totalVentas > 0);
        
        System.out.println("\n✓ Día completo de operaciones finalizado");
    }

    private void pausa() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}