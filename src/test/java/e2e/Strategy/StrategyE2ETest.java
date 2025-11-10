package e2e.Strategy;

import Comportamentales.Strategy.controlador.StrategyController;
import Comportamentales.Strategy.modelo.*;
import Comportamentales.Strategy.vista.ConsolaStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Strategy Pattern - End-to-End Tests")
public class StrategyE2ETest {
    private StrategyController controller;
    private ConsolaStrategy vista;

    @BeforeEach
    public void setUp() {
        vista = new ConsolaStrategy();
        controller = new StrategyController(vista);
    }

    @Test
    @DisplayName("E2E - Cliente realiza compra con descuento porcentaje")
    public void testCompraConDescuentoPorcentaje() {
        System.out.println("\n=== ESCENARIO E2E: COMPRA CON DESCUENTO PORCENTAJE ===\n");
        
        // Cliente agrega productos al carrito
        System.out.println("Cliente agrega productos al carrito:");
        System.out.println("- Laptop: $800");
        System.out.println("- Mouse: $25");
        System.out.println("- Teclado: $75");
        
        double totalCompra = 900;
        System.out.printf("\nTotal de compra: $%.2f%n", totalCompra);
        
        CarritoCompra carrito = controller.crearCarrito(totalCompra);
        
        // Cliente tiene cupón de 15% de descuento
        System.out.println("\nCliente aplica cupón de descuento del 15%");
        DescuentoStrategy descuento = controller.crearDescuentoPorcentaje(15);
        controller.aplicarEstrategia(carrito, descuento);
        
        // Calcular total
        double totalConDescuento = controller.calcularTotalConDescuento(carrito);
        double ahorro = totalCompra - totalConDescuento;
        
        System.out.println("\n" + "═".repeat(50));
        System.out.printf("Total original: $%.2f%n", totalCompra);
        System.out.printf("Descuento (15%%): -$%.2f%n", ahorro);
        System.out.printf("TOTAL A PAGAR: $%.2f%n", totalConDescuento);
        System.out.println("═".repeat(50));
        
        assertEquals(765, totalConDescuento, 0.01);
        assertEquals(135, ahorro, 0.01);
        
        System.out.println("\n✓ Compra completada exitosamente");
    }

    @Test
    @DisplayName("E2E - Cliente usa promoción 2x1")
    public void testPromocion2x1() {
        System.out.println("\n=== ESCENARIO E2E: PROMOCIÓN 2x1 ===\n");
        
        System.out.println("Cliente compra 2 productos iguales:");
        System.out.println("- 2x Shampoo ($10 c/u) = $20");
        
        double totalCompra = 20;
        CarritoCompra carrito = controller.crearCarrito(totalCompra);
        
        // Aplicar promoción 2x1
        System.out.println("\nPromoción 2x1 aplicada: Paga 1, lleva 2");
        DescuentoStrategy promo = controller.crearDescuento2x1();
        controller.aplicarEstrategia(carrito, promo);
        
        double totalConPromo = controller.calcularTotalConDescuento(carrito);
        
        System.out.println("\n" + "═".repeat(50));
        System.out.printf("Precio normal 2 productos: $%.2f%n", totalCompra);
        System.out.printf("Con promoción 2x1: $%.2f%n", totalConPromo);
        System.out.printf("Ahorro: $%.2f (50%%)%n", totalCompra - totalConPromo);
        System.out.println("═".repeat(50));
        
        assertEquals(10, totalConPromo, 0.01);
        
        System.out.println("\n✓ Promoción aplicada exitosamente");
    }

    @Test
    @DisplayName("E2E - Cliente compara estrategias antes de pagar")
    public void testClienteComparaEstrategias() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE COMPARA ESTRATEGIAS ===\n");
        
        double totalCompra = 1000;
        System.out.printf("Total de compra: $%.2f%n", totalCompra);
        System.out.println("\nCliente tiene varias opciones de descuento:");
        
        // Opción 1: Cupón 10%
        CarritoCompra carrito1 = controller.crearCarrito(totalCompra);
        carrito1.setEstrategia(controller.crearDescuentoPorcentaje(10));
        double total1 = carrito1.calcularTotal();
        System.out.printf("\n1. Cupón 10%% descuento: $%.2f (ahorro: $%.2f)%n", 
            total1, totalCompra - total1);
        
        // Opción 2: Cupón 15%
        CarritoCompra carrito2 = controller.crearCarrito(totalCompra);
        carrito2.setEstrategia(controller.crearDescuentoPorcentaje(15));
        double total2 = carrito2.calcularTotal();
        System.out.printf("2. Cupón 15%% descuento: $%.2f (ahorro: $%.2f)%n", 
            total2, totalCompra - total2);
        
