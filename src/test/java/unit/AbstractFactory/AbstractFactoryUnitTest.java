package unit.AbstractFactory;


import Creacionales.AbstractFactory.modelo.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Abstract Factory Pattern - Tests Unitarios")
@ExtendWith(MockitoExtension.class)
class AbstractFactoryUnitTest {

    @Nested
    @DisplayName("POSVentaFactory - Creación de componentes")
    class POSFactoryTests {

        private VentaComponentFactory factory;

        @BeforeEach
        void setUp() {
            factory = new POSVentaFactory();
        }

        @Test
        @DisplayName("Factory crea GeneradorFactura POS")
        void testCrearGeneradorFactura() {
            // Act
            GeneradorFactura generador = factory.crearGeneradorFactura();

            // Assert
            assertAll("Verificar GeneradorFactura POS",
                () -> assertNotNull(generador),
                () -> assertInstanceOf(POSGeneradorFactura.class, generador),
                () -> assertEquals("Generador POS - Factura Física/Ticket", generador.obtenerTipoGenerador()),
                () -> assertFalse(generador.requiereFacturaElectronica())
            );
        }

        @Test
        @DisplayName("Factory crea ValidadorStock POS")
        void testCrearValidadorStock() {
            // Act
            ValidadorStock validador = factory.crearValidadorStock();

            // Assert
            assertAll("Verificar ValidadorStock POS",
                () -> assertNotNull(validador),
                () -> assertInstanceOf(POSValidadorStock.class, validador),
                () -> assertEquals("Validador POS - Stock Local en Tiempo Real", validador.obtenerTipoValidador())
            );
        }

        @Test
        @DisplayName("Factory crea CalculadorEnvio POS")
        void testCrearCalculadorEnvio() {
            // Act
            CalculadorEnvio calculador = factory.crearCalculadorEnvio();

            // Assert
            assertAll("Verificar CalculadorEnvio POS",
                () -> assertNotNull(calculador),
                () -> assertInstanceOf(POSCalculadorEnvio.class, calculador),
                () -> assertEquals("Calculador POS - Sin Envío (Retiro Inmediato)", calculador.obtenerTipoCalculador()),
                () -> assertFalse(calculador.soportaEnvioExpress())
            );
        }

        @Test
        @DisplayName("Factory crea GestorDevolucion POS")
        void testCrearGestorDevolucion() {
            // Act
            GestorDevolucion gestor = factory.crearGestorDevolucion();

            // Assert
            assertAll("Verificar GestorDevolucion POS",
                () -> assertNotNull(gestor),
                () -> assertInstanceOf(POSGestorDevolucion.class, gestor),
                () -> assertEquals("Gestor POS - Devolución con Ticket Físico", gestor.obtenerTipoGestor()),
                () -> assertTrue(gestor.obtenerPoliticaDevolucion().contains("15 días"))
            );
        }

        @Test
        @DisplayName("Factory retorna tipo de canal correcto")
        void testGetTipoCanal() {
            // Act & Assert
            assertEquals("POS", factory.getTipoCanal());
        }

        @Test
        @DisplayName("Múltiples llamadas crean instancias diferentes")
        void testMultiplesInstancias() {
            // Act
            GeneradorFactura gen1 = factory.crearGeneradorFactura();
            GeneradorFactura gen2 = factory.crearGeneradorFactura();

            // Assert
            assertNotSame(gen1, gen2, "Cada llamada debe crear nueva instancia");
        }
    }

    @Nested
    @DisplayName("POSGeneradorFactura - Generación de facturas")
    class POSGeneradorFacturaTests {

        private GeneradorFactura generador;

        @BeforeEach
        void setUp() {
            generador = new POSGeneradorFactura();
        }

        @Test
        @DisplayName("Genera número de factura con formato correcto")
        void testGenerarNumeroFactura() {
            // Act
            String numero = generador.generarNumeroFactura();

            // Assert
            assertAll("Verificar formato de número",
                () -> assertNotNull(numero),
                () -> assertTrue(numero.startsWith("POS-FAC-")),
                () -> assertEquals(14, numero.length())
            );
        }

        @Test
        @DisplayName("Números de factura son consecutivos")
        void testNumerosConsecutivos() {
            // Act
            String num1 = generador.generarNumeroFactura();
            String num2 = generador.generarNumeroFactura();

            // Assert
            assertNotEquals(num1, num2, "Los números deben ser diferentes");
        }

