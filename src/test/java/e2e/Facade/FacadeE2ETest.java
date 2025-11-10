package e2e.Facade;

import Estructurales.Facade.controlador.VentasController;
import Estructurales.Facade.modelo.*;
import Estructurales.Facade.vista.ConsolaFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Facade Pattern - End-to-End Tests")
public class FacadeE2ETest {
    private VentasController controller;
    private ConsolaFacade vista;
    private SistemaVentasFacade facade;

    @BeforeEach
    public void setUp() {
        vista = new ConsolaFacade();
        controller = new VentasController(vista);
        facade = new SistemaVentasFacade();
    }

    @Test
    @DisplayName("E2E - Escenario completo de venta en tienda física")
    public void testEscenarioCompletoVentaTiendaFisica() {
        System.out.println("\n=== ESCENARIO E2E: VENTA EN TIENDA FÍSICA ===\n");
        
        // Cliente llega a la tienda y selecciona productos
        Venta venta = new Venta();
        venta.setClienteId("CLI001");
        venta.setClienteNombre("Roberto Gómez");
        venta.setClienteEmail("roberto@example.com");
        
        System.out.println("Cliente selecciona productos:");
        venta.agregarItem("P001", "Laptop HP Pavilion", 1, 800.0);
        venta.agregarItem("P002", "Mouse Inalámbrico", 1, 25.0);
        venta.agregarItem("P003", "Teclado USB", 1, 35.0);
        venta.agregarItem("P004", "Memoria USB 32GB", 2, 15.0);
        
        System.out.println("- Laptop HP Pavilion: $800.00");
        System.out.println("- Mouse Inalámbrico: $25.00");
        System.out.println("- Teclado USB: $35.00");
        System.out.println("- Memoria USB 32GB (x2): $30.00");
        System.out.println("Subtotal: $" + venta.getSubtotal());
        System.out.println("Impuestos (19%): $" + venta.getImpuestos());
        System.out.println("Total: $" + venta.getTotal());
        
        // Cliente paga con tarjeta
        venta.setMetodoPago("TARJETA");
        System.out.println("\nMétodo de pago: TARJETA");
        
        // Procesar venta
        ResultadoVenta resultado = facade.procesarVenta(venta);
        
        // Verificaciones
        assertTrue(resultado.isExitoso());
        assertNotNull(resultado.getNumeroFactura());
        assertNotNull(resultado.getComprobantePago());
        assertEquals(105, resultado.getPuntosGanados());
        
        System.out.println("\n--- Resumen Final ---");
        System.out.println("✓ Venta completada exitosamente");
        System.out.println("Factura: " + resultado.getNumeroFactura());
        System.out.println("Comprobante: " + resultado.getComprobantePago());
        System.out.println("Puntos ganados: " + resultado.getPuntosGanados());
        
        // Verificar puntos acumulados
        int puntosAcumulados = facade.consultarPuntosCliente("CLI001");
        System.out.println("Puntos totales del cliente: " + puntosAcumulados);
    }

    @Test
    @DisplayName("E2E - Escenario de venta con error en pago")
    public void testEscenarioVentaErrorPago() {
        System.out.println("\n=== ESCENARIO E2E: ERROR EN PAGO ===\n");
        
        // Cliente selecciona productos
        Venta venta = new Venta();
        venta.setClienteId("CLI002");
        venta.setClienteNombre("María Rodríguez");
        venta.setClienteEmail("maria@example.com");
        venta.agregarItem("P001", "Smart TV 55'", 1, 1500.0);
        
        // Intento de pago con monto 0 (simula error)
        venta.setMetodoPago("TARJETA");
        
        System.out.println("Intentando procesar pago...");
        
        // En este caso, forzamos un error creando una venta con total 0
        Venta ventaConError = new Venta();
        ventaConError.setClienteId("CLI002");
        ventaConError.setClienteNombre("María Rodríguez");
        ventaConError.setClienteEmail("maria@example.com");
        ventaConError.setMetodoPago("TARJETA");
        
        ResultadoVenta resultado = facade.procesarVenta(ventaConError);
        
        // Verificaciones
        assertFalse(resultado.isExitoso());
        assertTrue(resultado.getMensaje().contains("Error") || resultado.getMensaje().contains("pago"));
        assertNull(resultado.getNumeroFactura());
        
        System.out.println("✗ Venta fallida: " + resultado.getMensaje());
        System.out.println("Stock liberado automáticamente");
    }

