package Integration.Facade;

import Estructurales.Facade.modelo.*;
import Estructurales.Facade.controlador.VentasController;
import Estructurales.Facade.vista.ConsolaFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Facade Pattern - Integration Tests")
public class FacadeIntegrationTest {
    
    // ==================== Facade con subsistemas Tests ====================
    
    @Test
    @DisplayName("Integración - Venta exitosa completa")
    public void testVentaExitosaCompleta() {
        // Crear facade
        SistemaVentasFacade facade = new SistemaVentasFacade();
        
        // Crear venta
        Venta venta = new Venta();
        venta.setClienteId("CLI001");
        venta.setClienteNombre("Juan Pérez");
        venta.setClienteEmail("juan@example.com");
        venta.setMetodoPago("TARJETA");
        venta.agregarItem("P001", "Laptop Dell", 1, 1000.0);
        venta.agregarItem("P002", "Mouse Logitech", 2, 25.0);
        
        // Procesar venta
        ResultadoVenta resultado = facade.procesarVenta(venta);
        
        // Verificaciones
        assertTrue(resultado.isExitoso());
        assertNotNull(resultado.getNumeroFactura());
        assertNotNull(resultado.getComprobantePago());
        assertTrue(resultado.getPuntosGanados() > 0);
    }
    
    @Test
    @DisplayName("Integración - Venta con stock insuficiente")
    public void testVentaStockInsuficiente() {
        // Crear facade
        SistemaVentasFacade facade = new SistemaVentasFacade();
        
        // Crear venta con cantidad que excede el stock
        Venta venta = new Venta();
        venta.setClienteId("CLI002");
        venta.setClienteNombre("María González");
        venta.setClienteEmail("maria@example.com");
        venta.setMetodoPago("EFECTIVO");
        venta.agregarItem("P001", "Laptop Dell", 200, 1000.0);
        
        // Procesar venta
        ResultadoVenta resultado = facade.procesarVenta(venta);
        
        // Verificaciones
        assertFalse(resultado.isExitoso());
        assertTrue(resultado.getMensaje().contains("Stock insuficiente"));
        assertNull(resultado.getNumeroFactura());
    }
    
    @Test
    @DisplayName("Integración - Consultar puntos cliente")
    public void testConsultarPuntosCliente() {
        // Crear facade
        SistemaVentasFacade facade = new SistemaVentasFacade();
        
        // Consultar puntos
        int puntos = facade.consultarPuntosCliente("CLI001");
        
        // Verificaciones
        assertTrue(puntos >= 0);
    }
    
    @Test
    @DisplayName("Integración - Generar reporte de ventas")
    public void testGenerarReporteVentas() {
        // Crear facade
        SistemaVentasFacade facade = new SistemaVentasFacade();
        
        // Generar reporte
        java.util.Date fechaInicio = new java.util.Date();
        java.util.Date fechaFin = new java.util.Date();
        String reporte = facade.generarReporteVentas(fechaInicio, fechaFin);
        
        // Verificaciones
        assertNotNull(reporte);
        assertTrue(reporte.startsWith("REPORTE-VENTAS-"));
    }
    
    // ==================== MVC Integration Tests ====================
    
    @Test
    @DisplayName("MVC - Controller procesa venta exitosa")
    public void testControllerProcesaVentaExitosa() {
        // Crear componentes MVC
        ConsolaFacade vista = new ConsolaFacade();
        VentasController controller = new VentasController(vista);
        
        // Crear venta
        Venta venta = new Venta();
        venta.setClienteId("CLI003");
        venta.setClienteNombre("Carlos López");
        venta.setClienteEmail("carlos@example.com");
        venta.setMetodoPago("TARJETA");
        venta.agregarItem("P001", "Tablet Samsung", 1, 500.0);
        
        // Procesar venta a través del controller
        assertDoesNotThrow(() -> controller.procesarVenta(venta));
    }
    
