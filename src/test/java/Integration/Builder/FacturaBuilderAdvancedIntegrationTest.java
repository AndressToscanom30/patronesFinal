package Integration.Builder;

import Creacionales.Builder.controlador.FacturaControlador;
import Creacionales.Builder.modelo.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Builder Pattern - Advanced Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FacturaBuilderAdvancedIntegrationTest {
    private FacturaControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new FacturaControlador();
    }

    @Test
    @Order(1)
    @DisplayName("Validación de límites de facturación")
    void testValidacionLimitesFacturacion() {
        // Arrange
        Cliente cliente = new Cliente("C001", "Test Cliente", "123");
        Factura.Builder builder = controlador.iniciarFactura()
                .paraCliente(cliente);
        
        // Act & Assert - Agregar items hasta alcanzar límite
        for (int i = 1; i <= 50; i++) {
            builder.agregarItem(new ItemFactura("P" + i, "Producto " + i, 1, 1000));
        }
            Factura factura = builder.conMetodoPago("EFECTIVO").build();
        assertEquals(50, factura.getItems().size(), "La factura debe aceptar hasta 50 items");
    }

    @Test
    @Order(2)
    @DisplayName("Facturación con descuentos escalonados")
    void testDescuentosEscalonados() {
        // Arrange
        Cliente cliente = new Cliente("C002", "Cliente Mayorista", "456");
        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Producto Bulk", 100, 1000))
                .conDescuentos(30000) // Descuento fijo
                .calcularImpuestosAutomaticamente()
                   .conMetodoPago("EFECTIVO")
                .build();

        // Assert
        assertTrue(factura.getDescuentos() > 0, "Debe aplicar descuentos");
        assertTrue(factura.calcularTotal() < (100 * 1000), "El total debe ser menor que el subtotal");
    }

    @Test
    @Order(3)
    @DisplayName("Facturación con múltiples métodos de pago")
    void testMultiplesMetodosPago() {
        // Arrange
        Cliente cliente = new Cliente("C003", "Cliente Mix", "789");
        
        // Act
        Factura factura1 = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Item 1", 1, 1000))
                .conMetodoPago("EFECTIVO")
                .build();

        Factura factura2 = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Item 1", 1, 1000))
                .conMetodoPago("TARJETA")
                .build();

        // Assert
        assertNotEquals(factura1.getMetodoPago(), factura2.getMetodoPago());
    }

    @Test
    @Order(4)
    @DisplayName("Actualización de facturas existentes")
    void testActualizacionFacturas() {
        // Arrange
        Cliente cliente = new Cliente("C004", "Cliente Update", "101");
        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Item 1", 1, 1000))
                .conMetodoPago("EFECTIVO")
                .build();

        controlador.guardarFactura(factura);

        // Act
        factura = controlador.buscarFactura(factura.getNumeroFactura());
        assertNotNull(factura, "La factura debe existir");

        factura = new Factura.Builder(factura.getNumeroFactura())
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Item 1", 2, 1000)) // Cantidad actualizada
                .conMetodoPago("EFECTIVO")
                .build();

        controlador.guardarFactura(factura);

        // Assert
        Factura actualizada = controlador.buscarFactura(factura.getNumeroFactura());
        assertEquals(2, actualizada.getItems().get(0).getCantidad());
    }

    @Test
    @Order(5)
    @DisplayName("Validación de datos fiscales")
    void testValidacionDatosFiscales() {
        // Arrange
        Cliente cliente = new Cliente("C005", "Empresa SA", "RUT123");
        cliente.setEmail("empresa@test.com");
        
        // Act
        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Servicio 1", 1, 10000))
                .conMetodoPago("TRANSFERENCIA")
                .conObservaciones("Factura electrónica")
                .calcularImpuestosAutomaticamente()
                .build();

        controlador.guardarFactura(factura);

        // Assert
        assertAll("Verificar datos fiscales",
            () -> assertNotNull(factura.getNumeroFactura()),
            () -> assertNotNull(factura.getFechaEmision()),
            () -> assertTrue(factura.getImpuestos() > 0),
            () -> assertEquals("RUT123", factura.getCliente().getNumeroDocumento())
        );
    }
}