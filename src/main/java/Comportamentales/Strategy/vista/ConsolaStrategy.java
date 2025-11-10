package Comportamentales.Strategy.vista;

import Comportamentales.Strategy.controlador.StrategyController;
import Comportamentales.Strategy.modelo.*;
import java.util.Scanner;

public class ConsolaStrategy {
    private final StrategyController controlador;
    private final Scanner scanner;
    private CarritoCompra carritoActual;

    public ConsolaStrategy() {
        this.controlador = new StrategyController(this);
        this.scanner = new Scanner(System.in);
        this.carritoActual = null;
    }

    public void mostrarMenu() {
        System.out.println("\n=== STRATEGY - Sistema de Descuentos ===");
        System.out.println("1. Crear nuevo carrito");
        System.out.println("2. Aplicar descuento por porcentaje");
        System.out.println("3. Aplicar descuento 2x1");
        System.out.println("4. Aplicar descuento 3x2");
        System.out.println("5. Calcular total con descuento");
        System.out.println("6. Ver carrito actual");
        System.out.println("7. Demo: Comparar estrategias");
        System.out.println("8. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE DESCUENTOS - PATRÓN STRATEGY  ║");
        System.out.println("║   Estrategias intercambiables de descuento ║");
        System.out.println("╚════════════════════════════════════════════╝");

        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> crearCarrito();
                case 2 -> aplicarDescuentoPorcentaje();
                case 3 -> aplicarDescuento2x1();
                case 4 -> aplicarDescuento3x2();
                case 5 -> calcularTotal();
                case 6 -> verCarrito();
                case 7 -> demoComparacion();
                case 8 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("\n¡Gracias por usar el sistema de descuentos!");
    }

    private void crearCarrito() {
        System.out.println("\n--- Crear Nuevo Carrito ---");
        System.out.print("Total de la compra: $");
        double total = scanner.nextDouble();
        scanner.nextLine();
        
        carritoActual = controlador.crearCarrito(total);
        System.out.printf("\n✓ Carrito creado con total: $%.2f%n", total);
    }

    private void aplicarDescuentoPorcentaje() {
        if (!validarCarrito()) return;
        
        System.out.println("\n--- Aplicar Descuento por Porcentaje ---");
        System.out.print("Porcentaje de descuento (ejemplo: 10 para 10%): ");
        double porcentaje = scanner.nextDouble();
        scanner.nextLine();
        
        DescuentoStrategy estrategia = controlador.crearDescuentoPorcentaje(porcentaje);
        controlador.aplicarEstrategia(carritoActual, estrategia);
        
        System.out.printf("\n✓ Descuento del %.2f%% aplicado%n", porcentaje);
        mostrarResultadoDescuento();
    }

    private void aplicarDescuento2x1() {
        if (!validarCarrito()) return;
        
        System.out.println("\n--- Aplicar Descuento 2x1 ---");
        System.out.println("(Paga 1 y lleva 2 - 50% de descuento)");
        
        DescuentoStrategy estrategia = controlador.crearDescuento2x1();
        controlador.aplicarEstrategia(carritoActual, estrategia);
        
        System.out.println("\n✓ Descuento 2x1 aplicado");
        mostrarResultadoDescuento();
    }

    private void aplicarDescuento3x2() {
        if (!validarCarrito()) return;
        
        System.out.println("\n--- Aplicar Descuento 3x2 ---");
        System.out.print("Cantidad mínima de productos: ");
        int cantidadMin = scanner.nextInt();
        
        System.out.print("Porcentaje de descuento: ");
        double porcentaje = scanner.nextDouble();
        scanner.nextLine();
        
        DescuentoStrategy estrategia = controlador.crearDescuento3x2(cantidadMin, porcentaje);
        controlador.aplicarEstrategia(carritoActual, estrategia);
        
        System.out.printf("\n✓ Descuento 3x2 aplicado (mín %d productos, %.2f%% desc)%n", 
            cantidadMin, porcentaje);
        mostrarResultadoDescuento();
    }

    private void calcularTotal() {
        if (!validarCarrito()) return;
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("CÁLCULO DEL TOTAL");
        System.out.println("═".repeat(50));
        
        double totalOriginal = carritoActual.getTotal();
        double totalConDescuento = controlador.calcularTotalConDescuento(carritoActual);
        double ahorro = totalOriginal - totalConDescuento;
        
        System.out.printf("Total original: $%.2f%n", totalOriginal);
        
        if (carritoActual.getEstrategia() != null) {
            System.out.printf("Total con descuento: $%.2f%n", totalConDescuento);
            System.out.printf("Ahorro: $%.2f (%.2f%%)%n", ahorro, (ahorro / totalOriginal) * 100);
        } else {
            System.out.println("No hay descuento aplicado");
            System.out.printf("Total a pagar: $%.2f%n", totalConDescuento);
        }
        
        System.out.println("═".repeat(50));
    }

