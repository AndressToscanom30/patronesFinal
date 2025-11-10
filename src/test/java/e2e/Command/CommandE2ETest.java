package e2e.Command;

import Comportamentales.Command.controlador.CommandController;
import Comportamentales.Command.modelo.*;
import Comportamentales.Command.vista.ConsolaCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Command Pattern - End-to-End Tests")
public class CommandE2ETest {
    private CommandController controller;
    private ConsolaCommand vista;
    private Producto leche;
    private Producto pan;
    private Producto huevos;
    private Producto arroz;

    @BeforeEach
    public void setUp() {
        vista = new ConsolaCommand();
        controller = new CommandController(vista);
        leche = new Producto("P001", "Leche Entera 1L", 3500, "Lácteos");
        pan = new Producto("P002", "Pan Integral", 2500, "Panadería");
        huevos = new Producto("P003", "Huevos x12", 8000, "Huevos");
        arroz = new Producto("P004", "Arroz 1kg", 4000, "Granos");
    }

    @Test
    @DisplayName("E2E - Cliente realiza compra simple")
    public void testClienteRealizaCompraSimple() {
        System.out.println("\n=== ESCENARIO E2E: COMPRA SIMPLE ===\n");
        
        System.out.println("Cliente ingresa al supermercado...");
        System.out.println("\nPaso 1: Agregar productos al carrito");
        
        controller.agregarProducto(leche, 2);
        System.out.println("✓ Agregado: 2 x Leche");
        
        controller.agregarProducto(pan, 1);
        System.out.println("✓ Agregado: 1 x Pan");
        
        controller.agregarProducto(huevos, 1);
        System.out.println("✓ Agregado: 1 x Huevos");
        
        System.out.println("\nCarrito actual:");
        System.out.printf("Total: $%.2f%n", controller.getCarrito().calcularTotal());
        
        assertEquals(3, controller.getCarrito().getCantidadItems());
        assertEquals(17500, controller.getCarrito().calcularTotal(), 0.01);
        
        System.out.println("\n✓ Compra simple completada");
    }

    @Test
    @DisplayName("E2E - Cliente cambia de opinión y usa Undo")
    public void testClienteCambiaOpinionUndo() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE USA UNDO ===\n");
        
        System.out.println("Cliente agrega productos al carrito:");
        
        controller.agregarProducto(leche, 2);
        System.out.println("✓ Agregado: 2 x Leche ($7,000)");
        
        controller.agregarProducto(pan, 3);
        System.out.println("✓ Agregado: 3 x Pan ($7,500)");
        
        controller.agregarProducto(huevos, 2);
        System.out.println("✓ Agregado: 2 x Huevos ($16,000)");
        
        double totalAntes = controller.getCarrito().calcularTotal();
        System.out.printf("\nTotal: $%.2f%n", totalAntes);
        
        System.out.println("\n¡Cliente se arrepiente de los huevos!");
        System.out.println("Presiona DESHACER (Ctrl+Z)...");
        
        controller.deshacer();
        
        double totalDespues = controller.getCarrito().calcularTotal();
        System.out.printf("Nuevo total: $%.2f%n", totalDespues);
        
        assertEquals(2, controller.getCarrito().getCantidadItems());
        assertEquals(14500, totalDespues, 0.01);
        assertTrue(totalDespues < totalAntes);
        
