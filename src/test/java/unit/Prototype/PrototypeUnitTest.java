package unit.Prototype;

import Creacionales.Prototype.modelo.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Prototype Pattern - Tests Unitarios")
@ExtendWith(MockitoExtension.class)
class PrototypeUnitTest {

    @Nested
    @DisplayName("Comportamiento de clonación")
    class CloneBehaviorTests {

        @Test
        @DisplayName("PromocionPorcentaje clona correctamente")
        void testPromocionPorcentajeClone() {
            // Arrange
            PromocionPorcentaje original = new PromocionPorcentaje(
                "PROMO-001", 
                "Descuento Verano", 
                "10% en toda la tienda"
            );
            original.setPorcentajeDescuento(10.0);
            original.setMontoMinimo(50000.0);
            original.setActiva(true);
            original.agregarCategoria("ROPA");
            original.agregarCategoria("CALZADO");

            // Act
            PromocionPorcentaje clonada = original.clone();

            // Assert
            assertAll("Verificar clonación de PromocionPorcentaje",
                () -> assertNotSame(original, clonada, "Debe ser una instancia diferente"),
                () -> assertEquals(original.getId(), clonada.getId()),
                () -> assertEquals(original.getNombre(), clonada.getNombre()),
                () -> assertEquals(original.getPorcentajeDescuento(), clonada.getPorcentajeDescuento()),
                () -> assertEquals(original.getMontoMinimo(), clonada.getMontoMinimo()),
                () -> assertEquals(original.isActiva(), clonada.isActiva()),
                () -> assertEquals(original.getCategoriasAplicables(), clonada.getCategoriasAplicables()),
                () -> assertNotSame(original.getCategoriasAplicables(), clonada.getCategoriasAplicables(), 
                    "Lista de categorías debe ser copia profunda")
            );
        }

        @Test
        @DisplayName("PromocionDosXUno clona correctamente")
        void testPromocionDosXUnoClone() {
            // Arrange
            PromocionDosXUno original = new PromocionDosXUno(
                "2X1-001", 
                "Promo 2x1 Gaseosas", 
                "Lleva 2 paga 1"
            );
            original.setCantidadCompra(2);
            original.setCantidadPaga(1);
            original.setActiva(true);
            original.setLimiteUnidades(10);

            // Act
            PromocionDosXUno clonada = original.clone();

            // Assert
            assertAll("Verificar clonación de PromocionDosXUno",
                () -> assertNotSame(original, clonada),
                () -> assertEquals(original.getCantidadCompra(), clonada.getCantidadCompra()),
                () -> assertEquals(original.getCantidadPaga(), clonada.getCantidadPaga()),
                () -> assertEquals(original.getLimiteUnidades(), clonada.getLimiteUnidades())
            );
        }

        @Test
        @DisplayName("PromocionCombo clona correctamente")
        void testPromocionComboClone() {
            // Arrange
            PromocionCombo original = new PromocionCombo(
                "COMBO-001", 
                "Combo Asado", 
                "Carne + Carbón + Chimichurri"
            );
            original.agregarProducto("CARNE-001");
            original.agregarProducto("CARBON-001");
            original.agregarProducto("CHIMI-001");
            original.setPrecioCombo(45000.0);
            original.setActiva(true);

            // Act
            PromocionCombo clonada = original.clone();

            // Assert
            assertAll("Verificar clonación de PromocionCombo",
                () -> assertNotSame(original, clonada),
                () -> assertEquals(original.getProductosRequeridos(), clonada.getProductosRequeridos()),
                () -> assertNotSame(original.getProductosRequeridos(), clonada.getProductosRequeridos(), 
                    "Lista de productos debe ser copia profunda"),
                () -> assertEquals(original.getPrecioCombo(), clonada.getPrecioCombo())
            );
        }

        @Test
        @DisplayName("Clonación profunda de listas es independiente")
        void testClonacionProfundaListas() {
            // Arrange
            PromocionPorcentaje original = new PromocionPorcentaje(
                "PROMO-001", "Test", "Test"
            );
            original.agregarCategoria("CATEGORIA1");

            // Act
            PromocionPorcentaje clonada = original.clone();
            clonada.agregarCategoria("CATEGORIA2");

            // Assert
            assertAll("Verificar independencia de listas",
                () -> assertEquals(1, original.getCategoriasAplicables().size()),
                () -> assertEquals(2, clonada.getCategoriasAplicables().size()),
                () -> assertTrue(original.getCategoriasAplicables().contains("CATEGORIA1")),
                () -> assertFalse(original.getCategoriasAplicables().contains("CATEGORIA2"))
            );
        }

