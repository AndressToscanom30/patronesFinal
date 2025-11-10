package unit.Adapter;

import Estructurales.Adapter.modelo.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Adapter Pattern - Tests Unitarios")
class AdapterUnitTest {

    @Nested
    @DisplayName("Producto - Modelo interno")
    class ProductoTests {

        @Test
        @DisplayName("Producto se crea correctamente")
        void testCrearProducto() {
            Producto producto = new Producto("P001", "Arroz", 3500, "ALIMENTOS", 100);

            assertAll("Verificar creación de producto",
                () -> assertEquals("P001", producto.getCodigo()),
                () -> assertEquals("Arroz", producto.getNombre()),
                () -> assertEquals(3500, producto.getPrecio()),
                () -> assertEquals("ALIMENTOS", producto.getCategoria()),
                () -> assertEquals(100, producto.getStock())
            );
        }

        @Test
        @DisplayName("Setters actualizan valores")
        void testSetters() {
            Producto producto = new Producto("P001", "Test", 1000, "CAT", 10);

            producto.setCodigo("P002");
            producto.setNombre("Nuevo");
            producto.setPrecio(2000);
            producto.setCategoria("NUEVA");
            producto.setStock(20);

            assertAll("Verificar setters",
                () -> assertEquals("P002", producto.getCodigo()),
                () -> assertEquals("Nuevo", producto.getNombre()),
                () -> assertEquals(2000, producto.getPrecio()),
                () -> assertEquals("NUEVA", producto.getCategoria()),
                () -> assertEquals(20, producto.getStock())
            );
        }

        @Test
        @DisplayName("toString contiene información relevante")
        void testToString() {
            Producto producto = new Producto("P001", "Arroz", 3500, "ALIMENTOS", 100);
            String toString = producto.toString();

            assertAll("Verificar toString",
                () -> assertTrue(toString.contains("P001")),
                () -> assertTrue(toString.contains("Arroz")),
                () -> assertTrue(toString.contains("3500"))
            );
        }
    }

    @Nested
    @DisplayName("ProductoExterno - Modelo externo")
    class ProductoExternoTests {

        @Test
        @DisplayName("ProductoExterno se crea correctamente")
        void testCrearProductoExterno() {
            ProductoExterno externo = new ProductoExterno("EXT-001", "Rice", 3200, "FOOD", 500, "SUP-A");

            assertAll("Verificar creación de producto externo",
                () -> assertEquals("EXT-001", externo.getProductId()),
                () -> assertEquals("Rice", externo.getProductName()),
                () -> assertEquals(3200, externo.getUnitPrice()),
                () -> assertEquals("FOOD", externo.getCategoryCode()),
                () -> assertEquals(500, externo.getAvailableQuantity()),
                () -> assertEquals("SUP-A", externo.getSupplierCode())
            );
        }

        @Test
        @DisplayName("Setters actualizan valores externos")
        void testSettersExternos() {
            ProductoExterno externo = new ProductoExterno("EXT-001", "Test", 1000, "CAT", 10, "SUP-X");

            externo.setProductId("EXT-002");
            externo.setProductName("New");
            externo.setUnitPrice(2000);
            externo.setCategoryCode("NEW");
            externo.setAvailableQuantity(20);
            externo.setSupplierCode("SUP-Y");

            assertAll("Verificar setters externos",
                () -> assertEquals("EXT-002", externo.getProductId()),
                () -> assertEquals("New", externo.getProductName()),
                () -> assertEquals(2000, externo.getUnitPrice()),
                () -> assertEquals("NEW", externo.getCategoryCode()),
                () -> assertEquals(20, externo.getAvailableQuantity()),
                () -> assertEquals("SUP-Y", externo.getSupplierCode())
            );
        }
    }

    @Nested
    @DisplayName("SistemaInventarioInterno")
    class SistemaInventarioInternoTests {

        private SistemaInventarioInterno sistema;

        @BeforeEach
        void setUp() {
            sistema = new SistemaInventarioInterno();
        }

        @Test
        @DisplayName("Sistema inicializa con productos")
        void testInicializacion() {
            assertFalse(sistema.obtenerTodosLosProductos().isEmpty());
        }

        @Test
        @DisplayName("Buscar producto existente")
        void testBuscarProductoExistente() {
            Producto producto = sistema.buscarProducto("P001");
            assertNotNull(producto);
        }

        @Test
        @DisplayName("Buscar producto inexistente retorna null")
        void testBuscarProductoInexistente() {
            Producto producto = sistema.buscarProducto("NOEXISTE");
            assertNull(producto);
        }

        @Test
        @DisplayName("Agregar producto nuevo")
        void testAgregarProducto() {
            Producto nuevo = new Producto("P999", "Nuevo", 1000, "TEST", 10);
            sistema.agregarProducto(nuevo);

            Producto encontrado = sistema.buscarProducto("P999");
            assertNotNull(encontrado);
            assertEquals("Nuevo", encontrado.getNombre());
        }

        @Test
        @DisplayName("Actualizar stock existente")
        void testActualizarStock() {
            boolean actualizado = sistema.actualizarStock("P001", 150);
            assertTrue(actualizado);

            Producto producto = sistema.buscarProducto("P001");
            assertEquals(150, producto.getStock());
        }

        @Test
        @DisplayName("Actualizar stock de producto inexistente")
        void testActualizarStockInexistente() {
            boolean actualizado = sistema.actualizarStock("NOEXISTE", 100);
            assertFalse(actualizado);
        }

        @Test
        @DisplayName("Verificar disponibilidad suficiente")
        void testVerificarDisponibilidadSuficiente() {
            assertTrue(sistema.verificarDisponibilidad("P001", 10));
        }

