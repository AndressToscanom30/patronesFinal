package Comportamentales.ChainOfResponsibility.vista;

import Comportamentales.ChainOfResponsibility.controlador.ChainOfResponsibilityController;
import Comportamentales.ChainOfResponsibility.modelo.*;
import java.util.Scanner;

public class ConsolaChainOfResponsibility {
    private final ChainOfResponsibilityController controlador;
    private final Scanner scanner;

    public ConsolaChainOfResponsibility() {
        this.controlador = new ChainOfResponsibilityController(this);
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== CHAIN OF RESPONSIBILITY - Sistema de Descuentos ===");
        System.out.println("1. Solicitar descuento (completo)");
        System.out.println("2. Solicitar descuento por cupón");
        System.out.println("3. Solicitar descuento cliente frecuente");
        System.out.println("4. Solicitar descuento por monto");
        System.out.println("5. Solicitar descuento por cantidad");
        System.out.println("6. Ver cupones disponibles");
        System.out.println("7. Demo: Comparar diferentes solicitudes");
        System.out.println("8. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   DESCUENTOS - CHAIN OF RESPONSIBILITY     ║");
        System.out.println("║   Cadena de validación de descuentos       ║");
        System.out.println("╚════════════════════════════════════════════╝");

        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> solicitarDescuentoCompleto();
                case 2 -> solicitarDescuentoCupon();
                case 3 -> solicitarDescuentoClienteFrecuente();
                case 4 -> solicitarDescuentoPorMonto();
                case 5 -> solicitarDescuentoPorCantidad();
                case 6 -> verCuponesDisponibles();
                case 7 -> demoComparacion();
                case 8 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("\n¡Gracias por usar el sistema de descuentos!");
    }

    private void solicitarDescuentoCompleto() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║     SOLICITUD DE DESCUENTO COMPLETA        ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.print("\nID del cliente: ");
        String clienteId = scanner.nextLine();
        
        System.out.print("Monto total de la compra: $");
        double monto = scanner.nextDouble();
        
        System.out.print("Cantidad de productos: ");
        int cantidad = scanner.nextInt();
        
        System.out.print("Número de compras previas: ");
        int numeroCompras = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Código de cupón (Enter si no tiene): ");
        String cupon = scanner.nextLine();
        if (cupon.isEmpty()) cupon = null;
        
        SolicitudDescuento solicitud = controlador.crearSolicitud(
            clienteId, monto, cantidad, numeroCompras, cupon, "GENERAL");
        
        ResultadoDescuento resultado = controlador.procesarSolicitud(solicitud);
        