        @Test
        @DisplayName("Genera formato de factura completo")
        void testGenerarFormatoFactura() {
            // Arrange
            DatosVenta datos = new DatosVenta("POS-001", "C001", "Juan Pérez");
            datos.agregarItem(new ItemVenta("PROD-001", "Arroz", 2, 2500));
            datos.setMetodoPago("EFECTIVO");

            // Act
            String factura = generador.generarFormatoFactura(datos);

            // Assert
            assertAll("Verificar contenido de factura",
                () -> assertNotNull(factura),
                () -> assertTrue(factura.contains("FACTURA POS")),
                () -> assertTrue(factura.contains("Juan Pérez")),
                () -> assertTrue(factura.contains("Arroz")),
                () -> assertTrue(factura.contains("TOTAL"))
            );
        }

        @Test
        @DisplayName("No requiere factura electrónica")
        void testNoRequiereFacturaElectronica() {
            // Act & Assert
            assertFalse(generador.requiereFacturaElectronica());
        }
    }

    @Nested
    @DisplayName("POSValidadorStock - Validación de inventario")
    class POSValidadorStockTests {

        private ValidadorStock validador;

        @BeforeEach
        void setUp() {
            validador = new POSValidadorStock();
        }

        @Test
        @DisplayName("Valida disponibilidad correctamente")
        void testValidarDisponibilidad() {
            // Act & Assert
            assertTrue(validador.validarDisponibilidad("PROD-001", 10),
                "Debe haber stock disponible");
        }

        @Test
        @DisplayName("Rechaza cantidad mayor al stock")
        void testRechazaCantidadExcesiva() {
            // Act & Assert
            assertFalse(validador.validarDisponibilidad("PROD-001", 1000),
                "No debe haber tanto stock");
        }

        @Test
        @DisplayName("Retorna stock disponible")
        void testObtenerStockDisponible() {
            // Act
            int stock = validador.obtenerStockDisponible("PROD-001");

            // Assert
            assertTrue(stock > 0, "Debe haber stock para producto demo");
        }

        @Test
        @DisplayName("Reserva stock correctamente")
        void testReservarStock() {
            // Arrange
            int stockInicial = validador.obtenerStockDisponible("PROD-001");

            // Act
            boolean reservado = validador.reservarStock("PROD-001", 5);
            int stockFinal = validador.obtenerStockDisponible("PROD-001");

            // Assert
            assertAll("Verificar reserva de stock",
                () -> assertTrue(reservado),
                () -> assertEquals(stockInicial - 5, stockFinal)
            );
        }

        @Test
        @DisplayName("No reserva si no hay stock suficiente")
        void testNoReservaSinStock() {
            // Act
            boolean reservado = validador.reservarStock("PROD-001", 10000);

            // Assert
            assertFalse(reservado, "No debe reservar más de lo disponible");
        }

        @Test
        @DisplayName("Retorna 0 para producto inexistente")
        void testProductoInexistente() {
            // Act
            int stock = validador.obtenerStockDisponible("PROD-NOEXISTE");

            // Assert
            assertEquals(0, stock);
        }

        @Test
        @DisplayName("Múltiples reservas actualizan stock correctamente")
        void testMultiplesReservas() {
            // Arrange
            int stockInicial = validador.obtenerStockDisponible("PROD-002");

            // Act
            validador.reservarStock("PROD-002", 5);
            validador.reservarStock("PROD-002", 3);
            validador.reservarStock("PROD-002", 2);
            int stockFinal = validador.obtenerStockDisponible("PROD-002");

            // Assert
            assertEquals(stockInicial - 10, stockFinal);
        }
    }

    @Nested
    @DisplayName("POSCalculadorEnvio - Cálculo de envío")
    class POSCalculadorEnvioTests {

        private CalculadorEnvio calculador;

        @BeforeEach
        void setUp() {
            calculador = new POSCalculadorEnvio();
        }

        @Test
        @DisplayName("Costo de envío es siempre cero")
        void testCostoEnvioCero() {
            // Act & Assert
            assertAll("Verificar que no hay costo de envío",
                () -> assertEquals(0.0, calculador.calcularCostoEnvio("Bogotá", 5.0)),
                () -> assertEquals(0.0, calculador.calcularCostoEnvio("Cali", 10.0)),
                () -> assertEquals(0.0, calculador.calcularCostoEnvio("", 0.0))
            );
        }

