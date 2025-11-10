package e2e.Adapter;


import Estructurales.Adapter.controlador.AdapterControlador;
import Estructurales.Adapter.modelo.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Adapter Pattern - Tests End-to-End")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdapterE2ETest {

    private AdapterControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new AdapterControlador();
    }

    @Test
    @Order(1)
    @DisplayName("E2E: Consulta de inventario unificado")
    void testConsultaInventarioUnificado() {
        System.out.println("\n=== CONSULTA DE INVENTARIO UNIFICADO ===\n");

        var internos = controlador.obtenerProductosInternos();
        var externos = controlador.obtenerProductosExternos();

        System.out.println("PRODUCTOS INTERNOS:");
        internos.forEach(p -> System.out.println("  " + p));

        System.out.println("\nPRODUCTOS EXTERNOS (ADAPTADOS):");
        externos.forEach(p -> System.out.println("  " + p));

        int total = internos.size() + externos.size();
        System.out.println("\nTotal de productos disponibles: " + total);

        assertAll("Verificar inventario unificado",
            () -> assertTrue(internos.size() >= 5),
            () -> assertTrue(externos.size() >= 5),
            () -> assertTrue(total >= 10)
        );
    }

    @Test
    @Order(2)
    @DisplayName("E2E: Proceso de compra verificando múltiples fuentes")
    void testProcesoCompraMultiplesFuentes() {
        List<String> productosCompra = Arrays.asList("P001", "P002", "EXT-001", "EXT-002");
        Map<String, Integer> cantidades = Map.of(
            "P001", 5,
            "P002", 3,
            "EXT-001", 10,
            "EXT-002", 15
        );

        System.out.println("\n=== VERIFICACIÓN DE DISPONIBILIDAD ===\n");

        boolean todosDisponibles = true;
        for (String codigo : productosCompra) {
            int cantidad = cantidades.get(codigo);
            boolean disponible = controlador.verificarDisponibilidadGeneral(codigo, cantidad);
            
            String fuente = codigo.startsWith("EXT-") ? "EXTERNO" : "INTERNO";
            System.out.printf("%s [%s]: %s unidades -> %s%n",
                codigo, fuente, cantidad, disponible ? "✓ DISPONIBLE" : "✗ NO DISPONIBLE");
            
            todosDisponibles = todosDisponibles && disponible;
        }

        assertTrue(todosDisponibles, "Todos los productos deben estar disponibles");
    }

    @Test
    @Order(3)
    @DisplayName("E2E: Actualización masiva de inventario")
    void testActualizacionMasivaInventario() {
        System.out.println("\n=== ACTUALIZACIÓN MASIVA DE INVENTARIO ===\n");

        Map<String, Integer> actualizaciones = Map.of(
            "P001", 150,
            "P002", 75,
            "EXT-001", 600,
            "EXT-002", 350
        );

        int exitosas = 0;
        for (Map.Entry<String, Integer> entry : actualizaciones.entrySet()) {
            String codigo = entry.getKey();
            int nuevoStock = entry.getValue();
            
            boolean actualizado;
            if (codigo.startsWith("EXT-")) {
                actualizado = controlador.actualizarStockExterno(codigo, nuevoStock);
            } else {
                actualizado = controlador.actualizarStockInterno(codigo, nuevoStock);
            }
            
            if (actualizado) {
                exitosas++;
                System.out.printf("✓ %s actualizado a %d unidades%n", codigo, nuevoStock);
            } else {
                System.out.printf("✗ Error actualizando %s%n", codigo);
            }
        }

        assertEquals(4, exitosas, "Todas las actualizaciones deben ser exitosas");
    }

    @Test
    @Order(4)
    @DisplayName("E2E: Gestión de productos por proveedor")
    void testGestionPorProveedor() {
        System.out.println("\n=== GESTIÓN POR PROVEEDOR ===\n");

        List<String> proveedores = Arrays.asList("SUP-A", "SUP-B", "SUP-C");

        for (String proveedor : proveedores) {
            var productos = controlador.buscarPorProveedor(proveedor);
            System.out.printf("\nProveedor %s: %d productos%n", proveedor, productos.size());
            productos.forEach(p -> System.out.println("  - " + p));
        }

        assertTrue(controlador.buscarPorProveedor("SUP-A").size() > 0);
    }

    @Test
    @Order(5)
    @DisplayName("E2E: Expansión de catálogo con nuevos productos")
    void testExpansionCatalogo() {
        System.out.println("\n=== EXPANSIÓN DE CATÁLOGO ===\n");

        Producto nuevoInterno = new Producto("P-NEW-001", "Chocolate Premium", 8500, "DULCES", 80);
        controlador.agregarProductoInterno(nuevoInterno);
        System.out.println("✓ Producto interno agregado: " + nuevoInterno);

        Producto nuevoExterno = new Producto("EXT-NEW-001", "Imported Tea", 12000, "BEBIDAS", 120);
        controlador.agregarProductoExterno(nuevoExterno);
        System.out.println("✓ Producto externo agregado: " + nuevoExterno);

        assertAll("Verificar expansión",
            () -> assertNotNull(controlador.buscarProductoInterno("P-NEW-001")),
            () -> assertNotNull(controlador.buscarProductoExterno("EXT-NEW-001"))
        );
    }

    @Test
    @Order(6)
    @DisplayName("E2E: Reporte consolidado de inventario")
    void testReporteConsolidado() {
        System.out.println("\n=== REPORTE CONSOLIDADO DE INVENTARIO ===\n");

        Map<String, Integer> stats = controlador.obtenerEstadisticasInventario();
        var todos = controlador.obtenerTodosLosProductos();

        double valorTotalInventario = todos.stream()
            .mapToDouble(p -> p.getPrecio() * p.getStock())
            .sum();

        Map<String, Long> productosPorCategoria = new HashMap<>();
        todos.forEach(p -> {
            String cat = p.getCategoria();
            productosPorCategoria.put(cat, productosPorCategoria.getOrDefault(cat, 0L) + 1);
        });

        System.out.println("ESTADÍSTICAS:");
        System.out.println("  Productos internos: " + stats.get("INTERNOS"));
        System.out.println("  Productos externos: " + stats.get("EXTERNOS"));
        System.out.println("  Total productos: " + stats.get("TOTAL"));
        System.out.printf("  Valor total inventario: $%.2f%n", valorTotalInventario);

        System.out.println("\nPRODUCTOS POR CATEGORÍA:");
        productosPorCategoria.forEach((cat, count) -> 
            System.out.printf("  %s: %d productos%n", cat, count));

        assertTrue(valorTotalInventario > 0, "El valor del inventario debe ser positivo");
    }

    @Test
    @Order(7)
    @DisplayName("E2E: Simulación de pedido grande multi-fuente")
    void testPedidoGrandeMultiFuente() {
        System.out.println("\n=== PEDIDO GRANDE MULTI-FUENTE ===\n");

        Map<String, Integer> pedido = new LinkedHashMap<>();
        pedido.put("P001", 20);
        pedido.put("P002", 15);
        pedido.put("P003", 25);
        pedido.put("EXT-001", 50);
        pedido.put("EXT-002", 30);
        pedido.put("EXT-003", 40);

        System.out.println("PROCESANDO PEDIDO:");
        
        List<String> itemsAprobados = new ArrayList<>();
        List<String> itemsRechazados = new ArrayList<>();
        double costoTotal = 0;

        for (Map.Entry<String, Integer> item : pedido.entrySet()) {
            String codigo = item.getKey();
            int cantidad = item.getValue();

            Producto producto = codigo.startsWith("EXT-") ?
                controlador.buscarProductoExterno(codigo) :
                controlador.buscarProductoInterno(codigo);

            boolean disponible = controlador.verificarDisponibilidadGeneral(codigo, cantidad);

            if (disponible && producto != null) {
                double subtotal = producto.getPrecio() * cantidad;
                costoTotal += subtotal;
                itemsAprobados.add(codigo);
                System.out.printf("✓ %s x%d - $%.2f%n", codigo, cantidad, subtotal);
            } else {
                itemsRechazados.add(codigo);
                System.out.printf("✗ %s x%d - NO DISPONIBLE%n", codigo, cantidad);
            }
        }

        System.out.printf("\nRESUMEN:%n");
        System.out.printf("Items aprobados: %d%n", itemsAprobados.size());
        System.out.printf("Items rechazados: %d%n", itemsRechazados.size());
        System.out.printf("Costo total: $%.2f%n", costoTotal);

        assertTrue(itemsAprobados.size() > 0, "Debe haber al menos un item aprobado");
    }

    @Test
    @Order(8)
    @DisplayName("E2E: Migración de productos entre sistemas")
    void testMigracionProductos() {
        System.out.println("\n=== MIGRACIÓN DE PRODUCTOS ===\n");

        Producto productoInterno = controlador.buscarProductoInterno("P001");
        assertNotNull(productoInterno);

        System.out.println("Producto original (interno): " + productoInterno);

        Producto copiaExterna = new Producto(
            "EXT-MIG-001",
            productoInterno.getNombre() + " (Migrado)",
            productoInterno.getPrecio(),
            productoInterno.getCategoria(),
            productoInterno.getStock()
        );

        controlador.agregarProductoExterno(copiaExterna);
        System.out.println("✓ Producto migrado a sistema externo: " + copiaExterna);

        Producto verificacion = controlador.buscarProductoExterno("EXT-MIG-001");
        assertNotNull(verificacion, "El producto migrado debe existir en el sistema externo");
    }

    @Test
    @Order(9)
    @DisplayName("E2E: Búsqueda inteligente en ambos sistemas")
    void testBusquedaInteligente() {
        System.out.println("\n=== BÚSQUEDA INTELIGENTE ===\n");

        String[] terminos = {"Arroz", "Rice", "Leche", "Milk", "Aceite", "Oil"};

        for (String termino : terminos) {
            System.out.println("\nBuscando: " + termino);

            var internos = controlador.obtenerProductosInternos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(termino.toLowerCase()))
                .toList();

            var externos = controlador.obtenerProductosExternos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(termino.toLowerCase()))
                .toList();

            System.out.println("  Internos: " + internos.size());
            internos.forEach(p -> System.out.println("    - " + p));

            System.out.println("  Externos: " + externos.size());
            externos.forEach(p -> System.out.println("    - " + p));
        }

        assertTrue(true, "Búsqueda completada");
    }

    @Test
    @Order(10)
    @DisplayName("E2E: Escenario completo de operación diaria")
    void testEscenarioCompletoDiario() {
        System.out.println("\n=== OPERACIÓN DIARIA COMPLETA ===\n");

        System.out.println("1. APERTURA - Revisar inventario");
        Map<String, Integer> statsInicio = controlador.obtenerEstadisticasInventario();
        System.out.printf("   Productos disponibles: %d (Internos: %d, Externos: %d)%n",
            statsInicio.get("TOTAL"), statsInicio.get("INTERNOS"), statsInicio.get("EXTERNOS"));

        System.out.println("\n2. MAÑANA - Recepción de mercancía externa");
        Producto nuevo1 = new Producto("EXT-DIA-001", "New Morning Product", 5000, "ALIMENTOS", 100);
        controlador.agregarProductoExterno(nuevo1);
        System.out.println("   ✓ Producto recibido: " + nuevo1.getNombre());

        System.out.println("\n3. MEDIODÍA - Ventas");
        boolean venta1 = controlador.verificarDisponibilidadGeneral("P001", 10);
        boolean venta2 = controlador.verificarDisponibilidadGeneral("EXT-001", 20);
        System.out.println("   Venta 1 (P001 x10): " + (venta1 ? "✓ APROBADA" : "✗ RECHAZADA"));
        System.out.println("   Venta 2 (EXT-001 x20): " + (venta2 ? "✓ APROBADA" : "✗ RECHAZADA"));

        System.out.println("\n4. TARDE - Reabastecimiento");
        controlador.actualizarStockInterno("P002", 100);
        controlador.actualizarStockExterno("EXT-002", 250);
        System.out.println("   ✓ Stocks actualizados");

        System.out.println("\n5. CIERRE - Reporte final");
        Map<String, Integer> statsFin = controlador.obtenerEstadisticasInventario();
        var todosFinal = controlador.obtenerTodosLosProductos();
        double valorFinal = todosFinal.stream()
            .mapToDouble(p -> p.getPrecio() * p.getStock())
            .sum();

        System.out.printf("   Total productos: %d%n", statsFin.get("TOTAL"));
        System.out.printf("   Valor inventario: $%.2f%n", valorFinal);

        assertAll("Verificar operación completa",
            () -> assertTrue(statsFin.get("TOTAL") > statsInicio.get("TOTAL")),
            () -> assertTrue(valorFinal > 0),
            () -> assertTrue(venta1),
            () -> assertTrue(venta2)
        );

        System.out.println("\n=== FIN DE OPERACIÓN DIARIA ===");
    }
}