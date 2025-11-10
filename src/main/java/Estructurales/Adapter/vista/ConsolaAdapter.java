package Estructurales.Adapter.vista;

import Estructurales.Adapter.controlador.AdapterControlador;
import Estructurales.Adapter.modelo.*;
import java.util.*;

public class ConsolaAdapter {
    private final AdapterControlador controlador;
    private final Scanner scanner;

    public ConsolaAdapter() {
        this.controlador = new AdapterControlador();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== ADAPTER - Integración de Inventarios ===");
        System.out.println("1. Buscar producto interno");
        System.out.println("2. Buscar producto externo");
        System.out.println("3. Listar productos internos");
        System.out.println("4. Listar productos externos");
        System.out.println("5. Listar todos los productos");
        System.out.println("6. Verificar disponibilidad");
        System.out.println("7. Actualizar stock");
        System.out.println("8. Buscar por proveedor");
        System.out.println("9. Ver estadísticas");
        System.out.println("10. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1 -> buscarProductoInterno();
                case 2 -> buscarProductoExterno();
                case 3 -> listarProductosInternos();
                case 4 -> listarProductosExternos();
                case 5 -> listarTodosLosProductos();
                case 6 -> verificarDisponibilidad();
                case 7 -> actualizarStock();
                case 8 -> buscarPorProveedor();
                case 9 -> verEstadisticas();
                case 10 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }
        
        System.out.println("¡Hasta luego!");
    }

    private void buscarProductoInterno() {
        System.out.print("\nCódigo del producto interno (P###): ");
        String codigo = scanner.nextLine();
        
        Producto producto = controlador.buscarProductoInterno(codigo);
        
        if (producto != null) {
            System.out.println("✓ Producto encontrado: " + producto);
        } else {
            System.out.println("✗ Producto no encontrado");
        }
    }

    private void buscarProductoExterno() {
        System.out.print("\nCódigo del producto externo (EXT-###): ");
        String codigo = scanner.nextLine();
        
        Producto producto = controlador.buscarProductoExterno(codigo);
        
        if (producto != null) {
            System.out.println("✓ Producto encontrado (adaptado): " + producto);
        } else {
            System.out.println("✗ Producto no encontrado");
        }
    }

    private void listarProductosInternos() {
        System.out.println("\n--- Productos Internos ---");
        List<Producto> productos = controlador.obtenerProductosInternos();
        
        if (productos.isEmpty()) {
            System.out.println("No hay productos internos");
        } else {
            productos.forEach(p -> System.out.println("- " + p));
        }
    }

    private void listarProductosExternos() {
        System.out.println("\n--- Productos Externos (Adaptados) ---");
        List<Producto> productos = controlador.obtenerProductosExternos();
        
        if (productos.isEmpty()) {
            System.out.println("No hay productos externos");
        } else {
            productos.forEach(p -> System.out.println("- " + p));
        }
    }

    private void listarTodosLosProductos() {
        System.out.println("\n--- Todos los Productos ---");
        List<Producto> productos = controlador.obtenerTodosLosProductos();
        
        System.out.println("\n** INTERNOS **");
        controlador.obtenerProductosInternos().forEach(p -> 
            System.out.println("- " + p));
        
        System.out.println("\n** EXTERNOS **");
        controlador.obtenerProductosExternos().forEach(p -> 
            System.out.println("- " + p));
        
        System.out.println("\nTotal: " + productos.size() + " productos");
    }

    private void verificarDisponibilidad() {
        System.out.print("\nCódigo del producto: ");
        String codigo = scanner.nextLine();
        
        System.out.print("Cantidad requerida: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();
        
        boolean disponible = controlador.verificarDisponibilidadGeneral(codigo, cantidad);
        
        if (disponible) {
            System.out.println("✓ Producto disponible");
        } else {
            System.out.println("✗ Stock insuficiente");
        }
    }

    private void actualizarStock() {
        System.out.println("\n1. Interno  2. Externo");
        int tipo = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Código del producto: ");
        String codigo = scanner.nextLine();
        
        System.out.print("Nuevo stock: ");
        int nuevoStock = scanner.nextInt();
        scanner.nextLine();
        
        boolean actualizado;
        if (tipo == 1) {
            actualizado = controlador.actualizarStockInterno(codigo, nuevoStock);
        } else {
            actualizado = controlador.actualizarStockExterno(codigo, nuevoStock);
        }
        
        if (actualizado) {
            System.out.println("✓ Stock actualizado");
        } else {
            System.out.println("✗ Error al actualizar stock");
        }
    }

    private void buscarPorProveedor() {
        System.out.print("\nCódigo del proveedor (SUP-A, SUP-B, SUP-C): ");
        String proveedor = scanner.nextLine();
        
        List<Producto> productos = controlador.buscarPorProveedor(proveedor);
        
        System.out.println("\n--- Productos del proveedor " + proveedor + " ---");
        if (productos.isEmpty()) {
            System.out.println("No hay productos de este proveedor");
        } else {
            productos.forEach(p -> System.out.println("- " + p));
        }
    }

    private void verEstadisticas() {
        System.out.println("\n--- Estadísticas de Inventario ---");
        Map<String, Integer> stats = controlador.obtenerEstadisticasInventario();
        
        System.out.println("Productos internos: " + stats.get("INTERNOS"));
        System.out.println("Productos externos: " + stats.get("EXTERNOS"));
        System.out.println("Total de productos: " + stats.get("TOTAL"));
    }

    public static void main(String[] args) {
        ConsolaAdapter vista = new ConsolaAdapter();
        vista.ejecutar();
    }
}
