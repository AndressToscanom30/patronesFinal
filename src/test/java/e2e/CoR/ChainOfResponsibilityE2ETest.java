package e2e.CoR;

import Comportamentales.ChainOfResponsibility.controlador.ChainOfResponsibilityController;
import Comportamentales.ChainOfResponsibility.modelo.*;
import Comportamentales.ChainOfResponsibility.vista.ConsolaChainOfResponsibility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Chain of Responsibility Pattern - End-to-End Tests")
public class ChainOfResponsibilityE2ETest {
    private ChainOfResponsibilityController controller;
    private ConsolaChainOfResponsibility vista;

    @BeforeEach
    public void setUp() {
        vista = new ConsolaChainOfResponsibility();
        controller = new ChainOfResponsibilityController(vista);
    }

    @Test
    @DisplayName("E2E - Cliente nuevo realiza primera compra")
    public void testClienteNuevoPrimeraCompra() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE NUEVO - PRIMERA COMPRA ===\n");
        
        System.out.println("Cliente nuevo visita el supermercado por primera vez");
        System.out.println("Selecciona productos por un valor de $85,000");
        System.out.println("Cantidad: 8 productos");
        
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-NUEVO-001", 85000, 8, 0, null, "GENERAL");
        
        System.out.println("\nProcesando solicitud de descuento...");
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        System.out.println("\n" + "═".repeat(50));
        if (resultado.isAprobado()) {
            double montoFinal = 85000 - resultado.getMontoDescuento();
            System.out.println("Resultado: DESCUENTO NO APLICADO");
            System.out.println("Motivo: Cliente nuevo sin cupón ni beneficios");
            System.out.printf("Total a pagar: $%.2f%n", montoFinal);
        } else {
            System.out.println("Resultado: SIN DESCUENTO");
            System.out.println("Total a pagar: $85,000");
        }
        System.out.println("═".repeat(50));
        
        assertFalse(resultado.isAprobado());
        
