package unit.Builder;

import Creacionales.Builder.modelo.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Builder Pattern - Tests Unitarios")
@ExtendWith(MockitoExtension.class)
class BuilderUnitTest {

        @Nested
        @DisplayName("Comportamiento del Builder")
        class BuilderBehaviorTests {

                @Test
                @DisplayName("Builder crea factura básica correctamente")
                void testBuilderCreaFacturaBasica() {
                        Cliente cliente = new Cliente("C001", "Juan Pérez", "12345678");
                        ItemFactura item = new ItemFactura("P001", "Arroz", 2, 2500.0);

                        Factura factura = new Factura.Builder("F-001")
                                        .paraCliente(cliente)
                                        .agregarItem(item)
                                        .conMetodoPago("EFECTIVO")
                                        .build();

                        assertAll("Verificar factura básica",
                                        () -> assertNotNull(factura, "La factura no debe ser nula"),
                                        () -> assertEquals("F-001", factura.getNumeroFactura()),
                                        () -> assertEquals(cliente, factura.getCliente()),
                                        () -> assertEquals(1, factura.getItems().size()),
                                        () -> assertEquals("EFECTIVO", factura.getMetodoPago()),
                                        () -> assertEquals(5000.0, factura.getSubtotal(), 0.01));
                }

                @Test
                @DisplayName("Builder construye factura completa con todos los parámetros")
                void testBuilderFacturaCompleta() {
                        Cliente cliente = new Cliente("C001", "María García", "87654321");
                        cliente.setEmail("maria@email.com");
                        ItemFactura item1 = new ItemFactura("P001", "Leche", 3, 3500.0);
                        ItemFactura item2 = new ItemFactura("P002", "Pan", 5, 2000.0);

                        Factura factura = new Factura.Builder("F-002")
                                        .paraCliente(cliente)
                                        .agregarItem(item1)
                                        .agregarItem(item2)
                                        .conImpuestos(4180.0)
                                        .conDescuentos(1000.0)
                                        .conPropina(5000.0)
                                        .conMetodoPago("TARJETA")
                                        .conObservaciones("Cliente frecuente")
                                        .build();

                        assertAll("Verificar factura completa",
                                        () -> assertEquals(20500.0, factura.getSubtotal(), 0.01),
                                        () -> assertEquals(4180.0, factura.getImpuestos(), 0.01),
                                        () -> assertEquals(1000.0, factura.getDescuentos(), 0.01),
                                        () -> assertEquals(5000.0, factura.getPropina(), 0.01),
                                        () -> assertEquals("Cliente frecuente", factura.getObservaciones()),
                                        () -> assertEquals(28680.0, factura.calcularTotal(), 0.01));
                }

                @Test
                @DisplayName("Builder calcula impuestos automáticamente")
                void testCalculoAutomaticoImpuestos() {
                        Cliente cliente = new Cliente("C001", "Pedro López", "11223344");
                        ItemFactura item = new ItemFactura("P001", "Producto", 1, 10000.0);

                        Factura factura = new Factura.Builder("F-003")
                                        .paraCliente(cliente)
                                        .agregarItem(item)
                                        .conMetodoPago("EFECTIVO")
                                        .calcularImpuestosAutomaticamente()
                                        .build();

                        assertEquals(1900.0, factura.getImpuestos(), 0.01,
                                        "IVA 19% sobre 10000 debe ser 1900");
                        assertEquals(11900.0, factura.calcularTotal(), 0.01);
                }

                @Test
                @DisplayName("Builder configura envío correctamente")
                void testBuilderConEnvio() {
                        Cliente cliente = new Cliente("C001", "Ana Martínez", "55667788");
                        ItemFactura item = new ItemFactura("E001", "Licuadora", 1, 150000.0);

                        Factura factura = new Factura.Builder("F-004")
                                        .paraCliente(cliente)
                                        .agregarItem(item)
                                        .conMetodoPago("TRANSFERENCIA")
                                        .conDireccionEnvio("Calle 123 #45-67")
                                        .build();

                        // Assert
                        assertAll("Verificar configuración de envío",
                                        () -> assertTrue(factura.isRequiereEnvio()),
                                        () -> assertEquals("Calle 123 #45-67", factura.getDireccionEnvio()));
                }

