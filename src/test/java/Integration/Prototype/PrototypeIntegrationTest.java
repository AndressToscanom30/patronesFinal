package Integration.Prototype;

import Creacionales.Prototype.controlador.PromocionControlador;
import Creacionales.Prototype.modelo.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Prototype Pattern - Tests de Integración")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PrototypeIntegrationTest {

    private PromocionControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new PromocionControlador();
    }

    @Test
    @Order(1)
    @DisplayName("Integración: Crear promoción desde prototipo base")
    void testCrearDesdePrototipoBase() {
        // Arrange & Act
        Promocion promocion = controlador.crearDesdePrototipo(
            "BASE_PORCENTAJE", 
            "Descuento Black Friday"
        );

        // Assert
        assertAll("Verificar creación desde prototipo",
            () -> assertNotNull(promocion),
            () -> assertEquals("Descuento Black Friday", promocion.getNombre()),
            () -> assertTrue(promocion.getId().startsWith("PROMO-")),
            () -> assertInstanceOf(PromocionPorcentaje.class, promocion)
        );
    }

    @Test
    @Order(2)
    @DisplayName("Integración: Guardar y buscar promoción")
    void testGuardarYBuscar() {
        // Arrange
        Promocion promocion = controlador.crearPromocionPorcentaje(
            "Verano 2025",
            20.0,
            LocalDate.now(),
            LocalDate.now().plusMonths(3)
        );

        // Act
        controlador.guardarPromocion(promocion);
        Promocion encontrada = controlador.buscarPromocion(promocion.getId());

        // Assert
        assertAll("Verificar guardado y búsqueda",
            () -> assertNotNull(encontrada),
            () -> assertEquals(promocion.getId(), encontrada.getId()),
            () -> assertEquals(promocion.getNombre(), encontrada.getNombre())
        );
    }

    @Test
    @Order(3)
    @DisplayName("Integración: Clonar promoción existente")
    void testClonarPromocionExistente() {
        // Arrange
        Promocion original = controlador.crearPromocionPorcentaje(
            "Promo Original",
            15.0,
            LocalDate.now(),
            LocalDate.now().plusDays(30)
        );
        controlador.guardarPromocion(original);

        // Act
        Promocion clonada = controlador.clonarPromocion(
            original.getId(),
            "Promo Clonada"
        );
        controlador.guardarPromocion(clonada);

        // Assert
        assertAll("Verificar clonación",
            () -> assertNotNull(clonada),
            () -> assertNotEquals(original.getId(), clonada.getId()),
            () -> assertEquals("Promo Clonada", clonada.getNombre()),
            () -> assertEquals(original.getPorcentajeDescuento(), clonada.getPorcentajeDescuento()),
            () -> assertEquals(2, controlador.obtenerTodasLasPromociones().size())
        );
    }

    @Test
    @Order(4)
    @DisplayName("Integración: Crear múltiples promociones")
    void testCrearMultiplesPromociones() {
        // Arrange & Act
        for (int i = 1; i <= 5; i++) {
            Promocion promo = controlador.crearPromocionPorcentaje(
                "Promo " + i,
                10.0 * i,
                LocalDate.now(),
                LocalDate.now().plusDays(i * 10)
            );
            controlador.guardarPromocion(promo);
        }

        // Assert
        List<Promocion> todas = controlador.obtenerTodasLasPromociones();
        assertEquals(5, todas.size());
    }

    @Test
    @Order(5)
    @DisplayName("Integración: Buscar promociones vigentes")
    void testBuscarPromocionesVigentes() {
        // Arrange
        // Promoción vigente
        Promocion vigente = controlador.crearPromocionPorcentaje(
            "Vigente",
            10.0,
            LocalDate.now().minusDays(5),
            LocalDate.now().plusDays(5)
        );
        controlador.guardarPromocion(vigente);

        // Promoción vencida
        Promocion vencida = controlador.crearPromocionPorcentaje(
            "Vencida",
            15.0,
            LocalDate.now().minusDays(30),
            LocalDate.now().minusDays(1)
        );
        controlador.guardarPromocion(vencida);

        // Promoción futura
        Promocion futura = controlador.crearPromocionPorcentaje(
            "Futura",
            20.0,
            LocalDate.now().plusDays(10),
            LocalDate.now().plusDays(20)
        );
        controlador.guardarPromocion(futura);

        // Act
        List<Promocion> vigentes = controlador.buscarPromocionesVigentes();

        // Assert
        assertAll("Verificar búsqueda de vigentes",
            () -> assertEquals(1, vigentes.size()),
            () -> assertEquals("Vigente", vigentes.get(0).getNombre())
        );
    }

    @Test
    @Order(6)
    @DisplayName("Integración: Buscar por categoría")
    void testBuscarPorCategoria() {
        // Arrange
        Promocion promoElectronica = controlador.crearPromocionPorcentaje(
            "Promo Electrónica",
            25.0,
            LocalDate.now(),
            LocalDate.now().plusDays(30)
        );
        promoElectronica.agregarCategoria("ELECTRONICA");
        controlador.guardarPromocion(promoElectronica);

        Promocion promoRopa = controlador.crearPromocionPorcentaje(
            "Promo Ropa",
            30.0,
            LocalDate.now(),
            LocalDate.now().plusDays(30)
        );
        promoRopa.agregarCategoria("ROPA");
        controlador.guardarPromocion(promoRopa);

        // Act
        List<Promocion> promosElectronica = controlador.buscarPorCategoria("ELECTRONICA");
        List<Promocion> promosRopa = controlador.buscarPorCategoria("ROPA");

        // Assert
        assertAll("Verificar búsqueda por categoría",
            () -> assertEquals(1, promosElectronica.size()),
            () -> assertEquals(1, promosRopa.size()),
            () -> assertEquals("Promo Electrónica", promosElectronica.get(0).getNombre()),
            () -> assertEquals("Promo Ropa", promosRopa.get(0).getNombre())
        );
    }

    @Test
    @Order(7)
    @DisplayName("Integración: Registrar y usar prototipo personalizado")
    void testPrototipoPersonalizado() {
        // Arrange
        PromocionPorcentaje custom = new PromocionPorcentaje(
            "CUSTOM-VIP",
            "Prototipo VIP",
            "Para clientes VIP"
        );
        custom.setPorcentajeDescuento(50.0);
        custom.agregarCategoria("TODOS");

        controlador.registrarPrototipoPersonalizado("VIP_PROTOTIPO", custom);

        // Act
        Promocion nuevaVIP = controlador.crearDesdePrototipo(
            "VIP_PROTOTIPO",
            "Cliente VIP Juan"
        );

        // Assert
        assertAll("Verificar prototipo personalizado",
            () -> assertNotNull(nuevaVIP),
            () -> assertEquals("Cliente VIP Juan", nuevaVIP.getNombre()),
            () -> assertEquals(50.0, nuevaVIP.getPorcentajeDescuento())
        );
    }

    @Test
    @Order(8)
    @DisplayName("Integración: Crear promoción 2x1")
    void testCrearPromocion2x1() {
        // Arrange & Act
        Promocion promo2x1 = controlador.crearPromocion2x1(
            "Gaseosas 2x1",
            LocalDate.now(),
            LocalDate.now().plusDays(15)
        );
        controlador.guardarPromocion(promo2x1);

        // Assert
        assertAll("Verificar promoción 2x1",
            () -> assertNotNull(promo2x1),
            () -> assertInstanceOf(PromocionDosXUno.class, promo2x1),
            () -> assertEquals("Gaseosas 2x1", promo2x1.getNombre()),
            () -> assertTrue(promo2x1.isActiva())
        );
    }

    @Test
    @Order(9)
    @DisplayName("Integración: Modificar promoción clonada sin afectar original")
    void testModificarClonadaSinAfectarOriginal() {
        // Arrange
        Promocion original = controlador.crearPromocionPorcentaje(
            "Original",
            10.0,
            LocalDate.now(),
            LocalDate.now().plusDays(30)
        );
        original.agregarCategoria("ALIMENTOS");
        controlador.guardarPromocion(original);

        // Act
        Promocion clonada = controlador.clonarPromocion(
            original.getId(),
            "Clonada"
        );
        clonada.setPorcentajeDescuento(20.0);
        clonada.agregarCategoria("BEBIDAS");
        controlador.guardarPromocion(clonada);

        // Recuperar original del controlador
        Promocion originalRecuperada = controlador.buscarPromocion(original.getId());

        // Assert
        assertAll("Verificar independencia de clon",
            () -> assertEquals(10.0, originalRecuperada.getPorcentajeDescuento()),
            () -> assertEquals(20.0, clonada.getPorcentajeDescuento()),
            () -> assertEquals(1, originalRecuperada.getCategoriasAplicables().size()),
            () -> assertEquals(2, clonada.getCategoriasAplicables().size())
        );
    }

    @Test
    @Order(10)
    @DisplayName("Integración: Flujo completo de gestión de promociones")
    void testFlujoCompletoGestion() {
        // 1. Crear prototipo personalizado
        PromocionCombo comboProto = new PromocionCombo(
            "COMBO-BASE",
            "Combo Asado Base",
            "Base para combos de asado"
        );
        comboProto.agregarProducto("CARNE");
        comboProto.agregarProducto("CARBON");
        controlador.registrarPrototipoPersonalizado("COMBO_ASADO", comboProto);

        // 2. Crear múltiples promociones desde el prototipo
        for (int i = 1; i <= 3; i++) {
            Promocion combo = controlador.crearDesdePrototipo(
                "COMBO_ASADO",
                "Combo Asado " + i
            );
            if (combo instanceof PromocionCombo pc) {
                pc.setPrecioCombo(30000.0 + (i * 5000));
            }
            combo.setFechaInicio(LocalDate.now());
            combo.setFechaFin(LocalDate.now().plusDays(30));
            combo.setActiva(true);
            controlador.guardarPromocion(combo);
        }

        // 3. Crear promoción de descuento
        Promocion descuento = controlador.crearPromocionPorcentaje(
            "Descuento General",
            15.0,
            LocalDate.now(),
            LocalDate.now().plusMonths(1)
        );
        controlador.guardarPromocion(descuento);

        // 4. Verificar estado final
        List<Promocion> todas = controlador.obtenerTodasLasPromociones();
        List<Promocion> vigentes = controlador.buscarPromocionesVigentes();

        assertAll("Verificar flujo completo",
            () -> assertEquals(4, todas.size(), "3 combos + 1 descuento"),
            () -> assertEquals(4, vigentes.size(), "Todas deben estar vigentes"),
            () -> assertTrue(controlador.getRegistro().existePrototipo("COMBO_ASADO"))
        );
    }
}