    @Test
    @DisplayName("MVC - Controller consulta puntos")
    public void testControllerConsultaPuntos() {
        // Crear componentes MVC
        ConsolaFacade vista = new ConsolaFacade();
        VentasController controller = new VentasController(vista);
        
        // Consultar puntos
        assertDoesNotThrow(() -> controller.consultarPuntos("CLI001"));
    }
    
    @Test
    @DisplayName("MVC - Controller genera reporte")
    public void testControllerGeneraReporte() {
        // Crear componentes MVC
        ConsolaFacade vista = new ConsolaFacade();
        VentasController controller = new VentasController(vista);
        
        // Generar reporte
        java.util.Date fechaInicio = new java.util.Date();
        java.util.Date fechaFin = new java.util.Date();
        assertDoesNotThrow(() -> controller.generarReporte(fechaInicio, fechaFin));
    }
    
    // ==================== Flujo completo de venta Tests ====================
    
    @Test
    @DisplayName("Integración - Flujo completo con múltiples items")
    public void testFlujoCompletoMultiplesItems() {
        // Crear facade
        SistemaVentasFacade facade = new SistemaVentasFacade();
        
        // Crear venta con múltiples productos
        Venta venta = new Venta();
        venta.setClienteId("CLI004");
        venta.setClienteNombre("Ana Martínez");
        venta.setClienteEmail("ana@example.com");
        venta.setMetodoPago("TARJETA");
        venta.agregarItem("P001", "Monitor LG", 2, 300.0);
        venta.agregarItem("P002", "Teclado Mecánico", 2, 120.0);
        venta.agregarItem("P003", "Mouse Gaming", 2, 80.0);
        
        // Procesar venta
        ResultadoVenta resultado = facade.procesarVenta(venta);
        
        // Verificaciones
        assertTrue(resultado.isExitoso());
        assertEquals(3, venta.getItems().size());
        assertEquals(1000.0, venta.getSubtotal(), 0.01);
        assertTrue(resultado.getPuntosGanados() >= 119);
    }
    
    @Test
    @DisplayName("Integración - Venta con diferentes métodos de pago")
    public void testVentaDiferentesMetodosPago() {
        // Crear facade
        SistemaVentasFacade facade = new SistemaVentasFacade();
        
        // Venta con efectivo
        Venta venta1 = new Venta();
        venta1.setClienteId("CLI005");
        venta1.setClienteNombre("Pedro Ramírez");
        venta1.setClienteEmail("pedro@example.com");
        venta1.setMetodoPago("EFECTIVO");
        venta1.agregarItem("P001", "Producto", 1, 50.0);
        
        ResultadoVenta resultado1 = facade.procesarVenta(venta1);
        assertTrue(resultado1.isExitoso());
        
        // Venta con tarjeta
        Venta venta2 = new Venta();
        venta2.setClienteId("CLI006");
        venta2.setClienteNombre("Laura Sánchez");
        venta2.setClienteEmail("laura@example.com");
        venta2.setMetodoPago("TARJETA");
        venta2.agregarItem("P002", "Producto", 1, 100.0);
        
        ResultadoVenta resultado2 = facade.procesarVenta(venta2);
        assertTrue(resultado2.isExitoso());
    }
    
    @Test
    @DisplayName("Integración - Cálculo correcto de puntos")
    public void testCalculoCorrectoPuntos() {
        // Crear facade
        SistemaVentasFacade facade = new SistemaVentasFacade();
        
        // Venta de 1000 pesos debe dar 119 puntos (incluyendo impuestos)
        Venta venta = new Venta();
        venta.setClienteId("CLI007");
        venta.setClienteNombre("Diego Torres");
        venta.setClienteEmail("diego@example.com");
        venta.setMetodoPago("TARJETA");
        venta.agregarItem("P001", "Producto Caro", 1, 1000.0);
        
        ResultadoVenta resultado = facade.procesarVenta(venta);
        
        // 1000 + 19% = 1190, dividido entre 10 = 119 puntos
        assertTrue(resultado.isExitoso());
        assertEquals(119, resultado.getPuntosGanados());
    }
}