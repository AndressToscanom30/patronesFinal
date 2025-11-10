package e2e.TemplateMethod;

import Comportamentales.TemplateMethod.controlador.TemplateMethodController;
import Comportamentales.TemplateMethod.modelo.*;
import Comportamentales.TemplateMethod.vista.ConsolaTemplateMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Template Method Pattern - End-to-End Tests")
public class TemplateMethodE2ETest {
    private TemplateMethodController controller;
    private ConsolaTemplateMethod vista;

    @BeforeEach
    public void setUp() {
        vista = new ConsolaTemplateMethod();
        controller = new TemplateMethodController(vista);
    }

    @Test
    @DisplayName("E2E - Cliente paga compra en efectivo")
    public void testClientePagaEnEfectivo() {
        System.out.println("\n=== ESCENARIO E2E: PAGO EN EFECTIVO ===\n");
        
        System.out.println("Cliente realiza compra en supermercado:");
        System.out.println("- Leche: $5,000");
        System.out.println("- Pan: $3,000");
        System.out.println("- Huevos: $7,000");
        
        double total = 15000;
        System.out.printf("\nTotal a pagar: $%.0f%n", total);
        
        System.out.println("\nCliente decide pagar en efectivo...\n");
        System.out.println("═".repeat(50));
        
        String resultado = controller.procesarVentaEfectivo(total);
        System.out.println(resultado);
        
        System.out.println("═".repeat(50));
        
        assertTrue(resultado.contains("Iniciando transacción"));
        assertTrue(resultado.contains("efectivo"));
        assertTrue(resultado.contains("15000"));
        assertTrue(resultado.contains("billetes"));
        assertTrue(resultado.contains("finalizada"));
        
        System.out.println("✓ Pago en efectivo completado exitosamente");
    }

    @Test
    @DisplayName("E2E - Cliente paga con tarjeta de crédito")
    public void testClientePagaConTarjetaCredito() {
        System.out.println("\n=== ESCENARIO E2E: PAGO CON TARJETA DE CRÉDITO ===\n");
        
        System.out.println("Cliente realiza compra grande:");
        System.out.println("- Televisor: $1,200,000");
        System.out.println("- Soporte: $50,000");
        
        double total = 1250000;
        System.out.printf("\nTotal a pagar: $%.0f%n", total);
        
        System.out.println("\nCliente paga con tarjeta de crédito...\n");
        System.out.println("═".repeat(50));
        
        String resultado = controller.procesarVentaTarjeta(total, "CREDITO");
        System.out.println(resultado);
        
        System.out.println("═".repeat(50));
        
        assertTrue(resultado.contains("Iniciando transacción"));
        assertTrue(resultado.contains("tarjeta"));
        assertTrue(resultado.contains("CREDITO"));
        assertTrue(resultado.contains("banco"));
        assertTrue(resultado.contains("PIN"));
        assertTrue(resultado.contains("finalizada"));
        
        System.out.println("✓ Pago con tarjeta de crédito completado");
    }

    @Test
    @DisplayName("E2E - Cliente paga con tarjeta débito")
    public void testClientePagaConTarjetaDebito() {
        System.out.println("\n=== ESCENARIO E2E: PAGO CON TARJETA DÉBITO ===\n");
        
        System.out.println("Compra del mercado semanal:");
        double total = 85000;
        System.out.printf("Total: $%.0f%n", total);
        
        System.out.println("\nProcesando pago con tarjeta débito...\n");
        System.out.println("═".repeat(50));
        
        String resultado = controller.procesarVentaTarjeta(total, "DEBITO");
        System.out.println(resultado);
        
        System.out.println("═".repeat(50));
        
        assertTrue(resultado.contains("tarjeta"));
        assertTrue(resultado.contains("DEBITO"));
        assertTrue(resultado.contains("finalizada"));
        
        System.out.println("✓ Pago con débito procesado correctamente");
    }

