package Integration.CoR;

import Comportamentales.ChainOfResponsibility.modelo.*;
import Comportamentales.ChainOfResponsibility.controlador.ChainOfResponsibilityController;
import Comportamentales.ChainOfResponsibility.vista.ConsolaChainOfResponsibility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Chain of Responsibility Pattern - Integration Tests")
public class ChainOfResponsibilityIntegrationTest {
    
    private ChainOfResponsibilityController controller;
    private ConsolaChainOfResponsibility vista;
    
    @BeforeEach
    public void setUp() {
        vista = new ConsolaChainOfResponsibility();
        controller = new ChainOfResponsibilityController(vista);
    }
    
    // ==================== MVC Integration Tests ====================
    
    @Test
    @DisplayName("MVC - Controller procesa solicitud")
    public void testControllerProcesaSolicitud() {
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-001", 150000, 5, 0, null, "GENERAL");
        
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        assertNotNull(resultado);
        assertTrue(resultado.isAprobado());
    }
    
    @Test
    @DisplayName("MVC - Controller crea solicitud")
    public void testControllerCreaSolicitud() {
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-002", 100000, 10, 5, "VERANO2024", "GENERAL");
        
        assertEquals("CLI-002", solicitud.getClienteId());
        assertEquals(100000, solicitud.getMontoTotal(), 0.01);
        assertEquals(10, solicitud.getCantidad());
        assertEquals(5, solicitud.getNumeroCompras());
        assertEquals("VERANO2024", solicitud.getCodigoCupon());
    }
    
    @Test
    @DisplayName("MVC - Controller configura cadena personalizada")
    public void testControllerConfiguraCadenaPersonalizada() {
        ManejadorDescuento nuevaCadena = new DescuentoPorMonto(50000, 20);
        controller.configurarCadenaPersonalizada(nuevaCadena);
        
        assertNotNull(controller.getCadenaDescuentos());
    }
    
    // ==================== Integración Cadena Completa ====================
    
    @Test
    @DisplayName("Integración - Cadena evalúa en orden correcto")
    public void testCadenaEvaluaEnOrden() {
        // La cadena por defecto es: Cupón -> Frecuente -> Monto -> Cantidad
        
        // Solicitud que califica para cupón (debe detenerse ahí)
        SolicitudDescuento sol1 = controller.crearSolicitud(
            "CLI-001", 200000, 20, 10, "BLACKFRIDAY", "GENERAL");
        ResultadoDescuento res1 = controller.procesarSolicitud(sol1);
        
        assertEquals("CUPON", res1.getTipoDescuento());
    }
    
    @Test
    @DisplayName("Integración - Cadena salta manejadores que no aplican")
    public void testCadenaSaltaManejadores() {
        // Solicitud que NO califica para cupón ni frecuente, pero SÍ para monto
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-001", 150000, 5, 0, null, "GENERAL");
        
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals("MONTO", resultado.getTipoDescuento());
    }
    
    @Test
    @DisplayName("Integración - Múltiples solicitudes independientes")
    public void testMultiplesSolicitudesIndependientes() {
        SolicitudDescuento sol1 = controller.crearSolicitud(
            "CLI-001", 150000, 5, 0, "VERANO2024", "GENERAL");
        SolicitudDescuento sol2 = controller.crearSolicitud(
            "CLI-002", 120000, 15, 0, null, "GENERAL");
        SolicitudDescuento sol3 = controller.crearSolicitud(
            "CLI-003", 80000, 3, 10, null, "GENERAL");
        
        ResultadoDescuento res1 = controller.procesarSolicitud(sol1);
        ResultadoDescuento res2 = controller.procesarSolicitud(sol2);
        ResultadoDescuento res3 = controller.procesarSolicitud(sol3);
        
        assertEquals("CUPON", res1.getTipoDescuento());
        assertEquals("MONTO", res2.getTipoDescuento());
        assertEquals("CLIENTE_FRECUENTE", res3.getTipoDescuento());
    }
    
    // ==================== Escenarios de Negocio ====================
    