                @Test
                @DisplayName("Builder permite método fluido encadenado")
                void testMetodoFluido() {
                        // Arrange & Act
                        Factura factura = new Factura.Builder("F-005")
                                        .paraCliente(new Cliente("C001", "Test", "123"))
                                        .agregarItem(new ItemFactura("P001", "Item1", 1, 1000))
                                        .agregarItem(new ItemFactura("P002", "Item2", 2, 2000))
                                        .conMetodoPago("EFECTIVO")
                                        .conImpuestos(950.0)
                                        .conDescuentos(500.0)
                                        .conPropina(1000.0)
                                        .conObservaciones("Test")
                                        .build();

                        // Assert
                        assertNotNull(factura);
                        assertEquals(2, factura.getItems().size()); // CAMBIAR DE 3 A 2
                }

                @Test
                @DisplayName("Factura es inmutable después de construcción")
                void testInmutabilidadFactura() {
                        // Arrange
                        Factura factura = new Factura.Builder("F-006")
                                        .paraCliente(new Cliente("C001", "Test", "123"))
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                                        .conMetodoPago("EFECTIVO")
                                        .build();

                        // Act & Assert
                        List<ItemFactura> items = factura.getItems();
                        assertThrows(UnsupportedOperationException.class,
                                        () -> items.add(new ItemFactura("P002", "Nuevo", 1, 500)),
                                        "La lista de items debe ser inmutable");
                }
        }

        @Nested
        @DisplayName("Validaciones del Builder")
        class ValidationTests {

                @Test
                @DisplayName("Builder rechaza número de factura nulo")
                void testNumeroFacturaNulo() {
                        assertThrows(IllegalArgumentException.class,
                                        () -> new Factura.Builder(null),
                                        "Debe rechazar número de factura nulo");
                }

                @Test
                @DisplayName("Builder rechaza número de factura vacío")
                void testNumeroFacturaVacio() {
                        assertThrows(IllegalArgumentException.class,
                                        () -> new Factura.Builder(""),
                                        "Debe rechazar número de factura vacío");
                }

                @Test
                @DisplayName("Build falla sin items")
                void testBuildSinItems() {
                        // Arrange
                        Factura.Builder builder = new Factura.Builder("F-001")
                                        .paraCliente(new Cliente("C001", "Test", "123"))
                                        .conMetodoPago("EFECTIVO");

                        // Act & Assert
                        assertThrows(IllegalStateException.class,
                                        builder::build,
                                        "Debe requerir al menos un item");
                }

                @Test
                @DisplayName("Build falla sin método de pago")
                void testBuildSinMetodoPago() {
                        // Arrange
                        Factura.Builder builder = new Factura.Builder("F-001")
                                        .paraCliente(new Cliente("C001", "Test", "123"))
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000));

