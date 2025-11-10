package e2e.FactoryMethod;

import Creacionales.FactoryMethod.controlador.ProductoControlador;
import Creacionales.FactoryMethod.modelo.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Factory Method - Tests End-to-End")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FactoryMethodE2ETest {

    private ProductoControlador controlador;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        controlador = new ProductoControlador();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @Order(1)
    @DisplayName("E2E: Escenario completo de supermercado - Día de inventario")
    void testEscenarioCompletoInventario() {

        Producto pan = controlador.crearProducto("SIMPLE", "PAN001", "Pan integral", 2800.0, "PANADERIA");
        ((ProductoSimple) pan).setStock(150);

        Producto carne = controlador.crearProducto("PESO", "CAR001", "Carne molida", 18000.0, "CARNES");
        ((ProductoPeso) carne).setPesoPromedio(0.5);

        Producto pollo = controlador.crearProducto("PESO", "CAR002", "Pechuga de pollo", 15000.0, "CARNES");
        ((ProductoPeso) pollo).setPesoPromedio(1.2);

        Producto leche = controlador.crearProducto("SIMPLE", "LAC001", "Leche entera", 3500.0, "LACTEOS");
        ((ProductoSimple) leche).setStock(80);
        ((ProductoSimple) leche).setRefrigeracion(true);

        Producto queso = controlador.crearProducto("PESO", "LAC002", "Queso campesino", 22000.0, "LACTEOS");
        ((ProductoPeso) queso).setPesoPromedio(0.8);

        List<Producto> inventario = controlador.obtenerTodosLosProductos();

        assertAll("Verificar inventario del día",
                () -> assertEquals(5, inventario.size(), "Debe haber 5 productos"),
                () -> assertEquals(150, ((ProductoSimple) pan).getStock(), "Pan debe tener stock"),
                () -> assertTrue(leche.requiereRefrigeracion(), "Leche requiere refrigeración"),
                () -> assertEquals(9000.0, carne.calcularPrecioFinal(), 0.01, "Precio carne calculado"),
                () -> assertEquals(18000.0, pollo.calcularPrecioFinal(), 0.01, "Precio pollo calculado"),
                () -> assertEquals(17600.0, queso.calcularPrecioFinal(), 0.01, "Precio queso calculado")
        );
    }

    @Test
    @Order(2)
    @DisplayName("E2E: Escenario de venta - Cliente compra múltiples productos")
    void testEscenarioVentaCliente() {
        Producto arroz = controlador.crearProducto("SIMPLE", "GRA001", "Arroz", 2500.0, "GRANOS");
        ((ProductoSimple) arroz).setStock(100);

        Producto frijol = controlador.crearProducto("SIMPLE", "GRA002", "Frijol", 4500.0, "GRANOS");
        ((ProductoSimple) frijol).setStock(80);

        Producto tomate = controlador.crearProducto("PESO", "FRU001", "Tomate", 3500.0, "FRUTAS");
        ((ProductoPeso) tomate).setPesoPromedio(2.0);

        Producto arrozSeleccionado = controlador.buscarPorCodigo("GRA001");
        Producto frijolSeleccionado = controlador.buscarPorCodigo("GRA002");
        Producto tomateSeleccionado = controlador.buscarPorCodigo("FRU001");

        double totalCompra = arrozSeleccionado.calcularPrecioFinal() +
                frijolSeleccionado.calcularPrecioFinal() +
                tomateSeleccionado.calcularPrecioFinal();

        ((ProductoSimple) arrozSeleccionado).setStock(99);
        ((ProductoSimple) frijolSeleccionado).setStock(79);

        assertAll("Verificar compra del cliente",
                () -> assertEquals(14000.0, totalCompra, 0.01, "Total de compra correcto"),
                () -> assertEquals(99, ((ProductoSimple) arrozSeleccionado).getStock(), "Stock arroz actualizado"),
                () -> assertEquals(79, ((ProductoSimple) frijolSeleccionado).getStock(), "Stock frijol actualizado")
        );
    }

    @Test
    @Order(3)
    @DisplayName("E2E: Escenario de reabastecimiento - Proveedor entrega productos")
    void testEscenarioReabastecimiento() {
        Producto azucar = controlador.crearProducto("SIMPLE", "GRA003", "Azúcar", 3000.0, "GRANOS");
        ((ProductoSimple) azucar).setStock(5);

        Producto sal = controlador.crearProducto("SIMPLE", "GRA004", "Sal", 1500.0, "GRANOS");
        ((ProductoSimple) sal).setStock(3);

        int stockAnteriorAzucar = ((ProductoSimple) azucar).getStock();
        int stockAnteriorSal = ((ProductoSimple) sal).getStock();

        ((ProductoSimple) azucar).setStock(stockAnteriorAzucar + 100);
        ((ProductoSimple) sal).setStock(stockAnteriorSal + 150);

        assertAll("Verificar reabastecimiento",
                () -> assertEquals(105, ((ProductoSimple) azucar).getStock(), "Stock azúcar aumentado"),
                () -> assertEquals(153, ((ProductoSimple) sal).getStock(), "Stock sal aumentado")
        );
    }

    @Test
    @Order(4)
    @DisplayName("E2E: Escenario de cambio de precios - Inflación")
    void testEscenarioCambioPreciosInflacion() {
        Producto p1 = controlador.crearProducto("SIMPLE", "P001", "Producto A", 1000.0, "VARIOS");
        Producto p2 = controlador.crearProducto("SIMPLE", "P002", "Producto B", 2000.0, "VARIOS");
        Producto p3 = controlador.crearProducto("PESO", "P003", "Producto C", 5000.0, "VARIOS");

        double porcentajeIncremento = 1.10;

        p1.setPrecio(p1.getPrecio() * porcentajeIncremento);
        p2.setPrecio(p2.getPrecio() * porcentajeIncremento);
        p3.setPrecio(p3.getPrecio() * porcentajeIncremento);

        assertAll("Verificar incremento de precios",
                () -> assertEquals(1100.0, p1.getPrecio(), 0.01, "Precio P001 incrementado"),
                () -> assertEquals(2200.0, p2.getPrecio(), 0.01, "Precio P002 incrementado"),
                () -> assertEquals(5500.0, p3.getPrecio(), 0.01, "Precio P003 incrementado")
        );
    }

    @Test
    @Order(5)
    @DisplayName("E2E: Escenario de auditoría - Verificar productos vencidos")
    void testEscenarioAuditoriaProductos() {
        Producto p1 = controlador.crearProducto("SIMPLE", "P001", "Producto reciente", 1000.0, "VARIOS");
        Producto p2 = controlador.crearProducto("SIMPLE", "P002", "Producto antiguo", 2000.0, "VARIOS");

        LocalDate fechaHoy = LocalDate.now();

        assertAll("Verificar auditoría de fechas",
                () -> assertEquals(fechaHoy, p1.getFechaCreacion(), "Producto creado hoy"),
                () -> assertEquals(fechaHoy, p2.getFechaCreacion(), "Producto creado hoy"),
                () -> assertNotNull(p1.getFechaCreacion(), "Fecha no debe ser nula"),
                () -> assertNotNull(p2.getFechaCreacion(), "Fecha no debe ser nula")
        );
    }

    @Test
    @Order(6)
    @DisplayName("E2E: Escenario de carnicería - Pesaje en báscula")
    void testEscenarioCarniceriaPesaje() {
        // Setup: Cliente pide carne en carnicería
        Producto lomoDeCerdo = controlador.crearProducto("PESO", "CAR003", "Lomo de cerdo", 28000.0, "CARNES");
        ProductoPeso lomo = (ProductoPeso) lomoDeCerdo;

        // Simulate: Carnicero corta y pesa
        lomo.setPesoPromedio(1.350); // Cliente pidió 1.350 kg

        // Calcular precio final
        double precioFinal = lomoDeCerdo.calcularPrecioFinal();

        assertAll("Verificar pesaje en carnicería",
                () -> assertTrue(lomo.isRequiereBascula(), "Debe requerir báscula"),
                () -> assertTrue(lomoDeCerdo.requiereRefrigeracion(), "Carnes requieren refrigeración"),
                () -> assertEquals(37800.0, precioFinal, 0.01, "Precio = 28000 × 1.350")
        );
    }

    @Test
    @Order(7)
    @DisplayName("E2E: Escenario de frutería - Venta por peso")
    void testEscenarioFruteriaPeso() {
        // Setup: Productos de frutería
        Producto manzana = controlador.crearProducto("PESO", "FRU002", "Manzana", 4500.0, "FRUTAS");
        Producto banano = controlador.crearProducto("PESO", "FRU003", "Banano", 2800.0, "FRUTAS");

        ProductoPeso manzanas = (ProductoPeso) manzana;
        ProductoPeso bananos = (ProductoPeso) banano;

        // Simulate: Cliente escoge frutas y se pesan
        manzanas.setPesoPromedio(2.5); // 2.5 kg de manzanas
        bananos.setPesoPromedio(1.8);  // 1.8 kg de bananos

        double totalFrutas = manzana.calcularPrecioFinal() + banano.calcularPrecioFinal();

        assertAll("Verificar compra de frutas",
                () -> assertEquals(11250.0, manzana.calcularPrecioFinal(), 0.01, "Precio manzanas"),
                () -> assertEquals(5040.0, banano.calcularPrecioFinal(), 0.01, "Precio bananos"),
                () -> assertEquals(16290.0, totalFrutas, 0.01, "Total frutas"),
                () -> assertFalse(manzana.requiereRefrigeracion(), "Frutas no requieren refrigeración")
        );
    }

    @Test
    @Order(8)
    @DisplayName("E2E: Escenario de cierre de caja - Resumen del día")
    void testEscenarioCierreCaja() {
        // Setup: Crear ventas del día
        controlador.crearProducto("SIMPLE", "V001", "Venta 1", 10000.0, "VARIOS");
        controlador.crearProducto("SIMPLE", "V002", "Venta 2", 15000.0, "VARIOS");
        controlador.crearProducto("PESO", "V003", "Venta 3", 20000.0, "VARIOS");
        controlador.crearProducto("SIMPLE", "V004", "Venta 4", 8000.0, "VARIOS");

        // Simulate: Calcular totales del día
        List<Producto> ventasDelDia = controlador.obtenerTodosLosProductos();
        double totalVentas = ventasDelDia.stream()
                .mapToDouble(Producto::calcularPrecioFinal)
                .sum();

        long productosSimples = ventasDelDia.stream()
                .filter(p -> p.obtenerTipo().equals("SIMPLE"))
                .count();

        long productosPeso = ventasDelDia.stream()
                .filter(p -> p.obtenerTipo().equals("PESO"))
                .count();

        assertAll("Verificar cierre de caja",
                () -> assertEquals(4, ventasDelDia.size(), "Total de ventas"),
                () -> assertEquals(3, productosSimples, "Productos simples"),
                () -> assertEquals(1, productosPeso, "Productos por peso"),
                () -> assertEquals(53000.0, totalVentas, 0.01, "Total vendido en el día")
        );
    }

    @Test
    @Order(9)
    @DisplayName("E2E: Escenario de error - Manejo de productos inválidos")
    void testEscenarioManejoErrores() {
        // Simulate: Intentar crear productos con datos inválidos

        assertAll("Verificar manejo de errores",
                () -> assertThrows(IllegalArgumentException.class,
                        () -> controlador.crearProducto("SIMPLE", "", "Sin código", 1000.0, "CAT"),
                        "Debe rechazar código vacío"),

                () -> assertThrows(IllegalArgumentException.class,
                        () -> controlador.crearProducto("SIMPLE", "P001", null, 1000.0, "CAT"),
                        "Debe rechazar nombre nulo"),

                () -> assertThrows(IllegalArgumentException.class,
                        () -> controlador.crearProducto("SIMPLE", "P001", "Producto", -100.0, "CAT"),
                        "Debe rechazar precio negativo"),

                () -> assertNull(controlador.crearProducto("TIPO_INVALIDO", "P001", "Producto", 1000.0, "CAT"),
                        "Debe retornar null para tipo inválido")
        );
    }

    @Test
    @Order(10)
    @DisplayName("E2E: Escenario completo de supermercado en un día")
    void testEscenarioCompletoDiaSupermercado() {
        // MAÑANA: Apertura y recepción de inventario
        Producto pan = controlador.crearProducto("SIMPLE", "PAN001", "Pan", 2500.0, "PANADERIA");
        ((ProductoSimple) pan).setStock(200);

        Producto carne = controlador.crearProducto("PESO", "CAR001", "Carne", 20000.0, "CARNES");
        Producto leche = controlador.crearProducto("SIMPLE", "LAC001", "Leche", 3500.0, "LACTEOS");
        ((ProductoSimple) leche).setStock(150);
        ((ProductoSimple) leche).setRefrigeracion(true);

        // MEDIODÍA: Ventas matutinas
        ((ProductoSimple) pan).setStock(180); // Se vendieron 20 panes
        ((ProductoSimple) leche).setStock(140); // Se vendieron 10 leches

        // TARDE: Cliente compra carne (pesaje)
        ((ProductoPeso) carne).setPesoPromedio(1.5);
        double ventaCarne = carne.calcularPrecioFinal();

        // TARDE: Ajuste de precios
        leche.setPrecio(3800.0); // Incremento de precio

        // NOCHE: Cierre y resumen
        List<Producto> inventarioFinal = controlador.obtenerTodosLosProductos();

        assertAll("Verificar día completo de operaciones",
                () -> assertEquals(3, inventarioFinal.size(), "Productos en inventario"),
                () -> assertEquals(180, ((ProductoSimple) pan).getStock(), "Stock pan actualizado"),
                () -> assertEquals(140, ((ProductoSimple) leche).getStock(), "Stock leche actualizado"),
                () -> assertEquals(30000.0, ventaCarne, 0.01, "Venta de carne"),
                () -> assertEquals(3800.0, leche.getPrecio(), 0.01, "Precio leche actualizado"),
                () -> assertTrue(leche.requiereRefrigeracion(), "Leche en refrigeración"),
                () -> assertTrue(carne.requiereRefrigeracion(), "Carne en refrigeración")
        );
    }
}