        @Test
        @DisplayName("Tiempo de entrega es inmediato")
        void testTiempoEntregaInmediato() {
            // Act & Assert
            assertEquals(0, calculador.calcularTiempoEntrega("Cualquier destino"));
        }

        @Test
        @DisplayName("No soporta envío express")
        void testNoSoportaExpress() {
            // Act & Assert
            assertFalse(calculador.soportaEnvioExpress());
        }
    }

    @Nested
    @DisplayName("POSGestorDevolucion - Gestión de devoluciones")
    class POSGestorDevolucionTests {

        private GestorDevolucion gestor;

        @BeforeEach
        void setUp() {
            gestor = new POSGestorDevolucion();
        }

        @Test
        @DisplayName("Permite devolución dentro de 15 días")
        void testPermiteDevolucionDentroDelPlazo() {
            // Act & Assert
            assertAll("Verificar devoluciones permitidas",
                () -> assertTrue(gestor.permitirDevolucion("POS-001", 1)),
                () -> assertTrue(gestor.permitirDevolucion("POS-002", 7)),
                () -> assertTrue(gestor.permitirDevolucion("POS-003", 15))
            );
        }

        @Test
        @DisplayName("No permite devolución después de 15 días")
        void testNoPermiteDevolucionFueraDePlazo() {
            // Act & Assert
            assertAll("Verificar devoluciones rechazadas",
                () -> assertFalse(gestor.permitirDevolucion("POS-001", 16)),
                () -> assertFalse(gestor.permitirDevolucion("POS-002", 30)),
                () -> assertFalse(gestor.permitirDevolucion("POS-003", 100))
            );
        }

        @Test
        @DisplayName("Reembolso completo primeros 7 días")
        void testReembolsoCompletoPrimerosDias() {
            // Arrange
            double monto = 100000.0;

            // Act & Assert
            assertAll("Verificar reembolso completo",
                () -> assertEquals(monto, gestor.calcularReembolso(monto, 1)),
                () -> assertEquals(monto, gestor.calcularReembolso(monto, 5)),
                () -> assertEquals(monto, gestor.calcularReembolso(monto, 7))
            );
        }

        @Test
        @DisplayName("Reembolso con penalización después de 7 días")
        void testReembolsoConPenalizacion() {
            // Arrange
            double monto = 100000.0;

            // Act
            double reembolsoDia8 = gestor.calcularReembolso(monto, 8);
            double reembolsoDia10 = gestor.calcularReembolso(monto, 10);
            double reembolsoDia15 = gestor.calcularReembolso(monto, 15);

            // Assert
            assertAll("Verificar penalización",
                () -> assertTrue(reembolsoDia8 < monto, "Debe tener penalización día 8"),
                () -> assertTrue(reembolsoDia10 < reembolsoDia8, "Penalización aumenta con días"),
                () -> assertTrue(reembolsoDia15 < reembolsoDia10, "Penalización sigue aumentando"),
                () -> assertTrue(reembolsoDia15 > 0, "Siempre hay algo de reembolso")
            );
        }

        @Test
        @DisplayName("No hay reembolso después de 15 días")
        void testNoReembolsoFueraDePlazo() {
            // Arrange
            double monto = 100000.0;

            // Act & Assert
            assertEquals(0.0, gestor.calcularReembolso(monto, 16));
        }

        @Test
        @DisplayName("Política contiene información correcta")
        void testPoliticaDevolucion() {
            // Act
            String politica = gestor.obtenerPoliticaDevolucion();

            // Assert
            assertAll("Verificar contenido de política",
                () -> assertTrue(politica.contains("15 días")),
                () -> assertTrue(politica.contains("7 días")),
                () -> assertTrue(politica.contains("2%"))
            );
        }
    }

    @Nested
    @DisplayName("DatosVenta - Gestión de datos")
    class DatosVentaTests {