                        // Act & Assert
                        assertThrows(IllegalStateException.class,
                                        builder::build,
                                        "Debe requerir método de pago");
                }

                @Test
                @DisplayName("Builder rechaza impuestos negativos")
                void testImpuestosNegativos() {
                        // Arrange
                        Factura.Builder builder = new Factura.Builder("F-001");

                        // Act & Assert
                        assertThrows(IllegalArgumentException.class,
                                        () -> builder.conImpuestos(-100),
                                        "Debe rechazar impuestos negativos");
                }

                @Test
                @DisplayName("Builder rechaza descuentos negativos")
                void testDescuentosNegativos() {
                        // Arrange
                        Factura.Builder builder = new Factura.Builder("F-001");

                        // Act & Assert
                        assertThrows(IllegalArgumentException.class,
                                        () -> builder.conDescuentos(-100),
                                        "Debe rechazar descuentos negativos");
                }

                @Test
                @DisplayName("Builder rechaza propina negativa")
                void testPropinaNegativa() {
                        // Arrange
                        Factura.Builder builder = new Factura.Builder("F-001");

                        // Act & Assert
                        assertThrows(IllegalArgumentException.class,
                                        () -> builder.conPropina(-100),
                                        "Debe rechazar propina negativa");
                }

                @Test
                @DisplayName("Builder rechaza item nulo")
                void testItemNulo() {
                        // Arrange
                        Factura.Builder builder = new Factura.Builder("F-001");

                        // Act & Assert
                        assertThrows(IllegalArgumentException.class,
                                        () -> builder.agregarItem(null),
                                        "Debe rechazar item nulo");
                }

                @Test
                @DisplayName("Builder requiere dirección cuando requiere envío")
                void testEnvioSinDireccion() {
                        // Arrange
                        Factura.Builder builder = new Factura.Builder("F-001")
                                        .paraCliente(new Cliente("C001", "Test", "123"))
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                                        .conMetodoPago("EFECTIVO")
                                        .requiereEnvio(true);

                        // Act & Assert
                        assertThrows(IllegalStateException.class,
                                        builder::build,
                                        "Debe requerir dirección cuando requiere envío");
                }
        }

        @Nested
        @DisplayName("Cálculos de ItemFactura")
        class ItemFacturaTests {

                @Test
                @DisplayName("ItemFactura calcula total correctamente")
                void testItemCalculaTotal() {
                        // Arrange
                        ItemFactura item = new ItemFactura("P001", "Arroz", 3, 2500.0);

                        // Act
                        double total = item.calcularTotal();

                        // Assert
                        assertEquals(7500.0, total, 0.01);
                }

                @Test
                @DisplayName("ItemFactura aplica descuento correctamente")
                void testItemConDescuento() {
                        // Arrange
                        ItemFactura item = new ItemFactura("P001", "Azúcar", 2, 3000.0);
                        item.setDescuentoItem(1000.0);

                        // Act
                        double total = item.calcularTotal();

                        // Assert
                        assertEquals(5000.0, total, 0.01, "6000 - 1000 = 5000");
                }

                @Test
                @DisplayName("ItemFactura rechaza cantidad no positiva")
                void testItemCantidadInvalida() {
                        assertThrows(IllegalArgumentException.class,
                                        () -> new ItemFactura("P001", "Test", 0, 1000),
                                        "Debe rechazar cantidad cero o negativa");
                }

                @Test
                @DisplayName("ItemFactura rechaza precio negativo")
                void testItemPrecioNegativo() {
                        assertThrows(IllegalArgumentException.class,
                                        () -> new ItemFactura("P001", "Test", 1, -1000),
                                        "Debe rechazar precio negativo");
                }

                @Test
                @DisplayName("ItemFactura rechaza descuento negativo")
                void testItemDescuentoNegativo() {
                        // Arrange
                        ItemFactura item = new ItemFactura("P001", "Test", 1, 1000);

                        // Act & Assert
                        assertThrows(IllegalArgumentException.class,
                                        () -> item.setDescuentoItem(-100),
                                        "Debe rechazar descuento negativo");
                }
        }

        @Nested
        @DisplayName("Operaciones de Factura")
        class FacturaOperationsTests {

                @Test
                @DisplayName("Factura calcula total correctamente")
                void testFacturaCalculaTotal() {
                        // Arrange
                        Factura factura = new Factura.Builder("F-001")
                                        .paraCliente(new Cliente("C001", "Test", "123"))
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 10000))
                                        .conMetodoPago("EFECTIVO")
                                        .conImpuestos(1900)
                                        .conDescuentos(500)
                                        .conPropina(1000)
                                        .build();

                        // Act
                        double total = factura.calcularTotal();

                        // Assert
                        assertEquals(12400.0, total, 0.01, "10000 + 1900 - 500 + 1000 = 12400");
                }

                @Test
                @DisplayName("Factura puede generar PDF")
                void testFacturaGeneraPDF() {
                        // Arrange
                        Factura factura = new Factura.Builder("F-001")
                                        .paraCliente(new Cliente("C001", "Test", "123"))
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                                        .conMetodoPago("EFECTIVO")
                                        .build();

                        // Act
                        String resultado = factura.generarPDF();

                        // Assert
                        assertNotNull(resultado);
                        assertTrue(resultado.contains("F-001"));
                }

                @Test
                @DisplayName("Factura puede enviar por correo si cliente tiene email")
                void testFacturaEnviarCorreo() {
                        // Arrange
                        Cliente cliente = new Cliente("C001", "Test", "123");
                        cliente.setEmail("test@email.com");

                        Factura factura = new Factura.Builder("F-001")
                                        .paraCliente(cliente)
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                                        .conMetodoPago("EFECTIVO")
                                        .build();

                        // Act
                        boolean resultado = factura.enviarPorCorreo();

                        // Assert
                        assertTrue(resultado);
                }

                @Test
                @DisplayName("Factura no puede enviar por correo sin email")
                void testFacturaNoEnviaSinEmail() {
                        // Arrange
                        Cliente cliente = new Cliente("C001", "Test", "123");

                        Factura factura = new Factura.Builder("F-001")
                                        .paraCliente(cliente)
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                                        .conMetodoPago("EFECTIVO")
                                        .build();

                        // Act
                        boolean resultado = factura.enviarPorCorreo();

                        // Assert
                        assertFalse(resultado);
                }

                @Test
                @DisplayName("Factura tiene fecha de emisión por defecto")
                void testFacturaFechaDefault() {
                        // Arrange & Act
                        Factura factura = new Factura.Builder("F-001")
                                        .paraCliente(new Cliente("C001", "Test", "123"))
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                                        .conMetodoPago("EFECTIVO")
                                        .build();

                        // Assert
                        assertEquals(LocalDate.now(), factura.getFechaEmision());
                }

                @Test
                @DisplayName("Factura puede tener fecha personalizada")
                void testFacturaFechaPersonalizada() {
                        // Arrange
                        LocalDate fechaCustom = LocalDate.of(2025, 1, 15);

                        // Act
                        Factura factura = new Factura.Builder("F-001")
                                        .conFechaEmision(fechaCustom)
                                        .paraCliente(new Cliente("C001", "Test", "123"))
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                                        .conMetodoPago("EFECTIVO")
                                        .build();

                        // Assert
                        assertEquals(fechaCustom, factura.getFechaEmision());
                }
        }

        @Nested
        @DisplayName("Tests de toString y equals")
        class ToStringEqualsTests {

                @Test
                @DisplayName("toString contiene información relevante")
                void testToString() {
                        // Arrange
                        Cliente cliente = new Cliente("C001", "Juan Pérez", "123");
                        Factura factura = new Factura.Builder("F-001")
                                        .paraCliente(cliente)
                                        .agregarItem(new ItemFactura("P001", "Test", 1, 1000))
                                        .conMetodoPago("EFECTIVO")
                                        .build();

                        // Act
                        String toString = factura.toString();

                        // Assert
                        assertAll("Verificar contenido de toString",
                                        () -> assertTrue(toString.contains("F-001")),
                                        () -> assertTrue(toString.contains("Juan Pérez")),
                                        () -> assertTrue(toString.contains("1000")));
                }

                @Test
                @DisplayName("Cliente puede actualizar email y teléfono")
                void testClienteActualizacion() {
                        // Arrange
                        Cliente cliente = new Cliente("C001", "Test", "123");

                        // Act
                        cliente.setEmail("nuevo@email.com");
                        cliente.setTelefono("555-1234");
                        cliente.setDireccion("Calle Nueva");

                        // Assert
                        assertAll("Verificar actualización de cliente",
                                        () -> assertEquals("nuevo@email.com", cliente.getEmail()),
                                        () -> assertEquals("555-1234", cliente.getTelefono()),
                                        () -> assertEquals("Calle Nueva", cliente.getDireccion()));
                }
        }
}