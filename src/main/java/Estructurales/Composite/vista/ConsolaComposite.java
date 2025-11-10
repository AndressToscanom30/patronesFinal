package Estructurales.Composite.vista;

import Estructurales.Composite.controlador.InventarioController;
import Estructurales.Composite.modelo.*;
import java.util.Scanner;

public class ConsolaComposite {
    private final InventarioController controlador;
    private final Scanner scanner;
    private Categoria categoriaRaiz;

    public ConsolaComposite() {
        this.controlador = new InventarioController(this);
        this.scanner = new Scanner(System.in);
        inicializarInventario();
    }

    private void inicializarInventario() {
        categoriaRaiz = new Categoria("Supermercado", "Inventario completo", 0, "store", 0, true, 0);
        controlador.setRaiz(categoriaRaiz);
    }

    public void mostrarMenu() {
        System.out.println("\n=== COMPOSITE - Sistema de Inventario ===");
        System.out.println("1. Crear categoría");
        System.out.println("2. Agregar producto individual");
        System.out.println("3. Crear combo");
        System.out.println("4. Crear paquete promocional");
        System.out.println("5. Ver inventario completo");
        System.out.println("6. Aplicar descuento global");
        System.out.println("7. Ver totales del inventario");
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
                case 1 -> crearCategoria();
                case 2 -> agregarProductoIndividual();
                case 3 -> crearCombo();
                case 4 -> crearPaquetePromocional();
                case 5 -> verInventarioCompleto();
                case 6 -> aplicarDescuentoGlobal();
                case 7 -> verTotales();
                case 8 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("¡Hasta luego!");
    }

    private void crearCategoria() {
        System.out.println("\n--- Crear Categoría ---");

        System.out.print("Nombre de la categoría: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Icono: ");
        String icono = scanner.nextLine();

        Categoria nuevaCategoria = new Categoria(nombre, descripcion, 0, icono, 1, true, 1);
        categoriaRaiz.agregarSubcategoria(nuevaCategoria);

        System.out.println("✓ Categoría '" + nombre + "' creada exitosamente");
    }

    private void agregarProductoIndividual() {
        System.out.println("\n--- Agregar Producto Individual ---");

        System.out.print("Código del producto: ");
        String codigo = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Precio: ");
        double precio = scanner.nextDouble();

        System.out.print("Peso (kg): ");
        double peso = scanner.nextDouble();
        scanner.nextLine();

        ProductoIndividual producto = new ProductoIndividual(codigo, nombre, descripcion, precio, peso);
        categoriaRaiz.agregarSubcategoria(producto);

        System.out.println("✓ Producto '" + nombre + "' agregado exitosamente");
    }

