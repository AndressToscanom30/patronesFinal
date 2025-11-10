package Estructurales.Proxy.vista;

import Estructurales.Proxy.controlador.CatalogoController;
import Estructurales.Proxy.modelo.*;
import java.util.Scanner;

public class ConsolaProxy {
    private final CatalogoController controlador;
    private final Scanner scanner;

    public ConsolaProxy() {
        this.controlador = new CatalogoController(this);
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== PROXY - Sistema de Catálogo con Lazy Loading ===");
        System.out.println("1. Cargar productos al catálogo");
        System.out.println("2. Ver lista de productos (lazy)");
        System.out.println("3. Ver detalles de producto específico");
        System.out.println("4. Cargar todas las imágenes");
        System.out.println("5. Limpiar caché de imágenes");
        System.out.println("6. Ver estadísticas de acceso");
        System.out.println("7. Demo: Navegación típica");
        System.out.println("8. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   CATÁLOGO DE PRODUCTOS - PATRÓN PROXY     ║");
        System.out.println("║   Optimizando carga de imágenes            ║");
        System.out.println("╚════════════════════════════════════════════╝");

        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> cargarProductos();
                case 2 -> verListaProductos();
                case 3 -> verDetalleProducto();
                case 4 -> cargarTodasLasImagenes();
                case 5 -> limpiarCache();
                case 6 -> verEstadisticas();
                case 7 -> demoNavegacion();
                case 8 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("\n¡Hasta luego!");
    }

    private void cargarProductos() {
        System.out.println("\n--- Cargar Productos al Catálogo ---");
        System.out.print("¿Cuántos productos desea cargar? ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        long inicio = System.currentTimeMillis();

        for (int i = 1; i <= cantidad; i++) {
            String codigo = "PROD" + String.format("%03d", i);
            controlador.agregarProducto(codigo);
        }

        long fin = System.currentTimeMillis();

        System.out.println("\n✓ " + cantidad + " productos cargados en " + (fin - inicio) + "ms");
        System.out.println("(Gracias al Proxy, las imágenes NO se cargaron todavía - Lazy Loading)");
    }

    private void verListaProductos() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║          LISTA DE PRODUCTOS                ║");
        System.out.println("╚════════════════════════════════════════════╝");

        int cantidad = controlador.getCantidadProductos();

        if (cantidad == 0) {
            System.out.println("\nNo hay productos en el catálogo.");
            System.out.println("Use la opción 1 para cargar productos.");
            return;
        }

        System.out.println("\nMostrando miniaturas (sin cargar imágenes completas)...\n");

        for (int i = 0; i < cantidad; i++) {
            System.out.printf("%d. Producto #%d [Miniatura]%n", (i + 1), (i + 1));
        }

        System.out.println("\n✓ Lista mostrada sin cargar imágenes completas (Lazy Loading)");
    }

    private void verDetalleProducto() {
        System.out.println("\n--- Ver Detalle de Producto ---");

        int cantidad = controlador.getCantidadProductos();

        if (cantidad == 0) {
            System.out.println("No hay productos en el catálogo");
            return;
        }

        System.out.print("Número de producto (1-" + cantidad + "): ");
        int numero = scanner.nextInt();
        scanner.nextLine();

        if (numero < 1 || numero > cantidad) {
            System.out.println("✗ Número inválido");
            return;
        }

        System.out.println("\n" + "═".repeat(50));
        System.out.println("Cargando imagen en alta resolución...");
        System.out.println("═".repeat(50));

        long inicio = System.currentTimeMillis();
        controlador.mostrarImagen(numero - 1);
        long fin = System.currentTimeMillis();

        System.out.println("═".repeat(50));
        System.out.println("Tiempo de carga: " + (fin - inicio) + "ms");
        System.out.println("═".repeat(50));

        ImagenProducto imagen = controlador.getProducto(numero - 1);
        if (imagen instanceof ImagenProductoProxy) {
            ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
            System.out.println("\nEstado: " + (proxy.isCargada() ? "Cargada en memoria" : "No cargada"));
            System.out.println("Accesos: " + proxy.getContadorAccesos());
        }
    }

    private void cargarTodasLasImagenes() {
        System.out.println("\n--- Cargar Todas las Imágenes ---");

        int cantidad = controlador.getCantidadProductos();

        if (cantidad == 0) {
            System.out.println("No hay productos para cargar");
            return;
        }

        System.out.println("\nCargando " + cantidad + " imágenes...");
        System.out.print("¿Confirmar? (s/n): ");

        if (!scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada");
            return;
        }

        long inicio = System.currentTimeMillis();

        for (int i = 0; i < cantidad; i++) {
            System.out.print("Cargando imagen " + (i + 1) + "/" + cantidad + "...\r");
            controlador.cargarImagen(i);
        }

        long fin = System.currentTimeMillis();

        System.out.println("\n\n✓ Todas las imágenes cargadas en " + (fin - inicio) + "ms");
    }

