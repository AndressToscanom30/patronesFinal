package Estructurales.Decorator.vista;

import Estructurales.Decorator.controlador.DecoratorControlador;
import Estructurales.Decorator.modelo.*;
import java.util.*;

public class ConsolaDecorator {
    private final DecoratorControlador controlador;
    private final Scanner scanner;

    public ConsolaDecorator() {
        this.controlador = new DecoratorControlador();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== DECORATOR - Personalización de Productos ===");
        System.out.println("1. Ver catálogo de productos");
        System.out.println("2. Configurar producto con decoradores");
        System.out.println("3. Agregar solo empaque regalo");
        System.out.println("4. Agregar solo garantía extendida");
        System.out.println("5. Agregar solo personalización");
        System.out.println("6. Ver productos configurados");
        System.out.println("7. Ver estadísticas");
        System.out.println("8. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> verCatalogo();
                case 2 -> configurarProductoCompleto();
                case 3 -> agregarSoloEmpaque();
                case 4 -> agregarSoloGarantia();
                case 5 -> agregarSoloPersonalizacion();
                case 6 -> verProductosConfigurados();
                case 7 -> verEstadisticas();
                case 8 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("¡Hasta luego!");
    }

    private void verCatalogo() {
        System.out.println("\n--- Catálogo de Productos ---");
        var productos = controlador.obtenerCatalogo();

        productos.forEach(p -> {
            System.out.println("\n" + p);
            System.out.println("Descripción: " + p.obtenerDescripcion());
            System.out.printf("Precio: $%.2f%n", p.obtenerPrecio());
            System.out.printf("Peso: %.2fkg%n", p.obtenerPeso());
        });
    }

    private void configurarProductoCompleto() {
        System.out.println("\n--- Configurar Producto Completo ---");

        System.out.print("Código del producto: ");
        String codigo = scanner.nextLine();

        ProductoBase base = controlador.obtenerProductoBase(codigo);
        if (base == null) {
            System.out.println("✗ Producto no encontrado");
            return;
        }

        ProductoVendible producto = base;
        System.out.println("Producto base: " + base.obtenerDescripcion());
        System.out.printf("Precio base: $%.2f%n", base.obtenerPrecio());

        System.out.print("\n¿Agregar empaque regalo? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.print("Tipo (BASICO/PREMIUM/LUJO): ");
            String tipo = scanner.nextLine();
            System.out.print("¿Incluir tarjeta? (s/n): ");
            boolean tarjeta = scanner.nextLine().equalsIgnoreCase("s");
            producto = controlador.agregarEmpaqueRegalo(producto, tipo, tarjeta);
            System.out.println("✓ Empaque agregado");
        }

        System.out.print("\n¿Agregar garantía extendida? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.print("Meses (12/24/36): ");
            int meses = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Cobertura (BASICA/COMPLETA): ");
            String cobertura = scanner.nextLine();
            
            try {
                producto = controlador.agregarGarantiaExtendida(producto, meses, cobertura);
                System.out.println("✓ Garantía agregada");
            } catch (IllegalArgumentException e) {
                System.out.println("✗ " + e.getMessage());
            }
        }

        System.out.print("\n¿Agregar personalización? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.print("Texto a personalizar: ");
            String texto = scanner.nextLine();
            System.out.print("Tipo (GRABADO/BORDADO/IMPRESION): ");
            String tipo = scanner.nextLine();
            System.out.print("Posición (FRENTE/ATRAS/LATERAL): ");
            String posicion = scanner.nextLine();
            
            try {
                producto = controlador.agregarPersonalizacion(producto, texto, tipo, posicion);
                System.out.println("✓ Personalización agregada");
            } catch (IllegalArgumentException e) {
                System.out.println("✗ " + e.getMessage());
            }
        }

        System.out.println("\n=== PRODUCTO FINAL ===");
        System.out.println("Descripción: " + producto.obtenerDescripcion());
        System.out.printf("Precio total: $%.2f%n", producto.obtenerPrecio());
        System.out.printf("Peso total: %.2fkg%n", producto.obtenerPeso());
        System.out.println("Tiempo de entrega: " + producto.obtenerTiempoEntrega() + " días");
        System.out.println("Requiere refrigeración: " + (producto.requiereRefrigeracion() ? "Sí" : "No"));

        controlador.guardarProductoConfigurado(producto);
        System.out.println("\n✓ Producto configurado guardado");
    }

    private void agregarSoloEmpaque() {
        System.out.println("\n--- Agregar Empaque Regalo ---");
        
        System.out.print("Código del producto: ");
        String codigo = scanner.nextLine();
        
        ProductoBase base = controlador.obtenerProductoBase(codigo);
        if (base == null) {
            System.out.println("✗ Producto no encontrado");
            return;
        }

        System.out.print("Tipo de empaque (BASICO/PREMIUM/LUJO): ");
        String tipo = scanner.nextLine();
        
        System.out.print("¿Incluir tarjeta? (s/n): ");
        boolean tarjeta = scanner.nextLine().equalsIgnoreCase("s");

        ProductoVendible conEmpaque = controlador.agregarEmpaqueRegalo(base, tipo, tarjeta);
        
        System.out.println("\n✓ Empaque agregado");
        mostrarResumenProducto(conEmpaque);
        
        controlador.guardarProductoConfigurado(conEmpaque);
    }

    private void agregarSoloGarantia() {
        System.out.println("\n--- Agregar Garantía Extendida ---");
        
        System.out.print("Código del producto: ");
        String codigo = scanner.nextLine();
        
        ProductoBase base = controlador.obtenerProductoBase(codigo);
        if (base == null) {
            System.out.println("✗ Producto no encontrado");
            return;
        }

        System.out.print("Meses de garantía (12/24/36): ");
        int meses = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Cobertura (BASICA/COMPLETA): ");
        String cobertura = scanner.nextLine();

        try {
            ProductoVendible conGarantia = controlador.agregarGarantiaExtendida(base, meses, cobertura);
            System.out.println("\n✓ Garantía agregada");
            mostrarResumenProducto(conGarantia);
            controlador.guardarProductoConfigurado(conGarantia);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void agregarSoloPersonalizacion() {
        System.out.println("\n--- Agregar Personalización ---");
        
        System.out.print("Código del producto: ");
        String codigo = scanner.nextLine();
        
        ProductoBase base = controlador.obtenerProductoBase(codigo);
        if (base == null) {
            System.out.println("✗ Producto no encontrado");
            return;
        }

        System.out.print("Texto a personalizar: ");
        String texto = scanner.nextLine();
        
        System.out.print("Tipo (GRABADO/BORDADO/IMPRESION): ");
        String tipo = scanner.nextLine();
        
        System.out.print("Posición (FRENTE/ATRAS/LATERAL): ");
        String posicion = scanner.nextLine();

        try {
            ProductoVendible personalizado = controlador.agregarPersonalizacion(base, texto, tipo, posicion);
            System.out.println("\n✓ Personalización agregada");
            mostrarResumenProducto(personalizado);
            controlador.guardarProductoConfigurado(personalizado);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void verProductosConfigurados() {
        System.out.println("\n--- Productos Configurados ---");
        var productos = controlador.obtenerProductosConfigurados();

        if (productos.isEmpty()) {
            System.out.println("No hay productos configurados");
            return;
        }

        for (int i = 0; i < productos.size(); i++) {
            ProductoVendible p = productos.get(i);
            System.out.println("\n" + (i + 1) + ". " + p.obtenerDescripcion());
            System.out.printf("   Precio: $%.2f%n", p.obtenerPrecio());
            System.out.println("   Detalles:");
            p.obtenerDetalles().forEach(d -> System.out.println("     " + d));
        }
    }

    private void verEstadisticas() {
        System.out.println("\n--- Estadísticas ---");
        
        Map<String, Integer> stats = controlador.obtenerEstadisticasDecoradores();
        double total = controlador.calcularTotalVentas();
        int cantidad = controlador.obtenerProductosConfigurados().size();

        System.out.println("Total de productos configurados: " + cantidad);
        System.out.println("Con empaque regalo: " + stats.get("EMPAQUE_REGALO"));
        System.out.println("Con garantía extendida: " + stats.get("GARANTIA"));
        System.out.println("Con personalización: " + stats.get("PERSONALIZACION"));
        System.out.printf("Valor total: $%.2f%n", total);
    }

    private void mostrarResumenProducto(ProductoVendible producto) {
        System.out.println("\nDescripción: " + producto.obtenerDescripcion());
        System.out.printf("Precio: $%.2f%n", producto.obtenerPrecio());
        System.out.printf("Peso: %.2fkg%n", producto.obtenerPeso());
        System.out.println("Tiempo entrega: " + producto.obtenerTiempoEntrega() + " días");
    }

    public static void main(String[] args) {
        ConsolaDecorator vista = new ConsolaDecorator();
        vista.ejecutar();
    }
}