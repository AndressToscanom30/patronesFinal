package Comportamentales.TemplateMethod.vista;

import Comportamentales.TemplateMethod.controlador.TemplateMethodController;
import Comportamentales.TemplateMethod.modelo.*;
import java.util.Scanner;

public class ConsolaTemplateMethod {
    private final TemplateMethodController controlador;
    private final Scanner scanner;

    public ConsolaTemplateMethod() {
        this.controlador = new TemplateMethodController(this);
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== TEMPLATE METHOD - Procesos de Venta ===");
        System.out.println("1. Procesar venta en efectivo");
        System.out.println("2. Procesar venta con tarjeta");
        System.out.println("3. Procesar venta por transferencia");
        System.out.println("4. Comparar procesos de venta");
        System.out.println("5. Demo: Flujo completo de ventas");
        System.out.println("6. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE VENTAS - TEMPLATE METHOD      ║");
        System.out.println("║   Estructura común, implementación única   ║");
        System.out.println("╚════════════════════════════════════════════╝");

        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> procesarVentaEfectivo();
                case 2 -> procesarVentaTarjeta();
                case 3 -> procesarVentaTransferencia();
                case 4 -> compararProcesos();
                case 5 -> demoFlujoCompleto();
                case 6 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("\n¡Gracias por usar el sistema de ventas!");
    }

    private void procesarVentaEfectivo() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       VENTA EN EFECTIVO                    ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.print("\nMonto a pagar: $");
        double monto = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("PROCESANDO VENTA EN EFECTIVO");
        System.out.println("═".repeat(50));
        
        String resultado = controlador.procesarVentaEfectivo(monto);
        System.out.println(resultado);
        
        System.out.println("═".repeat(50));
        System.out.println("✓ Venta completada");
        System.out.println("═".repeat(50));
    }

    private void procesarVentaTarjeta() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       VENTA CON TARJETA                    ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.print("\nMonto a pagar: $");
        double monto = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Tipo de cuenta (CREDITO/DEBITO): ");
        String tipoCuenta = scanner.nextLine();
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("PROCESANDO VENTA CON TARJETA");
        System.out.println("═".repeat(50));
        
        String resultado = controlador.procesarVentaTarjeta(monto, tipoCuenta);
        System.out.println(resultado);
        
        System.out.println("═".repeat(50));
        System.out.println("✓ Venta completada");
        System.out.println("═".repeat(50));
    }

    private void procesarVentaTransferencia() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       VENTA POR TRANSFERENCIA              ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.print("\nMonto a pagar: $");
        double monto = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Banco: ");
        String banco = scanner.nextLine();
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("PROCESANDO TRANSFERENCIA");
        System.out.println("═".repeat(50));
        
        String resultado = controlador.procesarVentaTransferencia(monto, banco);
        System.out.println(resultado);
        
        System.out.println("═".repeat(50));
        System.out.println("✓ Venta completada");
        System.out.println("═".repeat(50));
    }

    private void compararProcesos() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       COMPARACIÓN DE PROCESOS              ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        double monto = 100000;
        System.out.printf("\nMonto de prueba: $%.2f%n", monto);
        
        // Efectivo
        System.out.println("\n" + "─".repeat(50));
        System.out.println("1. PROCESO: EFECTIVO");
        System.out.println("─".repeat(50));
        ProcesoVenta ventaEfectivo = new VentaEfectivo();
        System.out.println(ventaEfectivo.procesarVenta(monto));
        
        // Tarjeta
        System.out.println("\n" + "─".repeat(50));
        System.out.println("2. PROCESO: TARJETA");
        System.out.println("─".repeat(50));
        ProcesoVenta ventaTarjeta = new VentaTarjeta("CREDITO");
        System.out.println(ventaTarjeta.procesarVenta(monto));
        
        // Transferencia
        System.out.println("\n" + "─".repeat(50));
        System.out.println("3. PROCESO: TRANSFERENCIA");
        System.out.println("─".repeat(50));
        ProcesoVenta ventaTransferencia = new VentaTransferencia("Banco Nacional");
        System.out.println(ventaTransferencia.procesarVenta(monto));
        
        System.out.println("═".repeat(50));
        System.out.println("CONCLUSIÓN:");
        System.out.println("Todos los procesos siguen la misma estructura (Template)");
        System.out.println("pero cada uno tiene su implementación específica.");
        System.out.println("═".repeat(50));
    }

    private void demoFlujoCompleto() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       DEMO: FLUJO COMPLETO                 ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.println("\nSimulando día de ventas en el supermercado...\n");
        
        // Venta 1: Efectivo
        System.out.println("═".repeat(50));
        System.out.println("VENTA #1: Cliente paga en efectivo");
        System.out.println("═".repeat(50));
        String resultado1 = controlador.procesarVentaEfectivo(50000);
        System.out.println(resultado1);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        
        // Venta 2: Tarjeta
        System.out.println("\n" + "═".repeat(50));
        System.out.println("VENTA #2: Cliente paga con tarjeta");
        System.out.println("═".repeat(50));
        String resultado2 = controlador.procesarVentaTarjeta(150000, "DEBITO");
        System.out.println(resultado2);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        
        // Venta 3: Transferencia
        System.out.println("\n" + "═".repeat(50));
        System.out.println("VENTA #3: Cliente paga por transferencia");
        System.out.println("═".repeat(50));
        String resultado3 = controlador.procesarVentaTransferencia(80000, "Bancolombia");
        System.out.println(resultado3);
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESUMEN DEL DÍA");
        System.out.println("═".repeat(50));
        System.out.println("Total de ventas: 3");
        System.out.println("- Efectivo: 1 venta ($50,000)");
        System.out.println("- Tarjeta: 1 venta ($150,000)");
        System.out.println("- Transferencia: 1 venta ($80,000)");
        System.out.println("Total recaudado: $280,000");
        System.out.println("═".repeat(50));
        
        System.out.println("\n✓ Demo completado exitosamente");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("ERROR: " + error);
    }

    public static void main(String[] args) {
        ConsolaTemplateMethod consola = new ConsolaTemplateMethod();
        consola.ejecutar();
    }
}