    @Test
    @DisplayName("E2E - Cliente paga por transferencia bancaria")
    public void testClientePagaPorTransferencia() {
        System.out.println("\n=== ESCENARIO E2E: PAGO POR TRANSFERENCIA ===\n");
        
        System.out.println("Cliente empresarial realiza compra al por mayor:");
        double total = 5000000;
        System.out.printf("Total: $%.0f%n", total);
        
        String banco = "Bancolombia";
        System.out.println("Banco: " + banco);
        
        System.out.println("\nProcesando transferencia bancaria...\n");
        System.out.println("═".repeat(50));
        
        String resultado = controller.procesarVentaTransferencia(total, banco);
        System.out.println(resultado);
        
        System.out.println("═".repeat(50));
        
        assertTrue(resultado.contains("transferencia"));
        assertTrue(resultado.contains("Bancolombia"));
        assertTrue(resultado.contains("referencia"));
        assertTrue(resultado.contains("finalizada"));
        
        System.out.println("✓ Transferencia bancaria completada");
    }

    @Test
    @DisplayName("E2E - Día completo de ventas con diferentes métodos")
    public void testDiaCompletoVentas() {
        System.out.println("\n=== ESCENARIO E2E: DÍA COMPLETO DE VENTAS ===\n");
        
        System.out.println("Simulando un día típico en el supermercado...\n");
        
        // Venta 1: Mañana - Efectivo
        System.out.println("─".repeat(50));
        System.out.println("8:00 AM - VENTA #1 (Efectivo)");
        System.out.println("─".repeat(50));
        double venta1 = 25000;
        System.out.printf("Monto: $%.0f%n", venta1);
        String resultado1 = controller.procesarVentaEfectivo(venta1);
        System.out.println(resultado1);
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        // Venta 2: Medio día - Tarjeta
        System.out.println("\n" + "─".repeat(50));
        System.out.println("12:00 PM - VENTA #2 (Tarjeta Crédito)");
        System.out.println("─".repeat(50));
        double venta2 = 150000;
        System.out.printf("Monto: $%.0f%n", venta2);
        String resultado2 = controller.procesarVentaTarjeta(venta2, "CREDITO");
        System.out.println(resultado2);
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        // Venta 3: Tarde - Débito
        System.out.println("\n" + "─".repeat(50));
        System.out.println("3:00 PM - VENTA #3 (Tarjeta Débito)");
        System.out.println("─".repeat(50));
        double venta3 = 75000;
        System.out.printf("Monto: $%.0f%n", venta3);
        String resultado3 = controller.procesarVentaTarjeta(venta3, "DEBITO");
        System.out.println(resultado3);
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        // Venta 4: Tarde - Efectivo
        System.out.println("\n" + "─".repeat(50));
        System.out.println("5:00 PM - VENTA #4 (Efectivo)");
        System.out.println("─".repeat(50));
        double venta4 = 40000;
        System.out.printf("Monto: $%.0f%n", venta4);
        String resultado4 = controller.procesarVentaEfectivo(venta4);
        System.out.println(resultado4);
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        // Venta 5: Noche - Transferencia
        System.out.println("\n" + "─".repeat(50));
        System.out.println("7:00 PM - VENTA #5 (Transferencia)");
        System.out.println("─".repeat(50));
        double venta5 = 200000;
        System.out.printf("Monto: $%.0f%n", venta5);
        String resultado5 = controller.procesarVentaTransferencia(venta5, "Davivienda");
        System.out.println(resultado5);
        
        // Resumen del día
        double totalDia = venta1 + venta2 + venta3 + venta4 + venta5;
        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESUMEN DEL DÍA");
        System.out.println("═".repeat(50));
        System.out.println("Total de ventas: 5");
        System.out.println("- Efectivo: 2 ventas ($65,000)");
        System.out.println("- Tarjeta Crédito: 1 venta ($150,000)");
        System.out.println("- Tarjeta Débito: 1 venta ($75,000)");
        System.out.println("- Transferencia: 1 venta ($200,000)");
        System.out.printf("TOTAL RECAUDADO: $%.0f%n", totalDia);
        System.out.println("═".repeat(50));
        
        // Verificaciones
        assertTrue(resultado1.contains("efectivo"));
        assertTrue(resultado2.contains("CREDITO"));
        assertTrue(resultado3.contains("DEBITO"));
        assertTrue(resultado4.contains("efectivo"));
        assertTrue(resultado5.contains("transferencia"));
        
        assertEquals(490000, totalDia, 0.01);
        
        System.out.println("\n✓ Día de ventas completado exitosamente");
    }

