package Comportamentales.Command.vista;

import Comportamentales.Command.controlador.CommandController;
import Comportamentales.Command.modelo.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsolaCommand {
    private final CommandController controlador;
    private final Scanner scanner;
    private final List<Producto> catalogoProductos;

    public ConsolaCommand() {
        this.controlador = new CommandController(this);
        this.scanner = new Scanner(System.in);
        this.catalogoProductos = new ArrayList<>();
        inicializarCatalogo();
    }

    private void inicializarCatalogo() {
        catalogoProductos.add(new Producto("P001", "Leche Entera 1L", 3500, "Lácteos"));
        catalogoProductos.add(new Producto("P002", "Pan Integral", 2500, "Panadería"));
        catalogoProductos.add(new Producto("P003", "Huevos x12", 8000, "Huevos"));
        catalogoProductos.add(new Producto("P004", "Arroz 1kg", 4000, "Granos"));
        catalogoProductos.add(new Producto("P005", "Aceite 1L", 12000, "Aceites"));
        catalogoProductos.add(new Producto("P006", "Azúcar 1kg", 3000, "Endulzantes"));
        catalogoProductos.add(new Producto("P007", "Café 500g", 15000, "Bebidas"));
        catalogoProductos.add(new Producto("P008", "Pasta 500g", 2800, "Granos"));
    }

    public void mostrarMenu() {
        System.out.println("\n=== COMMAND - Gestión de Carrito de Compras ===");
        System.out.println("1. Ver catálogo de productos");
        System.out.println("2. Agregar producto al carrito");
        System.out.println("3. Eliminar producto del carrito");
        System.out.println("4. Aplicar descuento");
        System.out.println("5. Vaciar carrito");
        System.out.println("6. Ver carrito");
        System.out.println("7. Deshacer última operación (Undo)");
        System.out.println("8. Rehacer operación (Redo)");
        System.out.println("9. Ver historial de operaciones");
        System.out.println("10. Demo: Flujo completo con Undo/Redo");
        System.out.println("11. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   CARRITO DE COMPRAS - PATRÓN COMMAND      ║");
        System.out.println("║   Operaciones reversibles con Undo/Redo    ║");
        System.out.println("╚════════════════════════════════════════════╝");

        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> verCatalogo();
                case 2 -> agregarProducto();
                case 3 -> eliminarProducto();
                case 4 -> aplicarDescuento();
                case 5 -> vaciarCarrito();
                case 6 -> verCarrito();
                case 7 -> deshacer();
                case 8 -> rehacer();
                case 9 -> verHistorial();
                case 10 -> demoFlujoCompleto();
                case 11 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("\n¡Gracias por usar el sistema de carrito!");
    }

    private void verCatalogo() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          CATÁLOGO DE PRODUCTOS             ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        for (int i = 0; i < catalogoProductos.size(); i++) {
            Producto p = catalogoProductos.get(i);
            System.out.printf("%d. %s - $%.2f [%s]%n", 
                (i + 1), p.getNombre(), p.getPrecio(), p.getCategoria());
        }
    }

    private void agregarProducto() {
        System.out.println("\n--- Agregar Producto al Carrito ---");
        
        verCatalogo();
        
        System.out.print("\nSeleccione producto (1-" + catalogoProductos.size() + "): ");
        int indice = scanner.nextInt() - 1;
        
        if (indice < 0 || indice >= catalogoProductos.size()) {
            System.out.println("✗ Producto inválido");
            return;
        }
        
        System.out.print("Cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();
        
        if (cantidad <= 0) {
            System.out.println("✗ Cantidad inválida");
            return;
        }
        
        Producto producto = catalogoProductos.get(indice);
        controlador.agregarProducto(producto, cantidad);
    }

    private void eliminarProducto() {
        System.out.println("\n--- Eliminar Producto del Carrito ---");
        
        if (controlador.getCarrito().estaVacio()) {
            System.out.println("✗ El carrito está vacío");
            return;
        }
        
        List<ItemCarrito> items = controlador.getCarrito().getItems();
        
        System.out.println("\nProductos en el carrito:");
        for (int i = 0; i < items.size(); i++) {
            ItemCarrito item = items.get(i);
            System.out.printf("%d. %s%n", (i + 1), item);
        }
        
        System.out.print("\nSeleccione producto a eliminar (1-" + items.size() + "): ");
        int indice = scanner.nextInt() - 1;
        
        if (indice < 0 || indice >= items.size()) {
            System.out.println("✗ Selección inválida");
            return;
        }
        
        System.out.print("Cantidad a eliminar: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();
        
        if (cantidad <= 0) {
            System.out.println("✗ Cantidad inválida");
            return;
        }
        
        ItemCarrito item = items.get(indice);
        controlador.eliminarProducto(item.getProducto(), cantidad);
    }

    private void aplicarDescuento() {
        System.out.println("\n--- Aplicar Descuento ---");
        
        if (controlador.getCarrito().estaVacio()) {
            System.out.println("✗ El carrito está vacío");
            return;
        }
        
        System.out.print("Porcentaje de descuento (ejemplo: 10 para 10%): ");
        double porcentaje = scanner.nextDouble();
        scanner.nextLine();
        
        if (porcentaje < 0 || porcentaje > 100) {
            System.out.println("✗ Porcentaje inválido");
            return;
        }
        
        controlador.aplicarDescuento(porcentaje);
    }

    private void vaciarCarrito() {
        System.out.println("\n--- Vaciar Carrito ---");
        
        if (controlador.getCarrito().estaVacio()) {
            System.out.println("✗ El carrito ya está vacío");
            return;
        }
        
        System.out.print("¿Está seguro de vaciar el carrito? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("s")) {
            controlador.vaciarCarrito();
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private void verCarrito() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          CARRITO DE COMPRAS                ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        CarritoCompras carrito = controlador.getCarrito();
        
        if (carrito.estaVacio()) {
            System.out.println("\nEl carrito está vacío");
            return;
        }
        
        System.out.println("\nProductos:");
        for (ItemCarrito item : carrito.getItems()) {
            System.out.println("  " + item);
        }
        
        System.out.println("\n" + "─".repeat(50));
        System.out.printf("Subtotal: $%.2f%n", carrito.calcularSubtotal());
        
        if (carrito.getDescuento() > 0) {
            System.out.printf("Descuento (%.2f%%): -$%.2f%n", 
                carrito.getDescuento(), carrito.calcularDescuento());
        }
        
        System.out.printf("TOTAL: $%.2f%n", carrito.calcularTotal());
        System.out.println("─".repeat(50));
    }

    private void deshacer() {
        System.out.println("\n--- Deshacer Operación (Undo) ---");
        
        if (!controlador.puedeDeshacer()) {
            System.out.println("✗ No hay operaciones para deshacer");
            return;
        }
        
        controlador.deshacer();
    }

    private void rehacer() {
        System.out.println("\n--- Rehacer Operación (Redo) ---");
        
        if (!controlador.puedeRehacer()) {
            System.out.println("✗ No hay operaciones para rehacer");
            return;
        }
        
        controlador.rehacer();
    }

    private void verHistorial() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       HISTORIAL DE OPERACIONES            ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        List<String> historial = controlador.obtenerHistorial();
        
        if (historial.isEmpty()) {
            System.out.println("No hay operaciones en el historial");
            return;
        }
        
        for (int i = 0; i < historial.size(); i++) {
            System.out.printf("%d. %s%n", (i + 1), historial.get(i));
        }
        
        System.out.println("\n" + "─".repeat(50));
        System.out.println("Puede deshacer: " + (controlador.puedeDeshacer() ? "Sí" : "No"));
        System.out.println("Puede rehacer: " + (controlador.puedeRehacer() ? "Sí" : "No"));
    }

    private void demoFlujoCompleto() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       DEMO: FLUJO COMPLETO                 ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        System.out.println("Simulando compra con operaciones reversibles...\n");
        
        // Operación 1: Agregar leche
        System.out.println("═".repeat(50));
        System.out.println("PASO 1: Cliente agrega 2 litros de leche");
        System.out.println("═".repeat(50));
        controlador.agregarProducto(catalogoProductos.get(0), 2);
        verCarrito();
        pausa();
        
        // Operación 2: Agregar pan
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 2: Cliente agrega 1 pan integral");
        System.out.println("═".repeat(50));
        controlador.agregarProducto(catalogoProductos.get(1), 1);
        verCarrito();
        pausa();
        
        // Operación 3: Agregar huevos
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 3: Cliente agrega 1 caja de huevos");
        System.out.println("═".repeat(50));
        controlador.agregarProducto(catalogoProductos.get(2), 1);
        verCarrito();
        pausa();
        
        // Operación 4: Cliente se arrepiente del pan
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 4: Cliente se arrepiente y deshace (quita el pan)");
        System.out.println("═".repeat(50));
        controlador.deshacer();
        verCarrito();
        pausa();
        
        // Operación 5: Cliente aplica descuento
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 5: Cliente aplica cupón de 15% descuento");
        System.out.println("═".repeat(50));
        controlador.aplicarDescuento(15);
        verCarrito();
        pausa();
        
        // Operación 6: Cliente decide que sí quiere el pan
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 6: Cliente rehace (vuelve a agregar el pan)");
        System.out.println("═".repeat(50));
        controlador.rehacer();
        verCarrito();
        pausa();
        
        // Mostrar historial final
        System.out.println("\n═".repeat(50));
        System.out.println("HISTORIAL FINAL DE OPERACIONES");
        System.out.println("═".repeat(50));
        verHistorial();
        
        System.out.println("\n✓ Demo completado - Todas las operaciones son reversibles");
    }

    private void pausa() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("ERROR: " + error);
    }

    public static void main(String[] args) {
        ConsolaCommand consola = new ConsolaCommand();
        consola.ejecutar();
    }
}