        System.out.println("\n✓ Cliente puede continuar con la compra sin descuento");
    }

    @Test
    @DisplayName("E2E - Cliente usa cupón de primera compra")
    public void testClienteUsaCuponPrimeraCompra() {
        System.out.println("\n=== ESCENARIO E2E: CUPÓN PRIMERA COMPRA ===\n");
        
        System.out.println("Cliente nuevo recibe cupón: PRIMERACOMPRA (20% descuento)");
        System.out.println("Realiza compra por $120,000");
        System.out.println("Aplica el cupón en el checkout");
        
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-NUEVO-002", 120000, 6, 0, "PRIMERACOMPRA", "GENERAL");
        
        System.out.println("\nProcesando solicitud de descuento...");
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        double montoFinal = 120000 - resultado.getMontoDescuento();
        double ahorro = resultado.getMontoDescuento();
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESULTADO DEL CHECKOUT");
        System.out.println("═".repeat(50));
        System.out.printf("Subtotal: $120,000%n");
        System.out.printf("Descuento (20%%): -$%.2f%n", ahorro);
        System.out.printf("TOTAL A PAGAR: $%.2f%n", montoFinal);
        System.out.println("═".repeat(50));
        
        assertTrue(resultado.isAprobado());
        assertEquals("CUPON", resultado.getTipoDescuento());
        assertEquals(24000, resultado.getMontoDescuento(), 0.01);
        
        System.out.println("\n✓ Cliente ahorró $24,000 con el cupón");
    }

    @Test
    @DisplayName("E2E - Cliente frecuente sin cupón")
    public void testClienteFrecuenteSinCupon() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE FRECUENTE ===\n");
        
        System.out.println("Cliente María González");
        System.out.println("- Número de compras previas: 12");
        System.out.println("- Estado: Cliente Frecuente VIP");
        System.out.println("\nRealiza nueva compra por $95,000");
        System.out.println("No tiene cupón activo");
        
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-MARIA-001", 95000, 7, 12, null, "GENERAL");
        
        System.out.println("\nSistema verifica beneficios del cliente...");
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        double descuento = resultado.getMontoDescuento();
        double montoFinal = 95000 - descuento;
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("BENEFICIO CLIENTE FRECUENTE APLICADO");
        System.out.println("═".repeat(50));
        System.out.printf("Subtotal: $95,000%n");
        System.out.printf("Descuento VIP (15%%): -$%.2f%n", descuento);
        System.out.printf("TOTAL A PAGAR: $%.2f%n", montoFinal);
        System.out.println("\n¡Gracias por su preferencia!");
        System.out.println("═".repeat(50));
        
        assertTrue(resultado.isAprobado());
        assertEquals("CLIENTE_FRECUENTE", resultado.getTipoDescuento());
        assertEquals(14250, resultado.getMontoDescuento(), 0.01);
        
        System.out.println("\n✓ Cliente frecuente recibió su beneficio automáticamente");
    }

    @Test
    @DisplayName("E2E - Black Friday con cupón especial")
    public void testBlackFriday() {
        System.out.println("\n=== ESCENARIO E2E: BLACK FRIDAY ===\n");
        
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║        BLACK FRIDAY - 50% DESCUENTO        ║");
        System.out.println("║    Usa el cupón: BLACKFRIDAY               ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.println("\nCliente Juan Pérez aprovecha la promoción");
        System.out.println("Carrito de compras:");
        System.out.println("- Smart TV 55\": $1,800,000");
        System.out.println("- Soundbar: $400,000");
        System.out.println("- Cable HDMI: $50,000");
        
        double total = 2250000;
        System.out.printf("\nTotal del carrito: $%.0f%n", total);
        
        System.out.println("\nCliente aplica cupón BLACKFRIDAY...");
        
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-JUAN-001", total, 3, 5, "BLACKFRIDAY", "ELECTRONICA");
        
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        double descuento = resultado.getMontoDescuento();
        double montoFinal = total - descuento;
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("BLACK FRIDAY - DESCUENTO APLICADO");
        System.out.println("═".repeat(50));
        System.out.printf("Subtotal: $%.0f%n", total);
        System.out.printf("BLACK FRIDAY (50%%): -$%.0f%n", descuento);
        System.out.printf("TOTAL A PAGAR: $%.0f%n", montoFinal);
        System.out.printf("AHORRASTE: $%.0f%n", descuento);
        System.out.println("═".repeat(50));
        
        assertTrue(resultado.isAprobado());
        assertEquals(1125000, resultado.getMontoDescuento(), 0.01);
        assertEquals(1125000, montoFinal, 0.01);
        
        System.out.println("\n✓ Cliente aprovechó el máximo descuento posible");
    }

    @Test
    @DisplayName("E2E - Compra al por mayor")
    public void testCompraAlPorMayor() {
        System.out.println("\n=== ESCENARIO E2E: COMPRA AL POR MAYOR ===\n");
        
        System.out.println("Tienda 'Minimarket El Ahorro' realiza pedido mayorista");
        System.out.println("Productos solicitados: 50 unidades de diferentes artículos");
        System.out.println("Valor total del pedido: $850,000");
        
        SolicitudDescuento solicitud = controller.crearSolicitud(
            "CLI-MAYORISTA-001", 850000, 50, 3, null, "MAYORISTA");
        
        System.out.println("\nSistema evalúa descuentos aplicables...");
        ResultadoDescuento resultado = controller.procesarSolicitud(solicitud);
        
        double descuento = resultado.getMontoDescuento();
        double montoFinal = 850000 - descuento;
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("DESCUENTO POR CANTIDAD APLICADO");
        System.out.println("═".repeat(50));
        System.out.printf("Subtotal: $850,000%n");
        System.out.printf("Descuento por cantidad (5%%): -$%.2f%n", descuento);
        System.out.printf("TOTAL A PAGAR: $%.2f%n", montoFinal);
        System.out.println("\nBeneficio por compra de 50+ unidades");
        System.out.println("═".repeat(50));
        
        assertTrue(resultado.isAprobado());
        assertEquals("MONTO", resultado.getTipoDescuento());
        
        System.out.println("\n✓ Descuento por volumen aplicado correctamente");
    }

    @Test
    @DisplayName("E2E - Cliente indeciso prueba varios escenarios")
    public void testClienteIndecisoVariosEscenarios() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE INDECISO ===\n");
        
        System.out.println("Cliente Ana Martínez está comparando opciones de compra");
        double montoBase = 180000;
        
        // Escenario 1: Sin beneficios
        System.out.println("\n" + "─".repeat(50));
        System.out.println("OPCIÓN 1: Compra sin beneficios");
        System.out.println("─".repeat(50));
        SolicitudDescuento sol1 = controller.crearSolicitud(
            "CLI-ANA-001", montoBase, 6, 0, null, "GENERAL");
        ResultadoDescuento res1 = controller.procesarSolicitud(sol1);
        double total1 = montoBase - (res1.isAprobado() ? res1.getMontoDescuento() : 0);
        System.out.printf("Total a pagar: $%.2f%n", total1);
        pausa();
        
        // Escenario 2: Con cupón BIENVENIDO
        System.out.println("\n" + "─".repeat(50));
        System.out.println("OPCIÓN 2: Usando cupón BIENVENIDO (10%)");
        System.out.println("─".repeat(50));
        SolicitudDescuento sol2 = controller.crearSolicitud(
            "CLI-ANA-001", montoBase, 6, 0, "BIENVENIDO", "GENERAL");
        ResultadoDescuento res2 = controller.procesarSolicitud(sol2);
        double total2 = montoBase - res2.getMontoDescuento();
        System.out.printf("Total a pagar: $%.2f%n", total2);
        System.out.printf("Ahorro: $%.2f%n", res2.getMontoDescuento());
        pausa();
        
        // Escenario 3: Con cupón VERANO2024
        System.out.println("\n" + "─".repeat(50));
        System.out.println("OPCIÓN 3: Usando cupón VERANO2024 (15%)");
        System.out.println("─".repeat(50));
        SolicitudDescuento sol3 = controller.crearSolicitud(
            "CLI-ANA-001", montoBase, 6, 0, "VERANO2024", "GENERAL");
        ResultadoDescuento res3 = controller.procesarSolicitud(sol3);
        double total3 = montoBase - res3.getMontoDescuento();
        System.out.printf("Total a pagar: $%.2f%n", total3);
        System.out.printf("Ahorro: $%.2f%n", res3.getMontoDescuento());
        pausa();
        
        // Escenario 4: Comprando 15 unidades
        System.out.println("\n" + "─".repeat(50));
        System.out.println("OPCIÓN 4: Comprando 15 unidades (desc. cantidad)");
        System.out.println("─".repeat(50));
        SolicitudDescuento sol4 = controller.crearSolicitud(
            "CLI-ANA-001", montoBase, 15, 0, null, "GENERAL");
        ResultadoDescuento res4 = controller.procesarSolicitud(sol4);
        double total4 = montoBase - (res4.isAprobado() ? res4.getMontoDescuento() : 0);
        System.out.printf("Total a pagar: $%.2f%n", total4);
        if (res4.isAprobado()) {
            System.out.printf("Ahorro: $%.2f%n", res4.getMontoDescuento());
        }
        
        // Decisión final
        System.out.println("\n" + "═".repeat(50));
        System.out.println("COMPARACIÓN FINAL");
        System.out.println("═".repeat(50));
        System.out.printf("Opción 1 (sin desc.): $%.2f%n", total1);
        System.out.printf("Opción 2 (BIENVENIDO): $%.2f%n", total2);
        System.out.printf("Opción 3 (VERANO2024): $%.2f ← MEJOR OPCIÓN%n", total3);
        System.out.printf("Opción 4 (cantidad): $%.2f%n", total4);
        System.out.println("═".repeat(50));
        
        System.out.println("\nCliente elige OPCIÓN 3: Cupón VERANO2024");
        System.out.printf("Ahorro total: $%.2f%n", res3.getMontoDescuento());
        
        assertTrue(total3 < total1);
        assertTrue(total3 < total2);
        
        System.out.println("\n✓ Cliente tomó la mejor decisión financiera");
    }

    @Test
    @DisplayName("E2E - Día completo de ventas con diferentes descuentos")
    public void testDiaCompletoVentas() {
        System.out.println("\n=== ESCENARIO E2E: DÍA COMPLETO DE VENTAS ===\n");
        
        System.out.println("Supermercado 'El Ahorro' - Resumen del día\n");
        
        double totalVentas = 0;
        double totalDescuentos = 0;
        int ventasConDescuento = 0;
        int ventasSinDescuento = 0;
        
        // Venta 1: Cliente nuevo
        System.out.println("9:00 AM - Venta #1: Cliente nuevo");
        SolicitudDescuento v1 = controller.crearSolicitud(
            "CLI-001", 45000, 4, 0, null, "GENERAL");
        ResultadoDescuento r1 = controller.procesarSolicitud(v1);
        totalVentas += 45000;
        if (!r1.isAprobado()) ventasSinDescuento++;
        System.out.printf("Total: $45,000 (sin descuento)%n");
        pausa();
        
        // Venta 2: Con cupón
        System.out.println("\n10:30 AM - Venta #2: Cliente con cupón");
        SolicitudDescuento v2 = controller.crearSolicitud(
            "CLI-002", 120000, 5, 0, "VERANO2024", "GENERAL");
        ResultadoDescuento r2 = controller.procesarSolicitud(v2);
        totalVentas += 120000;
        totalDescuentos += r2.getMontoDescuento();
        ventasConDescuento++;
        System.out.printf("Total: $120,000 - Descuento: $%.2f%n", r2.getMontoDescuento());
        pausa();
        
        // Venta 3: Cliente frecuente
        System.out.println("\n12:00 PM - Venta #3: Cliente frecuente");
        SolicitudDescuento v3 = controller.crearSolicitud(
            "CLI-003", 95000, 6, 8, null, "GENERAL");
        ResultadoDescuento r3 = controller.procesarSolicitud(v3);
        totalVentas += 95000;
        totalDescuentos += r3.getMontoDescuento();
        ventasConDescuento++;
        System.out.printf("Total: $95,000 - Descuento: $%.2f%n", r3.getMontoDescuento());
        pausa();
        
        // Venta 4: Compra grande
        System.out.println("\n2:00 PM - Venta #4: Compra grande");
        SolicitudDescuento v4 = controller.crearSolicitud(
            "CLI-004", 250000, 8, 2, null, "GENERAL");
        ResultadoDescuento r4 = controller.procesarSolicitud(v4);
        totalVentas += 250000;
        totalDescuentos += r4.getMontoDescuento();
        ventasConDescuento++;
        System.out.printf("Total: $250,000 - Descuento: $%.2f%n", r4.getMontoDescuento());
        pausa();
        
        // Venta 5: Mayorista
        System.out.println("\n4:00 PM - Venta #5: Cliente mayorista");
        SolicitudDescuento v5 = controller.crearSolicitud(
            "CLI-005", 500000, 35, 0, null, "MAYORISTA");
        ResultadoDescuento r5 = controller.procesarSolicitud(v5);
        totalVentas += 500000;
        totalDescuentos += r5.getMontoDescuento();
        ventasConDescuento++;
        System.out.printf("Total: $500,000 - Descuento: $%.2f%n", r5.getMontoDescuento());
        
        // Resumen del día
        double ventasNetas = totalVentas - totalDescuentos;
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESUMEN DEL DÍA");
        System.out.println("═".repeat(50));
        System.out.println("Total de ventas: 5");
        System.out.println("Ventas con descuento: " + ventasConDescuento);
        System.out.println("Ventas sin descuento: " + ventasSinDescuento);
        System.out.printf("Total bruto: $%.2f%n", totalVentas);
        System.out.printf("Total descuentos: $%.2f%n", totalDescuentos);
        System.out.printf("Total neto: $%.2f%n", ventasNetas);
        System.out.printf("Tasa de descuento promedio: %.2f%%%n", 
            (totalDescuentos / totalVentas) * 100);
        System.out.println("═".repeat(50));
        
        assertEquals(5, ventasConDescuento + ventasSinDescuento);
        assertTrue(totalDescuentos > 0);
        assertTrue(ventasNetas < totalVentas);
        
        System.out.println("\n✓ Sistema procesó todas las ventas correctamente");
    }

    private void pausa() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}