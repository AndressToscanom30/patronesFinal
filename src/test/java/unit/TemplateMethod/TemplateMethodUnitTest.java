package unit.TemplateMethod;

import Comportamentales.TemplateMethod.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Template Method Pattern - Unit Tests")
public class TemplateMethodUnitTest {
    
    // ==================== VentaEfectivo Tests ====================
    
    @Test
    @DisplayName("VentaEfectivo - Crear instancia")
    public void testCrearVentaEfectivo() {
        VentaEfectivo venta = new VentaEfectivo();
        assertNotNull(venta);
    }
    
    @Test
    @DisplayName("VentaEfectivo - Procesar venta")
    public void testProcesarVentaEfectivo() {
        VentaEfectivo venta = new VentaEfectivo();
        String resultado = venta.procesarVenta(100);
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("Iniciando transacción"));
        assertTrue(resultado.contains("efectivo"));
        assertTrue(resultado.contains("finalizada"));
    }
    
    @Test
    @DisplayName("VentaEfectivo - Requiere validación adicional")
    public void testVentaEfectivoValidacionAdicional() {
        VentaEfectivo venta = new VentaEfectivo();
        assertTrue(venta.requiereValidacionAdicional());
    }
    
    @Test
    @DisplayName("VentaEfectivo - Contiene verificación de billetes")
    public void testVentaEfectivoVerificacionBilletes() {
        VentaEfectivo venta = new VentaEfectivo();
        String resultado = venta.procesarVenta(100);
        
        assertTrue(resultado.contains("billetes"));
    }
    
    // ==================== VentaTarjeta Tests ====================
    
    @Test
    @DisplayName("VentaTarjeta - Crear con tipo crédito")
    public void testCrearVentaTarjetaCredito() {
        VentaTarjeta venta = new VentaTarjeta("CREDITO");
        assertNotNull(venta);
        assertEquals("CREDITO", venta.getTipoCuenta());
    }
    
    @Test
    @DisplayName("VentaTarjeta - Crear con tipo débito")
    public void testCrearVentaTarjetaDebito() {
        VentaTarjeta venta = new VentaTarjeta("DEBITO");
        assertNotNull(venta);
        assertEquals("DEBITO", venta.getTipoCuenta());
    }
    
    @Test
    @DisplayName("VentaTarjeta - Procesar venta")
    public void testProcesarVentaTarjeta() {
        VentaTarjeta venta = new VentaTarjeta("CREDITO");
        String resultado = venta.procesarVenta(100);
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("Iniciando transacción"));
        assertTrue(resultado.contains("tarjeta"));
        assertTrue(resultado.contains("finalizada"));
    }
    
    @Test
    @DisplayName("VentaTarjeta - Requiere validación adicional")
    public void testVentaTarjetaValidacionAdicional() {
        VentaTarjeta venta = new VentaTarjeta("CREDITO");
        assertTrue(venta.requiereValidacionAdicional());
    }
    
    @Test
    @DisplayName("VentaTarjeta - Contiene validación banco")
    public void testVentaTarjetaValidacionBanco() {
        VentaTarjeta venta = new VentaTarjeta("CREDITO");
        String resultado = venta.procesarVenta(100);
        
        assertTrue(resultado.contains("banco"));
    }
    
    @Test
    @DisplayName("VentaTarjeta - Contiene tipo de cuenta en resultado")
    public void testVentaTarjetaTipoCuentaEnResultado() {
        VentaTarjeta venta = new VentaTarjeta("DEBITO");
        String resultado = venta.procesarVenta(100);
        
        assertTrue(resultado.contains("DEBITO"));
    }
    
    // ==================== VentaTransferencia Tests ====================
    
    @Test
    @DisplayName("VentaTransferencia - Crear con banco")
    public void testCrearVentaTransferencia() {
        VentaTransferencia venta = new VentaTransferencia("Bancolombia");
        assertNotNull(venta);
        assertEquals("Bancolombia", venta.getBanco());
    }
    
    @Test
    @DisplayName("VentaTransferencia - Procesar venta")
    public void testProcesarVentaTransferencia() {
        VentaTransferencia venta = new VentaTransferencia("Banco Nacional");
        String resultado = venta.procesarVenta(100);
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("Iniciando transacción"));
        assertTrue(resultado.contains("transferencia"));
        assertTrue(resultado.contains("finalizada"));
    }
    
    @Test
    @DisplayName("VentaTransferencia - No requiere validación adicional por defecto")
    public void testVentaTransferenciaSinValidacionAdicional() {
        VentaTransferencia venta = new VentaTransferencia("Banco Nacional");
        assertFalse(venta.requiereValidacionAdicional());
    }
    
    @Test
    @DisplayName("VentaTransferencia - Contiene nombre del banco")
    public void testVentaTransferenciaNombreBanco() {
        VentaTransferencia venta = new VentaTransferencia("Bancolombia");
        String resultado = venta.procesarVenta(100);
        
        assertTrue(resultado.contains("Bancolombia"));
    }
    
    @Test
    @DisplayName("VentaTransferencia - Genera código de referencia")
    public void testVentaTransferenciaCodigoReferencia() {
        VentaTransferencia venta = new VentaTransferencia("Banco Nacional");
        String resultado = venta.procesarVenta(100);
        
        assertTrue(resultado.contains("referencia"));
    }
    
    // ==================== Template Method Structure Tests ====================
    
    @Test
    @DisplayName("Template Method - Todos los tipos siguen la misma estructura")
    public void testEstructuraComun() {
        ProcesoVenta efectivo = new VentaEfectivo();
        ProcesoVenta tarjeta = new VentaTarjeta("CREDITO");
        ProcesoVenta transferencia = new VentaTransferencia("Banco");
        
        String resultado1 = efectivo.procesarVenta(100);
        String resultado2 = tarjeta.procesarVenta(100);
        String resultado3 = transferencia.procesarVenta(100);
        
        // Todos deben iniciar la transacción
        assertTrue(resultado1.contains("Iniciando transacción"));
        assertTrue(resultado2.contains("Iniciando transacción"));
        assertTrue(resultado3.contains("Iniciando transacción"));
        
        // Todos deben finalizar la transacción
        assertTrue(resultado1.contains("finalizada"));
        assertTrue(resultado2.contains("finalizada"));
        assertTrue(resultado3.contains("finalizada"));
    }
    
    @Test
    @DisplayName("Template Method - Cada tipo tiene implementación específica")
    public void testImplementacionEspecifica() {
        ProcesoVenta efectivo = new VentaEfectivo();
        ProcesoVenta tarjeta = new VentaTarjeta("CREDITO");
        ProcesoVenta transferencia = new VentaTransferencia("Banco");
        
        String resultado1 = efectivo.procesarVenta(100);
        String resultado2 = tarjeta.procesarVenta(100);
        String resultado3 = transferencia.procesarVenta(100);
        
        // Cada uno debe tener su implementación específica
        assertTrue(resultado1.contains("efectivo"));
        assertTrue(resultado2.contains("tarjeta"));
        assertTrue(resultado3.contains("transferencia"));
        
        // No deben contener términos de otros tipos
        assertFalse(resultado1.contains("tarjeta"));
        assertFalse(resultado2.contains("transferencia"));
        assertFalse(resultado3.contains("efectivo"));
    }
    
    @Test
    @DisplayName("Template Method - Diferentes montos procesados correctamente")
    public void testDiferentesMontos() {
        ProcesoVenta venta = new VentaEfectivo();
        
        String resultado1 = venta.procesarVenta(50);
        String resultado2 = venta.procesarVenta(100);
        String resultado3 = venta.procesarVenta(1000);
        
        assertTrue(resultado1.contains("50"));
        assertTrue(resultado2.contains("100"));
        assertTrue(resultado3.contains("1000"));
    }
}