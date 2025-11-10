package e2e.Builder;

import Creacionales.Builder.controlador.FacturaControlador;
import Creacionales.Builder.modelo.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Builder Pattern - Tests End-to-End")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FacturaBuilderE2ETest {

    private FacturaControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new FacturaControlador();
    }

    @Test
    @Order(1)
    @DisplayName("E2E: Compra simple en caja - Cliente de contado")
    void testCompraSimpleEnCaja() {
        // Simulate: Cliente llega a caja con productos básicos

        // Cliente
        Cliente cliente = new Cliente("C001", "José Ramírez", "10123456");

        // Productos escaneados
        ItemFactura arroz = new ItemFactura("GRA001", "Arroz Diana 500g", 2, 2500);
        ItemFactura aceite = new ItemFactura("ACE001", "Aceite Gourmet 1L", 1, 8500);
        ItemFactura sal = new ItemFactura("GRA002", "Sal Refisal 500g", 1, 1500);

        // Cajero procesa venta
        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(arroz)
                .agregarItem(aceite)
                .agregarItem(sal)
                .conMetodoPago("EFECTIVO")
                .calcularImpuestosAutomaticamente()
                .build();

        controlador.guardarFactura(factura);

        // Assertions
        assertAll("Verificar compra simple en caja",
                () -> assertEquals(15000.0, factura.getSubtotal(), 0.01),
                () -> assertEquals(2850.0, factura.getImpuestos(), 0.01),
                () -> assertEquals(17850.0, factura.calcularTotal(), 0.01),
                () -> assertEquals("EFECTIVO", factura.getMetodoPago())
        );
    }

    @Test
    @Order(2)
    @DisplayName("E2E: Compra grande con descuento - Cliente frecuente")
    void testCompraGrandeConDescuento() {
        // Simulate: Cliente frecuente hace mercado mensual

        Cliente clienteVIP = new Cliente("C002", "María Elena Torres", "20987654");
        clienteVIP.setEmail("maria.torres@email.com");
        clienteVIP.setTelefono("3001234567");

        // Carrito de compras
        Factura factura = controlador.iniciarFactura()
                .paraCliente(clienteVIP)
                .agregarItem(new ItemFactura("LAC001", "Leche Alpina 1L", 6, 3500))
                .agregarItem(new ItemFactura("PAN001", "Pan Tajado", 3, 4500))
                .agregarItem(new ItemFactura("CAR001", "Pollo Entero", 2, 15000))
                .agregarItem(new ItemFactura("FRU001", "Manzanas kg", 3, 4500))
                .agregarItem(new ItemFactura("VER001", "Tomate kg", 2, 3500))
                .agregarItem(new ItemFactura("GRA003", "Azúcar 1kg", 2, 3000))
                .conMetodoPago("TARJETA")
                .calcularImpuestosAutomaticamente()
                .conDescuentos(8000) // Descuento cliente frecuente
                .conObservaciones("Cliente VIP - 10% descuento aplicado")
                .build();

        controlador.guardarFactura(factura);

        // Assertions
        assertAll("Verificar compra grande con descuento",
                () -> assertEquals(91000.0, factura.getSubtotal(), 0.01),
                () -> assertEquals(17290.0, factura.getImpuestos(), 0.01),
                () -> assertEquals(8000.0, factura.getDescuentos(), 0.01),
                () -> assertEquals(100290.0, factura.calcularTotal(), 0.01),
                () -> assertTrue(factura.enviarPorCorreo())
        );
    }

    @Test
    @Order(3)
    @DisplayName("E2E: Compra en restaurante con propina")
    void testCompraRestauranteConPropina() {
        // Simulate: Cliente consume en restaurante del supermercado

        Cliente cliente = new Cliente("C003", "Carlos Mendoza", "30456789");

        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("REST001", "Almuerzo Ejecutivo", 2, 18000))
                .agregarItem(new ItemFactura("BEB001", "Jugo Natural", 2, 5000))
                .agregarItem(new ItemFactura("POST001", "Postre", 1, 8000))
                .conMetodoPago("TARJETA")
                .calcularImpuestosAutomaticamente()
                .conPropina(6000) // 10% propina voluntaria
                .conObservaciones("Mesa 5 - Excelente servicio")
                .build();

        controlador.guardarFactura(factura);

        // Assertions
        assertAll("Verificar consumo en restaurante",
                () -> assertEquals(54000.0, factura.getSubtotal(), 0.01),
                () -> assertEquals(10260.0, factura.getImpuestos(), 0.01),
                () -> assertEquals(6000.0, factura.getPropina(), 0.01),
                () -> assertEquals(70260.0, factura.calcularTotal(), 0.01)
        );
    }

    @Test
    @Order(4)
    @DisplayName("E2E: Compra online con envío a domicilio")
    void testCompraOnlineConEnvio() {
        // Simulate: Cliente ordena por app con domicilio

        Cliente cliente = new Cliente("C004", "Ana Lucía Gómez", "40789123");
        cliente.setEmail("ana.gomez@email.com");
        cliente.setTelefono("3109876543");
        cliente.setDireccion("Carrera 15 #45-67, Apto 501");

        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("ELEC001", "Licuadora Oster", 1, 150000))
                .agregarItem(new ItemFactura("ELEC002", "Batidora KitchenAid", 1, 280000))
                .agregarItem(new ItemFactura("HOG001", "Set Ollas", 1, 120000))
                .conMetodoPago("TRANSFERENCIA")
                .calcularImpuestosAutomaticamente()
                .conDireccionEnvio(cliente.getDireccion())
                .conObservaciones("Envío programado - Mañana 2-5pm")
                .build();

        controlador.guardarFactura(factura);

        // Assertions
        assertAll("Verificar compra online con envío",
                () -> assertEquals(550000.0, factura.getSubtotal(), 0.01),
                () -> assertEquals(104500.0, factura.getImpuestos(), 0.01),
                () -> assertTrue(factura.isRequiereEnvio()),
                () -> assertNotNull(factura.getDireccionEnvio()),
                () -> assertEquals(654500.0, factura.calcularTotal(), 0.01)
        );
    }

    @Test
    @Order(5)
    @DisplayName("E2E: Día de mercado - Múltiples clientes")
    void testDiaDeMercadoMultiplesClientes() {
        // Simulate: Sábado de mercado con varios clientes

        // Cliente 1: Compra pequeña
        Cliente c1 = new Cliente("C101", "Pedro Ruiz", "111");
        controlador.guardarFactura(
                controlador.iniciarFactura()
                        .paraCliente(c1)
                        .agregarItem(new ItemFactura("P001", "Pan", 2, 2000))
                        .agregarItem(new ItemFactura("P002", "Leche", 1, 3500))
                        .conMetodoPago("EFECTIVO")
                        .build()
        );

        // Cliente 2: Compra mediana
        Cliente c2 = new Cliente("C102", "Laura Díaz", "222");
        controlador.guardarFactura(
                controlador.iniciarFactura()
                        .paraCliente(c2)
                        .agregarItem(new ItemFactura("P003", "Arroz", 3, 2500))
                        .agregarItem(new ItemFactura("P004", "Aceite", 2, 8000))
                        .agregarItem(new ItemFactura("P005", "Pollo", 1, 15000))
                        .conMetodoPago("TARJETA")
                        .calcularImpuestosAutomaticamente()
                        .build()
        );

        // Cliente 3: Compra grande
        Cliente c3 = new Cliente("C103", "Roberto Sánchez", "333");
        controlador.guardarFactura(
                controlador.iniciarFactura()
                        .paraCliente(c3)
                        .agregarItem(new ItemFactura("P006", "Carne", 5, 18000))
                        .agregarItem(new ItemFactura("P007", "Frutas", 10, 4500))
                        .agregarItem(new ItemFactura("P008", "Verduras", 8, 3000))
                        .conMetodoPago("EFECTIVO")
                        .calcularImpuestosAutomaticamente()
                        .conDescuentos(5000)
                        .build()
        );

        // Assertions
        List<Factura> todasFacturas = controlador.obtenerTodasLasFacturas();
        double totalVentas = controlador.calcularTotalVentas();

        assertAll("Verificar día de mercado",
                () -> assertEquals(3, todasFacturas.size()),
                () -> assertTrue(totalVentas > 100000, "Total ventas debe superar 100mil")
        );
    }

    @Test
    @Order(6)
    @DisplayName("E2E: Cliente corporativo - Factura con datos fiscales")
    void testClienteCorporativo() {
        // Simulate: Compra corporativa para empresa

        Cliente empresa = new Cliente("CORP001", "Empresa ABC S.A.S", "900123456-1");
        empresa.setTipoDocumento("NIT");
        empresa.setEmail("facturacion@empresaabc.com");
        empresa.setDireccion("Zona Industrial Calle 80");

        Factura factura = controlador.iniciarFactura()
                .paraCliente(empresa)
                .agregarItem(new ItemFactura("OF001", "Café Institucional 500g", 20, 12000))
                .agregarItem(new ItemFactura("OF002", "Azúcar Sobres x100", 10, 8000))
                .agregarItem(new ItemFactura("OF003", "Agua Botellón 20L", 15, 7000))
                .agregarItem(new ItemFactura("OF004", "Vasos Desechables x100", 30, 5000))
                .conMetodoPago("CREDITO_30_DIAS")
                .calcularImpuestosAutomaticamente()
                .conObservaciones("Orden de compra #OC-2025-001 - Crédito 30 días")
                .build();

        controlador.guardarFactura(factura);

        // Assertions
        assertAll("Verificar factura corporativa",
                () -> assertEquals(575000.0, factura.getSubtotal(), 0.01),
                () -> assertEquals("900123456-1", factura.getCliente().getNumeroDocumento()),
                () -> assertEquals("CREDITO_30_DIAS", factura.getMetodoPago()),
                () -> assertTrue(factura.getObservaciones().contains("OC-2025"))
        );
    }

    @Test
    @Order(7)
    @DisplayName("E2E: Devolución parcial - Ajuste de factura")
    void testDevolucionParcial() {
        // Simulate: Cliente compra, pero devuelve un producto

        Cliente cliente = new Cliente("C005", "Gloria Pérez", "50111222");

        // Compra original
        Factura original = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Televisor", 1, 800000))
                .agregarItem(new ItemFactura("P002", "DVD Player", 1, 120000))
                .agregarItem(new ItemFactura("P003", "Cables HDMI", 2, 25000))
                .conMetodoPago("TARJETA")
                .calcularImpuestosAutomaticamente()
                .build();

        controlador.guardarFactura(original);

        // Factura de ajuste (devolución DVD Player)
        Factura ajuste = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Televisor", 1, 800000))
                .agregarItem(new ItemFactura("P003", "Cables HDMI", 2, 25000))
                .conMetodoPago("TARJETA")
                .calcularImpuestosAutomaticamente()
                .conObservaciones("Ajuste por devolución - Factura original: " + original.getNumeroFactura())
                .build();

        controlador.guardarFactura(ajuste);

        double diferencia = original.calcularTotal() - ajuste.calcularTotal();

        // Assertions
        assertAll("Verificar devolución parcial",
                () -> assertTrue(diferencia > 100000, "Diferencia debe ser el producto devuelto"),
                () -> assertEquals(2, controlador.obtenerTodasLasFacturas().size())
        );
    }

    @Test
    @Order(8)
    @DisplayName("E2E: Cierre de caja - Resumen del día")
    void testCierreDeCaja() {
        // Simulate: Al final del día, supervisor hace cierre

        LocalDate hoy = LocalDate.now();

        // Ventas del día
        for (int i = 1; i <= 10; i++) {
            Cliente cliente = new Cliente("CDIA" + i, "Cliente " + i, "000" + i);

            Factura factura = controlador.iniciarFactura()
                    .conFechaEmision(hoy)
                    .paraCliente(cliente)
                    .agregarItem(new ItemFactura("P" + i, "Producto " + i, i, 5000.0))
                    .conMetodoPago(i % 2 == 0 ? "TARJETA" : "EFECTIVO")
                    .calcularImpuestosAutomaticamente()
                    .build();

            controlador.guardarFactura(factura);
        }

        // Cierre
        List<Factura> facturasHoy = controlador.buscarPorFecha(hoy);
        double totalDia = facturasHoy.stream()
                .mapToDouble(Factura::calcularTotal)
                .sum();

        long ventasEfectivo = facturasHoy.stream()
                .filter(f -> f.getMetodoPago().equals("EFECTIVO"))
                .count();

        long ventasTarjeta = facturasHoy.stream()
                .filter(f -> f.getMetodoPago().equals("TARJETA"))
                .count();

        // Assertions
        assertAll("Verificar cierre de caja",
                () -> assertEquals(10, facturasHoy.size(), "10 ventas del día"),
                () -> assertEquals(5, ventasEfectivo, "5 ventas en efectivo"),
                () -> assertEquals(5, ventasTarjeta, "5 ventas con tarjeta"),
                () -> assertTrue(totalDia > 250000, "Total del día razonable")
        );
    }

    @Test
    @Order(9)
    @DisplayName("E2E: Promoción especial - Descuento por volumen")
    void testPromocionEspecial() {

        Cliente cliente = new Cliente("C006", "Sofía Martínez", "60333444");

        ItemFactura item1 = new ItemFactura("PROM001", "Detergente", 3, 8000);
        item1.setDescuentoItem(8000); // El tercero gratis

        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(item1)
                .agregarItem(new ItemFactura("PROM002", "Jabón x3", 2, 5000))
                .conMetodoPago("EFECTIVO")
                .calcularImpuestosAutomaticamente()
                .conObservaciones("Promoción 3x2 aplicada en detergente")
                .build();

        controlador.guardarFactura(factura);

       assertAll("Verificar promoción 3x2",
                () -> assertEquals(16000.0, item1.calcularTotal(), 0.01, "3 x 8000 - 8000 = 16000"),
                () -> assertTrue(factura.getObservaciones().contains("3x2"))
        );
    }

    @Test
    @Order(10)
    @DisplayName("E2E: Escenario completo - De entrada a salida")
    void testEscenarioCompleto() {

        Cliente cliente = new Cliente("C999", "Jorge Luis Hernández", "70555666");
        cliente.setEmail("jorge@email.com");
        cliente.setTelefono("3201234567");

        ItemFactura prod1 = new ItemFactura("COMP001", "Arroz Premium 1kg", 2, 4500);
        ItemFactura prod2 = new ItemFactura("COMP002", "Aceite Oliva", 1, 25000);
        ItemFactura prod3 = new ItemFactura("COMP003", "Vino Tinto", 2, 35000);
        ItemFactura prod4 = new ItemFactura("COMP004", "Queso Parmesano", 1, 28000);
        ItemFactura prod5 = new ItemFactura("COMP005", "Galletas Gourmet", 3, 6000);

        Factura.Builder builder = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(prod1)
                .agregarItem(prod2)
                .agregarItem(prod3)
                .agregarItem(prod4)
                .agregarItem(prod5);

        builder.calcularImpuestosAutomaticamente();

        builder.conMetodoPago("TARJETA_CREDITO")
                .conPropina(8000);

        builder.conDescuentos(10000)
                .conObservaciones("Cliente Gold - 5% descuento aplicado");

        Factura facturaFinal = builder.build();
        controlador.guardarFactura(facturaFinal);

        boolean emailEnviado = facturaFinal.enviarPorCorreo();

        String pdf = facturaFinal.generarPDF();

        assertAll("Verificar escenario completo",
                () -> assertNotNull(facturaFinal.getNumeroFactura(), "Factura generada"),
                () -> assertEquals(5, facturaFinal.getItems().size(), "5 productos"),
                () -> assertEquals(150000.0, facturaFinal.getSubtotal(), 0.01),
                () -> assertTrue(facturaFinal.getImpuestos() > 0, "Impuestos calculados"),
                () -> assertEquals(10000.0, facturaFinal.getDescuentos(), 0.01),
                () -> assertEquals(8000.0, facturaFinal.getPropina(), 0.01),
                () -> assertTrue(emailEnviado, "Email enviado"),
                () -> assertNotNull(pdf, "PDF generado"),
                () -> assertEquals(1, controlador.obtenerTodasLasFacturas().size())
        );

        System.out.println("\n=== RESUMEN DE COMPRA ===");
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Subtotal: $" + facturaFinal.getSubtotal());
        System.out.println("Impuestos: $" + facturaFinal.getImpuestos());
        System.out.println("Descuentos: $" + facturaFinal.getDescuentos());
        System.out.println("Propina: $" + facturaFinal.getPropina());
        System.out.println("TOTAL: $" + facturaFinal.calcularTotal());
        System.out.println("========================\n");
    }
}
