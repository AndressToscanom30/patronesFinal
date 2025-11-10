package Comportamentales.State.vista;

import Comportamentales.State.controlador.StateController;
import Comportamentales.State.modelo.*;
import java.util.List;
import java.util.Scanner;

public class ConsolaState {
    private final StateController controlador;
    private final Scanner scanner;
    private Pedido pedidoActual;

    public ConsolaState() {
        this.controlador = new StateController(this);
        this.scanner = new Scanner(System.in);
        this.pedidoActual = null;
    }

    public void mostrarMenu() {
        System.out.println("\n=== STATE - Sistema de Gestión de Pedidos ===");
        System.out.println("1. Crear nuevo pedido");
        System.out.println("2. Agregar items al pedido");
        System.out.println("3. Procesar pedido");
        System.out.println("4. Confirmar pago");
        System.out.println("5. Enviar pedido");
        System.out.println("6. Entregar pedido");
        System.out.println("7. Cancelar pedido");
        System.out.println("8. Ver pedido actual");
        System.out.println("9. Ver todos los pedidos");
        System.out.println("10. Ver historial de estados");
        System.out.println("11. Demo: Flujo completo de pedido");
        System.out.println("12. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   GESTIÓN DE PEDIDOS - PATRÓN STATE        ║");
        System.out.println("║   Comportamiento según el estado           ║");
        System.out.println("╚════════════════════════════════════════════╝");

        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> crearPedido();
                case 2 -> agregarItems();
                case 3 -> procesarPedido();
                case 4 -> confirmarPago();
                case 5 -> enviarPedido();
                case 6 -> entregarPedido();
                case 7 -> cancelarPedido();
                case 8 -> verPedidoActual();
                case 9 -> verTodosPedidos();
                case 10 -> verHistorialEstados();
                case 11 -> demoFlujoCompleto();
                case 12 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("\n¡Gracias por usar el sistema de pedidos!");
    }

    private void crearPedido() {
        System.out.println("\n--- Crear Nuevo Pedido ---");
        
        System.out.print("Nombre del cliente: ");
        String cliente = scanner.nextLine();
        
        pedidoActual = controlador.crearPedido(cliente);
        
        System.out.println("\n═".repeat(50));
        System.out.println(pedidoActual);
        System.out.println("═".repeat(50));
    }

    private void agregarItems() {
        if (!validarPedidoActual()) return;
        
        System.out.println("\n--- Agregar Items al Pedido ---");
        
        boolean agregar = true;
        while (agregar) {
            System.out.print("\nProducto: ");
            String producto = scanner.nextLine();
            
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            
            System.out.print("Precio unitario: $");
            double precio = scanner.nextDouble();
            scanner.nextLine();
            
            controlador.agregarItemPedido(pedidoActual, producto, cantidad, precio);
            
            System.out.print("\n¿Agregar otro item? (s/n): ");
            agregar = scanner.nextLine().equalsIgnoreCase("s");
        }
        
        System.out.println("\n" + "═".repeat(50));
        System.out.printf("Total del pedido: $%.2f%n", pedidoActual.getTotal());
        System.out.println("═".repeat(50));
    }

    private void procesarPedido() {
        if (!validarPedidoActual()) return;
        
        System.out.println("\n═".repeat(50));
        System.out.println("PROCESAR PEDIDO");
        System.out.println("═".repeat(50));
        
        controlador.procesarPedido(pedidoActual);
        
        System.out.println("\nEstado actual: " + pedidoActual.getEstadoActual());
    }

    private void confirmarPago() {
        if (!validarPedidoActual()) return;
        
        System.out.println("\n═".repeat(50));
        System.out.println("CONFIRMAR PAGO");
        System.out.println("═".repeat(50));
        
        controlador.confirmarPago(pedidoActual);
        
        System.out.println("\nEstado actual: " + pedidoActual.getEstadoActual());
    }

    private void enviarPedido() {
        if (!validarPedidoActual()) return;
        
        System.out.println("\n═".repeat(50));
        System.out.println("ENVIAR PEDIDO");
        System.out.println("═".repeat(50));
        
        controlador.enviarPedido(pedidoActual);
        
        System.out.println("\nEstado actual: " + pedidoActual.getEstadoActual());
    }

    private void entregarPedido() {
        if (!validarPedidoActual()) return;
        
        System.out.println("\n═".repeat(50));
        System.out.println("ENTREGAR PEDIDO");
        System.out.println("═".repeat(50));
        
        controlador.entregarPedido(pedidoActual);
        
        System.out.println("\nEstado actual: " + pedidoActual.getEstadoActual());
    }

