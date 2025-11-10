package unit.Decorator;

import Estructurales.Decorator.modelo.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DisplayName("Decorator Pattern - Tests Unitarios")
class DecoratorUnitTest {

    @Nested
    @DisplayName("ProductoBase")
    class ProductoBaseTests {

        @Test
        @DisplayName("ProductoBase se crea correctamente")
        void testCrearProductoBase() {
            ProductoBase producto = new ProductoBase("PROD-001", "Laptop", "Laptop Dell", 1500000, 2.5);

            assertAll("Verificar creación",
                () -> assertEquals("PROD-001", producto.getCodigo()),
                () -> assertEquals("Laptop", producto.getNombre()),
                () -> assertEquals(1500000, producto.getPrecioBase()),
                () -> assertEquals(2.5, producto.obtenerPeso())
            );
        }

        @Test
        @DisplayName("ProductoBase implementa interface correctamente")
        void testImplementaInterface() {
            ProductoBase producto = new ProductoBase("PROD-001", "Mouse", "Mouse Logitech", 80000, 0.2);

            assertAll("Verificar interface",
                () -> assertEquals(80000, producto.obtenerPrecio()),
                () -> assertEquals("Mouse Logitech", producto.obtenerDescripcion()),
                () -> assertEquals(0.2, producto.obtenerPeso()),
                () -> assertFalse(producto.requiereRefrigeracion()),
                () -> assertEquals(3, producto.obtenerTiempoEntrega()),
                () -> assertFalse(producto.obtenerDetalles().isEmpty())
            );
        }

        @Test
        @DisplayName("Detalles contiene información correcta")
        void testDetalles() {
            ProductoBase producto = new ProductoBase("PROD-001", "Teclado", "Teclado RGB", 250000, 1.0);
            List<String> detalles = producto.obtenerDetalles();

            assertTrue(detalles.stream().anyMatch(d -> d.contains("PROD-001")));
            assertTrue(detalles.stream().anyMatch(d -> d.contains("Teclado")));
            assertTrue(detalles.stream().anyMatch(d -> d.contains("250000")));
        }
    }

    @Nested
    @DisplayName("EmpaqueRegalo")
    class EmpaqueRegaloTests {

        @Test
        @DisplayName("Empaque básico agrega costo correcto")
        void testEmpaqueBasico() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            EmpaqueRegalo empaque = new EmpaqueRegalo(base, "BASICO", false);

            assertAll("Verificar empaque básico",
                () -> assertEquals(103000, empaque.obtenerPrecio(), "100000 + 3000"),
                () -> assertEquals(3000, empaque.getCostoEmpaque()),
                () -> assertEquals("BASICO", empaque.getTipoEmpaque()),
                () -> assertEquals("No", empaque.getIncluyeTarjeta())
            );
        }

        @Test
        @DisplayName("Empaque premium con tarjeta")
        void testEmpaquePremiumConTarjeta() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            EmpaqueRegalo empaque = new EmpaqueRegalo(base, "PREMIUM", true);

