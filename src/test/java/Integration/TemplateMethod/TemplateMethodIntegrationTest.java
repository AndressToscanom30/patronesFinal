package Integration.TemplateMethod;

import Comportamentales.TemplateMethod.modelo.*;
import Comportamentales.TemplateMethod.controlador.TemplateMethodController;
import Comportamentales.TemplateMethod.vista.ConsolaTemplateMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Template Method Pattern - Integration Tests")
public class TemplateMethodIntegrationTest {
    private TemplateMethodController controller;
    private ConsolaTemplateMethod vista;

    @BeforeEach
    public void setUp() {
        vista = new ConsolaTemplateMethod();
        controller = new TemplateMethodController(vista);
    }
    
    // ==================== MVC Integration Tests ====================
    
    @Test
    @DisplayName("MVC - Controller procesa venta en efectivo")
    public void testControllerProcesaVentaEfectivo() {
        String resultado = controller.procesarVentaEfectivo(1000);
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("efectivo"));
        assertTrue(resultado.contains("finalizada"));
    }
    
    @Test
    @DisplayName("MVC - Controller procesa venta con tarjeta")
    public void testControllerProcesaVentaTarjeta() {
        String resultado = controller.procesarVentaTarjeta(1000, "CREDITO");
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("tarjeta"));
        assertTrue(resultado.contains("CREDITO"));
    }
    
    @Test
    @DisplayName("MVC - Controller procesa venta por transferencia")
    public void testControllerProcesaVentaTransferencia() {
        String resultado = controller.procesarVentaTransferencia(1000, "Bancolombia");
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("transferencia"));
        assertTrue(resultado.contains("Bancolombia"));
    }
    
    @Test
    @DisplayName("MVC - Controller crea proceso de venta efectivo")
    public void testControllerCreaProcesoEfectivo() {
        ProcesoVenta proceso = controller.crearProcesoVenta("EFECTIVO");
        
        assertNotNull(proceso);
        assertTrue(proceso instanceof VentaEfectivo);
    }
    
    @Test
    @DisplayName("MVC - Controller crea proceso de venta tarjeta")
    public void testControllerCreaProcesoTarjeta() {
        ProcesoVenta proceso = controller.crearProcesoVenta("TARJETA", "DEBITO");
        
        assertNotNull(proceso);
        assertTrue(proceso instanceof VentaTarjeta);
        assertEquals("DEBITO", ((VentaTarjeta) proceso).getTipoCuenta());
    }
    
    @Test
    @DisplayName("MVC - Controller crea proceso de venta transferencia")
    public void testControllerCreaProcesoTransferencia() {
        ProcesoVenta proceso = controller.crearProcesoVenta("TRANSFERENCIA", "Banco Nacional");
        
        assertNotNull(proceso);
        assertTrue(proceso instanceof VentaTransferencia);
        assertEquals("Banco Nacional", ((VentaTransferencia) proceso).getBanco());
    }
    
    @Test
    @DisplayName("MVC - Controller maneja tipo inválido")
    public void testControllerManejaTipoInvalido() {
        ProcesoVenta proceso = controller.crearProcesoVenta("INVALIDO");
        assertNull(proceso);
    }
    
    // ==================== Integración entre diferentes tipos de venta ====================
    
    @Test
    @DisplayName("Integración - Secuencia de ventas diferentes")
    public void testSecuenciaVentasDiferentes() {
        // Venta 1: Efectivo
        String resultado1 = controller.procesarVentaEfectivo(100);
        assertTrue(resultado1.contains("efectivo"));
        
        // Venta 2: Tarjeta
        String resultado2 = controller.procesarVentaTarjeta(200, "CREDITO");
        assertTrue(resultado2.contains("tarjeta"));
        
        // Venta 3: Transferencia
        String resultado3 = controller.procesarVentaTransferencia(300, "Bancolombia");
        assertTrue(resultado3.contains("transferencia"));
        
        // Todas deben seguir la misma estructura
        assertTrue(resultado1.contains("Iniciando transacción"));
        assertTrue(resultado2.contains("Iniciando transacción"));
        assertTrue(resultado3.contains("Iniciando transacción"));
    }
    
    @Test
    @DisplayName("Integración - Mismo monto, diferentes métodos")
    public void testMismoMontoDiferentesMetodos() {
        double monto = 5000;
        
        String resultado1 = controller.procesarVentaEfectivo(monto);
        String resultado2 = controller.procesarVentaTarjeta(monto, "DEBITO");
        String resultado3 = controller.procesarVentaTransferencia(monto, "Banco");
        
        // Todos deben procesar el mismo monto
        assertTrue(resultado1.contains("5000"));
        assertTrue(resultado2.contains("5000"));
        assertTrue(resultado3.contains("5000"));
        
        // Pero con diferentes métodos
        assertTrue(resultado1.contains("efectivo"));
        assertTrue(resultado2.contains("tarjeta"));
        assertTrue(resultado3.contains("transferencia"));
    }
    
    // ==================== Escenarios de Negocio ====================
    
    @Test
    @DisplayName("Integración - Cliente cambia método de pago")
    public void testClienteCambiaMetodoPago() {
        double monto = 10000;
        
        // Cliente intenta pagar con efectivo
        String resultado1 = controller.procesarVentaEfectivo(monto);
        assertTrue(resultado1.contains("efectivo"));
        
        // Cliente cambia a tarjeta
        String resultado2 = controller.procesarVentaTarjeta(monto, "CREDITO");
        assertTrue(resultado2.contains("tarjeta"));
        
        // Ambos procesos son válidos e independientes
        assertNotEquals(resultado1, resultado2);
    }
    
    @Test
    @DisplayName("Integración - Múltiples ventas simultáneas")
    public void testMultiplesVentasSimultaneas() {
        // Simular 3 cajas procesando ventas al mismo tiempo
        String venta1 = controller.procesarVentaEfectivo(1000);
        String venta2 = controller.procesarVentaTarjeta(2000, "DEBITO");
        String venta3 = controller.procesarVentaTransferencia(3000, "Bancolombia");
        
        // Todas deben completarse exitosamente
        assertTrue(venta1.contains("finalizada"));
        assertTrue(venta2.contains("finalizada"));
        assertTrue(venta3.contains("finalizada"));
    }
}