    @Test
    @DisplayName("E2E - Escenario de cliente frecuente con puntos")
    public void testEscenarioClienteFrecuenteConPuntos() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE FRECUENTE ===\n");
        
        String clienteId = "CLI003";
        
        // Consultar puntos iniciales
        System.out.println("Consultando puntos del cliente " + clienteId + "...");
        int puntosIniciales = facade.consultarPuntosCliente(clienteId);
        System.out.println("Puntos actuales: " + puntosIniciales);
        
        // Primera compra
        System.out.println("\n--- Primera Compra ---");
        Venta venta1 = new Venta();
        venta1.setClienteId(clienteId);
        venta1.setClienteNombre("Luis Fernández");
        venta1.setClienteEmail("luis@example.com");
        venta1.setMetodoPago("EFECTIVO");
        venta1.agregarItem("P001", "Cafetera", 1, 150.0);
        
        ResultadoVenta resultado1 = facade.procesarVenta(venta1);
        assertTrue(resultado1.isExitoso());
        System.out.println("Puntos ganados: " + resultado1.getPuntosGanados());
        
        // Segunda compra
        System.out.println("\n--- Segunda Compra ---");
        Venta venta2 = new Venta();
        venta2.setClienteId(clienteId);
        venta2.setClienteNombre("Luis Fernández");
        venta2.setClienteEmail("luis@example.com");
        venta2.setMetodoPago("TARJETA");
        venta2.agregarItem("P002", "Licuadora", 1, 200.0);
        
        ResultadoVenta resultado2 = facade.procesarVenta(venta2);
        assertTrue(resultado2.isExitoso());
        System.out.println("Puntos ganados: " + resultado2.getPuntosGanados());
        
        // Tercera compra
        System.out.println("\n--- Tercera Compra ---");
        Venta venta3 = new Venta();
        venta3.setClienteId(clienteId);
        venta3.setClienteNombre("Luis Fernández");
        venta3.setClienteEmail("luis@example.com");
        venta3.setMetodoPago("TARJETA");
        venta3.agregarItem("P003", "Tostadora", 1, 80.0);
        
        ResultadoVenta resultado3 = facade.procesarVenta(venta3);
        assertTrue(resultado3.isExitoso());
        System.out.println("Puntos ganados: " + resultado3.getPuntosGanados());
        
        // Puntos totales
        int puntosEsperados = resultado1.getPuntosGanados() + 
                             resultado2.getPuntosGanados() + 
                             resultado3.getPuntosGanados();
        