        @Test
        @DisplayName("LocalDate inmutable no requiere clonación profunda")
        void testLocalDateInmutable() {
            // Arrange
            PromocionPorcentaje original = new PromocionPorcentaje(
                "PROMO-001", "Test", "Test"
            );
            LocalDate fecha = LocalDate.of(2025, 12, 31);
            original.setFechaInicio(fecha);

            // Act
            PromocionPorcentaje clonada = original.clone();

            // Assert
            assertSame(original.getFechaInicio(), clonada.getFechaInicio(),
                "LocalDate es inmutable, puede compartir referencia");
        }
    }

    @Nested
    @DisplayName("Aplicación de descuentos")
    class DiscountApplicationTests {

        @Test
        @DisplayName("PromocionPorcentaje aplica descuento correctamente")
        void testPromocionPorcentajeAplicaDescuento() {
            // Arrange
            PromocionPorcentaje promo = new PromocionPorcentaje(
                "PROMO-001", "Descuento 20%", "Desc"
            );
            promo.setPorcentajeDescuento(20.0);
            promo.setMontoMinimo(0);

            // Act
            double resultado = promo.aplicarDescuento(100000.0);

            // Assert
            assertEquals(80000.0, resultado, 0.01, 
                "100000 con 20% descuento = 80000");
        }

        @Test
        @DisplayName("PromocionPorcentaje respeta monto mínimo")
        void testPromocionPorcentajeMontoMinimo() {
            // Arrange
            PromocionPorcentaje promo = new PromocionPorcentaje(
                "PROMO-001", "Descuento 15%", "Desc"
            );
            promo.setPorcentajeDescuento(15.0);
            promo.setMontoMinimo(50000.0);

            // Act
            double conMinimo = promo.aplicarDescuento(60000.0);
            double sinMinimo = promo.aplicarDescuento(40000.0);

            // Assert
            assertAll("Verificar monto mínimo",
                () -> assertEquals(51000.0, conMinimo, 0.01, "Aplica descuento si supera mínimo"),
                () -> assertEquals(40000.0, sinMinimo, 0.01, "No aplica si no supera mínimo")
            );
        }

        @Test
        @DisplayName("PromocionDosXUno calcula correctamente con unidades")
        void testPromocionDosXUnoConUnidades() {
            // Arrange
            PromocionDosXUno promo = new PromocionDosXUno(
                "2X1-001", "2x1", "Desc"
            );
            promo.setCantidadCompra(2);
            promo.setCantidadPaga(1);

            // Act
            double precio2Unidades = promo.aplicarDescuentoConUnidades(5000.0, 2);
            double precio4Unidades = promo.aplicarDescuentoConUnidades(5000.0, 4);
            double precio5Unidades = promo.aplicarDescuentoConUnidades(5000.0, 5);

            // Assert
            assertAll("Verificar 2x1",
                () -> assertEquals(5000.0, precio2Unidades, 0.01, "2 unidades = paga 1"),
                () -> assertEquals(10000.0, precio4Unidades, 0.01, "4 unidades = paga 2"),
                () -> assertEquals(15000.0, precio5Unidades, 0.01, "5 unidades = paga 3")
            );
        }

        @Test
        @DisplayName("PromocionDosXUno 3x2 calcula correctamente")
        void testPromocion3x2() {
            // Arrange
            PromocionDosXUno promo = new PromocionDosXUno(
                "3X2-001", "3x2", "Desc"
            );
            promo.setCantidadCompra(3);
            promo.setCantidadPaga(2);

            // Act
            double precio3Unidades = promo.aplicarDescuentoConUnidades(3000.0, 3);
            double precio6Unidades = promo.aplicarDescuentoConUnidades(3000.0, 6);
            double precio7Unidades = promo.aplicarDescuentoConUnidades(3000.0, 7);

            // Assert
            assertAll("Verificar 3x2",
                () -> assertEquals(6000.0, precio3Unidades, 0.01, "3 unidades = paga 2"),
                () -> assertEquals(12000.0, precio6Unidades, 0.01, "6 unidades = paga 4"),
                () -> assertEquals(15000.0, precio7Unidades, 0.01, "7 unidades = paga 5")
            );
        }