    @Test
    @DisplayName("E2E - Cliente intenta diferentes métodos de pago")
    public void testClienteIntentaDiferentesMetodos() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE COMPARA MÉTODOS ===\n");
        
        double totalCompra = 120000;
        System.out.printf("Total de la compra: $%.0f%n", totalCompra);
        
        System.out.println("\nCliente revisa las opciones de pago disponibles:\n");
        
        // Opción 1: Efectivo
        System.out.println("═".repeat(50));
        System.out.println("OPCIÓN 1: EFECTIVO");
        System.out.println("═".repeat(50));
        System.out.println("✓ Pago inmediato");
        System.out.println("✓ No requiere saldo en cuenta");
        System.out.println("✗ Debe contar efectivo");
        String proceso1 = controller.procesarVentaEfectivo(totalCompra);
        assertTrue(proceso1.contains("efectivo"));
        
        // Opción 2: Tarjeta
        System.out.println("\n" + "═".repeat(50));
        System.out.println("OPCIÓN 2: TARJETA DE CRÉDITO");
        System.out.println("═".repeat(50));
        System.out.println("✓ Pago diferido");
        System.out.println("✓ Acumula puntos");
        System.out.println("✗ Requiere aprobación bancaria");
        String proceso2 = controller.procesarVentaTarjeta(totalCompra, "CREDITO");
        assertTrue(proceso2.contains("tarjeta"));
        
        // Opción 3: Transferencia
        System.out.println("\n" + "═".repeat(50));
        System.out.println("OPCIÓN 3: TRANSFERENCIA BANCARIA");
        System.out.println("═".repeat(50));
        System.out.println("✓ Sin límite de monto");
        System.out.println("✓ Comprobante digital");
        System.out.println("✗ Requiere confirmación");
        String proceso3 = controller.procesarVentaTransferencia(totalCompra, "Banco Nacional");
        assertTrue(proceso3.contains("transferencia"));
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("Cliente elige pagar con tarjeta de crédito");
        System.out.println("═".repeat(50));
        
        System.out.println("\n✓ Cliente tomó decisión informada");
    }

    @Test
    @DisplayName("E2E - Flujo completo con validaciones de seguridad")
    public void testFlujoCompletoConValidaciones() {
        System.out.println("\n=== ESCENARIO E2E: FLUJO CON VALIDACIONES ===\n");
        
        // Compra de alto valor
        double montoAlto = 3000000;
        System.out.printf("Compra de alto valor: $%.0f%n", montoAlto);
        
        System.out.println("\nPor seguridad, se requieren validaciones adicionales\n");
        
        // Tarjeta con validaciones
        System.out.println("═".repeat(50));
        System.out.println("Procesando con tarjeta (validaciones de seguridad)");
        System.out.println("═".repeat(50));
        
        String resultado = controller.procesarVentaTarjeta(montoAlto, "CREDITO");
        System.out.println(resultado);
        
        // Verificar que se realizaron las validaciones
        assertTrue(resultado.contains("PIN"));
        assertTrue(resultado.contains("3D Secure"));
        assertTrue(resultado.contains("finalizada"));
        
        System.out.println("═".repeat(50));
        System.out.println("✓ Todas las validaciones de seguridad aprobadas");
        System.out.println("✓ Compra de alto valor procesada correctamente");
        System.out.println("═".repeat(50));
    }
}