        @Test
        @DisplayName("DatosVenta se crea correctamente")
        void testCrearDatosVenta() {
            // Act
            DatosVenta datos = new DatosVenta("POS-001", "C001", "Juan Pérez");

            // Assert
            assertAll("Verificar creación de DatosVenta",
                () -> assertEquals("POS-001", datos.getNumeroVenta()),
                () -> assertEquals("C001", datos.getClienteId()),
                () -> assertEquals("Juan Pérez", datos.getClienteNombre()),
                () -> assertTrue(datos.getItems().isEmpty()),
                () -> assertEquals(0.0, datos.getSubtotal()),
                () -> assertEquals(0.0, datos.getTotal())
            );
        }

        @Test
        @DisplayName("Agregar item actualiza totales")
        void testAgregarItemActualizaTotales() {
            // Arrange
            DatosVenta datos = new DatosVenta("POS-001", "C001", "Test");

            // Act
            datos.agregarItem(new ItemVenta("PROD-001", "Arroz", 2, 2500));

            // Assert
            assertAll("Verificar totales",
                () -> assertEquals(1, datos.getItems().size()),
                () -> assertEquals(5000.0, datos.getSubtotal(), 0.01),
                () -> assertEquals(950.0, datos.getImpuestos(), 0.01),
                () -> assertEquals(5950.0, datos.getTotal(), 0.01)
            );
        }

        @Test
        @DisplayName("Múltiples items calculan total correcto")
        void testMultiplesItems() {
            // Arrange
            DatosVenta datos = new DatosVenta("POS-001", "C001", "Test");

            // Act
            datos.agregarItem(new ItemVenta("PROD-001", "Arroz", 2, 2500));
            datos.agregarItem(new ItemVenta("PROD-002", "Aceite", 1, 8500));
            datos.agregarItem(new ItemVenta("PROD-003", "Sal", 3, 1500));

            // Assert
            assertAll("Verificar cálculos con múltiples items",
                () -> assertEquals(3, datos.getItems().size()),
                () -> assertEquals(18000.0, datos.getSubtotal(), 0.01),
                () -> assertEquals(3420.0, datos.getImpuestos(), 0.01),
                () -> assertEquals(21420.0, datos.getTotal(), 0.01)
            );
        }

        @Test
        @DisplayName("Puede establecer método de pago")
        void testEstablecerMetodoPago() {
            // Arrange
            DatosVenta datos = new DatosVenta("POS-001", "C001", "Test");

            // Act
            datos.setMetodoPago("TARJETA");

            // Assert
            assertEquals("TARJETA", datos.getMetodoPago());
        }

        @Test
        @DisplayName("Puede establecer dirección de envío")
        void testEstablecerDireccionEnvio() {
            // Arrange
            DatosVenta datos = new DatosVenta("POS-001", "C001", "Test");

            // Act
            datos.setDireccionEnvio("Calle 123 #45-67");

            // Assert
            assertEquals("Calle 123 #45-67", datos.getDireccionEnvio());
        }

        @Test
        @DisplayName("Lista de items es inmutable")
        void testListaItemsInmutable() {
            // Arrange
            DatosVenta datos = new DatosVenta("POS-001", "C001", "Test");
            datos.agregarItem(new ItemVenta("PROD-001", "Test", 1, 1000));

            // Act & Assert
            assertThrows(UnsupportedOperationException.class,
                () -> datos.getItems().add(new ItemVenta("PROD-002", "Test2", 1, 2000)),
                "No debe poder modificar la lista directamente");
        }
    }

    @Nested
    @DisplayName("ItemVenta - Cálculo de items")
    class ItemVentaTests {

        @Test
        @DisplayName("ItemVenta calcula subtotal correctamente")
        void testCalcularSubtotal() {
            // Arrange
            ItemVenta item = new ItemVenta("PROD-001", "Arroz", 3, 2500);

            // Act
            double subtotal = item.getSubtotal();

            // Assert
            assertEquals(7500.0, subtotal, 0.01);
        }

        @Test
        @DisplayName("ItemVenta con cantidad 1")
        void testItemCantidadUno() {
            // Arrange
            ItemVenta item = new ItemVenta("PROD-001", "Aceite", 1, 8500);

            // Act & Assert
            assertEquals(8500.0, item.getSubtotal(), 0.01);
        }