    @Test
    @DisplayName("Integración - Cliente nuevo sin beneficios")
    public void testClienteNuevoSinBeneficios() {
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-NUEVO", 50000, 3, 0, null, "GENERAL");
        
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        assertFalse(resultado.isAprobado());
    }
    
    @Test
    @DisplayName("Integración - Cliente VIP con múltiples beneficios")
    public void testClienteVIP() {
        // Cliente VIP califica para todo, pero el cupón tiene prioridad
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-VIP", 200000, 20, 15, "BLACKFRIDAY", "GENERAL");
        
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals("CUPON", resultado.getTipoDescuento());
        assertEquals(100000, resultado.getMontoDescuento(), 0.01); // 50% de 200000
    }
    
    @Test
    @DisplayName("Integración - Compra grande sin cupón")
    public void testCompraGrandeSinCupon() {
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-001", 500000, 3, 2, null, "GENERAL");
        
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals("MONTO", resultado.getTipoDescuento());
        assertEquals(50000, resultado.getMontoDescuento(), 0.01); // 10% de 500000
    }
    
    @Test
    @DisplayName("Integración - Compra de muchas unidades")
    public void testCompraMuchasUnidades() {
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-001", 80000, 25, 0, null, "GENERAL");
        
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals("CANTIDAD", resultado.getTipoDescuento());
        assertEquals(4000, resultado.getMontoDescuento(), 0.01); // 5% de 80000
    }
    
    @Test
    @DisplayName("Integración - Cliente frecuente sin cupón")
    public void testClienteFrecuenteSinCupon() {
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-FRECUENTE", 80000, 3, 8, null, "GENERAL");
        
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals("CLIENTE_FRECUENTE", resultado.getTipoDescuento());
        assertEquals(12000, resultado.getMontoDescuento(), 0.01); // 15% de 80000
    }
    
    // ==================== Comparación de Descuentos ====================
    
    @Test
    @DisplayName("Integración - Comparar todos los tipos de descuento")
    public void testCompararTodosDescuentos() {
        double montoBase = 150000;
        
        // Cupón BLACKFRIDAY (50%)
        SolicitudDescuento sol1 = controller.crearSolicitud(
            "CLI-001", montoBase, 5, 0, "BLACKFRIDAY", "GENERAL");
        ResultadoDescuento res1 = controller.procesarSolicitud(sol1);
        
        // Cliente frecuente (15%)
        SolicitudDescuento sol2 = controller.crearSolicitud(
            "CLI-002", montoBase, 5, 10, null, "GENERAL");
        ResultadoDescuento res2 = controller.procesarSolicitud(sol2);
        
        // Por monto (10%)
        SolicitudDescuento sol3 = controller.crearSolicitud(
            "CLI-003", montoBase, 5, 0, null, "GENERAL");
        ResultadoDescuento res3 = controller.procesarSolicitud(sol3);
        
        // Por cantidad (5%)
        SolicitudDescuento sol4 = controller.crearSolicitud(
            "CLI-004", 80000, 15, 0, null, "GENERAL");
        ResultadoDescuento res4 = controller.procesarSolicitud(sol4);
        
        // Verificar que los descuentos son progresivamente menores
        assertTrue(res1.getMontoDescuento() > res2.getMontoDescuento());
        assertTrue(res2.getMontoDescuento() > res3.getMontoDescuento());
    }
    
    @Test
    @DisplayName("Integración - Cadena maneja solicitudes concurrentes")
    public void testCadenaManejaConcurrentes() {
        // Simular múltiples solicitudes procesándose
        ResultadoDescuento[] resultados = new ResultadoDescuento[5];
        
        for (int i = 0; i < 5; i++) {
            SolicitudDescuento solicitud = controller.crearSolicitud(
                "CLI-" + i, 100000 + (i * 10000), 5 + i, i, null, "GENERAL");
            resultados[i] = controller.procesarSolicitud(solicitud);
        }
        
        // Verificar que todas se procesaron
        for (ResultadoDescuento resultado : resultados) {
            assertNotNull(resultado);
        }
    }
}