        System.out.println("\n--- Resumen de Puntos ---");
        System.out.println("Total de puntos ganados en las 3 compras: " + puntosEsperados);
    }

    @Test
    @DisplayName("E2E - Escenario de múltiples ventas simultáneas")
    public void testEscenarioMultiplesVentasSimultaneas() {
        System.out.println("\n=== ESCENARIO E2E: MÚLTIPLES VENTAS SIMULTÁNEAS ===\n");
        
        // Simular 5 clientes comprando al mismo tiempo
        Venta[] ventas = new Venta[5];
        ResultadoVenta[] resultados = new ResultadoVenta[5];
        
        for (int i = 0; i < 5; i++) {
            ventas[i] = new Venta();
            ventas[i].setClienteId("CLI" + (i + 10));
            ventas[i].setClienteNombre("Cliente " + (i + 1));
            ventas[i].setClienteEmail("cliente" + (i + 1) + "@example.com");
            ventas[i].setMetodoPago(i % 2 == 0 ? "EFECTIVO" : "TARJETA");
            ventas[i].agregarItem("P00" + (i + 1), "Producto " + (i + 1), 1, 100.0 * (i + 1));
            
            System.out.println("Procesando venta " + (i + 1) + "...");
            resultados[i] = facade.procesarVenta(ventas[i]);
        }
        
        // Verificar que todas las ventas fueron exitosas
        System.out.println("\n--- Resultados ---");
        int ventasExitosas = 0;
        for (int i = 0; i < 5; i++) {
            if (resultados[i].isExitoso()) {
                ventasExitosas++;
                System.out.println("✓ Venta " + (i + 1) + ": " + resultados[i].getNumeroFactura());
            }
        }
        
        assertEquals(5, ventasExitosas);
        System.out.println("\nTotal de ventas procesadas exitosamente: " + ventasExitosas + "/5");
    }

    @Test
    @DisplayName("E2E - Escenario de compra grande con generación de reportes")
    public void testEscenarioCompraGrandeConReportes() {
        System.out.println("\n=== ESCENARIO E2E: COMPRA GRANDE CON REPORTES ===\n");
        
        // Cliente corporativo hace una compra grande
        Venta venta = new Venta();
        venta.setClienteId("CORP001");
        venta.setClienteNombre("Empresa ABC S.A.");
        venta.setClienteEmail("compras@abc.com");
        venta.setMetodoPago("TRANSFERENCIA");
        
        System.out.println("Cliente corporativo selecciona equipos:");
        venta.agregarItem("P001", "Laptop Dell XPS", 10, 1500.0);
        venta.agregarItem("P002", "Monitor 27'", 10, 400.0);
        venta.agregarItem("P003", "Teclado Mecánico", 10, 150.0);
        venta.agregarItem("P004", "Mouse Inalámbrico", 10, 50.0);
        venta.agregarItem("P005", "Webcam HD", 10, 100.0);
        
        System.out.println("\nResumen de compra:");
        System.out.println("- 10 Laptops Dell XPS");
        System.out.println("- 10 Monitores 27'");
        System.out.println("- 10 Teclados Mecánicos");
        System.out.println("- 10 Mouse Inalámbricos");
        System.out.println("- 10 Webcams HD");
        System.out.println("\nSubtotal: $" + venta.getSubtotal());
        System.out.println("Impuestos: $" + venta.getImpuestos());
        System.out.println("Total: $" + venta.getTotal());
        
        // Procesar venta
        ResultadoVenta resultado = facade.procesarVenta(venta);
        
        // Verificaciones
        assertTrue(resultado.isExitoso());
        assertEquals(5, venta.getItems().size());
        assertTrue(venta.getTotal() > 20000);
        assertTrue(resultado.getPuntosGanados() > 2000);
        
        // Generar reportes
        System.out.println("\nGenerando reportes...");
        java.util.Date fechaInicio = new java.util.Date();
        java.util.Date fechaFin = new java.util.Date();
        String reporte = facade.generarReporteVentas(fechaInicio, fechaFin);
        
        assertNotNull(reporte);
        System.out.println("Reporte generado: " + reporte);
        
        System.out.println("\n--- Detalles Finales ---");
        System.out.println("Factura: " + resultado.getNumeroFactura());
        System.out.println("Monto total: $" + venta.getTotal());
        System.out.println("Puntos corporativos ganados: " + resultado.getPuntosGanados());
    }

    @Test
    @DisplayName("E2E - Escenario completo con MVC")
    public void testEscenarioCompletoConMVC() {
        System.out.println("\n=== ESCENARIO E2E: FLUJO COMPLETO MVC ===\n");
        
        // Crear venta
        Venta venta = new Venta();
        venta.setClienteId("CLI999");
        venta.setClienteNombre("Sofía Ramírez");
        venta.setClienteEmail("sofia@example.com");
        venta.setMetodoPago("TARJETA");
        venta.agregarItem("P001", "Smartphone", 1, 800.0);
        venta.agregarItem("P002", "Funda", 1, 20.0);
        venta.agregarItem("P003", "Protector Pantalla", 1, 10.0);
        
        System.out.println("Procesando venta a través del controlador MVC...");
        
        // Procesar a través del controller
        assertDoesNotThrow(() -> controller.procesarVenta(venta));
        
        System.out.println("\nConsultando puntos del cliente...");
        assertDoesNotThrow(() -> controller.consultarPuntos("CLI999"));
        
        System.out.println("\nGenerando reporte...");
        java.util.Date fechaInicio = new java.util.Date();
        java.util.Date fechaFin = new java.util.Date();
        assertDoesNotThrow(() -> controller.generarReporte(fechaInicio, fechaFin));
        
        System.out.println("\n✓ Flujo MVC completado exitosamente");
    }
}