        @Test
        @DisplayName("Verificar disponibilidad insuficiente")
        void testVerificarDisponibilidadInsuficiente() {
            assertFalse(sistema.verificarDisponibilidad("P001", 1000));
        }

        @Test
        @DisplayName("Obtener todos los productos")
        void testObtenerTodos() {
            var productos = sistema.obtenerTodosLosProductos();
            assertTrue(productos.size() >= 5);
        }
    }

    @Nested
    @DisplayName("SistemaProveedorExterno")
    class SistemaProveedorExternoTests {

        private SistemaProveedorExterno sistema;

        @BeforeEach
        void setUp() {
            sistema = new SistemaProveedorExterno();
        }

        @Test
        @DisplayName("Sistema externo inicializa con productos")
        void testInicializacion() {
            assertFalse(sistema.getAllProducts().isEmpty());
        }

        @Test
        @DisplayName("Buscar producto externo existente")
        void testBuscarProductoExistente() {
            ProductoExterno producto = sistema.findProductById("EXT-001");
            assertNotNull(producto);
        }

        @Test
        @DisplayName("Agregar producto externo")
        void testAgregarProducto() {
            ProductoExterno nuevo = new ProductoExterno("EXT-999", "New Product", 5000, "TEST", 50, "SUP-X");
            sistema.addProduct(nuevo);

            ProductoExterno encontrado = sistema.findProductById("EXT-999");
            assertNotNull(encontrado);
        }

        @Test
        @DisplayName("Actualizar cantidad disponible")
        void testActualizarCantidad() {
            boolean actualizado = sistema.updateQuantity("EXT-001", 600);
            assertTrue(actualizado);

            ProductoExterno producto = sistema.findProductById("EXT-001");
            assertEquals(600, producto.getAvailableQuantity());
        }

        @Test
        @DisplayName("Verificar disponibilidad en sistema externo")
        void testVerificarDisponibilidad() {
            assertTrue(sistema.checkAvailability("EXT-001", 100));
            assertFalse(sistema.checkAvailability("EXT-001", 10000));
        }

        @Test
        @DisplayName("Buscar por proveedor")
        void testBuscarPorProveedor() {
            var productos = sistema.searchBySupplier("SUP-A");
            assertFalse(productos.isEmpty());
            assertTrue(productos.stream().allMatch(p -> p.getSupplierCode().equals("SUP-A")));
        }
    }

    @Nested
    @DisplayName("InventarioAdapter - Adaptación")
    class InventarioAdapterTests {

        private SistemaProveedorExterno sistemaExterno;
        private InventarioAdapter adapter;

        @BeforeEach
        void setUp() {
            sistemaExterno = new SistemaProveedorExterno();
            adapter = new InventarioAdapter(sistemaExterno);
        }

        @Test
        @DisplayName("Adapter busca producto y lo convierte")
        void testBuscarProductoConversion() {
            Producto producto = adapter.buscarProducto("EXT-001");

            assertAll("Verificar conversión",
                () -> assertNotNull(producto),
                () -> assertEquals("EXT-001", producto.getCodigo()),
                () -> assertTrue(producto.getNombre().contains("Rice"))
            );
        }

        @Test
        @DisplayName("Adapter convierte categorías correctamente")
        void testConversionCategorias() {
            Producto food = adapter.buscarProducto("EXT-001");
            Producto dairy = adapter.buscarProducto("EXT-003");

            assertAll("Verificar mapeo de categorías",
                () -> assertEquals("ALIMENTOS", food.getCategoria()),
                () -> assertEquals("LACTEOS", dairy.getCategoria())
            );
        }

        @Test
        @DisplayName("Adapter obtiene todos los productos convertidos")
        void testObtenerTodosConvertidos() {
            var productos = adapter.obtenerTodosLosProductos();

            assertAll("Verificar conversión masiva",
                () -> assertFalse(productos.isEmpty()),
                () -> assertTrue(productos.stream().allMatch(p -> p instanceof Producto))
            );
        }

        @Test
        @DisplayName("Adapter actualiza stock en sistema externo")
        void testActualizarStockAdapter() {
            boolean actualizado = adapter.actualizarStock("EXT-001", 700);
            assertTrue(actualizado);

            ProductoExterno externo = sistemaExterno.findProductById("EXT-001");
            assertEquals(700, externo.getAvailableQuantity());
        }

        @Test
        @DisplayName("Adapter verifica disponibilidad")
        void testVerificarDisponibilidadAdapter() {
            assertTrue(adapter.verificarDisponibilidad("EXT-001", 100));
            assertFalse(adapter.verificarDisponibilidad("EXT-001", 10000));
        }

        @Test
        @DisplayName("Adapter busca por proveedor y convierte")
        void testBuscarPorProveedorAdapter() {
            var productos = adapter.buscarPorProveedor("SUP-A");

            assertAll("Verificar búsqueda por proveedor",
                () -> assertFalse(productos.isEmpty()),
                () -> assertTrue(productos.stream().allMatch(p -> p instanceof Producto))
            );
        }

        @Test
        @DisplayName("Adapter agrega producto convirtiendo formato")
        void testAgregarProductoAdapter() {
            Producto interno = new Producto("P-NEW", "Nuevo Interno", 5000, "BEBIDAS", 50);
            adapter.agregarProducto(interno);

            ProductoExterno encontrado = sistemaExterno.findProductById("P-NEW");
            assertNotNull(encontrado);
            assertEquals("BEVERAGE", encontrado.getCategoryCode());
        }
    }
}