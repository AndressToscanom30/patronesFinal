package Integration.FactoryMethod;

import Creacionales.FactoryMethod.controlador.ProductoControlador;
import Creacionales.FactoryMethod.modelo.*;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Factory Method - Tests de Integración")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FactoryMethodIntegrationTest {


    private ProductoControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new ProductoControlador();
    }

    @Test
    @Order(1)
    @DisplayName("Integración: Controlador crea producto simple correctamente")
    void testControladorCreaProductoSimple() {
        // Arrange & Act
        Producto producto = controlador.crearProducto("SIMPLE", "P001", "Arroz", 2500.0, "GRANOS");

        // Assert
        assertAll(
                () -> assertNotNull(producto),
                () -> assertEquals("SIMPLE", producto.obtenerTipo()),
                () -> assertEquals(1, controlador.obtenerTodosLosProductos().size())
        );
    }

    @Test
    @Order(2)
    @DisplayName("Integración: Controlador crea producto por peso correctamente")
    void testControladorCreaProductoPeso() {
        // Arrange & Act
        Producto producto = controlador.crearProducto("PESO", "C001", "Carne", 18000.0, "CARNES");

        // Assert
        assertAll(
                () -> assertNotNull(producto),
                () -> assertEquals("PESO", producto.obtenerTipo()),
                () -> assertTrue(producto instanceof ProductoPeso)
        );
    }

    @Test
    @Order(3)
    @DisplayName("Integración: Controlador maneja tipo inválido")
    void testControladorTipoInvalido() {
        // Arrange & Act
        Producto producto = controlador.crearProducto("INVALIDO", "X001", "Test", 1000.0, "TEST");

        // Assert
        assertNull(producto);
    }

    @Test
    @Order(4)
    @DisplayName("Integración: Crear múltiples productos y listarlos")
    void testCrearMultiplesProductosYListar() {
        // Arrange & Act
        controlador.crearProducto("SIMPLE", "P001", "Arroz", 2500.0, "GRANOS");
        controlador.crearProducto("SIMPLE", "P002", "Azúcar", 3000.0, "GRANOS");
        controlador.crearProducto("PESO", "C001", "Carne", 18000.0, "CARNES");
        controlador.crearProducto("PESO", "F001", "Manzanas", 4500.0, "FRUTAS");

        List<Producto> productos = controlador.obtenerTodosLosProductos();

        // Assert
        assertAll(
                () -> assertEquals(4, productos.size()),
                () -> assertEquals(2, productos.stream().filter(p -> p.obtenerTipo().equals("SIMPLE")).count()),
                () -> assertEquals(2, productos.stream().filter(p -> p.obtenerTipo().equals("PESO")).count())
        );
    }

    @Test
    @Order(5)
    @DisplayName("Integración: Buscar producto por código")
    void testBuscarProductoPorCodigo() {
        // Arrange
        controlador.crearProducto("SIMPLE", "P001", "Arroz", 2500.0, "GRANOS");
        controlador.crearProducto("PESO", "C001", "Carne", 18000.0, "CARNES");

        // Act
        Producto encontrado = controlador.buscarPorCodigo("C001");
        Producto noEncontrado = controlador.buscarPorCodigo("X999");

        // Assert
        assertAll(
                () -> assertNotNull(encontrado),
                () -> assertEquals("Carne", encontrado.getNombre()),
                () -> assertNull(noEncontrado)
        );
    }

    @Test
    @Order(6)
    @DisplayName("Integración: Modificar producto después de crearlo")
    void testModificarProductoDespuesDeCrear() {
        // Arrange
        Producto producto = controlador.crearProducto("SIMPLE", "P001", "Arroz", 2500.0, "GRANOS");
        ProductoSimple productoSimple = (ProductoSimple) producto;

        // Act
        productoSimple.setStock(100);
        productoSimple.setRefrigeracion(false);
        producto.setPrecio(2800.0);

        // Assert
        Producto buscado = controlador.buscarPorCodigo("P001");
        ProductoSimple buscadoSimple = (ProductoSimple) buscado;

        assertAll(
                () -> assertEquals(100, buscadoSimple.getStock()),
                () -> assertFalse(buscadoSimple.requiereRefrigeracion()),
                () -> assertEquals(2800.0, buscado.getPrecio())
        );
    }

    @Test
    @Order(7)
    @DisplayName("Integración: Calcular precio final de productos por peso")
    void testCalcularPrecioFinalProductoPeso() {
        // Arrange
        Producto producto = controlador.crearProducto("PESO", "C001", "Pollo", 15000.0, "CARNES");
        ProductoPeso productoPeso = (ProductoPeso) producto;

        // Act
        productoPeso.setPesoPromedio(2.5);
        double precioFinal = producto.calcularPrecioFinal();

        // Assert
        assertEquals(37500.0, precioFinal, 0.01);
    }

    @Test
    @Order(8)
    @DisplayName("Integración: Obtener tipos disponibles del controlador")
    void testObtenerTiposDisponibles() {
        // Arrange & Act
        var tipos = controlador.obtenerTiposDisponibles();

        // Assert
        assertAll(
                () -> assertNotNull(tipos),
                () -> assertEquals(2, tipos.size()),
                () -> assertTrue(tipos.contains("SIMPLE")),
                () -> assertTrue(tipos.contains("PESO"))
        );
    }

    @Test
    @Order(9)
    @DisplayName("Integración: Flujo completo de negocio")
    void testFlujoCompletoNegocio() {
        // Arrange - Crear inventario inicial
        Producto arroz = controlador.crearProducto("SIMPLE", "P001", "Arroz", 2500.0, "GRANOS");
        Producto carne = controlador.crearProducto("PESO", "C001", "Lomo", 25000.0, "CARNES");
        Producto leche = controlador.crearProducto("SIMPLE", "L001", "Leche", 3500.0, "LACTEOS");

        // Act - Configurar productos
        ((ProductoSimple) arroz).setStock(50);
        ((ProductoSimple) leche).setStock(30);
        ((ProductoSimple) leche).setRefrigeracion(true);
        ((ProductoPeso) carne).setPesoPromedio(1.8);

        // Assert - Verificar estado final
        List<Producto> todosProductos = controlador.obtenerTodosLosProductos();
        Producto arrozBuscado = controlador.buscarPorCodigo("P001");
        Producto carneBuscada = controlador.buscarPorCodigo("C001");

        assertAll(
                () -> assertEquals(3, todosProductos.size()),
                () -> assertEquals(50, ((ProductoSimple) arrozBuscado).getStock()),
                () -> assertTrue(leche.requiereRefrigeracion()),
                () -> assertEquals(45000.0, carneBuscada.calcularPrecioFinal(), 0.01)
        );
    }

    @Test
    @Order(10)
    @DisplayName("Integración: Productos con mismo código son iguales en búsqueda")
    void testProductosMismoCodigoEnBusqueda() {
        // Arrange
        Producto p1 = controlador.crearProducto("SIMPLE", "P001", "Producto A", 1000.0, "CAT1");

        // Act
        Producto p2 = controlador.buscarPorCodigo("P001");

        // Assert
        assertSame(p1, p2);
    }
}
