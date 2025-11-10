package unit.CoR;

import Comportamentales.ChainOfResponsibility.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Chain of Responsibility Pattern - Unit Tests")
public class ChainOfResponsibilityUnitTest {
    
    // ==================== SolicitudDescuento Tests ====================
    
    @Test
    @DisplayName("SolicitudDescuento - Crear solicitud completa")
    public void testCrearSolicitudCompleta() {
        SolicitudDescuento solicitud = new SolicitudDescuento(
            "CLI-001", 100000, 5, 10, "VERANO2024", "GENERAL");
        
        assertEquals("CLI-001", solicitud.getClienteId());
        assertEquals(100000, solicitud.getMontoTotal(), 0.01);
        assertEquals(5, solicitud.getCantidad());
        assertEquals(10, solicitud.getNumeroCompras());
        assertEquals("VERANO2024", solicitud.getCodigoCupon());
        assertEquals("GENERAL", solicitud.getCategoria());
    }
    
    @Test
    @DisplayName("SolicitudDescuento - Crear solicitud simplificada")
    public void testCrearSolicitudSimplificada() {
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-002", 50000, 3);
        
        assertEquals("CLI-002", solicitud.getClienteId());
        assertEquals(50000, solicitud.getMontoTotal(), 0.01);
        assertEquals(3, solicitud.getCantidad());
        assertEquals(0, solicitud.getNumeroCompras());
        assertNull(solicitud.getCodigoCupon());
    }
    
    @Test
    @DisplayName("SolicitudDescuento - Modificar número de compras")
    public void testModificarNumeroCompras() {
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 100000, 5);
        solicitud.setNumeroCompras(15);
        