    private void limpiarCache() {
        System.out.println("\n--- Limpiar Caché de Imágenes ---");

        int cantidad = controlador.getCantidadProductos();
        int cargadas = 0;

        for (int i = 0; i < cantidad; i++) {
            ImagenProducto imagen = controlador.getProducto(i);
            if (imagen instanceof ImagenProductoProxy) {
                ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
                if (proxy.isCargada()) {
                    cargadas++;
                }
            }
        }

        System.out.println("Imágenes en caché: " + cargadas + "/" + cantidad);
        System.out.print("¿Confirmar limpieza? (s/n): ");

        if (!scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada");
            return;
        }

        for (int i = 0; i < cantidad; i++) {
            ImagenProducto imagen = controlador.getProducto(i);
            if (imagen instanceof ImagenProductoProxy) {
                ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
                proxy.limpiarCache();
            }
        }

        System.out.println("\n✓ Caché limpiado exitosamente");
    }

    private void verEstadisticas() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║         ESTADÍSTICAS DEL CATÁLOGO          ║");
        System.out.println("╚════════════════════════════════════════════╝");

        int cantidad = controlador.getCantidadProductos();
        int cargadas = 0;
        int totalAccesos = 0;

        for (int i = 0; i < cantidad; i++) {
            ImagenProducto imagen = controlador.getProducto(i);
            if (imagen instanceof ImagenProductoProxy) {
                ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
                if (proxy.isCargada()) {
                    cargadas++;
                }
                totalAccesos += proxy.getContadorAccesos();
            }
        }

        System.out.println("\nTotal de productos: " + cantidad);
        System.out.println("Imágenes cargadas: " + cargadas);
        System.out.println("Imágenes no cargadas: " + (cantidad - cargadas));
        System.out.println("Total de accesos: " + totalAccesos);

        if (cantidad > 0) {
            double porcentajeCargado = (cargadas * 100.0) / cantidad;
            double ahorroMemoria = 100.0 - porcentajeCargado;
            System.out.printf("Porcentaje cargado: %.2f%%%n", porcentajeCargado);
            System.out.printf("Ahorro de memoria: %.2f%%%n", ahorroMemoria);
        }

        System.out.println("\n" + "═".repeat(50));
    }

    private void demoNavegacion() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║      DEMO: NAVEGACIÓN TÍPICA               ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.println("\nSimulando navegación de usuario típico...\n");

        // Cargar catálogo
        System.out.println("1. Usuario abre el catálogo (20 productos)");
        for (int i = 1; i <= 20; i++) {
            controlador.agregarProducto("DEMO" + String.format("%03d", i));
        }
        System.out.println("   ✓ Catálogo cargado instantáneamente (Lazy Loading)");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        // Ver producto
        System.out.println("\n2. Usuario hace clic en producto #5");
        System.out.println("   (Primera vez - carga desde disco)");
        long inicio1 = System.currentTimeMillis();
        controlador.mostrarImagen(4);
        long fin1 = System.currentTimeMillis();
        System.out.println("   ✓ Cargado en " + (fin1 - inicio1) + "ms");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        // Ver otro producto
        System.out.println("\n3. Usuario hace clic en producto #12");
        long inicio2 = System.currentTimeMillis();
        controlador.mostrarImagen(11);
        long fin2 = System.currentTimeMillis();
        System.out.println("   ✓ Cargado en " + (fin2 - inicio2) + "ms");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        // Volver al primer producto
        System.out.println("\n4. Usuario vuelve al producto #5");
        System.out.println("   (Desde caché - instantáneo)");
        long inicio3 = System.currentTimeMillis();
        controlador.mostrarImagen(4);
        long fin3 = System.currentTimeMillis();
        System.out.println("   ✓ Cargado en " + (fin3 - inicio3) + "ms (desde caché)");

        System.out.println("\n" + "═".repeat(50));
        System.out.println("RESUMEN");
        System.out.println("═".repeat(50));
        System.out.println("Total productos: 20");
        System.out.println("Imágenes cargadas: 2");
        System.out.println("Ahorro de memoria: 90%");
        System.out.println("Caché funcionando: ✓");
        System.out.println("═".repeat(50));
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.err.println("ERROR: " + error);
    }

    public void mostrarEstadisticas(int accesos, boolean cargada) {
        System.out.println("\n=== Estadísticas ===");
        System.out.println("Número de accesos: " + accesos);
        System.out.println("Imagen cargada: " + (cargada ? "Sí" : "No"));
    }

    public static void main(String[] args) {
        ConsolaProxy consola = new ConsolaProxy();
        consola.ejecutar();
    }
}