        @Test
        @DisplayName("PromocionCombo calcula ahorro correctamente")
        void testPromocionComboAhorro() {
            // Arrange
            PromocionCombo combo = new PromocionCombo(
                "COMBO-001", "Combo", "Desc"
            );
            combo.setPrecioCombo(25000.0);

            // Act
            double ahorro = combo.calcularAhorro(35000.0);

            // Assert
            assertEquals(10000.0, ahorro, 0.01, 
                "Ahorro = suma individual (35000) - precio combo (25000)");
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class ValidationTests {

        @Test
        @DisplayName("PromocionPorcentaje valida porcentaje correcto")
        void testValidacionPorcentaje() {
            // Arrange
            PromocionPorcentaje promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );
            promo.setPorcentajeDescuento(50.0);

            // Act & Assert
            assertTrue(promo.validar(), "Porcentaje válido debe pasar validación");
        }

        @Test
        @DisplayName("PromocionPorcentaje rechaza porcentaje inválido")
        void testValidacionPorcentajeInvalido() {
            // Arrange
            PromocionPorcentaje promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );

            // Act & Assert
            assertFalse(promo.validar(), "Porcentaje 0 no debe validar");
        }

        @Test
        @DisplayName("PromocionDosXUno valida correctamente")
        void testValidacion2x1() {
            // Arrange
            PromocionDosXUno promo = new PromocionDosXUno(
                "2X1-001", "Test", "Desc"
            );
            promo.setCantidadCompra(2);
            promo.setCantidadPaga(1);

            // Act & Assert
            assertTrue(promo.validar());
        }

        @Test
        @DisplayName("PromocionDosXUno no valida si compra <= paga")
        void testValidacion2x1Invalida() {
            // Arrange
            PromocionDosXUno promo = new PromocionDosXUno(
                "2X1-001", "Test", "Desc"
            );
            promo.setCantidadCompra(2);
            promo.setCantidadPaga(2);

            // Act & Assert
            assertFalse(promo.validar(), "Cantidad compra debe ser > cantidad paga");
        }

        @Test
        @DisplayName("PromocionCombo valida con productos y precio")
        void testValidacionCombo() {
            // Arrange
            PromocionCombo combo = new PromocionCombo(
                "COMBO-001", "Test", "Desc"
            );
            combo.agregarProducto("PROD-001");
            combo.setPrecioCombo(10000.0);

            // Act & Assert
            assertTrue(combo.validar());
        }

        @Test
        @DisplayName("PromocionCombo no valida sin productos")
        void testValidacionComboSinProductos() {
            // Arrange
            PromocionCombo combo = new PromocionCombo(
                "COMBO-001", "Test", "Desc"
            );
            combo.setPrecioCombo(10000.0);

            // Act & Assert
            assertFalse(combo.validar(), "Debe requerir al menos un producto");
        }

        @Test
        @DisplayName("Rechaza porcentaje fuera de rango")
        void testPorcentajeFueraDeRango() {
            // Arrange
            PromocionPorcentaje promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );

            // Act & Assert
            assertAll("Verificar rango de porcentaje",
                () -> assertThrows(IllegalArgumentException.class,
                    () -> promo.setPorcentajeDescuento(-5),
                    "Debe rechazar porcentaje negativo"),
                () -> assertThrows(IllegalArgumentException.class,
                    () -> promo.setPorcentajeDescuento(105),
                    "Debe rechazar porcentaje > 100")
            );
        }

