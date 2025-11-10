package Creacionales.FactoryMethod.vista;

import Creacionales.FactoryMethod.modelo.Producto;
import Creacionales.FactoryMethod.controlador.ProductoControlador;
import java.util.Scanner;

public class ConsolaProducto {

    private final ProductoControlador controlador;
    private final Scanner scanner;

    public ConsolaProducto() {
        this.controlador = new ProductoControlador();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== FACTORY METHOD - Gestión de Productos ===");
        System.out.println("1. Crear producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto");
        System.out.println("4. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> crearProductoInteractivo();
                case 2 -> listarProductos();
                case 3 -> buscarProductoInteractivo();
                case 4 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("¡Hasta luego!");
    }

    private void crearProductoInteractivo() {
        System.out.println("\n--- Crear Producto ---");
        System.out.println("Tipos disponibles: " + controlador.obtenerTiposDisponibles());
        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();

        System.out.print("Código: ");
        String codigo = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Precio: ");
        double precio = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();

        Producto producto = controlador.crearProducto(tipo, codigo, nombre, precio, categoria);

        if (producto != null) {
            System.out.println("✓ Producto creado: " + producto);
        } else {
            System.out.println("✗ Tipo de producto no válido");
        }
    }

    private void listarProductos() {
        System.out.println("\n--- Lista de Productos ---");
        var productos = controlador.obtenerTodosLosProductos();

        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados");
        } else {
            productos.forEach(p -> {
                System.out.printf("- %s | Tipo: %s | Precio final: $%.2f%n",
                        p.getNombre(), p.obtenerTipo(), p.calcularPrecioFinal());
            });
        }
    }

    private void buscarProductoInteractivo() {
        System.out.print("\nCódigo del producto: ");
        String codigo = scanner.nextLine();

        Producto producto = controlador.buscarPorCodigo(codigo);

        if (producto != null) {
            System.out.println("Encontrado: " + producto);
            System.out.println("Precio final: $" + producto.calcularPrecioFinal());
            System.out.println("Requiere refrigeración: " + producto.requiereRefrigeracion());
        } else {
            System.out.println("Producto no encontrado");
        }
    }

    public static void main(String[] args) {
        ConsolaProducto vista = new ConsolaProducto();
        vista.ejecutar();
    }

}
