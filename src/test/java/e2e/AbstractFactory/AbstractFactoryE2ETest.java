package e2e.AbstractFactory;

import Creacionales.AbstractFactory.controlador.VentaControlador;
import Creacionales.AbstractFactory.modelo.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Abstract Factory Pattern - Tests End-to-End (POS)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AbstractFactoryE2ETest {

    private VentaControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new VentaControlador();
    }

    @Test
    @Order(1)
    @DisplayName("E2E: Compra simple en punto de venta")
    void testCompraSimplePOS() {
        
        DatosVenta venta = controlador.crearVenta("POS", "C001", "José Ramírez");
        venta.agregarItem(new ItemVenta("PROD-001", "Arroz 1kg", 2, 3500));
        venta.agregarItem(new ItemVenta("PROD-002", "Aceite 1L", 1, 8500));
        venta.agregarItem(new ItemVenta("PROD-003", "Sal 500g", 1, 1500));
        
        venta.setMetodoPago("EFECTIVO");
        
        boolean procesada = controlador.procesarVenta("POS", venta);
        
        assertAll("Verificar compra simple POS",
            () -> assertTrue(procesada),
            () -> assertEquals(17000.0, venta.getSubtotal(), 0.01),
            () -> assertEquals(20230.0, venta.getTotal(), 0.01),
            () -> assertEquals("EFECTIVO", venta.getMetodoPago())
        );
    }

    @Test
    @Order(2)
    @DisplayName("E2E: Cliente intenta comprar sin stock suficiente")
    void testComprasinStockPOS() {
        
        DatosVenta venta = controlador.crearVenta("POS", "C002", "María Torres");
        venta.agregarItem(new ItemVenta("PROD-001", "Producto Agotado", 1000, 5000));
        venta.setMetodoPago("TARJETA");
        
        boolean procesada = controlador.procesarVenta("POS", venta);
        
        assertFalse(procesada, "No debe procesar venta sin stock");
    }

    @Test
    @Order(3)
    @DisplayName("E2E: Compra de mercado familiar")
    void testMercadoFamiliar() {
        
        DatosVenta venta = controlador.crearVenta("POS", "C003", "Familia González");
        venta.agregarItem(new ItemVenta("PROD-001", "Leche x6", 6, 3500));
        venta.agregarItem(new ItemVenta("PROD-002", "Pan tajado x3", 3, 4500));
        venta.agregarItem(new ItemVenta("PROD-003", "Huevos x30", 2, 8000));
        venta.agregarItem(new ItemVenta("PROD-004", "Pollo entero", 1, 15000));
        venta.agregarItem(new ItemVenta("PROD-005", "Frutas kg", 5, 3000));
        venta.setMetodoPago("TARJETA_DEBITO");
        
        boolean procesada = controlador.procesarVenta("POS", venta);
        
        assertAll("Verificar mercado familiar",
            () -> assertTrue(procesada),
            () -> assertTrue(venta.getSubtotal() > 60000),
            () -> assertEquals(5, venta.getItems().size())
        );
    }

    @Test
    @Order(4)
    @DisplayName("E2E: Devolución inmediata - mismo día")
    void testDevolucionInmediata() {
        DatosVenta venta = controlador.crearVenta("POS", "C004", "Pedro Sánchez");
        venta.agregarItem(new ItemVenta("PROD-001", "Producto", 1, 50000));
        venta.setMetodoPago("EFECTIVO");
        controlador.procesarVenta("POS", venta);
        
        boolean permitida = controlador.procesarDevolucion("POS", venta.getNumeroVenta(), 0, venta.getTotal());
        
        VentaComponentFactory factory = controlador.obtenerFactory("POS");
        GestorDevolucion gestor = factory.crearGestorDevolucion();
        double reembolso = gestor.calcularReembolso(venta.getTotal(), 0);
        
        assertAll("Verificar devolución inmediata",
            () -> assertTrue(permitida),
            () -> assertEquals(venta.getTotal(), reembolso, 0.01, "Reembolso completo mismo día")
        );
    }

    @Test
    @Order(5)
    @DisplayName("E2E: Devolución a los 7 días")
    void testDevolucion7Dias() {
        
        DatosVenta venta = controlador.crearVenta("POS", "C005", "Ana Martínez");
        venta.agregarItem(new ItemVenta("PROD-001", "Producto", 1, 100000));
        venta.setMetodoPago("TARJETA");
        controlador.procesarVenta("POS", venta);
        
        VentaComponentFactory factory = controlador.obtenerFactory("POS");
        GestorDevolucion gestor = factory.crearGestorDevolucion();
        
        boolean permitida = gestor.permitirDevolucion(venta.getNumeroVenta(), 7);
        double reembolso = gestor.calcularReembolso(venta.getTotal(), 7);
        
        assertAll("Verificar devolución día 7",
            () -> assertTrue(permitida),
            () -> assertEquals(venta.getTotal(), reembolso, 0.01)
        );
    }

    @Test
    @Order(6)
    @DisplayName("E2E: Devolución a los 10 días con penalización")
    void testDevolucion10DiasConPenalizacion() {
        // Simulate: Cliente devuelve después de 7 días
        
        DatosVenta venta = controlador.crearVenta("POS", "C006", "Carlos Ruiz");
        venta.agregarItem(new ItemVenta("PROD-001", "Producto", 1, 100000));
        venta.setMetodoPago("EFECTIVO");
        controlador.procesarVenta("POS", venta);
        
        VentaComponentFactory factory = controlador.obtenerFactory("POS");
        GestorDevolucion gestor = factory.crearGestorDevolucion();
        
        boolean permitida = gestor.permitirDevolucion(venta.getNumeroVenta(), 10);
        double reembolso = gestor.calcularReembolso(venta.getTotal(), 10);
        
        assertAll("Verificar devolución con penalización",
            () -> assertTrue(permitida),
            () -> assertTrue(reembolso < venta.getTotal(), "Debe tener penalización"),
            () -> assertTrue(reembolso > 0, "Debe haber reembolso parcial")
        );
    }

    @Test
    @Order(7)
    @DisplayName("E2E: Devolución rechazada después de 15 días")
    void testDevolucionRechazada() {
        // Simulate: Cliente intenta devolver fuera del plazo
        
        DatosVenta venta = controlador.crearVenta("POS", "C007", "Laura Díaz");
        venta.agregarItem(new ItemVenta("PROD-001", "Producto", 1, 50000));
        venta.setMetodoPago("TARJETA");
        controlador.procesarVenta("POS", venta);
        
        boolean permitida = controlador.procesarDevolucion("POS", venta.getNumeroVenta(), 20, venta.getTotal());
        
        assertFalse(permitida, "No debe permitir devolución después de 15 días");
    }
    @Test
    @Order(8)
    @DisplayName("E2E: Día ocupado en caja - múltiples clientes")
    void testDiaOcupadoCaja() {
        // Simulate: Sábado con mucho tráfico
        
        AtomicInteger clientesAtendidos = new AtomicInteger(0);
        
        for (int i = 1; i <= 10; i++) {
            DatosVenta venta = controlador.crearVenta("POS", "CSAB" + i, "Cliente Sábado " + i);
            
            // Diferentes tipos de compras
            if (i % 3 == 0) {
                // Compra pequeña
                venta.agregarItem(new ItemVenta("PROD-001", "Pan", 1, 3000));
                venta.agregarItem(new ItemVenta("PROD-002", "Leche", 1, 3500));
            } else if (i % 2 == 0) {
                // Compra mediana
                venta.agregarItem(new ItemVenta("PROD-003", "Arroz", 2, 3500));
                venta.agregarItem(new ItemVenta("PROD-004", "Aceite", 1, 8500));
                venta.agregarItem(new ItemVenta("PROD-005", "Pollo", 1, 15000));
            } else {
                // Compra grande
                venta.agregarItem(new ItemVenta("PROD-001", "Varios productos", 5, 5000));
            }
            
            venta.setMetodoPago(i % 2 == 0 ? "TARJETA" : "EFECTIVO");
            
            if (controlador.procesarVenta("POS", venta)) {
                clientesAtendidos.incrementAndGet();
            }
        }
        
        assertAll("Verificar día ocupado",
            () -> assertEquals(10, clientesAtendidos.get()),
            () -> assertTrue(controlador.calcularTotalVentas() > 100000)
        );
    }

    @Test
    @Order(9)
    @DisplayName("E2E: Cliente fiel - múltiples compras")
    void testClienteFiel() {
        // Simulate: Cliente que compra regularmente
        
        String clienteId = "C-FIEL-001";
        String clienteNombre = "Roberto Hernández";
        
        // Compra 1: Lunes
        DatosVenta compra1 = controlador.crearVenta("POS", clienteId, clienteNombre);
        compra1.agregarItem(new ItemVenta("PROD-001", "Leche", 2, 3500));
        compra1.setMetodoPago("EFECTIVO");
        controlador.procesarVenta("POS", compra1);
        
        // Compra 2: Miércoles
        DatosVenta compra2 = controlador.crearVenta("POS", clienteId, clienteNombre);
        compra2.agregarItem(new ItemVenta("PROD-002", "Pan", 3, 3000));
        compra2.setMetodoPago("TARJETA");
        controlador.procesarVenta("POS", compra2);
        
        // Compra 3: Sábado (mercado)
        DatosVenta compra3 = controlador.crearVenta("POS", clienteId, clienteNombre);
        compra3.agregarItem(new ItemVenta("PROD-003", "Frutas", 3, 4000));
        compra3.agregarItem(new ItemVenta("PROD-004", "Verduras", 2, 3500));
        compra3.agregarItem(new ItemVenta("PROD-005", "Carne", 1, 20000));
        compra3.setMetodoPago("TARJETA");
        controlador.procesarVenta("POS", compra3);
        
        List<DatosVenta> ventas = controlador.obtenerVentas();
        long ventasCliente = ventas.stream()
            .filter(v -> v.getClienteId().equals(clienteId))
            .count();
        
        assertEquals(3, ventasCliente, "Cliente debe tener 3 compras");
    }

    @Test
    @Order(10)
    @DisplayName("E2E: Escenario completo de operación diaria")
    void testEscenarioCompletoDiario() {
        // Simulate: Día completo de operación en punto de venta
        
        System.out.println("\n=== OPERACIÓN DIARIA POS ===\n");
        
        // Apertura de caja - 8:00 AM
        System.out.println("08:00 - Apertura de caja");
        
        // Primera venta del día
        DatosVenta venta1 = controlador.crearVenta("POS", "C-MAT-001", "Cliente Matutino");
        venta1.agregarItem(new ItemVenta("PROD-001", "Pan fresco", 2, 3000));
        venta1.agregarItem(new ItemVenta("PROD-002", "Café", 1, 8000));
        venta1.setMetodoPago("EFECTIVO");
        controlador.procesarVenta("POS", venta1);
        System.out.println("✓ Venta 1 procesada: $" + venta1.getTotal());
        
        // Hora pico - 12:00 PM
        System.out.println("\n12:00 - Hora pico del mediodía");
        for (int i = 1; i <= 5; i++) {
            DatosVenta venta = controlador.crearVenta("POS", "C-MID-" + i, "Cliente " + i);
            venta.agregarItem(new ItemVenta("PROD-00" + i, "Almuerzo " + i, 1, 12000));
            venta.setMetodoPago(i % 2 == 0 ? "TARJETA" : "EFECTIVO");
            controlador.procesarVenta("POS", venta);
            System.out.println("✓ Venta mediodía " + i + " procesada");
        }
        
        // Tarde - Compras grandes
        System.out.println("\n16:00 - Compras de tarde");
        DatosVenta ventaTarde = controlador.crearVenta("POS", "C-TAR-001", "Familia Tarde");
        ventaTarde.agregarItem(new ItemVenta("PROD-001", "Arroz 5kg", 1, 15000));
        ventaTarde.agregarItem(new ItemVenta("PROD-002", "Aceite 2L", 2, 17000));
        ventaTarde.agregarItem(new ItemVenta("PROD-003", "Azúcar 2kg", 1, 8000));
        ventaTarde.agregarItem(new ItemVenta("PROD-004", "Sal", 1, 2000));
        ventaTarde.setMetodoPago("TARJETA_CREDITO");
        controlador.procesarVenta("POS", ventaTarde);
        System.out.println("✓ Venta grande tarde procesada: $" + ventaTarde.getTotal());
        
        // Intento de compra sin stock
        System.out.println("\n17:30 - Cliente sin stock disponible");
        DatosVenta ventaSinStock = controlador.crearVenta("POS", "C-SIN", "Cliente Sin Stock");
        ventaSinStock.agregarItem(new ItemVenta("PROD-001", "Producto agotado", 500, 1000));
        ventaSinStock.setMetodoPago("EFECTIVO");
        boolean procesada = controlador.procesarVenta("POS", ventaSinStock);
        System.out.println(procesada ? "✓ Procesada" : "✗ Rechazada por falta de stock");
        
        // Devolución
        System.out.println("\n18:00 - Cliente con devolución");
        boolean devolucion = controlador.procesarDevolucion("POS", venta1.getNumeroVenta(), 1, venta1.getTotal());
        System.out.println(devolucion ? "✓ Devolución aprobada" : "✗ Devolución rechazada");
        
        // Cierre de caja - 20:00 PM
        System.out.println("\n20:00 - Cierre de caja");
        double totalDia = controlador.calcularTotalVentas();
        int cantidadVentas = controlador.obtenerVentas().size();
        
        System.out.println("\n--- RESUMEN DEL DÍA ---");
        System.out.println("Total de ventas: " + cantidadVentas);
        System.out.printf("Monto total: $%.2f%n", totalDia);
        System.out.println("========================\n");
        
        // Assertions finales
        assertAll("Verificar operación diaria completa",
            () -> assertTrue(cantidadVentas >= 7, "Debe haber al menos 7 ventas"),
            () -> assertTrue(totalDia > 100000, "Total debe superar $100,000"),
            () -> assertFalse(procesada, "Venta sin stock debe rechazarse"),
            () -> assertTrue(devolucion, "Devolución temprana debe aprobarse")
        );
    }
}