    private void verCarrito() {
        if (!validarCarrito()) return;
        
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          CARRITO ACTUAL                    ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.printf("\nTotal: $%.2f%n", carritoActual.getTotal());
        
        if (carritoActual.getEstrategia() != null) {
            DescuentoStrategy estrategia = carritoActual.getEstrategia();
            System.out.print("Estrategia: ");
            
            if (estrategia instanceof DescuentoPorcentaje) {
                DescuentoPorcentaje desc = (DescuentoPorcentaje) estrategia;
                System.out.printf("Porcentaje (%.2f%%)%n", desc.getPorcentaje());
            } else if (estrategia instanceof Descuento2x1) {
                System.out.println("2x1 (50% descuento)");
            } else if (estrategia instanceof Descuento3x2) {
                Descuento3x2 desc = (Descuento3x2) estrategia;
                System.out.printf("3x2 (mín %d, %.2f%% desc)%n", 
                    desc.getCantidadMin(), desc.getPorcentaje());
            }
            
            System.out.printf("Total con descuento: $%.2f%n", 
                controlador.calcularTotalConDescuento(carritoActual));
        } else {
            System.out.println("Estrategia: Ninguna");
        }
        
        System.out.println("\n" + "═".repeat(50));
    }

    private void demoComparacion() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║      DEMO: COMPARACIÓN DE ESTRATEGIAS      ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        double totalCompra = 100000;
        System.out.printf("\nTotal de compra: $%.2f%n", totalCompra);
        System.out.println("\nComparando diferentes estrategias de descuento:\n");
        
        // Sin descuento
        CarritoCompra carrito1 = controlador.crearCarrito(totalCompra);
        System.out.println("1. SIN DESCUENTO");
        System.out.printf("   Total a pagar: $%.2f%n", carrito1.calcularTotal());
        
        // Descuento por porcentaje 10%
        CarritoCompra carrito2 = controlador.crearCarrito(totalCompra);
        DescuentoStrategy desc10 = controlador.crearDescuentoPorcentaje(10);
        carrito2.setEstrategia(desc10);
        double total10 = carrito2.calcularTotal();
        System.out.println("\n2. DESCUENTO PORCENTAJE (10%)");
        System.out.printf("   Total a pagar: $%.2f%n", total10);
        System.out.printf("   Ahorro: $%.2f%n", totalCompra - total10);
        
        // Descuento por porcentaje 20%
        CarritoCompra carrito3 = controlador.crearCarrito(totalCompra);
        DescuentoStrategy desc20 = controlador.crearDescuentoPorcentaje(20);
        carrito3.setEstrategia(desc20);
        double total20 = carrito3.calcularTotal();
        System.out.println("\n3. DESCUENTO PORCENTAJE (20%)");
        System.out.printf("   Total a pagar: $%.2f%n", total20);
        System.out.printf("   Ahorro: $%.2f%n", totalCompra - total20);
        
        // Descuento 2x1
        CarritoCompra carrito4 = controlador.crearCarrito(totalCompra);
        DescuentoStrategy desc2x1 = controlador.crearDescuento2x1();
        carrito4.setEstrategia(desc2x1);
        double total2x1 = carrito4.calcularTotal();
        System.out.println("\n4. DESCUENTO 2x1 (50%)");
        System.out.printf("   Total a pagar: $%.2f%n", total2x1);
        System.out.printf("   Ahorro: $%.2f%n", totalCompra - total2x1);
        
        // Descuento 3x2
        CarritoCompra carrito5 = controlador.crearCarrito(totalCompra);
        DescuentoStrategy desc3x2 = controlador.crearDescuento3x2(3, 33.33);
        carrito5.setEstrategia(desc3x2);
        double total3x2 = carrito5.calcularTotal();
        System.out.println("\n5. DESCUENTO 3x2 (33.33%)");
        System.out.printf("   Total a pagar: $%.2f%n", total3x2);
        System.out.printf("   Ahorro: $%.2f%n", totalCompra - total3x2);
        
        System.out.println("\n" + "═".repeat(50));
        System.out.println("MEJOR OPCIÓN: 2x1 (Mayor ahorro)");
        System.out.println("═".repeat(50));
    }

    private boolean validarCarrito() {
        if (carritoActual == null) {
            System.out.println("\n✗ No hay carrito creado. Use la opción 1 primero.");
            return false;
        }
        return true;
    }

    private void mostrarResultadoDescuento() {
        double totalOriginal = carritoActual.getTotal();
        double totalConDescuento = controlador.calcularTotalConDescuento(carritoActual);
        double ahorro = totalOriginal - totalConDescuento;
        
        System.out.printf("\nTotal original: $%.2f%n", totalOriginal);
        System.out.printf("Total con descuento: $%.2f%n", totalConDescuento);
        System.out.printf("Ahorro: $%.2f%n", ahorro);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("ERROR: " + error);
    }

    public static void main(String[] args) {
        ConsolaStrategy consola = new ConsolaStrategy();
        consola.ejecutar();
    }
}