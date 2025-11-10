package Estructurales.Facade.vista;

import Estructurales.Facade.controlador.VentasController;
import Estructurales.Facade.modelo.*;
import java.util.Scanner;

public class ConsolaFacade {
    private final VentasController controlador;
    private final Scanner scanner;

    public ConsolaFacade() {
        this.controlador = new VentasController(this);
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== FACADE - Sistema de Ventas del Supermercado ===");
        System.out.println("1. Realizar venta simple");
        System.out.println("2. Realizar venta con múltiples productos");
        System.out.println("3. Consultar puntos de cliente");
        System.out.println("4. Generar reporte de ventas");
        System.out.println("5. Venta express (demo rápida)");
        System.out.println("6. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE VENTAS - PATRÓN FACADE        ║");
        System.out.println("║   Simplificando procesos complejos         ║");
        System.out.println("╚════════════════════════════════════════════╝");

        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> realizarVentaSimple();
                case 2 -> realizarVentaMultiple();
                case 3 -> consultarPuntos();
                case 4 -> generarReporte();
                case 5 -> ventaExpressDemo();
                case 6 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("\n¡Gracias por usar el sistema de ventas!");
    }

    private void realizarVentaSimple() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║         REALIZAR VENTA SIMPLE              ║");
        System.out.println("╚════════════════════════════════════════════╝");

        Venta venta = new Venta();

        System.out.print("\nID del cliente: ");
        String clienteId = scanner.nextLine();
        venta.setClienteId(clienteId);

        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        venta.setClienteNombre(nombre);

        System.out.print("Email del cliente: ");
        String email = scanner.nextLine();
        venta.setClienteEmail(email);

        System.out.print("\nCódigo del producto: ");
        String codigo = scanner.nextLine();

        System.out.print("Nombre del producto: ");
        String nombreProd = scanner.nextLine();

        System.out.print("Cantidad: ");
        int cantidad = scanner.nextInt();

        System.out.print("Precio unitario: ");
        double precio = scanner.nextDouble();
        scanner.nextLine();

        venta.agregarItem(codigo, nombreProd, cantidad, precio);

        System.out.print("\nMétodo de pago (EFECTIVO/TARJETA/TRANSFERENCIA): ");
        String metodoPago = scanner.nextLine();
        venta.setMetodoPago(metodoPago);

        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESUMEN DE LA COMPRA");
        System.out.println("═".repeat(50));
        System.out.printf("Subtotal: $%.2f%n", venta.getSubtotal());
        System.out.printf("Impuestos (19%%): $%.2f%n", venta.getImpuestos());
        System.out.printf("TOTAL: $%.2f%n", venta.getTotal());
        System.out.println("═".repeat(50));

        System.out.print("\n¿Confirmar venta? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            controlador.procesarVenta(venta);
        } else {
            System.out.println("✗ Venta cancelada");
        }
    }

    private void realizarVentaMultiple() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       REALIZAR VENTA MÚLTIPLE              ║");
        System.out.println("╚════════════════════════════════════════════╝");

        Venta venta = new Venta();

        System.out.print("\nID del cliente: ");
        String clienteId = scanner.nextLine();
        venta.setClienteId(clienteId);

        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        venta.setClienteNombre(nombre);

        System.out.print("Email del cliente: ");
        String email = scanner.nextLine();
        venta.setClienteEmail(email);

        System.out.print("\n¿Cuántos productos diferentes va a comprar? ");
        int numProductos = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= numProductos; i++) {
            System.out.println("\n--- Producto " + i + " ---");

            System.out.print("Código: ");
            String codigo = scanner.nextLine();

            System.out.print("Nombre: ");
            String nombreProd = scanner.nextLine();

            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();

            System.out.print("Precio unitario: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();

            venta.agregarItem(codigo, nombreProd, cantidad, precio);
        }

        System.out.print("\nMétodo de pago (EFECTIVO/TARJETA/TRANSFERENCIA): ");
        String metodoPago = scanner.nextLine();
        venta.setMetodoPago(metodoPago);

        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESUMEN DE LA COMPRA");
        System.out.println("═".repeat(50));
        System.out.println("Productos:");
        for (ItemVenta item : venta.getItems()) {
            System.out.printf("  - %s (x%d): $%.2f%n",
                    item.getNombreProducto(), item.getCantidad(), item.getSubtotal());
        }
        System.out.println("-".repeat(50));
        System.out.printf("Subtotal: $%.2f%n", venta.getSubtotal());
        System.out.printf("Impuestos (19%%): $%.2f%n", venta.getImpuestos());
        System.out.printf("TOTAL: $%.2f%n", venta.getTotal());
        System.out.println("═".repeat(50));

        System.out.print("\n¿Confirmar venta? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            controlador.procesarVenta(venta);
        } else {
            System.out.println("✗ Venta cancelada");
        }
    }

    private void consultarPuntos() {
        System.out.println("\n--- Consultar Puntos de Cliente ---");
        System.out.print("ID del cliente: ");
        String clienteId = scanner.nextLine();

        controlador.consultarPuntos(clienteId);
    }

    private void generarReporte() {
        System.out.println("\n--- Generar Reporte de Ventas ---");

        java.util.Date fechaInicio = new java.util.Date();
        java.util.Date fechaFin = new java.util.Date();

        System.out.println("Generando reporte del día actual...");
        controlador.generarReporte(fechaInicio, fechaFin);
    }

    private void ventaExpressDemo() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          VENTA EXPRESS - DEMO              ║");
        System.out.println("╚════════════════════════════════════════════╝");

        Venta venta = new Venta();
        venta.setClienteId("DEMO001");
        venta.setClienteNombre("Cliente Demo");
        venta.setClienteEmail("demo@supermercado.com");
        venta.setMetodoPago("TARJETA");

        venta.agregarItem("P001", "Leche Entera 1L", 2, 3500);
        venta.agregarItem("P002", "Pan Integral", 1, 2500);
        venta.agregarItem("P003", "Huevos x12", 1, 8000);

        System.out.println("\nVenta demo creada:");
        System.out.println("- 2x Leche Entera 1L");
        System.out.println("- 1x Pan Integral");
        System.out.println("- 1x Huevos x12");
        System.out.printf("\nTotal: $%.2f%n", venta.getTotal());

        System.out.print("\n¿Procesar venta demo? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            controlador.procesarVenta(venta);
        }
    }

    public void mostrarResultadoVenta(ResultadoVenta resultado) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESULTADO DE LA VENTA");
        System.out.println("═".repeat(50));

        if (resultado.isExitoso()) {
            System.out.println("Estado: ✓ EXITOSA");
            System.out.println("Número de Factura: " + resultado.getNumeroFactura());
            System.out.println("Comprobante de Pago: " + resultado.getComprobantePago());
            System.out.println("Puntos Ganados: " + resultado.getPuntosGanados());
            System.out.println(resultado.getMensaje());
        } else {
            System.out.println("Estado: ✗ FALLIDA");
            System.out.println("Motivo: " + resultado.getMensaje());
        }

        System.out.println("═".repeat(50));
    }

    public void mostrarPuntosCliente(String clienteId, int puntos) {
        System.out.println("\n" + "═".repeat(50));
        System.out.printf("Cliente %s tiene %d puntos acumulados%n", clienteId, puntos);
        System.out.println("═".repeat(50));
    }

    public void mostrarReporte(String reporte) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("Reporte generado: " + reporte);
        System.out.println("═".repeat(50));
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("ERROR: " + error);
    }

    public static void main(String[] args) {
        ConsolaFacade consola = new ConsolaFacade();
        consola.ejecutar();
    }
}