        assertEquals(15, solicitud.getNumeroCompras());
    }
    
    @Test
    @DisplayName("SolicitudDescuento - Establecer cupón")
    public void testEstablecerCupon() {
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 100000, 5);
        solicitud.setCodigoCupon("BLACKFRIDAY");
        
        assertEquals("BLACKFRIDAY", solicitud.getCodigoCupon());
    }
    
    // ==================== ResultadoDescuento Tests ====================
    
    @Test
    @DisplayName("ResultadoDescuento - Descuento aprobado")
    public void testDescuentoAprobado() {
        ResultadoDescuento resultado = new ResultadoDescuento(
            true, 15000, "Descuento aplicado", "MONTO");
        
        assertTrue(resultado.isAprobado());
        assertEquals(15000, resultado.getMontoDescuento(), 0.01);
        assertEquals("Descuento aplicado", resultado.getDescripcion());
        assertEquals("MONTO", resultado.getTipoDescuento());
    }
    
    @Test
    @DisplayName("ResultadoDescuento - Descuento rechazado")
    public void testDescuentoRechazado() {
        ResultadoDescuento resultado = new ResultadoDescuento(
            false, 0, "No califica", "NINGUNO");
        
        assertFalse(resultado.isAprobado());
        assertEquals(0, resultado.getMontoDescuento(), 0.01);
    }
    
    // ==================== DescuentoPorCantidad Tests ====================
    
    @Test
    @DisplayName("DescuentoPorCantidad - Crear manejador")
    public void testCrearDescuentoPorCantidad() {
        DescuentoPorCantidad descuento = new DescuentoPorCantidad(10, 5);
        
        assertEquals(10, descuento.getCantidadMinima());
        assertEquals(5, descuento.getPorcentajeDescuento(), 0.01);
    }
    
    @Test
    @DisplayName("DescuentoPorCantidad - Aprobar descuento")
    public void testDescuentoPorCantidadAprobar() {
        DescuentoPorCantidad descuento = new DescuentoPorCantidad(10, 5);
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 100000, 15);
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals(5000, resultado.getMontoDescuento(), 0.01); // 5% de 100000
        assertEquals("CANTIDAD", resultado.getTipoDescuento());
    }
    
    @Test
    @DisplayName("DescuentoPorCantidad - Rechazar descuento")
    public void testDescuentoPorCantidadRechazar() {
        DescuentoPorCantidad descuento = new DescuentoPorCantidad(10, 5);
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 100000, 5);
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertFalse(resultado.isAprobado());
    }
    
    @Test
    @DisplayName("DescuentoPorCantidad - Cantidad exacta mínima")
    public void testDescuentoPorCantidadExacta() {
        DescuentoPorCantidad descuento = new DescuentoPorCantidad(10, 5);
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 100000, 10);
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
    }
    
    // ==================== DescuentoPorMonto Tests ====================
    
    @Test
    @DisplayName("DescuentoPorMonto - Crear manejador")
    public void testCrearDescuentoPorMonto() {
        DescuentoPorMonto descuento = new DescuentoPorMonto(100000, 10);
        
        assertEquals(100000, descuento.getMontoMinimo(), 0.01);
        assertEquals(10, descuento.getPorcentajeDescuento(), 0.01);
    }
    
    @Test
    @DisplayName("DescuentoPorMonto - Aprobar descuento")
    public void testDescuentoPorMontoAprobar() {
        DescuentoPorMonto descuento = new DescuentoPorMonto(100000, 10);
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 150000, 5);
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals(15000, resultado.getMontoDescuento(), 0.01); // 10% de 150000
        assertEquals("MONTO", resultado.getTipoDescuento());
    }
    
    @Test
    @DisplayName("DescuentoPorMonto - Rechazar descuento")
    public void testDescuentoPorMontoRechazar() {
        DescuentoPorMonto descuento = new DescuentoPorMonto(100000, 10);
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 50000, 5);
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertFalse(resultado.isAprobado());
    }
    
    @Test
    @DisplayName("DescuentoPorMonto - Monto exacto mínimo")
    public void testDescuentoPorMontoExacto() {
        DescuentoPorMonto descuento = new DescuentoPorMonto(100000, 10);
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 100000, 5);
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals(10000, resultado.getMontoDescuento(), 0.01);
    }
    
    // ==================== DescuentoClienteFrecuente Tests ====================
    
    @Test
    @DisplayName("DescuentoClienteFrecuente - Crear manejador")
    public void testCrearDescuentoClienteFrecuente() {
        DescuentoClienteFrecuente descuento = new DescuentoClienteFrecuente(5, 15);
        
        assertEquals(5, descuento.getComprasMinimasRequeridas());
        assertEquals(15, descuento.getPorcentajeDescuento(), 0.01);
    }
    
    @Test
    @DisplayName("DescuentoClienteFrecuente - Aprobar descuento")
    public void testDescuentoClienteFrecuenteAprobar() {
        DescuentoClienteFrecuente descuento = new DescuentoClienteFrecuente(5, 15);
        SolicitudDescuento solicitud = new SolicitudDescuento(
            "CLI-001", 100000, 5, 10, null, "GENERAL");
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals(15000, resultado.getMontoDescuento(), 0.01); // 15% de 100000
        assertEquals("CLIENTE_FRECUENTE", resultado.getTipoDescuento());
    }
    
    @Test
    @DisplayName("DescuentoClienteFrecuente - Rechazar descuento")
    public void testDescuentoClienteFrecuenteRechazar() {
        DescuentoClienteFrecuente descuento = new DescuentoClienteFrecuente(5, 15);
        SolicitudDescuento solicitud = new SolicitudDescuento(
            "CLI-001", 100000, 5, 2, null, "GENERAL");
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertFalse(resultado.isAprobado());
    }
    
    @Test
    @DisplayName("DescuentoClienteFrecuente - Compras exactas mínimas")
    public void testDescuentoClienteFrecuenteExacto() {
        DescuentoClienteFrecuente descuento = new DescuentoClienteFrecuente(5, 15);
        SolicitudDescuento solicitud = new SolicitudDescuento(
            "CLI-001", 100000, 5, 5, null, "GENERAL");
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
    }
    
    // ==================== DescuentoCupon Tests ====================
    
    @Test
    @DisplayName("DescuentoCupon - Crear manejador")
    public void testCrearDescuentoCupon() {
        DescuentoCupon descuento = new DescuentoCupon();
        assertNotNull(descuento);
        assertFalse(descuento.getCuponesValidos().isEmpty());
    }
    
    @Test
    @DisplayName("DescuentoCupon - Cupón válido VERANO2024")
    public void testCuponValidoVerano() {
        DescuentoCupon descuento = new DescuentoCupon();
        SolicitudDescuento solicitud = new SolicitudDescuento(
            "CLI-001", 100000, 5, 0, "VERANO2024", "GENERAL");
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals(15000, resultado.getMontoDescuento(), 0.01); // 15% de 100000
        assertEquals("CUPON", resultado.getTipoDescuento());
    }
    
    @Test
    @DisplayName("DescuentoCupon - Cupón válido BLACKFRIDAY")
    public void testCuponValidoBlackFriday() {
        DescuentoCupon descuento = new DescuentoCupon();
        SolicitudDescuento solicitud = new SolicitudDescuento(
            "CLI-001", 100000, 5, 0, "BLACKFRIDAY", "GENERAL");
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals(50000, resultado.getMontoDescuento(), 0.01); // 50% de 100000
    }
    
    @Test
    @DisplayName("DescuentoCupon - Cupón inválido")
    public void testCuponInvalido() {
        DescuentoCupon descuento = new DescuentoCupon();
        SolicitudDescuento solicitud = new SolicitudDescuento(
            "CLI-001", 100000, 5, 0, "INVALIDO", "GENERAL");
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertFalse(resultado.isAprobado());
    }
    
    @Test
    @DisplayName("DescuentoCupon - Sin cupón")
    public void testSinCupon() {
        DescuentoCupon descuento = new DescuentoCupon();
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 100000, 5);
        
        ResultadoDescuento resultado = descuento.procesarSolicitud(solicitud);
        
        assertFalse(resultado.isAprobado());
    }
    
    @Test
    @DisplayName("DescuentoCupon - Agregar nuevo cupón")
    public void testAgregarNuevoCupon() {
        DescuentoCupon descuento = new DescuentoCupon();
        descuento.agregarCupon("NUEVO2024", 30.0);
        
        assertTrue(descuento.getCuponesValidos().containsKey("NUEVO2024"));
        assertEquals(30.0, descuento.getCuponesValidos().get("NUEVO2024"), 0.01);
    }
    
    // ==================== Cadena de Responsabilidad Tests ====================
    
    @Test
    @DisplayName("Cadena - Configurar siguiente manejador")
    public void testConfigurarSiguiente() {
        ManejadorDescuento desc1 = new DescuentoPorMonto(100000, 10);
        ManejadorDescuento desc2 = new DescuentoPorCantidad(10, 5);
        
        desc1.setSiguiente(desc2);
        
        assertNotNull(desc1);
    }
    
    @Test
    @DisplayName("Cadena - Primer manejador aprueba")
    public void testPrimerManejadorAprueba() {
        ManejadorDescuento desc1 = new DescuentoPorMonto(100000, 10);
        ManejadorDescuento desc2 = new DescuentoPorCantidad(10, 5);
        desc1.setSiguiente(desc2);
        
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 150000, 5);
        ResultadoDescuento resultado = desc1.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals("MONTO", resultado.getTipoDescuento());
    }
    
    @Test
    @DisplayName("Cadena - Pasa al siguiente manejador")
    public void testPasaAlSiguiente() {
        ManejadorDescuento desc1 = new DescuentoPorMonto(100000, 10);
        ManejadorDescuento desc2 = new DescuentoPorCantidad(10, 5);
        desc1.setSiguiente(desc2);
        
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 50000, 15);
        ResultadoDescuento resultado = desc1.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals("CANTIDAD", resultado.getTipoDescuento());
    }
    
    @Test
    @DisplayName("Cadena - Ningún manejador aprueba")
    public void testNingunManejadorAprueba() {
        ManejadorDescuento desc1 = new DescuentoPorMonto(100000, 10);
        ManejadorDescuento desc2 = new DescuentoPorCantidad(10, 5);
        desc1.setSiguiente(desc2);
        
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 50000, 5);
        ResultadoDescuento resultado = desc1.procesarSolicitud(solicitud);
        
        assertFalse(resultado.isAprobado());
    }
    
    @Test
    @DisplayName("Cadena - Cadena completa de 4 manejadores")
    public void testCadenaCompleta() {
        ManejadorDescuento cupon = new DescuentoCupon();
        ManejadorDescuento frecuente = new DescuentoClienteFrecuente(5, 15);
        ManejadorDescuento monto = new DescuentoPorMonto(100000, 10);
        ManejadorDescuento cantidad = new DescuentoPorCantidad(10, 5);
        
        cupon.setSiguiente(frecuente);
        frecuente.setSiguiente(monto);
        monto.setSiguiente(cantidad);
        
        // Solicitud que solo califica para cantidad
        SolicitudDescuento solicitud = new SolicitudDescuento("CLI-001", 50000, 15);
        ResultadoDescuento resultado = cupon.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals("CANTIDAD", resultado.getTipoDescuento());
    }
    
    @Test
    @DisplayName("Cadena - Cupón tiene prioridad")
    public void testCuponTienePrioridad() {
        ManejadorDescuento cupon = new DescuentoCupon();
        ManejadorDescuento monto = new DescuentoPorMonto(100000, 10);
        cupon.setSiguiente(monto);
        
        // Solicitud que califica para ambos
        SolicitudDescuento solicitud = new SolicitudDescuento(
            "CLI-001", 150000, 5, 0, "BLACKFRIDAY", "GENERAL");
        ResultadoDescuento resultado = cupon.procesarSolicitud(solicitud);
        
        assertTrue(resultado.isAprobado());
        assertEquals("CUPON", resultado.getTipoDescuento());
        assertEquals(75000, resultado.getMontoDescuento(), 0.01); // 50% es mayor que 10%
    }
}