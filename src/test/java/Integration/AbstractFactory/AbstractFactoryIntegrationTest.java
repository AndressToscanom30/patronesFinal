package Integration.AbstractFactory;

import Creacionales.AbstractFactory.controlador.VentaControlador;
import Creacionales.AbstractFactory.modelo.*;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Abstract Factory Pattern - Tests de Integración")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AbstractFactoryIntegrationTest {

    private VentaControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new VentaControlador();
    }

    @Test
    @Order(1)
    @DisplayName("Integración: Controlador inicializa factories")
    void testControladorInicializaFactories() {
        // Act
        Set<String> canales = controlador.obtenerCanalesDisponibles();

        // Assert
        assertAll("Verificar canales inicializados",
            () -> assertNotNull(canales),
            () -> assertTrue(canales.contains("POS")),
            () -> assertEquals(1, canales.size())
        );
    }

    @Test
    @Order(2)
    @DisplayName("Integración: Obtener factory POS")
    void testObtenerFactoryPOS() {
        // Act
        VentaComponentFactory factory = controlador.obtenerFactory("POS");

        // Assert
        assertAll("Verificar factory POS",
            () -> assertNotNull(factory),
            () -> assertInstanceOf(POSVentaFactory.class, factory),
            () -> assertEquals("POS", factory.getTipoCanal())
        );
    }

    @Test
    @Order(3)
    @DisplayName("Integración: Crear venta POS")
    void testCrearVentaPOS() {
        // Act
        DatosVenta venta = controlador.crearVenta("POS", "C001", "Juan Pérez");

        // Assert
        assertAll("Verificar creación de venta",
            () -> assertNotNull(venta),
            () -> assertTrue(venta.getNumeroVenta().startsWith("POS-")),
            () -> assertEquals("C001", venta.getClienteId()),
            () -> assertEquals("Juan Pérez", venta.getClienteNombre())
        );
    }

    @Test
    @Order(4)
    @DisplayName("Integración: Procesar venta POS completa")
    void testProcesarVentaPOSCompleta() {
        // Arrange
        DatosVenta venta = controlador.crearVenta("POS", "C001", "María López");
        venta.agregarItem(new ItemVenta("PROD-001", "Arroz", 2, 2500));
        venta.agregarItem(new ItemVenta("PROD-002", "Aceite", 1, 8500));
        venta.setMetodoPago("EFECTIVO");

        // Act
        boolean procesada = controlador.procesarVenta("POS", venta);

        // Assert
        assertAll("Verificar venta procesada",
            () -> assertTrue(procesada),
            () -> assertEquals(1, controlador.obtenerVentas().size()),
            () -> assertEquals(13500.0, venta.getSubtotal(), 0.01)
        );
    }

    @Test
    @Order(5)
    @DisplayName("Integración: Venta rechazada por stock insuficiente")
    void testVentaRechazadaPorStock() {
        // Arrange
        DatosVenta venta = controlador.crearVenta("POS", "C002", "Pedro Ruiz");
        venta.agregarItem(new ItemVenta("PROD-001", "Producto", 10000, 1000));
        venta.setMetodoPago("TARJETA");

        // Act
        boolean procesada = controlador.procesarVenta("POS", venta);

        // Assert
        assertFalse(procesada, "No debe procesar venta sin stock suficiente");
    }

    @Test
    @Order(6)
    @DisplayName("Integración: Calcular envío POS")
    void testCalcularEnvioPOS() {
        // Act
        double costo = controlador.calcularEnvio("POS", "Bogotá", 5.0);

        // Assert
        assertEquals(0.0, costo, "POS no debe tener costo de envío");
    }

    @Test
    @Order(7)
    @DisplayName("Integración: Procesar devolución POS permitida")
    void testProcesarDevolucionPermitida() {
        // Arrange
        DatosVenta venta = controlador.crearVenta("POS", "C003", "Ana García");
        venta.agregarItem(new ItemVenta("PROD-001", "Test", 1, 10000));
        venta.setMetodoPago("EFECTIVO");
        controlador.procesarVenta("POS", venta);

        // Act
        boolean devolucionPermitida = controlador.procesarDevolucion(
            "POS",
            venta.getNumeroVenta(),
            5,
            venta.getTotal()
        );

        // Assert
        assertTrue(devolucionPermitida, "Debe permitir devolución dentro de 15 días");
    }

    @Test
    @Order(8)
    @DisplayName("Integración: Procesar devolución POS rechazada")
    void testProcesarDevolucionRechazada() {
        // Arrange
        DatosVenta venta = controlador.crearVenta("POS", "C004", "Carlos Mendoza");
        venta.agregarItem(new ItemVenta("PROD-001", "Test", 1, 10000));
        venta.setMetodoPago("EFECTIVO");
        controlador.procesarVenta("POS", venta);

        // Act
        boolean devolucionPermitida = controlador.procesarDevolucion(
            "POS",
            venta.getNumeroVenta(),
            20,
            venta.getTotal()
        );

        // Assert
        assertFalse(devolucionPermitida, "No debe permitir devolución después de 15 días");
    }

    @Test
    @Order(9)
    @DisplayName("Integración: Buscar venta guardada")
    void testBuscarVentaGuardada() {
        // Arrange
        DatosVenta venta = controlador.crearVenta("POS", "C005", "Laura Díaz");
        venta.agregarItem(new ItemVenta("PROD-001", "Test", 1, 5000));
        venta.setMetodoPago("EFECTIVO");
        controlador.procesarVenta("POS", venta);

        // Act
        DatosVenta encontrada = controlador.buscarVenta(venta.getNumeroVenta());

        // Assert
        assertAll("Verificar venta encontrada",
            () -> assertNotNull(encontrada),
            () -> assertEquals(venta.getNumeroVenta(), encontrada.getNumeroVenta()),
            () -> assertEquals(venta.getClienteNombre(), encontrada.getClienteNombre())
        );
    }

    @Test
    @Order(10)
    @DisplayName("Integración: Buscar venta inexistente")
    void testBuscarVentaInexistente() {
        // Act
        DatosVenta venta = controlador.buscarVenta("POS-999999");

        // Assert
        assertNull(venta, "Debe retornar null para venta inexistente");
    }

    @Test
    @Order(11)
    @DisplayName("Integración: Obtener ventas por canal")
    void testObtenerVentasPorCanal() {
        // Arrange
        for (int i = 1; i <= 3; i++) {
            DatosVenta venta = controlador.crearVenta("POS", "C00" + i, "Cliente " + i);
            venta.agregarItem(new ItemVenta("PROD-001", "Test", 1, 1000 * i));
            venta.setMetodoPago("EFECTIVO");
            controlador.procesarVenta("POS", venta);
        }

        // Act
        List<DatosVenta> ventasPOS = controlador.obtenerVentasPorCanal("POS");

        // Assert
        assertEquals(3, ventasPOS.size(), "Debe haber 3 ventas POS");
    }

    @Test
    @Order(12)
    @DisplayName("Integración: Calcular total de ventas")
    void testCalcularTotalVentas() {
        // Arrange
        DatosVenta venta1 = controlador.crearVenta("POS", "C001", "Test1");
        venta1.agregarItem(new ItemVenta("PROD-001", "Test", 1, 10000));
        venta1.setMetodoPago("EFECTIVO");
        controlador.procesarVenta("POS", venta1);

        DatosVenta venta2 = controlador.crearVenta("POS", "C002", "Test2");
        venta2.agregarItem(new ItemVenta("PROD-002", "Test", 1, 15000));
        venta2.setMetodoPago("TARJETA");
        controlador.procesarVenta("POS", venta2);

        // Act
        double total = controlador.calcularTotalVentas();

        // Assert
        assertTrue(total > 25000, "Total debe incluir subtotal + impuestos de ambas ventas");
    }

    @Test
    @Order(13)
    @DisplayName("Integración: Estadísticas por canal")
    void testEstadisticasPorCanal() {
        // Arrange
        for (int i = 1; i <= 5; i++) {
            DatosVenta venta = controlador.crearVenta("POS", "C00" + i, "Cliente " + i);
            venta.agregarItem(new ItemVenta("PROD-001", "Test", 1, 1000));
            venta.setMetodoPago("EFECTIVO");
            controlador.procesarVenta("POS", venta);
        }

        // Act
        Map<String, Long> stats = controlador.obtenerEstadisticasPorCanal();

        // Assert
        assertAll("Verificar estadísticas",
            () -> assertTrue(stats.containsKey("POS")),
            () -> assertTrue(stats.get("POS") >= 5)
        );
    }

    @Test
    @Order(14)
    @DisplayName("Integración: Obtener información de factory")
    void testObtenerInformacionFactory() {
        // Act
        String info = controlador.obtenerInformacionFactory("POS");

        // Assert
        assertAll("Verificar información de factory",
            () -> assertNotNull(info),
            () -> assertTrue(info.contains("POS")),
            () -> assertTrue(info.contains("Generador")),
            () -> assertTrue(info.contains("Validador")),
            () -> assertTrue(info.contains("Calculador")),
            () -> assertTrue(info.contains("Gestor"))
        );
    }

    @Test
    @Order(15)
    @DisplayName("Integración: Factory inválida")
    void testFactoryInvalida() {
        // Act
        VentaComponentFactory factory = controlador.obtenerFactory("INEXISTENTE");

        // Assert
        assertNull(factory, "Debe retornar null para canal inexistente");
    }

    @Test
    @Order(16)
    @DisplayName("Integración: Múltiples ventas secuenciales")
    void testMultiplesVentasSecuenciales() {
        // Act
        for (int i = 1; i <= 10; i++) {
            DatosVenta venta = controlador.crearVenta("POS", "C" + i, "Cliente " + i);
            venta.agregarItem(new ItemVenta("PROD-00" + (i % 3 + 1), "Producto", 1, 1000 * i));
            venta.setMetodoPago(i % 2 == 0 ? "TARJETA" : "EFECTIVO");
            
            boolean procesada = controlador.procesarVenta("POS", venta);
            assertTrue(procesada, "Venta " + i + " debe procesarse");
        }

        // Assert
        List<DatosVenta> ventas = controlador.obtenerVentas();
        assertTrue(ventas.size() >= 10, "Debe haber al menos 10 ventas");
    }

    @Test
    @Order(17)
    @DisplayName("Integración: Reserva de stock concurrente")
    void testReservaStockConcurrente() {
        // Arrange
        VentaComponentFactory factory = controlador.obtenerFactory("POS");
        ValidadorStock validador = factory.crearValidadorStock();
        int stockInicial = validador.obtenerStockDisponible("PROD-003");

        // Act - Múltiples ventas del mismo producto
        int ventasProcesadas = 0;
        for (int i = 1; i <= 5; i++) {
            DatosVenta venta = controlador.crearVenta("POS", "C" + i, "Cliente " + i);
            venta.agregarItem(new ItemVenta("PROD-003", "Producto", 5, 1000));
            venta.setMetodoPago("EFECTIVO");
            
            if (controlador.procesarVenta("POS", venta)) {
                ventasProcesadas++;
            }
        }

        // Assert
        assertTrue(ventasProcesadas > 0, "Al menos una venta debe procesarse");
        assertTrue(ventasProcesadas <= stockInicial / 5, "No debe vender más del stock disponible");
    }
}