    private void cancelarPedido() {
        if (!validarPedidoActual()) return;
        
        System.out.println("\n═".repeat(50));
        System.out.println("CANCELAR PEDIDO");
        System.out.println("═".repeat(50));
        
        System.out.print("¿Está seguro de cancelar el pedido? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            controlador.cancelarPedido(pedidoActual);
            System.out.println("\nEstado actual: " + pedidoActual.getEstadoActual());
        } else {
            System.out.println("Operación cancelada");
        }
    }

    private void verPedidoActual() {
        if (!validarPedidoActual()) return;
        
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          PEDIDO ACTUAL                     ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.println("\nNúmero: " + pedidoActual.getNumeroPedido());
        System.out.println("Cliente: " + pedidoActual.getCliente());
        System.out.println("Estado: " + pedidoActual.getEstadoActual());
        System.out.println("Fecha: " + pedidoActual.getFechaCreacion());
        
        System.out.println("\nItems:");
        for (ItemPedido item : pedidoActual.getItems()) {
            System.out.println("  " + item);
        }
        
        System.out.println("\n" + "─".repeat(50));
        System.out.printf("TOTAL: $%.2f%n", pedidoActual.getTotal());
        System.out.println("─".repeat(50));
    }

    private void verTodosPedidos() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          TODOS LOS PEDIDOS                 ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        List<Pedido> pedidos = controlador.obtenerPedidos();
        
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados");
            return;
        }
        
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
    }

    private void verHistorialEstados() {
        if (!validarPedidoActual()) return;
        
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       HISTORIAL DE ESTADOS                 ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        System.out.println("Pedido: " + pedidoActual.getNumeroPedido());
        System.out.println("\nTransiciones de estado:\n");
        
        for (String registro : pedidoActual.getHistorialEstados()) {
            System.out.println("  " + registro);
        }
    }

    private void demoFlujoCompleto() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       DEMO: FLUJO COMPLETO                 ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        System.out.println("Simulando ciclo de vida completo de un pedido...\n");
        
        // Paso 1: Crear pedido
        System.out.println("═".repeat(50));
        System.out.println("PASO 1: CREAR PEDIDO");
        System.out.println("═".repeat(50));
        Pedido demo = controlador.crearPedido("Juan Pérez");
        controlador.agregarItemPedido(demo, "Laptop", 1, 2500000);
        controlador.agregarItemPedido(demo, "Mouse", 1, 50000);
        System.out.printf("Total: $%.2f%n", demo.getTotal());
        System.out.println("Estado: " + demo.getEstadoActual());
        pausa();
        
        // Paso 2: Procesar pedido
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 2: PROCESAR PEDIDO");
        System.out.println("═".repeat(50));
        controlador.procesarPedido(demo);
        pausa();
        
        // Paso 3: Confirmar pago
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 3: CONFIRMAR PAGO");
        System.out.println("═".repeat(50));
        controlador.confirmarPago(demo);
        pausa();
        
        // Paso 4: Enviar pedido
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 4: ENVIAR PEDIDO");
        System.out.println("═".repeat(50));
        controlador.enviarPedido(demo);
        pausa();
        
        // Paso 5: Entregar pedido
        System.out.println("\n═".repeat(50));
        System.out.println("PASO 5: ENTREGAR PEDIDO");
        System.out.println("═".repeat(50));
        controlador.entregarPedido(demo);
        pausa();
        
        // Resumen
        System.out.println("\n═".repeat(50));
        System.out.println("RESUMEN DEL PEDIDO");
        System.out.println("═".repeat(50));
        System.out.println("Pedido: " + demo.getNumeroPedido());
        System.out.println("Cliente: " + demo.getCliente());
        System.out.println("Estado final: " + demo.getEstadoActual());
        System.out.printf("Total: $%.2f%n", demo.getTotal());
        
        System.out.println("\nHistorial de estados:");
        for (String registro : demo.getHistorialEstados()) {
            System.out.println("  " + registro);
        }
        
        System.out.println("\n✓ Demo completado exitosamente");
    }

    private boolean validarPedidoActual() {
        if (pedidoActual == null) {
            System.out.println("\n✗ No hay pedido activo. Cree un pedido primero (opción 1)");
            return false;
        }
        return true;
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
        ConsolaState consola = new ConsolaState();
        consola.ejecutar();
    }
}