        @Test
        @DisplayName("Getters retornan valores correctos")
        void testGetters() {
            // Arrange
            ItemVenta item = new ItemVenta("PROD-001", "Sal", 2, 1500);

            // Act & Assert
            assertAll("Verificar getters",
                () -> assertEquals("PROD-001", item.getCodigoProducto()),
                () -> assertEquals("Sal", item.getNombreProducto()),
                () -> assertEquals(2, item.getCantidad()),
                () -> assertEquals(1500.0, item.getPrecioUnitario(), 0.01)
            );
        }
    }

    @Nested
    @DisplayName("Integración de componentes POS")
    class IntegracionComponentesPOSTests {

        private VentaComponentFactory factory;

        @BeforeEach
        void setUp() {
            factory = new POSVentaFactory();
        }

        @Test
        @DisplayName("Flujo completo de venta POS")
        void testFlujoCompletoVentaPOS() {
            // 1. Crear venta
            DatosVenta venta = new DatosVenta("POS-001", "C001", "Cliente Test");
            venta.agregarItem(new ItemVenta("PROD-001", "Producto 1", 2, 5000));
            venta.agregarItem(new ItemVenta("PROD-002", "Producto 2", 1, 10000));
            venta.setMetodoPago("EFECTIVO");

            // 2. Validar stock
            ValidadorStock validador = factory.crearValidadorStock();
            boolean stockValido = validador.validarDisponibilidad("PROD-001", 2) &&
                                 validador.validarDisponibilidad("PROD-002", 1);

            // 3. Reservar stock
            boolean reservado = validador.reservarStock("PROD-001", 2) &&
                               validador.reservarStock("PROD-002", 1);

            // 4. Generar factura
            GeneradorFactura generador = factory.crearGeneradorFactura();
            String factura = generador.generarFormatoFactura(venta);

            // 5. Verificar envío (no aplica)
            CalculadorEnvio calculador = factory.crearCalculadorEnvio();
            double costoEnvio = calculador.calcularCostoEnvio("", 0);

            // Assert
            assertAll("Verificar flujo completo",
                () -> assertTrue(stockValido, "Stock debe estar disponible"),
                () -> assertTrue(reservado, "Stock debe reservarse"),
                () -> assertNotNull(factura, "Factura debe generarse"),
                () -> assertEquals(0.0, costoEnvio, "No debe haber costo de envío"),
                () -> assertEquals(20000.0, venta.getSubtotal(), 0.01)
            );
        }

        @Test
        @DisplayName("Validación de devolución integrada")
        void testValidacionDevolucionIntegrada() {
            // Arrange
            DatosVenta venta = new DatosVenta("POS-001", "C001", "Cliente Test");
            venta.agregarItem(new ItemVenta("PROD-001", "Producto", 1, 50000));

            GestorDevolucion gestor = factory.crearGestorDevolucion();

            // Act & Assert - Diferentes escenarios
            assertAll("Verificar escenarios de devolución",
                () -> {
                    boolean permitida = gestor.permitirDevolucion("POS-001", 5);
                    double reembolso = gestor.calcularReembolso(venta.getTotal(), 5);
                    assertTrue(permitida && reembolso == venta.getTotal(),
                        "Devolución temprana debe ser completa");
                },
                () -> {
                    boolean permitida = gestor.permitirDevolucion("POS-001", 10);
                    double reembolso = gestor.calcularReembolso(venta.getTotal(), 10);
                    assertTrue(permitida && reembolso < venta.getTotal(),
                        "Devolución tardía debe tener penalización");
                },
                () -> {
                    boolean permitida = gestor.permitirDevolucion("POS-001", 20);
                    assertFalse(permitida, "No debe permitir después de 15 días");
                }
            );
        }

        @Test
        @DisplayName("Todos los componentes son del mismo tipo de canal")
        void testConsistenciaTipoCanal() {
            // Act
            GeneradorFactura generador = factory.crearGeneradorFactura();
            ValidadorStock validador = factory.crearValidadorStock();
            CalculadorEnvio calculador = factory.crearCalculadorEnvio();
            GestorDevolucion gestor = factory.crearGestorDevolucion();

            // Assert
            assertAll("Verificar consistencia de tipos",
                () -> assertTrue(generador.obtenerTipoGenerador().contains("POS")),
                () -> assertTrue(validador.obtenerTipoValidador().contains("POS")),
                () -> assertTrue(calculador.obtenerTipoCalculador().contains("POS")),
                () -> assertTrue(gestor.obtenerTipoGestor().contains("POS"))
            );
        }
    }
}
