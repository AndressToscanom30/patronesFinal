package Integration.Builder;

import Creacionales.Builder.controlador.FacturaControlador;
import Creacionales.Builder.modelo.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Builder Pattern - Tests de Integración")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FacturaBuilderIntegrationTest {

    private FacturaControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new FacturaControlador();
    }

    @Test
    @Order(1)
    @DisplayName("Integración: Controlador inicia builder correctamente")
    void testControladorIniciaBuilder() {
        // Arrange & Act
        Factura.Builder builder = controlador.iniciarFactura();

        // Assert
        assertNotNull(builder);
    }

    @Test
    @Order(2)
    @DisplayName("Integración: Flujo completo de creación y guardado")
    void testFlujoCompletoCreacionYGuardado() {
        // Arrange
        Cliente cliente = new Cliente("C001", "Pedro García", "12345678");
        ItemFactura item1 = new ItemFactura("P001", "Arroz", 2, 2500);
        ItemFactura item2 = new ItemFactura("P002", "Frijol", 1, 4500);

        // Act
        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(item1)
                .agregarItem(item2)
                .conMetodoPago("EFECTIVO")
                .calcularImpuestosAutomaticamente()
                .build();

        Factura guardada = controlador.guardarFactura(factura);

        // Assert
        assertAll("Verificar flujo completo",
                () -> assertNotNull(guardada),
                () -> assertEquals(factura.getNumeroFactura(), guardada.getNumeroFactura()),
                () -> assertEquals(1, controlador.obtenerTodasLasFacturas().size())
        );
    }

    @Test
    @Order(3)
    @DisplayName("Integración: Buscar factura guardada")
    void testBuscarFacturaGuardada() {
        // Arrange
        Cliente cliente = new Cliente("C001", "María López", "87654321");
        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Leche", 3, 3500))
                .conMetodoPago("TARJETA")
                .build();

        controlador.guardarFactura(factura);

        // Act
        Factura encontrada = controlador.buscarFactura(factura.getNumeroFactura());

        // Assert
        assertNotNull(encontrada);
        assertEquals(factura.getNumeroFactura(), encontrada.getNumeroFactura());
    }

    @Test
    @Order(4)
    @DisplayName("Integración: Crear múltiples facturas")
    void testCrearMultiplesFacturas() {
        // Arrange & Act
        for (int i = 0; i < 5; i++) {
            Cliente cliente = new Cliente("C00" + i, "Cliente " + i, "123");
            Factura factura = controlador.iniciarFactura()
                    .paraCliente(cliente)
                    .agregarItem(new ItemFactura("P001", "Producto", 1, 1000 * (i + 1)))
                    .conMetodoPago("EFECTIVO")
                    .build();
            controlador.guardarFactura(factura);
        }

        // Assert
        List<Factura> todas = controlador.obtenerTodasLasFacturas();
        assertEquals(5, todas.size());
    }

    @Test
    @Order(5)
    @DisplayName("Integración: Buscar facturas por cliente")
    void testBuscarPorCliente() {
        // Arrange
        Cliente cliente1 = new Cliente("C001", "Juan Pérez", "123");
        Cliente cliente2 = new Cliente("C002", "Ana García", "456");

        controlador.guardarFactura(
                controlador.iniciarFactura()
                        .paraCliente(cliente1)
                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                        .conMetodoPago("EFECTIVO")
                        .build()
        );

        controlador.guardarFactura(
                controlador.iniciarFactura()
                        .paraCliente(cliente2)
                        .agregarItem(new ItemFactura("P002", "Test", 1, 2000))
                        .conMetodoPago("TARJETA")
                        .build()
        );

        // Act
        List<Factura> facturasPedro = controlador.buscarPorCliente("Juan");

        // Assert
        assertEquals(1, facturasPedro.size());
        assertEquals("Juan Pérez", facturasPedro.get(0).getCliente().getNombre());
    }

    @Test
    @Order(6)
    @DisplayName("Integración: Buscar facturas por fecha")
    void testBuscarPorFecha() {
        // Arrange
        LocalDate hoy = LocalDate.now();
        LocalDate ayer = hoy.minusDays(1);

        controlador.guardarFactura(
                controlador.iniciarFactura()
                        .conFechaEmision(hoy)
                        .paraCliente(new Cliente("C001", "Test", "123"))
                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                        .conMetodoPago("EFECTIVO")
                        .build()
        );

        controlador.guardarFactura(
                controlador.iniciarFactura()
                        .conFechaEmision(ayer)
                        .paraCliente(new Cliente("C002", "Test", "456"))
                        .agregarItem(new ItemFactura("P002", "Test", 1, 2000))
                        .conMetodoPago("TARJETA")
                        .build()
        );

        // Act
        List<Factura> facturasHoy = controlador.buscarPorFecha(hoy);

        // Assert
        assertEquals(1, facturasHoy.size());
        assertEquals(hoy, facturasHoy.get(0).getFechaEmision());
    }

    @Test
    @Order(7)
    @DisplayName("Integración: Calcular total de ventas")
    void testCalcularTotalVentas() {
        // Arrange
        controlador.guardarFactura(
                controlador.iniciarFactura()
                        .paraCliente(new Cliente("C001", "Test1", "123"))
                        .agregarItem(new ItemFactura("P001", "Test", 1, 10000))
                        .conMetodoPago("EFECTIVO")
                        .build()
        );

        controlador.guardarFactura(
                controlador.iniciarFactura()
                        .paraCliente(new Cliente("C002", "Test2", "456"))
                        .agregarItem(new ItemFactura("P002", "Test", 1, 15000))
                        .conMetodoPago("TARJETA")
                        .build()
        );

        // Act
        double totalVentas = controlador.calcularTotalVentas();

        // Assert
        assertEquals(25000.0, totalVentas, 0.01);
    }

    @Test
    @Order(8)
    @DisplayName("Integración: Builder con múltiples items actualiza subtotal")
    void testBuilderActualizaSubtotal() {
        // Arrange & Act
        Factura factura = controlador.iniciarFactura()
                .paraCliente(new Cliente("C001", "Test", "123"))
                .agregarItem(new ItemFactura("P001", "Item1", 2, 1000))
                .agregarItem(new ItemFactura("P002", "Item2", 3, 2000))
                .agregarItem(new ItemFactura("P003", "Item3", 1, 5000))
                .conMetodoPago("EFECTIVO")
                .build();

        // Assert
        assertEquals(13000.0, factura.getSubtotal(), 0.01,
                "(2*1000) + (3*2000) + (1*5000) = 13000");
    }

    @Test
    @Order(9)
    @DisplayName("Integración: Factura con envío completa todos los datos")
    void testFacturaConEnvioCompleta() {
        // Arrange
        Cliente cliente = new Cliente("C001", "Carlos Ruiz", "98765432");
        cliente.setDireccion("Calle Principal #123");
        cliente.setEmail("carlos@email.com");
        cliente.setTelefono("555-1234");

        // Act
        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("E001", "Televisor", 1, 800000))
                .conMetodoPago("TRANSFERENCIA")
                .calcularImpuestosAutomaticamente()
                .conDireccionEnvio(cliente.getDireccion())
                .conObservaciones("Envío express")
                .build();

        controlador.guardarFactura(factura);

        // Assert
        assertAll("Verificar factura con envío completa",
                () -> assertTrue(factura.isRequiereEnvio()),
                () -> assertEquals(cliente.getDireccion(), factura.getDireccionEnvio()),
                () -> assertEquals(152000.0, factura.getImpuestos(), 0.01),
                () -> assertNotNull(factura.getObservaciones())
        );
    }

    @Test
    @Order(10)
    @DisplayName("Integración: Flujo completo de venta con descuentos y propina")
    void testFlujoVentaConDescuentosYPropina() {
        // Arrange
        Cliente cliente = new Cliente("C001", "Laura Martínez", "11223344");
        cliente.setEmail("laura@email.com");

        // Act
        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Vino", 2, 25000))
                .agregarItem(new ItemFactura("P002", "Queso", 1, 18000))
                .conMetodoPago("TARJETA")
                .calcularImpuestosAutomaticamente()
                .conDescuentos(5000)
                .conPropina(10000)
                .conObservaciones("Cliente VIP - Descuento aplicado")
                .build();

        controlador.guardarFactura(factura);

        double totalEsperado = 68000 + 12920 - 5000 + 10000; // subtotal + impuestos - descuentos + propina

        // Assert
        assertAll("Verificar venta completa",
                () -> assertEquals(68000.0, factura.getSubtotal(), 0.01),
                () -> assertEquals(12920.0, factura.getImpuestos(), 0.01),
                () -> assertEquals(5000.0, factura.getDescuentos(), 0.01),
                () -> assertEquals(10000.0, factura.getPropina(), 0.01),
                () -> assertEquals(totalEsperado, factura.calcularTotal(), 0.01),
                () -> assertTrue(factura.enviarPorCorreo())
        );
    }
}