    private void crearCombo() {
        System.out.println("\n--- Crear Combo ---");

        System.out.print("Nombre del combo: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();

        System.out.print("Precio especial: ");
        double precioEspecial = scanner.nextDouble();

        System.out.print("Ahorro: ");
        double ahorro = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Imagen: ");
        String imagen = scanner.nextLine();

        Combo combo = new Combo(nombre, descripcion, 0, tipo, precioEspecial, ahorro, imagen);

        System.out.print("¿Cuántos productos desea agregar al combo? ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\nProducto " + (i + 1) + ":");
            System.out.print("Código: ");
            String codigo = scanner.nextLine();
            System.out.print("Nombre: ");
            String nombreProd = scanner.nextLine();
            System.out.print("Precio: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();

            ProductoIndividual producto = new ProductoIndividual(codigo, nombreProd, "", precio, 0.5);
            combo.agregarProducto(producto);
        }

        categoriaRaiz.agregarSubcategoria(combo);
        System.out.println("\n✓ Combo '" + nombre + "' creado exitosamente");
        System.out.printf("Ahorro calculado: $%.2f%n", combo.calcularAhorro());
    }

    private void crearPaquetePromocional() {
        System.out.println("\n--- Crear Paquete Promocional ---");

        System.out.print("Nombre del paquete: ");
        String nombre = scanner.nextLine();

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        System.out.print("Porcentaje de descuento: ");
        double descuento = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Condiciones: ");
        String condiciones = scanner.nextLine();

        System.out.print("Límite de unidades: ");
        int limite = scanner.nextInt();
        scanner.nextLine();

        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Date fechaInicio = cal.getTime();
        cal.add(java.util.Calendar.DAY_OF_MONTH, 30);
        java.util.Date fechaFin = cal.getTime();

        PaquetePromocional paquete = new PaquetePromocional(
                nombre, descripcion, descuento, fechaInicio, fechaFin, condiciones, limite
        );

        System.out.print("¿Cuántos productos desea agregar al paquete? ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\nProducto " + (i + 1) + ":");
            System.out.print("Código: ");
            String codigo = scanner.nextLine();
            System.out.print("Nombre: ");
            String nombreProd = scanner.nextLine();
            System.out.print("Precio: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();

            ProductoIndividual producto = new ProductoIndividual(codigo, nombreProd, "", precio, 0.5);
            paquete.agregarProducto(producto);
        }

        categoriaRaiz.agregarSubcategoria(paquete);
        System.out.println("\n✓ Paquete promocional '" + nombre + "' creado exitosamente");
        System.out.println("Vigente: " + (paquete.validarVigencia() ? "Sí" : "No"));
    }

    private void verInventarioCompleto() {
        System.out.println("\n=== INVENTARIO COMPLETO ===");
        mostrarComponente(categoriaRaiz, 0);
    }

    private void mostrarComponente(ComponenteInventario componente, int nivel) {
        String indentacion = "  ".repeat(nivel);

        if (componente instanceof ProductoIndividual) {
            ProductoIndividual prod = (ProductoIndividual) componente;
            System.out.printf("%s- %s: $%.2f (%.2fkg)%n",
                    indentacion, prod.getNombre(), prod.calcularTotal(), prod.calcularPesoTotal());
        } else if (componente instanceof Categoria) {
            Categoria cat = (Categoria) componente;
            System.out.printf("%s[%s] - Items: %d, Total: $%.2f%n",
                    indentacion, cat.getNombre(), cat.contarItems(), cat.calcularTotal());
            for (ComponenteInventario hijo : cat.obtenerHijos()) {
                mostrarComponente(hijo, nivel + 1);
            }
        } else if (componente instanceof Combo) {
            Combo combo = (Combo) componente;
            System.out.printf("%s{COMBO: %s} - Precio: $%.2f, Ahorro: $%.2f%n",
                    indentacion, combo.getNombre(), combo.calcularTotal(), combo.calcularAhorro());
            for (ComponenteInventario hijo : combo.obtenerHijos()) {
                mostrarComponente(hijo, nivel + 1);
            }
        } else if (componente instanceof PaquetePromocional) {
            PaquetePromocional paquete = (PaquetePromocional) componente;
            System.out.printf("%s[PROMO: %s] - Total: $%.2f, Vigente: %s%n",
                    indentacion, paquete.getNombre(), paquete.calcularTotal(),
                    paquete.validarVigencia() ? "Sí" : "No");
            for (ComponenteInventario hijo : paquete.obtenerHijos()) {
                mostrarComponente(hijo, nivel + 1);
            }
        }
    }

    private void aplicarDescuentoGlobal() {
        System.out.println("\n--- Aplicar Descuento Global ---");
        System.out.print("Porcentaje de descuento: ");
        double porcentaje = scanner.nextDouble();
        scanner.nextLine();

        controlador.aplicarDescuentoGlobal(porcentaje);
        System.out.printf("✓ Descuento del %.2f%% aplicado a todo el inventario%n", porcentaje);
    }

    private void verTotales() {
        System.out.println("\n=== TOTALES DEL INVENTARIO ===");
        controlador.mostrarInformacion();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarTotal(double total) {
        System.out.printf("Total del inventario: $%.2f%n", total);
    }

    public void mostrarPesoTotal(double peso) {
        System.out.printf("Peso total: %.2f kg%n", peso);
    }

    public void mostrarCantidadItems(int cantidad) {
        System.out.println("Cantidad de items: " + cantidad);
    }

    public static void main(String[] args) {
        ConsolaComposite consola = new ConsolaComposite();
        consola.ejecutar();
    }
}