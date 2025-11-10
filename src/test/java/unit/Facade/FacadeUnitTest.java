package unit.Facade;

import Estructurales.Facade.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Facade Pattern - Unit Tests")
public class FacadeUnitTest {
    
    // ==================== ValidadorStock Tests ====================
    
    @Test
    @DisplayName("ValidadorStock - Validar disponibilidad con stock suficiente")
    public void testValidarDisponibilidadStockSuficiente() {
        ValidadorStock validador = new ValidadorStock();
        assertTrue(validador.validarDisponibilidad("P001", 50));
    }
    
    @Test
    @DisplayName("ValidadorStock - Validar disponibilidad con stock insuficiente")
    public void testValidarDisponibilidadStockInsuficiente() {
        ValidadorStock validador = new ValidadorStock();
        assertFalse(validador.validarDisponibilidad("P001", 150));
    }
    
    @Test
    @DisplayName("ValidadorStock - Reservar stock")
    public void testReservarStock() {
        ValidadorStock validador = new ValidadorStock();
        assertDoesNotThrow(() -> validador.reservarStock("P001", 10));
    }
    
    @Test
    @DisplayName("ValidadorStock - Liberar stock")
    public void testLiberarStock() {
        ValidadorStock validador = new ValidadorStock();
        assertDoesNotThrow(() -> validador.liberarStock("P001", 10));
    }
    
    // ==================== ProcesadorPagos Tests ====================
    
    @Test
    @DisplayName("ProcesadorPagos - Procesar pago exitoso")
    public void testProcesarPagoExitoso() {
        ProcesadorPagos procesador = new ProcesadorPagos();
        assertTrue(procesador.procesarPago("TARJETA", 100.0));
    }
    
    @Test
    @DisplayName("ProcesadorPagos - Procesar pago con monto cero")
    public void testProcesarPagoMontoCero() {
        ProcesadorPagos procesador = new ProcesadorPagos();
        assertFalse(procesador.procesarPago("EFECTIVO", 0.0));
    }
    
    @Test
    @DisplayName("ProcesadorPagos - Generar comprobante")
    public void testGenerarComprobante() {
        ProcesadorPagos procesador = new ProcesadorPagos();
        String comprobante = procesador.generarComprobante("TARJETA", 100.0);
        assertNotNull(comprobante);
        assertTrue(comprobante.startsWith("COMPROBANTE-"));
    }
    
    @Test
    @DisplayName("ProcesadorPagos - Revertir pago")
    public void testRevertirPago() {
        ProcesadorPagos procesador = new ProcesadorPagos();
        assertTrue(procesador.revertirPago("COMPROBANTE-12345"));
    }
    
    // ==================== GeneradorFacturas Tests ====================
    
    @Test
    @DisplayName("GeneradorFacturas - Generar factura")
    public void testGenerarFactura() {
        GeneradorFacturas generador = new GeneradorFacturas();
        String factura = generador.generarFactura("Juan Pérez", 100.0, 19.0);
        assertNotNull(factura);
        assertTrue(factura.startsWith("FAC-"));
    }
    
    @Test
    @DisplayName("GeneradorFacturas - Enviar factura electrónica")
    public void testEnviarFacturaElectronica() {
        GeneradorFacturas generador = new GeneradorFacturas();
        assertDoesNotThrow(() -> generador.enviarFacturaElectronica("FAC-001", "test@example.com"));
    }
    
    @Test
    @DisplayName("GeneradorFacturas - Generar PDF")
    public void testGenerarPDF() {
        GeneradorFacturas generador = new GeneradorFacturas();
        byte[] pdf = generador.generarPDF("FAC-001");
        assertNotNull(pdf);
    }
    
    // ==================== ActualizadorInventario Tests ====================
    
    @Test
    @DisplayName("ActualizadorInventario - Descontar stock")
    public void testDescontarStock() {
        ActualizadorInventario actualizador = new ActualizadorInventario();
        assertDoesNotThrow(() -> actualizador.descontarStock("P001", 5));
    }
    
    @Test
    @DisplayName("ActualizadorInventario - Registrar movimiento")
    public void testRegistrarMovimiento() {
        ActualizadorInventario actualizador = new ActualizadorInventario();
        assertDoesNotThrow(() -> actualizador.registrarMovimiento("P001", 5, "VENTA"));
    }
    
    @Test
    @DisplayName("ActualizadorInventario - Verificar stock mínimo")
    public void testVerificarStockMinimo() {
        ActualizadorInventario actualizador = new ActualizadorInventario();
        assertTrue(actualizador.verificarStockMinimo("P001"));
    }
    
    // ==================== SistemaFidelizacion Tests ====================
    
    @Test
    @DisplayName("SistemaFidelizacion - Calcular puntos")
    public void testCalcularPuntos() {
        SistemaFidelizacion sistema = new SistemaFidelizacion();
        assertEquals(10, sistema.calcularPuntos(100.0));
    }
    
    @Test
    @DisplayName("SistemaFidelizacion - Calcular puntos con decimales")
    public void testCalcularPuntosConDecimales() {
        SistemaFidelizacion sistema = new SistemaFidelizacion();
        assertEquals(15, sistema.calcularPuntos(155.0));
    }
    
    @Test
    @DisplayName("SistemaFidelizacion - Acreditar puntos")
    public void testAcreditarPuntos() {
        SistemaFidelizacion sistema = new SistemaFidelizacion();
        assertDoesNotThrow(() -> sistema.acreditarPuntos("CLI001", 50));
    }
    
    @Test
    @DisplayName("SistemaFidelizacion - Consultar puntos")
    public void testConsultarPuntos() {
        SistemaFidelizacion sistema = new SistemaFidelizacion();
        assertEquals(150, sistema.consultarPuntos("CLI001"));
    }
    