        System.out.println("\n✓ Operación deshecha exitosamente");
    }

    @Test
    @DisplayName("E2E - Cliente usa Undo y luego Redo")
    public void testClienteUsaUndoYRedo() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE USA UNDO Y REDO ===\n");
        
        System.out.println("Paso 1: Cliente agrega productos");
        controller.agregarProducto(leche, 2);
        controller.agregarProducto(pan, 1);
        
        System.out.println("✓ Carrito: Leche (2), Pan (1)");
        System.out.printf("Total: $%.2f%n", controller.getCarrito().calcularTotal());
        
        System.out.println("\nPaso 2: Cliente deshace última acción (quita pan)");
        controller.deshacer();
        System.out.println("✓ Pan eliminado del carrito");
        System.out.printf("Nuevo total: $%.2f%n", controller.getCarrito().calcularTotal());
        
        assertEquals(1, controller.getCarrito().getCantidadItems());
        
        System.out.println("\nPaso 3: Cliente se arrepiente y rehace (vuelve el pan)");
        controller.rehacer();
        System.out.println("✓ Pan de vuelta en el carrito");
        System.out.printf("Total final: $%.2f%n", controller.getCarrito().calcularTotal());
        
        assertEquals(2, controller.getCarrito().getCantidadItems());
        assertEquals(9500, controller.getCarrito().calcularTotal(), 0.01);
        
        System.out.println("\n✓ Undo/Redo funcionando perfectamente");
    }

    @Test
    @DisplayName("E2E - Cliente aplica y quita cupón de descuento")
    public void testClienteAplicaYQuitaCupon() {
        System.out.println("\n=== ESCENARIO E2E: CUPÓN DE DESCUENTO ===\n");
        
        System.out.println("Cliente agrega productos:");
        controller.agregarProducto(leche, 3);
        controller.agregarProducto(huevos, 1);
        controller.agregarProducto(arroz, 2);
        
        double totalSinDescuento = controller.getCarrito().calcularTotal();
        System.out.printf("Total sin descuento: $%.2f%n", totalSinDescuento);
        
        System.out.println("\nCliente aplica cupón del 20% de descuento...");
        controller.aplicarDescuento(20);
        
        double totalConDescuento = controller.getCarrito().calcularTotal();
        double descuento = controller.getCarrito().calcularDescuento();
        
        System.out.printf("Descuento: -$%.2f (20%%)%n", descuento);
        System.out.printf("Total con descuento: $%.2f%n", totalConDescuento);
        
        assertEquals(21200.0, totalConDescuento, 0.01);
        
        System.out.println("\nCliente se arrepiente y quita el cupón (Undo)...");
        controller.deshacer();
        
        System.out.printf("Total sin cupón: $%.2f%n", controller.getCarrito().calcularTotal());
        assertEquals(totalSinDescuento, controller.getCarrito().calcularTotal(), 0.01);
        
        System.out.println("\n✓ Gestión de cupones con Undo completada");
    }

    @Test
    @DisplayName("E2E - Cliente vacía carrito y lo restaura")
    public void testClienteVaciaYRestaurar() {
        System.out.println("\n=== ESCENARIO E2E: VACIAR Y RESTAURAR CARRITO ===\n");
        
        System.out.println("Cliente agrega varios productos:");
        controller.agregarProducto(leche, 2);
        controller.agregarProducto(pan, 3);
        controller.agregarProducto(huevos, 1);
        controller.agregarProducto(arroz, 2);
        
        int cantidadAntes = controller.getCarrito().getCantidadItems();
        double totalAntes = controller.getCarrito().calcularTotal();
        
        System.out.printf("Productos en carrito: %d%n", cantidadAntes);
        System.out.printf("Total: $%.2f%n", totalAntes);
        
        System.out.println("\nCliente decide vaciar todo el carrito...");
        controller.vaciarCarrito();
        
        assertTrue(controller.getCarrito().estaVacio());
        System.out.println("✓ Carrito vaciado");
        
        System.out.println("\n¡Cliente se arrepiente! Presiona DESHACER...");
        controller.deshacer();
        
        System.out.printf("Productos restaurados: %d%n", controller.getCarrito().getCantidadItems());
        System.out.printf("Total restaurado: $%.2f%n", controller.getCarrito().calcularTotal());
        
        assertEquals(cantidadAntes, controller.getCarrito().getCantidadItems());
        assertEquals(totalAntes, controller.getCarrito().calcularTotal(), 0.01);
        
        System.out.println("\n✓ Carrito completamente restaurado");
    }

    @Test
    @DisplayName("E2E - Flujo completo de compra con múltiples cambios")
    public void testFlujoCompletoConMultiplesCambios() {
        System.out.println("\n=== ESCENARIO E2E: FLUJO COMPLETO DE COMPRA ===\n");
        
        System.out.println("═".repeat(50));
        System.out.println("PASO 1: Cliente agrega productos iniciales");
        System.out.println("═".repeat(50));
        controller.agregarProducto(leche, 2);
        controller.agregarProducto(pan, 1);
        System.out.println("Carrito: Leche (2), Pan (1)");
        System.out.printf("Total: $%.2f%n", controller.getCarrito().calcularTotal());
        
        pausa();
        
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 2: Cliente agrega más productos");
        System.out.println("═".repeat(50));
        controller.agregarProducto(huevos, 1);
        controller.agregarProducto(arroz, 2);
        System.out.println("Agregados: Huevos (1), Arroz (2)");
        System.out.printf("Total: $%.2f%n", controller.getCarrito().calcularTotal());
        
        pausa();
        
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 3: Cliente aplica cupón de descuento");
        System.out.println("═".repeat(50));
        controller.aplicarDescuento(15);
        System.out.println("Cupón del 15% aplicado");
        System.out.printf("Total con descuento: $%.2f%n", controller.getCarrito().calcularTotal());
        
        pausa();
        
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 4: Cliente decide quitar el arroz");
        System.out.println("═".repeat(50));
        controller.eliminarProducto(arroz, 2);
        System.out.println("Arroz eliminado");
        System.out.printf("Nuevo total: $%.2f%n", controller.getCarrito().calcularTotal());
        
        pausa();
        
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 5: Cliente se arrepiente (Undo)");
        System.out.println("═".repeat(50));
        controller.deshacer();
        System.out.println("Arroz de vuelta en el carrito");
        System.out.printf("Total restaurado: $%.2f%n", controller.getCarrito().calcularTotal());
        
        pausa();
        
        System.out.println("\n═".repeat(50));
        System.out.println("HISTORIAL DE OPERACIONES");
        System.out.println("═".repeat(50));
        int contador = 1;
        for (String operacion : controller.obtenerHistorial()) {
            System.out.printf("%d. %s%n", contador++, operacion);
        }
        
        System.out.println("\n═".repeat(50));
        System.out.println("RESUMEN FINAL");
        System.out.println("═".repeat(50));
        System.out.printf("Productos en carrito: %d%n", controller.getCarrito().getCantidadItems());
        System.out.printf("Subtotal: $%.2f%n", controller.getCarrito().calcularSubtotal());
        System.out.printf("Descuento (15%%): -$%.2f%n", controller.getCarrito().calcularDescuento());
        System.out.printf("TOTAL A PAGAR: $%.2f%n", controller.getCarrito().calcularTotal());
        System.out.println("═".repeat(50));
        
        assertEquals(4, controller.getCarrito().getCantidadItems());
        assertTrue(controller.getCarrito().getDescuento() > 0);
        
        System.out.println("\n✓ Flujo completo de compra finalizado exitosamente");
    }

    @Test
    @DisplayName("E2E - Cliente modifica cantidades con Undo/Redo")
    public void testClienteModificaCantidades() {
        System.out.println("\n=== ESCENARIO E2E: MODIFICAR CANTIDADES ===\n");
        
        System.out.println("Cliente agrega 1 leche al carrito");
        controller.agregarProducto(leche, 1);
        System.out.printf("Total: $%.2f%n", controller.getCarrito().calcularTotal());
        
        System.out.println("\nCliente agrega 2 leches más");
        controller.agregarProducto(leche, 2);
        System.out.printf("Total: $%.2f (3 leches)%n", controller.getCarrito().calcularTotal());
        
        assertEquals(10500, controller.getCarrito().calcularTotal(), 0.01);
        
        System.out.println("\nCliente cree que son muchas, deshace última acción");
        controller.deshacer();
        System.out.printf("Total: $%.2f (1 leche)%n", controller.getCarrito().calcularTotal());
        
        assertEquals(3500, controller.getCarrito().calcularTotal(), 0.01);
        
        System.out.println("\nCliente cambia de opinión, rehace");
        controller.rehacer();
        System.out.printf("Total: $%.2f (3 leches otra vez)%n", controller.getCarrito().calcularTotal());
        
        assertEquals(10500, controller.getCarrito().calcularTotal(), 0.01);
        
        System.out.println("\n✓ Modificación de cantidades completada");
    }

    @Test
    @DisplayName("E2E - Escenario de indecisión extrema")
    public void testEscenarioIndecisionExtrema() {
        System.out.println("\n=== ESCENARIO E2E: CLIENTE INDECISO ===\n");
        
        System.out.println("Cliente muy indeciso haciendo compras...\n");
        
        // Agrega productos
        System.out.println("1. Agrega leche");
        controller.agregarProducto(leche, 2);
        
        System.out.println("2. Agrega pan");
        controller.agregarProducto(pan, 1);
        
        System.out.println("3. Agrega huevos");
        controller.agregarProducto(huevos, 1);
        
        System.out.printf("\nTotal: $%.2f%n", controller.getCarrito().calcularTotal());
        
        // Cambia de opinión
        System.out.println("\n¡Se arrepiente de TODO!");
        System.out.println("Deshace... deshace... deshace...");
        
        controller.deshacer(); // Quita huevos
        controller.deshacer(); // Quita pan
        controller.deshacer(); // Quita leche
        
        assertTrue(controller.getCarrito().estaVacio());
        System.out.println("Carrito vacío");
        
        // Vuelve a cambiar de opinión
        System.out.println("\n¡Espera! Sí quiere todo!");
        System.out.println("Rehace... rehace... rehace...");
        
        controller.rehacer(); // Vuelve leche
        controller.rehacer(); // Vuelve pan
        controller.rehacer(); // Vuelve huevos
        
        assertEquals(3, controller.getCarrito().getCantidadItems());
        System.out.printf("\nTotal final: $%.2f%n", controller.getCarrito().calcularTotal());
        
        System.out.println("\n✓ Cliente finalmente decidió");
        System.out.println("✓ Sistema manejó perfectamente la indecisión");
    }

    private void pausa() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}