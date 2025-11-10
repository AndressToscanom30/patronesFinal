package Integration.Adapter;

import Estructurales.Adapter.controlador.AdapterControlador;
import Estructurales.Adapter.modelo.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Adapter Pattern - Tests de Integración")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdapterIntegrationTest {

    private AdapterControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new AdapterControlador();
    }

    @Test
    @Order(1)
    @DisplayName("Integración: Controlador inicializa ambos sistemas")
    void testInicializacion() {
        assertAll("Verificar inicialización",
            () -> assertNotNull(controlador.getSistemaInterno()),
            () -> assertNotNull(controlador.getAdapter())
        );
    }

    @Test
    @Order(2)
    @DisplayName("Integración: Buscar en sistema interno")
    void testBuscarInterno() {
        Producto producto = controlador.buscarProductoInterno("P001");
        assertNotNull(producto);
        assertEquals("P001", producto.getCodigo());
    }

    @Test
    @Order(3)
    @DisplayName("Integración: Buscar en sistema externo a través del adapter")
    void testBuscarExterno() {
        Producto producto = controlador.buscarProductoExterno("EXT-001");
        assertNotNull(producto);
        assertEquals("EXT-001", producto.getCodigo());
    }

    @Test
    @Order(4)
    @DisplayName("Integración: Obtener productos de ambos sistemas")
    void testObtenerTodos() {
        var internos = controlador.obtenerProductosInternos();
        var externos = controlador.obtenerProductosExternos();
        var todos = controlador.obtenerTodosLosProductos();

        assertAll("Verificar productos combinados",
            () -> assertFalse(internos.isEmpty()),
            () -> assertFalse(externos.isEmpty()),
            () -> assertEquals(internos.size() + externos.size(), todos.size())
        );
    }

    @Test
    @Order(5)
    @DisplayName("Integración: Verificar disponibilidad en sistema correcto")
    void testVerificarDisponibilidadGeneral() {
        boolean internoDisponible = controlador.verificarDisponibilidadGeneral("P001", 10);
        boolean externoDisponible = controlador.verificarDisponibilidadGeneral("EXT-001", 100);

        assertAll("Verificar disponibilidad",
            () -> assertTrue(internoDisponible),
            () -> assertTrue(externoDisponible)
        );
    }

    @Test
    @Order(6)
    @DisplayName("Integración: Actualizar stock en sistema interno")
    void testActualizarStockInterno() {
        boolean actualizado = controlador.actualizarStockInterno("P001", 200);
        assertTrue(actualizado);

        Producto producto = controlador.buscarProductoInterno("P001");
        assertEquals(200, producto.getStock());
    }

    @Test
    @Order(7)
    @DisplayName("Integración: Actualizar stock en sistema externo")
    void testActualizarStockExterno() {
        boolean actualizado = controlador.actualizarStockExterno("EXT-001", 800);
        assertTrue(actualizado);

        Producto producto = controlador.buscarProductoExterno("EXT-001");
        assertEquals(800, producto.getStock());
    }

    @Test
    @Order(8)
    @DisplayName("Integración: Buscar productos por proveedor")
    void testBuscarPorProveedor() {
        var productos = controlador.buscarPorProveedor("SUP-A");

        assertAll("Verificar búsqueda por proveedor",
            () -> assertFalse(productos.isEmpty()),
            () -> assertTrue(productos.stream().allMatch(p -> p instanceof Producto))
        );
    }

    @Test
    @Order(9)
    @DisplayName("Integración: Agregar producto a sistema interno")
    void testAgregarProductoInterno() {
        Producto nuevo = new Producto("P-NEW-001", "Producto Nuevo", 5000, "TEST", 50);
        controlador.agregarProductoInterno(nuevo);

        Producto encontrado = controlador.buscarProductoInterno("P-NEW-001");
        assertNotNull(encontrado);
        assertEquals("Producto Nuevo", encontrado.getNombre());
    }

    @Test
    @Order(10)
    @DisplayName("Integración: Agregar producto a sistema externo")
    void testAgregarProductoExterno() {
        Producto nuevo = new Producto("EXT-NEW-001", "External Product", 6000, "BEBIDAS", 60);
        controlador.agregarProductoExterno(nuevo);

        Producto encontrado = controlador.buscarProductoExterno("EXT-NEW-001");
        assertNotNull(encontrado);
    }

    @Test
    @Order(11)
    @DisplayName("Integración: Obtener estadísticas combinadas")
    void testEstadisticas() {
        Map<String, Integer> stats = controlador.obtenerEstadisticasInventario();

        assertAll("Verificar estadísticas",
            () -> assertTrue(stats.containsKey("INTERNOS")),
            () -> assertTrue(stats.containsKey("EXTERNOS")),
            () -> assertTrue(stats.containsKey("TOTAL")),
            () -> assertEquals(stats.get("INTERNOS") + stats.get("EXTERNOS"), stats.get("TOTAL"))
        );
    }

    @Test
    @Order(12)
    @DisplayName("Integración: Verificar independencia de sistemas")
    void testIndependenciaSistemas() {
        Producto interno = new Producto("P-IND", "Independiente Interno", 1000, "TEST", 10);
        controlador.agregarProductoInterno(interno);

        Producto externo = new Producto("EXT-IND", "Independiente Externo", 2000, "TEST", 20);
        controlador.agregarProductoExterno(externo);

        assertAll("Verificar independencia",
            () -> assertNotNull(controlador.buscarProductoInterno("P-IND")),
            () -> assertNull(controlador.buscarProductoExterno("P-IND")),
            () -> assertNotNull(controlador.buscarProductoExterno("EXT-IND")),
            () -> assertNull(controlador.buscarProductoInterno("EXT-IND"))
        );
    }
}