        // Opción 3: Promoción 2x1
        CarritoCompra carrito3 = controller.crearCarrito(totalCompra);
        carrito3.setEstrategia(controller.crearDescuento2x1());
        double total3 = carrito3.calcularTotal();
        System.out.printf("3. Promoción 2x1: $%.2f (ahorro: $%.2f)%n", 
            total3, totalCompra - total3);
        
        // Cliente elige la mejor opción
        System.out.println("\n" + "═".repeat(50));
        System.out.println("MEJOR OPCIÓN: Promoción 2x1");
        System.out.printf("Total a pagar: $%.2f%n", total3);
        System.out.println("═".repeat(50));
        
        assertTrue(total3 < total2);
        assertTrue(total2 < total1);
        
        System.out.println("\n✓ Cliente eligió la mejor estrategia");
    }

    @Test
    @DisplayName("E2E - Cliente VIP con descuento especial")
    public void testClienteVIP() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE VIP ===\n");
        
        System.out.println("Cliente VIP realiza compra grande:");
        System.out.println("- Smart TV 55': $1,500");
        System.out.println("- Soundbar: $300");
        System.out.println("- Soporte: $50");
        
        double totalCompra = 1850;
        System.out.printf("\nTotal de compra: $%.2f%n", totalCompra);
        
        CarritoCompra carrito = controller.crearCarrito(totalCompra);
        
        // Cliente VIP tiene 25% de descuento
        System.out.println("\nDescuento VIP: 25%");
        DescuentoStrategy descuentoVIP = controller.crearDescuentoPorcentaje(25);
        controller.aplicarEstrategia(carrito, descuentoVIP);
        
        double totalVIP = controller.calcularTotalConDescuento(carrito);
        double ahorroVIP = totalCompra - totalVIP;
        
        System.out.println("\n" + "═".repeat(50));
        System.out.printf("Total original: $%.2f%n", totalCompra);
        System.out.printf("Descuento VIP (25%%): -$%.2f%n", ahorroVIP);
        System.out.printf("TOTAL A PAGAR: $%.2f%n", totalVIP);
        System.out.println("═".repeat(50));
        
        assertEquals(1387.5, totalVIP, 0.01);
        assertEquals(462.5, ahorroVIP, 0.01);
        
        System.out.println("\n✓ Beneficio VIP aplicado exitosamente");
    }

    @Test
    @DisplayName("E2E - Black Friday con múltiples descuentos")
    public void testBlackFriday() {
        System.out.println("\n=== ESCENARIO E2E: BLACK FRIDAY ===\n");
        
        System.out.println("EVENTO: Black Friday - Mega Descuentos");
        
        double totalCompra = 2000;
        System.out.printf("\nTotal de compra: $%.2f%n", totalCompra);
        
        // Descuento Black Friday del 50%
        CarritoCompra carrito = controller.crearCarrito(totalCompra);
        System.out.println("\nAplicando descuento Black Friday: 50% OFF");
        
        DescuentoStrategy blackFriday = controller.crearDescuentoPorcentaje(50);
        controller.aplicarEstrategia(carrito, blackFriday);
        
        double totalFinal = controller.calcularTotalConDescuento(carrito);
        double ahorro = totalCompra - totalFinal;
        
        System.out.println("\n" + "═".repeat(50));
        System.out.printf("Total original: $%.2f%n", totalCompra);
        System.out.printf("BLACK FRIDAY 50%% OFF: -$%.2f%n", ahorro);
        System.out.printf("TOTAL A PAGAR: $%.2f%n", totalFinal);
        System.out.println("═".repeat(50));
        
        assertEquals(1000, totalFinal, 0.01);
        assertEquals(1000, ahorro, 0.01);
        
        System.out.println("\n✓ ¡Aprovecha esta oferta increíble!");
    }

    @Test
    @DisplayName("E2E - Cliente cambia de estrategia en el carrito")
    public void testCambioEstrategia() {
        System.out.println("\n=== ESCENARIO E2E: CAMBIO DE ESTRATEGIA ===\n");
        
        double totalCompra = 500;
        System.out.printf("Total de compra: $%.2f%n", totalCompra);
        
        CarritoCompra carrito = controller.crearCarrito(totalCompra);
        
        // Cliente aplica cupón del 10%
        System.out.println("\n1. Cliente aplica cupón del 10%");
        DescuentoStrategy desc10 = controller.crearDescuentoPorcentaje(10);
        controller.aplicarEstrategia(carrito, desc10);
        double total1 = controller.calcularTotalConDescuento(carrito);
        System.out.printf("   Total con 10%%: $%.2f%n", total1);
        
        // Cliente encuentra mejor promoción (20%)
        System.out.println("\n2. Cliente encuentra cupón del 20%");
        DescuentoStrategy desc20 = controller.crearDescuentoPorcentaje(20);
        controller.aplicarEstrategia(carrito, desc20);
        double total2 = controller.calcularTotalConDescuento(carrito);
        System.out.printf("   Total con 20%%: $%.2f (mejor opción!)%n", total2);
        
        // Cliente ve promoción 2x1
        System.out.println("\n3. Cliente ve promoción 2x1 en algunos productos");
        DescuentoStrategy desc2x1 = controller.crearDescuento2x1();
        controller.aplicarEstrategia(carrito, desc2x1);
        double total3 = controller.calcularTotalConDescuento(carrito);
        System.out.printf("   Total con 2x1: $%.2f (¡MEJOR!)%n", total3);
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("ESTRATEGIA FINAL ELEGIDA: 2x1");
        System.out.printf("Total a pagar: $%.2f%n", total3);
        System.out.printf("Ahorro máximo: $%.2f%n", totalCompra - total3);
        System.out.println("═".repeat(50));
        
        assertEquals(450, total1, 0.01);
        assertEquals(400, total2, 0.01);
        assertEquals(250, total3, 0.01);
        
        System.out.println("\n✓ Cliente optimizó su ahorro cambiando estrategias");
    }

    @Test
    @DisplayName("E2E - Flujo completo de compra con Strategy")
    public void testFlujoCompletoCompra() {
        System.out.println("\n=== ESCENARIO E2E: FLUJO COMPLETO DE COMPRA ===\n");
        
        // 1. Cliente navega por productos
        System.out.println("PASO 1: Cliente selecciona productos");
        System.out.println("- Producto A: $200");
        System.out.println("- Producto B: $150");
        System.out.println("- Producto C: $50");
        
        double totalCompra = 400;
        System.out.printf("\nSubtotal: $%.2f%n", totalCompra);
        
        CarritoCompra carrito = controller.crearCarrito(totalCompra);
        
        // 2. Sistema muestra descuentos disponibles
        System.out.println("\n" + "-".repeat(50));
        System.out.println("PASO 2: Descuentos disponibles");
        System.out.println("-".repeat(50));
        System.out.println("A) Cupón 15% de descuento");
        System.out.println("B) Promoción 2x1 (50% desc)");
        System.out.println("C) Compra 3 y paga 2 (33% desc)");
        
        // 3. Cliente selecciona mejor opción
        System.out.println("\nPASO 3: Cliente analiza opciones...");
        
        CarritoCompra temp1 = controller.crearCarrito(totalCompra);
        temp1.setEstrategia(controller.crearDescuentoPorcentaje(15));
        double opcionA = temp1.calcularTotal();
        System.out.printf("Opción A (15%%): $%.2f%n", opcionA);
        
        CarritoCompra temp2 = controller.crearCarrito(totalCompra);
        temp2.setEstrategia(controller.crearDescuento2x1());
        double opcionB = temp2.calcularTotal();
        System.out.printf("Opción B (2x1): $%.2f%n", opcionB);
        
        CarritoCompra temp3 = controller.crearCarrito(totalCompra);
        temp3.setEstrategia(controller.crearDescuento3x2(3, 33));
        double opcionC = temp3.calcularTotal();
        System.out.printf("Opción C (3x2): $%.2f%n", opcionC);
        
        // 4. Cliente elige opción B (2x1)
        System.out.println("\nPASO 4: Cliente selecciona promoción 2x1");
        DescuentoStrategy estrategiaElegida = controller.crearDescuento2x1();
        controller.aplicarEstrategia(carrito, estrategiaElegida);
        
        // 5. Finalizar compra
        double totalFinal = controller.calcularTotalConDescuento(carrito);
        double ahorroTotal = totalCompra - totalFinal;
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("PASO 5: RESUMEN DE COMPRA");
        System.out.println("═".repeat(50));
        System.out.printf("Subtotal: $%.2f%n", totalCompra);
        System.out.printf("Descuento aplicado: -$%.2f%n", ahorroTotal);
        System.out.printf("TOTAL A PAGAR: $%.2f%n", totalFinal);
        System.out.println("═".repeat(50));
        
        assertEquals(200, totalFinal, 0.01);
        assertEquals(200, ahorroTotal, 0.01);
        
        System.out.println("\n✓ Compra finalizada con éxito");
        System.out.println("✓ Cliente ahorró $200 usando la mejor estrategia");
    }
}