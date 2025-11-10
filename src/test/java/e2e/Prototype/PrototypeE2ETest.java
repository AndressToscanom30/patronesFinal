package e2e.Prototype;

import Creacionales.Prototype.controlador.PromocionControlador;
import Creacionales.Prototype.modelo.*;
import Creacionales.Prototype.vista.ConsolaPromocion;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests End-to-End para Prototype Pattern.
 */
@DisplayName("Prototype Pattern - Tests End-to-End")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PrototypeE2ETest {

    private PromocionControlador controlador;

    @BeforeEach
    void setUp() {
        controlador = new PromocionControlador();
    }

    @Test
    @Order(1)
    @DisplayName("E2E: Campaña Black Friday - Múltiples promociones desde plantilla")
    void testCampanaBlackFriday() {
        // Simulate: Marketing crea campaña Black Friday con múltiples promociones

        // 1. Crear prototipo base para Black Friday
        PromocionPorcentaje protoBF = new PromocionPorcentaje(
                "BF-BASE",
                "Black Friday Base",
                "Plantilla para todas las promos de BF");
        protoBF.setPorcentajeDescuento(30.0);
        protoBF.setFechaInicio(LocalDate.of(2025, 11, 28));
        protoBF.setFechaFin(LocalDate.of(2025, 11, 30));
        controlador.registrarPrototipoPersonalizado("BLACK_FRIDAY", protoBF);

        // 2. Crear promociones específicas por categoría
        String[] categorias = { "ELECTRONICA", "ROPA", "HOGAR", "DEPORTES", "JUGUETES" };

        for (String categoria : categorias) {
            Promocion promo = controlador.crearDesdePrototipo(
                    "BLACK_FRIDAY",
                    "Black Friday " + categoria);
            promo.agregarCategoria(categoria);
            promo.setActiva(true);
            controlador.guardarPromocion(promo);
        }

        // 3. Verificar campaña completa
        List<Promocion> todasBF = controlador.obtenerTodasLasPromociones();

        assertAll("Verificar campaña Black Friday",
                () -> assertEquals(5, todasBF.size()),
                () -> assertTrue(todasBF.stream().allMatch(p -> p.getPorcentajeDescuento() == 30.0)),
                () -> assertEquals(1, controlador.buscarPorCategoria("ELECTRONICA").size()));
    }

    @Test
    @Order(2)
    @DisplayName("E2E: Temporada escolar - Clonación masiva de promociones")
    void testTemporadaEscolar() {
        // Simulate: Enero, temporada de regreso a clases

        LocalDate inicioEscolar = LocalDate.of(2025, 1, 15);
        LocalDate finEscolar = LocalDate.of(2025, 2, 15);

        // Promoción 1: Útiles escolares
        Promocion promoUtiles = controlador.crearPromocionPorcentaje(
                "Descuento Útiles",
                25.0,
                inicioEscolar,
                finEscolar);
        promoUtiles.agregarCategoria("UTILES_ESCOLARES");
        controlador.guardarPromocion(promoUtiles);

        // Promoción 2: 3x2 en cuadernos
        Promocion promo3x2 = controlador.crearDesdePrototipo("BASE_2X1", "3x2 Cuadernos");
        if (promo3x2 instanceof PromocionDosXUno p) {
            p.setCantidadCompra(3);
            p.setCantidadPaga(2);
        }
        promo3x2.setFechaInicio(inicioEscolar);
        promo3x2.setFechaFin(finEscolar);
        promo3x2.setActiva(true);
        promo3x2.agregarCategoria("CUADERNOS");
        controlador.guardarPromocion(promo3x2);

        // Clonar promociones para otras sucursales
        for (int sucursal = 2; sucursal <= 5; sucursal++) {
            Promocion clon1 = controlador.clonarPromocion(
                    promoUtiles.getId(),
                    "Descuento Útiles - Sucursal " + sucursal);
            controlador.guardarPromocion(clon1);

            Promocion clon2 = controlador.clonarPromocion(
                    promo3x2.getId(),
                    "3x2 Cuadernos - Sucursal " + sucursal);
            controlador.guardarPromocion(clon2);
        }

        assertAll("Verificar temporada escolar",
                () -> assertEquals(10, controlador.obtenerTodasLasPromociones().size()),
                () -> assertEquals(5, controlador.buscarPorCategoria("UTILES_ESCOLARES").size()));
    }

    @Test
    @Order(3)
    @DisplayName("E2E: Combo familiar - Construcción compleja de promoción")
    void testComboFamiliar() {
        // Simulate: Crear combo para fin de semana familiar

        PromocionCombo comboFamiliar = (PromocionCombo) controlador.crearDesdePrototipo(
                "BASE_COMBO",
                "Combo Familiar Fin de Semana");

        // Agregar productos al combo
        comboFamiliar.agregarProducto("CARNE-ASADO-1KG");
        comboFamiliar.agregarProducto("CHORIZO-500G");
        comboFamiliar.agregarProducto("CARBON-3KG");
        comboFamiliar.agregarProducto("GASEOSA-2L");
        comboFamiliar.agregarProducto("PAN-HAMBURGUESA");
        comboFamiliar.agregarProducto("CHIMICHURRI");

        comboFamiliar.setPrecioCombo(85000.0);
        comboFamiliar.setFechaInicio(LocalDate.now());
        comboFamiliar.setFechaFin(LocalDate.now().plusMonths(1));
        comboFamiliar.setActiva(true);

        controlador.guardarPromocion(comboFamiliar);

        // Calcular ahorro
        double precioIndividual = 120000.0; // Suma de precios individuales
        double ahorro = comboFamiliar.calcularAhorro(precioIndividual);

        assertAll("Verificar combo familiar",
                () -> assertEquals(6, comboFamiliar.getProductosRequeridos().size()),
                () -> assertEquals(85000.0, comboFamiliar.getPrecioCombo()),
                () -> assertEquals(35000.0, ahorro, 0.01),
                () -> assertTrue(comboFamiliar.contieneProducto("CARNE-ASADO-1KG")));
    }

    @Test
    @Order(4)
    @DisplayName("E2E: Día del Padre - Promociones escalonadas")
    void testDiaDelPadre() {
        // Simulate: Junio, promociones progresivas para Día del Padre

        LocalDate inicioPadre = LocalDate.of(2025, 6, 1);
        LocalDate finPadre = LocalDate.of(2025, 6, 21);

        // Semana 1: 10% descuento
        Promocion semana1 = controlador.crearPromocionPorcentaje(
                "Día del Padre - Semana 1",
                10.0,
                inicioPadre,
                inicioPadre.plusDays(7));
        semana1.agregarCategoria("HOMBRE");
        controlador.guardarPromocion(semana1);

        // Semana 2: Clonar y aumentar a 15%
        Promocion semana2 = controlador.clonarPromocion(
                semana1.getId(),
                "Día del Padre - Semana 2");
        semana2.setPorcentajeDescuento(15.0);
        semana2.setFechaInicio(inicioPadre.plusDays(7));
        semana2.setFechaFin(inicioPadre.plusDays(14));
        controlador.guardarPromocion(semana2);

        // Semana 3: Última semana, 20%
        Promocion semana3 = controlador.clonarPromocion(
                semana2.getId(),
                "Día del Padre - Semana 3");
        semana3.setPorcentajeDescuento(20.0);
        semana3.setFechaInicio(inicioPadre.plusDays(14));
        semana3.setFechaFin(finPadre);
        controlador.guardarPromocion(semana3);

        assertAll("Verificar promociones escalonadas",
                () -> assertEquals(3, controlador.obtenerTodasLasPromociones().size()),
                () -> assertEquals(10.0, semana1.getPorcentajeDescuento()),
                () -> assertEquals(15.0, semana2.getPorcentajeDescuento()),
                () -> assertEquals(20.0, semana3.getPorcentajeDescuento()));
    }

    @Test
    @Order(5)
    @DisplayName("E2E: Sistema de fidelización - Niveles VIP")
    void testSistemaFidelizacion() {
        // Simulate: Diferentes niveles de clientes VIP con beneficios incrementales

        // Cliente Bronce
        PromocionPorcentaje protoBronce = new PromocionPorcentaje(
                "VIP-BRONCE",
                "VIP Bronce Base",
                "Para clientes nivel Bronce");
        protoBronce.setPorcentajeDescuento(5.0);
        controlador.registrarPrototipoPersonalizado("VIP_BRONCE", protoBronce);

        // Cliente Plata (clonar Bronce y mejorar)
        Promocion protoPlata = controlador.crearDesdePrototipo("VIP_BRONCE", "VIP Plata Base");
        protoPlata.setId("VIP-PLATA");
        protoPlata.setPorcentajeDescuento(10.0);
        controlador.registrarPrototipoPersonalizado("VIP_PLATA", protoPlata);

        // Cliente Oro (clonar Plata y mejorar)
        Promocion protoOro = controlador.crearDesdePrototipo("VIP_PLATA", "VIP Oro Base");
        protoOro.setId("VIP-ORO");
        protoOro.setPorcentajeDescuento(15.0);
        controlador.registrarPrototipoPersonalizado("VIP_ORO", protoOro);

        // Crear promociones para clientes específicos
        Promocion clienteBronce = controlador.crearDesdePrototipo("VIP_BRONCE", "Cliente Juan - Bronce");
        clienteBronce.setActiva(true);
        clienteBronce.setFechaInicio(LocalDate.now());
        clienteBronce.setFechaFin(LocalDate.now().plusYears(1));
        controlador.guardarPromocion(clienteBronce);

        Promocion clienteOro = controlador.crearDesdePrototipo("VIP_ORO", "Cliente María - Oro");
        clienteOro.setActiva(true);
        clienteOro.setFechaInicio(LocalDate.now());
        clienteOro.setFechaFin(LocalDate.now().plusYears(1));
        controlador.guardarPromocion(clienteOro);

        assertAll("Verificar sistema VIP",
                () -> assertEquals(5.0, clienteBronce.getPorcentajeDescuento()),
                () -> assertEquals(15.0, clienteOro.getPorcentajeDescuento()),
                () -> assertTrue(controlador.getRegistro().existePrototipo("VIP_BRONCE")),
                () -> assertTrue(controlador.getRegistro().existePrototipo("VIP_ORO")));
    }

    @Test
    @Order(6)
    @DisplayName("E2E: Liquidación de inventario - Múltiples estrategias")
    void testLiquidacionInventario() {
        // Simulate: Fin de temporada, liquidar inventario con diferentes estrategias

        LocalDate inicioLiquidacion = LocalDate.now();
        LocalDate finLiquidacion = LocalDate.now().plusDays(30);

        // Estrategia 1: Descuento por porcentaje en ropa
        Promocion liquidacionRopa = controlador.crearPromocionPorcentaje(
                "Liquidación Ropa 50%",
                50.0,
                inicioLiquidacion,
                finLiquidacion);
        liquidacionRopa.agregarCategoria("ROPA");
        controlador.guardarPromocion(liquidacionRopa);

        // Estrategia 2: 3x2 en zapatos
        Promocion promo3x2Zapatos = controlador.crearDesdePrototipo("BASE_2X1", "3x2 Zapatos");
        if (promo3x2Zapatos instanceof PromocionDosXUno p) {
            p.setCantidadCompra(3);
            p.setCantidadPaga(2);
        }
        promo3x2Zapatos.setFechaInicio(inicioLiquidacion);
        promo3x2Zapatos.setFechaFin(finLiquidacion);
        promo3x2Zapatos.setActiva(true);
        promo3x2Zapatos.agregarCategoria("CALZADO");
        controlador.guardarPromocion(promo3x2Zapatos);

        // Estrategia 3: Combos de liquidación
        PromocionCombo comboLiquidacion = (PromocionCombo) controlador.crearDesdePrototipo(
                "BASE_COMBO",
                "Combo Liquidación Total");
        comboLiquidacion.agregarProducto("CAMISA-LIQUIDACION");
        comboLiquidacion.agregarProducto("PANTALON-LIQUIDACION");
        comboLiquidacion.agregarProducto("ZAPATOS-LIQUIDACION");
        comboLiquidacion.setPrecioCombo(50000.0);
        comboLiquidacion.setFechaInicio(inicioLiquidacion);
        comboLiquidacion.setFechaFin(finLiquidacion);
        comboLiquidacion.setActiva(true);
        controlador.guardarPromocion(comboLiquidacion);

        List<Promocion> vigentes = controlador.buscarPromocionesVigentes();

        assertAll("Verificar liquidación",
                () -> assertEquals(3, vigentes.size()),
                () -> assertEquals(1, controlador.buscarPorCategoria("ROPA").size()),
                () -> assertEquals(1, controlador.buscarPorCategoria("CALZADO").size())
        );
    }

    @Test
    @Order(7)
    @DisplayName("E2E: Aniversario de la tienda - 5 días de promociones")
    void testAniversarioTienda() {
        // Limpiar promociones anteriores o crear un nuevo controlador
        PromocionControlador controladorLimpio = new PromocionControlador();

        LocalDate diaInicial = LocalDate.of(2025, 9, 1);

        for (int dia = 0; dia < 5; dia++) {
            LocalDate fecha = diaInicial.plusDays(dia);
            double descuento = 10.0 + (dia * 5);

            Promocion promoDia = controladorLimpio.crearPromocionPorcentaje(
                    String.format("Aniversario Día %d", dia + 1),
                    descuento,
                    fecha,
                    fecha);
            controladorLimpio.guardarPromocion(promoDia);
        }

        List<Promocion> promos = controladorLimpio.obtenerTodasLasPromociones();

        assertAll("Verificar aniversario",
                () -> assertEquals(5, promos.size()),
                () -> assertEquals(20.0, promos.get(0).getPorcentajeDescuento()),
                () -> assertEquals(25.0, promos.get(4).getPorcentajeDescuento())         );
    }

    @Test
    @Order(8)
    @DisplayName("E2E: Promoción flash - Crear y activar rápidamente")
    void testPromocionFlash() {
        // Simulate: Gerente necesita crear promoción flash urgente

        long startTime = System.currentTimeMillis();

        // Usar prototipo existente para crear rápidamente
        Promocion flash = controlador.crearDesdePrototipo(
                "BASE_PORCENTAJE",
                "FLASH SALE - 2 HORAS");
        flash.setPorcentajeDescuento(40.0);
        flash.setFechaInicio(LocalDate.now());
        flash.setFechaFin(LocalDate.now());
        flash.setActiva(true);
        controlador.guardarPromocion(flash);

        long endTime = System.currentTimeMillis();
        long duracion = endTime - startTime;

        assertAll("Verificar creación flash",
                () -> assertNotNull(flash),
                () -> assertTrue(flash.estaVigente()),
                () -> assertTrue(duracion < 100, "Debe crear en menos de 100ms"));
    }

    @Test
    @Order(9)
    @DisplayName("E2E: Ajuste de promociones - Corrección masiva")
    void testAjustePromociones() {
        // Simulate: Error en configuración, necesita corregir múltiples promociones

        // Crear promociones con error (descuento muy bajo)
        for (int i = 1; i <= 10; i++) {
            Promocion promo = controlador.crearPromocionPorcentaje(
                    "Promo " + i,
                    5.0, // Error: muy bajo
                    LocalDate.now(),
                    LocalDate.now().plusDays(30));
            controlador.guardarPromocion(promo);
        }

        // Corregir usando clonación
        List<Promocion> promosOriginales = controlador.obtenerTodasLasPromociones();

        for (Promocion original : promosOriginales) {
            // Clonar con configuración correcta
            Promocion corregida = controlador.clonarPromocion(
                    original.getId(),
                    original.getNombre() + " - Corregida");
            corregida.setPorcentajeDescuento(20.0); // Corrección
            controlador.guardarPromocion(corregida);

            // Desactivar original
            original.setActiva(false);
        }

        long activas = controlador.obtenerTodasLasPromociones().stream()
                .filter(Promocion::isActiva)
                .count();

        assertAll("Verificar corrección masiva",
                () -> assertEquals(20, controlador.obtenerTodasLasPromociones().size()),
                () -> assertEquals(10, activas, "Solo las corregidas deben estar activas"));
    }

    @Test
    @Order(10)
    @DisplayName("E2E: Escenario completo de gestión anual")
    void testEscenarioCompletoAnual() {
        // Simulate: Año completo de gestión de promociones

        System.out.println("\n=== GESTIÓN ANUAL DE PROMOCIONES ===\n");

        // Q1: Año nuevo y San Valentín
        Promocion anoNuevo = controlador.crearPromocionPorcentaje(
                "Año Nuevo 2025",
                25.0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 15));
        controlador.guardarPromocion(anoNuevo);

        Promocion sanValentin = controlador.clonarPromocion(
                anoNuevo.getId(),
                "San Valentín");
        sanValentin.setFechaInicio(LocalDate.of(2025, 2, 10));
        sanValentin.setFechaFin(LocalDate.of(2025, 2, 14));
        sanValentin.agregarCategoria("REGALOS");
        controlador.guardarPromocion(sanValentin);

        // Q2: Día de la Madre
        Promocion diaMadre = controlador.crearPromocionPorcentaje(
                "Día de la Madre",
                30.0,
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 15));
        diaMadre.agregarCategoria("MUJER");
        controlador.guardarPromocion(diaMadre);

        // Q3: Mitad de Año
        Promocion mitadAno = controlador.crearPromocionPorcentaje(
                "Mitad de Año",
                40.0,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 15));
        controlador.guardarPromocion(mitadAno);

        // Q4: Black Friday y Navidad
        Promocion blackFriday = controlador.crearPromocionPorcentaje(
                "Black Friday 2025",
                50.0,
                LocalDate.of(2025, 11, 28),
                LocalDate.of(2025, 11, 30));
        controlador.guardarPromocion(blackFriday);

        Promocion navidad = controlador.clonarPromocion(
                blackFriday.getId(),
                "Navidad 2025");
        navidad.setFechaInicio(LocalDate.of(2025, 12, 15));
        navidad.setFechaFin(LocalDate.of(2025, 12, 31));
        navidad.setPorcentajeDescuento(45.0);
        controlador.guardarPromocion(navidad);

        // Reporte final
        List<Promocion> todasAnuales = controlador.obtenerTodasLasPromociones();

        System.out.println("Promociones creadas: " + todasAnuales.size());
        todasAnuales.forEach(p -> System.out.printf("- %s: %.0f%% (%s a %s)%n",
                p.getNombre(),
                p.getPorcentajeDescuento(),
                p.getFechaInicio(),
                p.getFechaFin()));

        assertAll("Verificar gestión anual completa",
                () -> assertEquals(6, todasAnuales.size()),
                () -> assertTrue(todasAnuales.stream().allMatch(p -> p.getPorcentajeDescuento() >= 25)),
                () -> assertEquals(50.0, blackFriday.getPorcentajeDescuento()),
                () -> assertEquals(5, controlador.buscarPorCategoria("REGALOS").size()),
                () -> assertEquals(5, controlador.buscarPorCategoria("MUJER").size()));

        System.out.println("\n=== FIN DE GESTIÓN ANUAL ===\n");
    }
}