    // ==================== ServicioNotificaciones Tests ====================
    
    @Test
    @DisplayName("ServicioNotificaciones - Enviar email")
    public void testEnviarEmail() {
        ServicioNotificaciones servicio = new ServicioNotificaciones();
        assertDoesNotThrow(() -> servicio.enviarEmail("test@example.com", "Prueba", "Mensaje"));
    }
    
    @Test
    @DisplayName("ServicioNotificaciones - Enviar SMS")
    public void testEnviarSMS() {
        ServicioNotificaciones servicio = new ServicioNotificaciones();
        assertDoesNotThrow(() -> servicio.enviarSMS("123456789", "Mensaje SMS"));
    }
    
    @Test
    @DisplayName("ServicioNotificaciones - Notificar venta")
    public void testNotificarVenta() {
        ServicioNotificaciones servicio = new ServicioNotificaciones();
        assertDoesNotThrow(() -> servicio.notificarVenta("CLI001", "FAC-001"));
    }
    
    // ==================== GeneradorReportes Tests ====================
    
    @Test
    @DisplayName("GeneradorReportes - Registrar venta")
    public void testRegistrarVenta() {
        GeneradorReportes generador = new GeneradorReportes();
        assertDoesNotThrow(() -> generador.registrarVenta("FAC-001", 100.0, new java.util.Date()));
    }
    
    @Test
    @DisplayName("GeneradorReportes - Actualizar estadísticas")
    public void testActualizarEstadisticas() {
        GeneradorReportes generador = new GeneradorReportes();
        assertDoesNotThrow(() -> generador.actualizarEstadisticas("SUC-001", 500.0));
    }
    
    @Test
    @DisplayName("GeneradorReportes - Generar reporte ventas")
    public void testGenerarReporteVentas() {
        GeneradorReportes generador = new GeneradorReportes();
        String reporte = generador.generarReporteVentas(new java.util.Date(), new java.util.Date());
        assertNotNull(reporte);
        assertTrue(reporte.startsWith("REPORTE-VENTAS-"));
    }
    
    // ==================== Venta Tests ====================
    
    @Test
    @DisplayName("Venta - Crear venta vacía")
    public void testCrearVentaVacia() {
        Venta venta = new Venta();
        assertNotNull(venta.getId());
        assertEquals(0, venta.getItems().size());
        assertEquals(0.0, venta.getTotal(), 0.01);
    }
    
    @Test
    @DisplayName("Venta - Agregar item")
    public void testAgregarItem() {
        Venta venta = new Venta();
        venta.agregarItem("P001", "Producto 1", 2, 50.0);
        assertEquals(1, venta.getItems().size());
    }
    
    @Test
    @DisplayName("Venta - Calcular subtotal")
    public void testCalcularSubtotal() {
        Venta venta = new Venta();
        venta.agregarItem("P001", "Producto 1", 2, 50.0);
        assertEquals(100.0, venta.getSubtotal(), 0.01);
    }
    
    @Test
    @DisplayName("Venta - Calcular impuestos")
    public void testCalcularImpuestos() {
        Venta venta = new Venta();
        venta.agregarItem("P001", "Producto 1", 2, 50.0);
        assertEquals(19.0, venta.getImpuestos(), 0.01);
    }
    
    @Test
    @DisplayName("Venta - Calcular total")
    public void testCalcularTotal() {
        Venta venta = new Venta();
        venta.agregarItem("P001", "Producto 1", 2, 50.0);
        assertEquals(119.0, venta.getTotal(), 0.01);
    }
    
    @Test
    @DisplayName("Venta - Agregar múltiples items")
    public void testAgregarMultiplesItems() {
        Venta venta = new Venta();
        venta.agregarItem("P001", "Producto 1", 2, 50.0);
        venta.agregarItem("P002", "Producto 2", 1, 30.0);
        assertEquals(2, venta.getItems().size());
        assertEquals(130.0, venta.getSubtotal(), 0.01);
    }
    
    // ==================== ItemVenta Tests ====================
    
    @Test
    @DisplayName("ItemVenta - Crear item")
    public void testCrearItem() {
        ItemVenta item = new ItemVenta("P001", "Producto 1", 2, 50.0);
        assertEquals("P001", item.getCodigoProducto());
        assertEquals("Producto 1", item.getNombreProducto());
        assertEquals(2, item.getCantidad());
        assertEquals(50.0, item.getPrecioUnitario(), 0.01);
    }
    
    @Test
    @DisplayName("ItemVenta - Calcular subtotal")
    public void testCalcularSubtotalItem() {
        ItemVenta item = new ItemVenta("P001", "Producto 1", 3, 25.0);
        assertEquals(75.0, item.getSubtotal(), 0.01);
    }
    
    // ==================== ResultadoVenta Tests ====================
    
    @Test
    @DisplayName("ResultadoVenta - Crear resultado")
    public void testCrearResultado() {
        ResultadoVenta resultado = new ResultadoVenta();
        assertFalse(resultado.isExitoso());
        assertNull(resultado.getMensaje());
    }
    
    @Test
    @DisplayName("ResultadoVenta - Setear valores")
    public void testSetearValores() {
        ResultadoVenta resultado = new ResultadoVenta();
        resultado.setExitoso(true);
        resultado.setMensaje("Venta exitosa");
        resultado.setNumeroFactura("FAC-001");
        resultado.setComprobantePago("COMP-001");
        resultado.setPuntosGanados(50);
        
        assertTrue(resultado.isExitoso());
        assertEquals("Venta exitosa", resultado.getMensaje());
        assertEquals("FAC-001", resultado.getNumeroFactura());
        assertEquals("COMP-001", resultado.getComprobantePago());
        assertEquals(50, resultado.getPuntosGanados());
    }
}