            assertAll("Verificar empaque premium",
                () -> assertEquals(110000, empaque.obtenerPrecio(), "100000 + 8000 + 2000"),
                () -> assertEquals(10000, empaque.getCostoEmpaque()),
                () -> assertEquals("Sí", empaque.getIncluyeTarjeta())
            );
        }

        @Test
        @DisplayName("Empaque lujo agrega peso")
        void testEmpaqueAgregaPeso() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            EmpaqueRegalo empaque = new EmpaqueRegalo(base, "LUJO", false);

            assertEquals(1.2, empaque.obtenerPeso(), 0.01, "1.0 + 0.2");
        }

        @Test
        @DisplayName("Empaque modifica descripción")
        void testEmpaqueDescripcion() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Producto Test", 100000, 1.0);
            EmpaqueRegalo empaque = new EmpaqueRegalo(base, "PREMIUM", true);

            assertTrue(empaque.obtenerDescripcion().contains("Empaque Regalo PREMIUM"));
        }

        @Test
        @DisplayName("Detalles incluyen información de empaque")
        void testDetallesEmpaque() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            EmpaqueRegalo empaque = new EmpaqueRegalo(base, "LUJO", true);
            
            List<String> detalles = empaque.obtenerDetalles();
            assertTrue(detalles.stream().anyMatch(d -> d.contains("Empaque Regalo")));
            assertTrue(detalles.stream().anyMatch(d -> d.contains("LUJO")));
        }
    }

    @Nested
    @DisplayName("GarantiaExtendida")
    class GarantiaExtendidaTests {

        @Test
        @DisplayName("Garantía 12 meses básica")
        void testGarantia12Meses() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            GarantiaExtendida garantia = new GarantiaExtendida(base, 12, "BASICA");

            assertAll("Verificar garantía 12 meses",
                () -> assertEquals(110000, garantia.obtenerPrecio(), "100000 + 10%"),
                () -> assertEquals(12, garantia.getMesesGarantia()),
                () -> assertEquals("BASICA", garantia.getCobertura())
            );
        }

        @Test
        @DisplayName("Garantía 24 meses completa")
        void testGarantia24MesesCompleta() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            GarantiaExtendida garantia = new GarantiaExtendida(base, 24, "COMPLETA");

            double costoEsperado = 100000 * 0.23;
            assertEquals(100000 + costoEsperado, garantia.obtenerPrecio(), 1.0, "18% + 5% completa");
        }

        @Test
        @DisplayName("Garantía 36 meses")
        void testGarantia36Meses() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            GarantiaExtendida garantia = new GarantiaExtendida(base, 36, "BASICA");

            assertEquals(125000, garantia.obtenerPrecio(), "100000 + 25%");
        }

        @Test
        @DisplayName("Validar aplicabilidad - producto caro")
        void testValidarAplicabilidadProductoCaro() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            GarantiaExtendida garantia = new GarantiaExtendida(base, 12, "BASICA");

            assertTrue(garantia.validarAplicabilidad());
        }

        @Test
        @DisplayName("Validar aplicabilidad - producto barato")
        void testValidarAplicabilidadProductoBarato() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 30000, 1.0);
            GarantiaExtendida garantia = new GarantiaExtendida(base, 12, "BASICA");

            assertFalse(garantia.validarAplicabilidad());
        }

        @Test
        @DisplayName("Descripción incluye meses de garantía")
        void testDescripcionGarantia() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test Producto", 100000, 1.0);
            GarantiaExtendida garantia = new GarantiaExtendida(base, 24, "BASICA");

            assertTrue(garantia.obtenerDescripcion().contains("24 meses"));
        }
    }

    @Nested
    @DisplayName("Personalizacion")
    class PersonalizacionTests {

        @Test
        @DisplayName("Personalización grabado básico")
        void testPersonalizacionGrabado() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            Personalizacion perso = new Personalizacion(base, "Juan Pérez", "GRABADO", "FRENTE");

            assertAll("Verificar personalización",
                () -> assertEquals(105000, perso.obtenerPrecio(), "100000 + 5000"),
                () -> assertEquals("Juan Pérez", perso.getTextoPersonalizado()),
                () -> assertEquals("GRABADO", perso.getTipoPersona()),
                () -> assertEquals("FRENTE", perso.getPosicion())
            );
        }

        @Test
        @DisplayName("Personalización bordado")
        void testPersonalizacionBordado() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            Personalizacion perso = new Personalizacion(base, "ABC Corp", "BORDADO", "ATRAS");

            assertEquals(108000, perso.obtenerPrecio(), "100000 + 8000");
        }

        @Test
        @DisplayName("Personalización texto largo")
        void testPersonalizacionTextoLargo() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            String textoLargo = "Este es un texto muy largo para personalización";
            Personalizacion perso = new Personalizacion(base, textoLargo, "IMPRESION", "LATERAL");

            assertEquals(105000, perso.obtenerPrecio(), "3000 + 2000 por texto largo");
        }

        @Test
        @DisplayName("Personalización aumenta tiempo de entrega")
        void testPersonalizacionTiempoEntrega() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            Personalizacion perso = new Personalizacion(base, "Test", "GRABADO", "FRENTE");

            assertEquals(5, perso.obtenerTiempoEntrega(), "3 días base + 2 días personalización");
        }

        @Test
        @DisplayName("Validar texto correcto")
        void testValidarTextoValido() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            Personalizacion perso = new Personalizacion(base, "Juan 123", "GRABADO", "FRENTE");

            assertTrue(perso.validarTexto());
        }

        @Test
        @DisplayName("Validar texto con caracteres especiales")
        void testValidarTextoConEspeciales() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            Personalizacion perso = new Personalizacion(base, "María José!", "GRABADO", "FRENTE");

            assertTrue(perso.validarTexto());
        }

        @Test
        @DisplayName("Rechazar texto vacío")
        void testRechazarTextoVacio() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            Personalizacion perso = new Personalizacion(base, "", "GRABADO", "FRENTE");

            assertFalse(perso.validarTexto());
        }

        @Test
        @DisplayName("Rechazar texto muy largo")
        void testRechazarTextoMuyLargo() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            String textoMuyLargo = "A".repeat(51);
            Personalizacion perso = new Personalizacion(base, textoMuyLargo, "GRABADO", "FRENTE");

            assertFalse(perso.validarTexto());
        }
    }

    @Nested
    @DisplayName("Decoradores múltiples")
    class DecoradoresMultiplesTests {

        @Test
        @DisplayName("Empaque + Garantía")
        void testEmpaqueYGarantia() {
            ProductoBase base = new ProductoBase("PROD-001", "Laptop", "Laptop Dell", 1500000, 2.5);
            ProductoVendible conEmpaque = new EmpaqueRegalo(base, "PREMIUM", true);
            ProductoVendible conGarantia = new GarantiaExtendida(conEmpaque, 12, "BASICA");

            double precioEsperado = 1500000 + 10000 + (1500000 * 0.10);
            assertEquals(precioEsperado, conGarantia.obtenerPrecio(), 1.0);
        }

        @Test
        @DisplayName("Empaque + Personalización")
        void testEmpaqueYPersonalizacion() {
            ProductoBase base = new ProductoBase("PROD-001", "Mouse", "Mouse", 80000, 0.2);
            ProductoVendible conEmpaque = new EmpaqueRegalo(base, "BASICO", false);
            ProductoVendible personalizado = new Personalizacion(conEmpaque, "Oficina", "IMPRESION", "ATRAS");

            double precioEsperado = 80000 + 3000 + 3000;
            assertEquals(precioEsperado, personalizado.obtenerPrecio());
        }

        @Test
        @DisplayName("Garantía + Personalización")
        void testGarantiaYPersonalizacion() {
            ProductoBase base = new ProductoBase("PROD-001", "Teclado", "Teclado", 250000, 1.0);
            ProductoVendible conGarantia = new GarantiaExtendida(base, 24, "COMPLETA");
            ProductoVendible personalizado = new Personalizacion(conGarantia, "Gaming Setup", "GRABADO", "LATERAL");

            assertTrue(personalizado.obtenerPrecio() > 250000);
        }

        @Test
        @DisplayName("Todos los decoradores")
        void testTodosLosDecoradores() {
            ProductoBase base = new ProductoBase("PROD-001", "Monitor", "Monitor LG", 600000, 4.5);
            ProductoVendible paso1 = new EmpaqueRegalo(base, "LUJO", true);
            ProductoVendible paso2 = new GarantiaExtendida(paso1, 36, "COMPLETA");
            ProductoVendible final_ = new Personalizacion(paso2, "Oficina Central", "GRABADO", "FRENTE");

            assertAll("Verificar todos los decoradores",
                () -> assertTrue(final_.obtenerPrecio() > 600000),
                () -> assertTrue(final_.obtenerDescripcion().contains("Empaque")),
                () -> assertTrue(final_.obtenerDescripcion().contains("Garantía")),
                () -> assertTrue(final_.obtenerDescripcion().contains("Personalización")),
                () -> assertEquals(10, final_.obtenerTiempoEntrega(), "3 base + 3 empaque + 2 garantía + 2 personalización")
            );
        }

        @Test
        @DisplayName("Decoradores acumulan peso")
        void testDecoradoresAcumulanPeso() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            ProductoVendible conEmpaque = new EmpaqueRegalo(base, "BASICO", false);

            assertEquals(1.2, conEmpaque.obtenerPeso(), 0.01);
        }

        @Test
        @DisplayName("Detalles incluyen todos los decoradores")
        void testDetallesCompletos() {
            ProductoBase base = new ProductoBase("PROD-001", "Test", "Test", 100000, 1.0);
            ProductoVendible paso1 = new EmpaqueRegalo(base, "PREMIUM", true);
            ProductoVendible final_ = new GarantiaExtendida(paso1, 12, "BASICA");

            List<String> detalles = final_.obtenerDetalles();
            assertTrue(detalles.stream().anyMatch(d -> d.contains("Empaque")));
            assertTrue(detalles.stream().anyMatch(d -> d.contains("Garantía")));
        }
    }
}