        if (resultado.isAprobado()) {
            double montoFinal = monto - resultado.getMontoDescuento();
            System.out.println("\n" + "═".repeat(50));
            System.out.printf("Monto original: $%.2f%n", monto);
            System.out.printf("Descuento: -$%.2f%n", resultado.getMontoDescuento());
            System.out.printf("TOTAL A PAGAR: $%.2f%n", montoFinal);
            System.out.println("═".repeat(50));
        }
    }

    private void solicitarDescuentoCupon() {
        System.out.println("\n--- Solicitar Descuento con Cupón ---");
        
        System.out.print("ID del cliente: ");
        String clienteId = scanner.nextLine();
        
        System.out.print("Monto total: $");
        double monto = scanner.nextDouble();
        
        System.out.print("Cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Código de cupón: ");
        String cupon = scanner.nextLine();
        
        SolicitudDescuento solicitud = controlador.crearSolicitud(
            clienteId, monto, cantidad, 0, cupon, "GENERAL");
        
        ResultadoDescuento resultado = controlador.procesarSolicitud(solicitud);
        mostrarResumen(monto, resultado);
    }

    private void solicitarDescuentoClienteFrecuente() {
        System.out.println("\n--- Descuento Cliente Frecuente ---");
        
        System.out.print("ID del cliente: ");
        String clienteId = scanner.nextLine();
        
        System.out.print("Monto total: $");
        double monto = scanner.nextDouble();
        
        System.out.print("Cantidad: ");
        int cantidad = scanner.nextInt();
        
        System.out.print("Número de compras previas: ");
        int numeroCompras = scanner.nextInt();
        scanner.nextLine();
        
        SolicitudDescuento solicitud = controlador.crearSolicitud(
            clienteId, monto, cantidad, numeroCompras, null, "GENERAL");
        
        ResultadoDescuento resultado = controlador.procesarSolicitud(solicitud);
        mostrarResumen(monto, resultado);
    }

    private void solicitarDescuentoPorMonto() {
        System.out.println("\n--- Descuento por Monto ---");
        
        System.out.print("ID del cliente: ");
        String clienteId = scanner.nextLine();
        
        System.out.print("Monto total: $");
        double monto = scanner.nextDouble();
        
        System.out.print("Cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();
        
        SolicitudDescuento solicitud = controlador.crearSolicitud(
            clienteId, monto, cantidad, 0, null, "GENERAL");
        
        ResultadoDescuento resultado = controlador.procesarSolicitud(solicitud);
        mostrarResumen(monto, resultado);
    }

    private void solicitarDescuentoPorCantidad() {
        System.out.println("\n--- Descuento por Cantidad ---");
        
        System.out.print("ID del cliente: ");
        String clienteId = scanner.nextLine();
        
        System.out.print("Monto total: $");
        double monto = scanner.nextDouble();
        
        System.out.print("Cantidad de productos: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();
        
        SolicitudDescuento solicitud = controlador.crearSolicitud(
            clienteId, monto, cantidad, 0, null, "GENERAL");
        
        ResultadoDescuento resultado = controlador.procesarSolicitud(solicitud);
        mostrarResumen(monto, resultado);
    }

    private void verCuponesDisponibles() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       CUPONES DISPONIBLES                  ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        System.out.println("Código          | Descuento");
        System.out.println("─".repeat(50));
        System.out.println("VERANO2024      | 15%");
        System.out.println("PRIMERACOMPRA   | 20%");
        System.out.println("BLACKFRIDAY     | 50%");
        System.out.println("NAVIDAD         | 25%");
        System.out.println("BIENVENIDO      | 10%");
        System.out.println("─".repeat(50));
    }

    private void demoComparacion() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       DEMO: COMPARACIÓN DE SOLICITUDES     ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        double montoBase = 150000;
        int cantidadBase = 15;
        
        System.out.println("Datos base:");
        System.out.printf("- Monto: $%.0f%n", montoBase);
        System.out.println("- Cantidad: " + cantidadBase);
        System.out.println("\nComparando diferentes escenarios...\n");
        
        // Escenario 1: Cliente nuevo sin cupón
        System.out.println("═".repeat(50));
        System.out.println("ESCENARIO 1: Cliente nuevo, sin cupón");
        System.out.println("═".repeat(50));
        SolicitudDescuento sol1 = controlador.crearSolicitud(
            "CLI-001", montoBase, cantidadBase, 0, null, "GENERAL");
        ResultadoDescuento res1 = controlador.procesarSolicitud(sol1);
        mostrarResumen(montoBase, res1);
        pausa();
        
        // Escenario 2: Cliente con cupón
        System.out.println("\n═".repeat(50));
        System.out.println("ESCENARIO 2: Cliente con cupón VERANO2024");
        System.out.println("═".repeat(50));
        SolicitudDescuento sol2 = controlador.crearSolicitud(
            "CLI-002", montoBase, cantidadBase, 0, "VERANO2024", "GENERAL");
        ResultadoDescuento res2 = controlador.procesarSolicitud(sol2);
        mostrarResumen(montoBase, res2);
        pausa();
        
        // Escenario 3: Cliente frecuente
        System.out.println("\n═".repeat(50));
        System.out.println("ESCENARIO 3: Cliente frecuente (10 compras)");
        System.out.println("═".repeat(50));
        SolicitudDescuento sol3 = controlador.crearSolicitud(
            "CLI-003", montoBase, cantidadBase, 10, null, "GENERAL");
        ResultadoDescuento res3 = controlador.procesarSolicitud(sol3);
        mostrarResumen(montoBase, res3);
        pausa();
        
        // Escenario 4: Black Friday
        System.out.println("\n═".repeat(50));
        System.out.println("ESCENARIO 4: Black Friday (cupón 50%)");
        System.out.println("═".repeat(50));
        SolicitudDescuento sol4 = controlador.crearSolicitud(
            "CLI-004", montoBase, cantidadBase, 0, "BLACKFRIDAY", "GENERAL");
        ResultadoDescuento res4 = controlador.procesarSolicitud(sol4);
        mostrarResumen(montoBase, res4);
        
        // Comparación final
        System.out.println("\n═".repeat(50));
        System.out.println("COMPARACIÓN FINAL");
        System.out.println("═".repeat(50));
        System.out.printf("Escenario 1 (sin desc.): $%.2f%n", 
            montoBase - (res1.isAprobado() ? res1.getMontoDescuento() : 0));
        System.out.printf("Escenario 2 (cupón): $%.2f%n", 
            montoBase - (res2.isAprobado() ? res2.getMontoDescuento() : 0));
        System.out.printf("Escenario 3 (frecuente): $%.2f%n", 
            montoBase - (res3.isAprobado() ? res3.getMontoDescuento() : 0));
        System.out.printf("Escenario 4 (Black Friday): $%.2f%n", 
            montoBase - (res4.isAprobado() ? res4.getMontoDescuento() : 0));
        System.out.println("═".repeat(50));
        
        System.out.println("\n✓ La cadena evalúa automáticamente el mejor descuento");
    }

    private void mostrarResumen(double montoOriginal, ResultadoDescuento resultado) {
        System.out.println("\n" + "─".repeat(50));
        if (resultado.isAprobado()) {
            double montoFinal = montoOriginal - resultado.getMontoDescuento();
            System.out.printf("Monto original: $%.2f%n", montoOriginal);
            System.out.printf("Descuento: -$%.2f%n", resultado.getMontoDescuento());
            System.out.printf("Total a pagar: $%.2f%n", montoFinal);
            System.out.printf("Ahorro: %.0f%%%n", 
                (resultado.getMontoDescuento() / montoOriginal) * 100);
        } else {
            System.out.printf("Total a pagar: $%.2f (sin descuento)%n", montoOriginal);
        }
        System.out.println("─".repeat(50));
    }

    private void pausa() {
        try {
            Thread.sleep(1000);
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
        ConsolaChainOfResponsibility consola = new ConsolaChainOfResponsibility();
        consola.ejecutar();
    }
}