        @Test
        @DisplayName("Rechaza monto mínimo negativo")
        void testMontoMinimoNegativo() {
            // Arrange
            PromocionPorcentaje promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> promo.setMontoMinimo(-1000),
                "Debe rechazar monto mínimo negativo");
        }

        @Test
        @DisplayName("Rechaza cantidad compra no positiva")
        void testCantidadCompraInvalida() {
            // Arrange
            PromocionDosXUno promo = new PromocionDosXUno(
                "2X1-001", "Test", "Desc"
            );

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> promo.setCantidadCompra(0),
                "Debe rechazar cantidad compra no positiva");
        }

        @Test
        @DisplayName("Rechaza precio combo negativo")
        void testPrecioComboNegativo() {
            // Arrange
            PromocionCombo combo = new PromocionCombo(
                "COMBO-001", "Test", "Desc"
            );

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> combo.setPrecioCombo(-5000),
                "Debe rechazar precio combo negativo");
        }

        @Test
        @DisplayName("Rechaza límite de unidades negativo")
        void testLimiteUnidadesNegativo() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> promo.setLimiteUnidades(-10),
                "Debe rechazar límite negativo");
        }

        @Test
        @DisplayName("Rechaza ID nulo o vacío")
        void testIDInvalido() {
            assertAll("Verificar validación de ID",
                () -> assertThrows(IllegalArgumentException.class,
                    () -> new PromocionPorcentaje(null, "Test", "Desc"),
                    "Debe rechazar ID nulo"),
                () -> assertThrows(IllegalArgumentException.class,
                    () -> new PromocionPorcentaje("", "Test", "Desc"),
                    "Debe rechazar ID vacío")
            );
        }

        @Test
        @DisplayName("Rechaza nombre nulo o vacío")
        void testNombreInvalido() {
            assertAll("Verificar validación de nombre",
                () -> assertThrows(IllegalArgumentException.class,
                    () -> new PromocionPorcentaje("ID-001", null, "Desc"),
                    "Debe rechazar nombre nulo"),
                () -> assertThrows(IllegalArgumentException.class,
                    () -> new PromocionPorcentaje("ID-001", "", "Desc"),
                    "Debe rechazar nombre vacío")
            );
        }
    }

    @Nested
    @DisplayName("Vigencia de promociones")
    class VigenciaTests {

        @Test
        @DisplayName("Promoción está vigente dentro del rango")
        void testPromocionVigente() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );
            promo.setFechaInicio(LocalDate.now().minusDays(5));
            promo.setFechaFin(LocalDate.now().plusDays(5));
            promo.setActiva(true);

            // Act & Assert
            assertTrue(promo.estaVigente(), "Debe estar vigente");
        }

        @Test
        @DisplayName("Promoción no vigente si no está activa")
        void testPromocionNoActivaNoVigente() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );
            promo.setFechaInicio(LocalDate.now().minusDays(5));
            promo.setFechaFin(LocalDate.now().plusDays(5));
            promo.setActiva(false);

            // Act & Assert
            assertFalse(promo.estaVigente(), "No debe estar vigente si no está activa");
        }

        @Test
        @DisplayName("Promoción no vigente si está vencida")
        void testPromocionVencida() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );
            promo.setFechaInicio(LocalDate.now().minusDays(10));
            promo.setFechaFin(LocalDate.now().minusDays(1));
            promo.setActiva(true);

            // Act & Assert
            assertFalse(promo.estaVigente(), "No debe estar vigente si ya venció");
        }

        @Test
        @DisplayName("Promoción no vigente si aún no inicia")
        void testPromocionFutura() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );
            promo.setFechaInicio(LocalDate.now().plusDays(5));
            promo.setFechaFin(LocalDate.now().plusDays(10));
            promo.setActiva(true);

            // Act & Assert
            assertFalse(promo.estaVigente(), "No debe estar vigente si aún no inicia");
        }

        @Test
        @DisplayName("Promoción vigente sin fechas establecidas")
        void testPromocionSinFechas() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );
            promo.setActiva(true);

            // Act & Assert
            assertTrue(promo.estaVigente(), "Debe estar vigente si está activa y sin fechas");
        }
    }

    @Nested
    @DisplayName("Categorías aplicables")
    class CategoriasTests {

        @Test
        @DisplayName("Promoción aplica a categoría específica")
        void testAplicaACategoriaEspecifica() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );
            promo.agregarCategoria("ELECTRONICA");
            promo.agregarCategoria("ROPA");

            // Act & Assert
            assertAll("Verificar aplicabilidad por categoría",
                () -> assertTrue(promo.esAplicableACategoria("ELECTRONICA")),
                () -> assertTrue(promo.esAplicableACategoria("ROPA")),
                () -> assertFalse(promo.esAplicableACategoria("ALIMENTOS"))
            );
        }

        @Test
        @DisplayName("Promoción sin categorías aplica a todas")
        void testSinCategoriasAplicaATodas() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );

            // Act & Assert
            assertAll("Verificar que aplica a cualquier categoría",
                () -> assertTrue(promo.esAplicableACategoria("ELECTRONICA")),
                () -> assertTrue(promo.esAplicableACategoria("ROPA")),
                () -> assertTrue(promo.esAplicableACategoria("ALIMENTOS"))
            );
        }

        @Test
        @DisplayName("No agrega categorías duplicadas")
        void testNoDuplicaCategorias() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Test", "Desc"
            );

            // Act
            promo.agregarCategoria("ELECTRONICA");
            promo.agregarCategoria("ELECTRONICA");
            promo.agregarCategoria("ELECTRONICA");

            // Assert
            assertEquals(1, promo.getCategoriasAplicables().size(),
                "No debe duplicar categorías");
        }
    }

    @Nested
    @DisplayName("RegistroPromocion - Prototype Registry")
    class RegistroPromocionTests {

        @Test
        @DisplayName("Registro inicializa con prototipos base")
        void testRegistroInicializaPrototipos() {
            // Arrange & Act
            RegistroPromocion registro = new RegistroPromocion();

            // Assert
            assertAll("Verificar prototipos iniciales",
                () -> assertTrue(registro.existePrototipo("BASE_PORCENTAJE")),
                () -> assertTrue(registro.existePrototipo("BASE_2X1")),
                () -> assertTrue(registro.existePrototipo("BASE_COMBO")),
                () -> assertEquals(3, registro.cantidadPrototipos())
            );
        }

        @Test
        @DisplayName("Registro permite obtener prototipo clonado")
        void testObtenerPrototipoClonado() {
            // Arrange
            RegistroPromocion registro = new RegistroPromocion();

            // Act
            Promocion promo1 = registro.obtenerPrototipo("BASE_PORCENTAJE");
            Promocion promo2 = registro.obtenerPrototipo("BASE_PORCENTAJE");

            // Assert
            assertAll("Verificar clonación desde registro",
                () -> assertNotNull(promo1),
                () -> assertNotNull(promo2),
                () -> assertNotSame(promo1, promo2, "Debe retornar clones independientes")
            );
        }

        @Test
        @DisplayName("Registro permite registrar prototipo personalizado")
        void testRegistrarPrototipoPersonalizado() {
            // Arrange
            RegistroPromocion registro = new RegistroPromocion();
            PromocionPorcentaje custom = new PromocionPorcentaje(
                "CUSTOM-001", "Custom", "Desc"
            );

            // Act
            registro.registrarPrototipo("MI_PROTOTIPO", custom);

            // Assert
            assertAll("Verificar registro personalizado",
                () -> assertTrue(registro.existePrototipo("MI_PROTOTIPO")),
                () -> assertEquals(4, registro.cantidadPrototipos())
            );
        }

        @Test
        @DisplayName("Registro retorna null para prototipo inexistente")
        void testPrototipoInexistente() {
            // Arrange
            RegistroPromocion registro = new RegistroPromocion();

            // Act
            Promocion promo = registro.obtenerPrototipo("NO_EXISTE");

            // Assert
            assertNull(promo, "Debe retornar null para prototipo inexistente");
        }

        @Test
        @DisplayName("Registro permite eliminar prototipos")
        void testEliminarPrototipo() {
            // Arrange
            RegistroPromocion registro = new RegistroPromocion();
            int cantidadInicial = registro.cantidadPrototipos();

            // Act
            registro.eliminarPrototipo("BASE_PORCENTAJE");

            // Assert
            assertAll("Verificar eliminación",
                () -> assertFalse(registro.existePrototipo("BASE_PORCENTAJE")),
                () -> assertEquals(cantidadInicial - 1, registro.cantidadPrototipos())
            );
        }

        @Test
        @DisplayName("Registro rechaza clave nula")
        void testRegistroRechazaClaveNula() {
            // Arrange
            RegistroPromocion registro = new RegistroPromocion();
            Promocion promo = new PromocionPorcentaje("ID", "Test", "Desc");

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> registro.registrarPrototipo(null, promo),
                "Debe rechazar clave nula");
        }

        @Test
        @DisplayName("Registro rechaza prototipo nulo")
        void testRegistroRechazaPrototipoNulo() {
            // Arrange
            RegistroPromocion registro = new RegistroPromocion();

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> registro.registrarPrototipo("CLAVE", null),
                "Debe rechazar prototipo nulo");
        }
    }

    @Nested
    @DisplayName("Tests de equals, hashCode y toString")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Promociones con mismo ID son iguales")
        void testPromocionesConMismoIDSonIguales() {
            // Arrange
            Promocion p1 = new PromocionPorcentaje("PROMO-001", "Nombre1", "Desc1");
            Promocion p2 = new PromocionPorcentaje("PROMO-001", "Nombre2", "Desc2");

            // Act & Assert
            assertEquals(p1, p2, "Promociones con mismo ID deben ser iguales");
            assertEquals(p1.hashCode(), p2.hashCode(), "HashCode debe ser igual");
        }

        @Test
        @DisplayName("Promociones con diferente ID son diferentes")
        void testPromocionesConDiferenteID() {
            // Arrange
            Promocion p1 = new PromocionPorcentaje("PROMO-001", "Test", "Desc");
            Promocion p2 = new PromocionPorcentaje("PROMO-002", "Test", "Desc");

            // Act & Assert
            assertNotEquals(p1, p2, "Promociones con diferente ID deben ser diferentes");
        }

        @Test
        @DisplayName("toString contiene información relevante")
        void testToString() {
            // Arrange
            Promocion promo = new PromocionPorcentaje(
                "PROMO-001", "Descuento Verano", "Desc"
            );
            promo.setPorcentajeDescuento(15.0);
            promo.setActiva(true);

            // Act
            String toString = promo.toString();

            // Assert
            assertAll("Verificar contenido de toString",
                () -> assertTrue(toString.contains("PROMO-001")),
                () -> assertTrue(toString.contains("Descuento Verano")),
                () -> assertTrue(toString.contains("15"))
            );
        }

        @Test
        @DisplayName("obtenerTipo retorna tipo correcto")
        void testObtenerTipo() {
            // Arrange
            Promocion porcentaje = new PromocionPorcentaje("P1", "Test", "Desc");
            Promocion dosXUno = new PromocionDosXUno("P2", "Test", "Desc");
            Promocion combo = new PromocionCombo("P3", "Test", "Desc");

            // Act & Assert
            assertAll("Verificar tipos",
                () -> assertEquals("PORCENTAJE", porcentaje.obtenerTipo()),
                () -> assertEquals("2X1", dosXUno.obtenerTipo()),
                () -> assertEquals("COMBO", combo.obtenerTipo())
            );
        }

        @Test
        @DisplayName("PromocionDosXUno tipo refleja configuración")
        void testTipo3x2() {
            // Arrange
            PromocionDosXUno promo = new PromocionDosXUno("P1", "Test", "Desc");
            promo.setCantidadCompra(3);
            promo.setCantidadPaga(2);

            // Act & Assert
            assertEquals("3X2", promo.obtenerTipo());
        }
    }

    @Nested
    @DisplayName("PromocionCombo - operaciones específicas")
    class PromocionComboTests {

        @Test
        @DisplayName("Combo agrega productos correctamente")
        void testComboAgregaProductos() {
            // Arrange
            PromocionCombo combo = new PromocionCombo("COMBO-001", "Test", "Desc");

            // Act
            combo.agregarProducto("PROD-001");
            combo.agregarProducto("PROD-002");
            combo.agregarProducto("PROD-003");

            // Assert
            assertEquals(3, combo.getProductosRequeridos().size());
        }

        @Test
        @DisplayName("Combo no duplica productos")
        void testComboNoDuplicaProductos() {
            // Arrange
            PromocionCombo combo = new PromocionCombo("COMBO-001", "Test", "Desc");

            // Act
            combo.agregarProducto("PROD-001");
            combo.agregarProducto("PROD-001");
            combo.agregarProducto("PROD-001");

            // Assert
            assertEquals(1, combo.getProductosRequeridos().size());
        }

        @Test
        @DisplayName("Combo verifica si contiene producto")
        void testComboContieneProducto() {
            // Arrange
            PromocionCombo combo = new PromocionCombo("COMBO-001", "Test", "Desc");
            combo.agregarProducto("PROD-001");

            // Act & Assert
            assertAll("Verificar contiene producto",
                () -> assertTrue(combo.contieneProducto("PROD-001")),
                () -> assertFalse(combo.contieneProducto("PROD-002"))
            );
        }
    }
}
