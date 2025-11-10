package unit.FactoryMethod;

import Creacionales.FactoryMethod.modelo.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Factory Method Pattern - Tests Unitarios")
@ExtendWith(MockitoExtension.class)
class FactoryMethodUnitTest {

    @Nested
    @DisplayName("Comportamiento del patrón")
    class PatternBehaviorTests {

        @Test
        @DisplayName("ProductoSimpleFactory crea productos simples correctamente")
        void testProductoSimpleFactoryCreation() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();

            // Act
            Producto producto = factory.crearProducto("P001", "Arroz", 2500.0, "GRANOS");

            // Assert
            assertAll("Verificar producto simple creado",
                    () -> assertNotNull(producto, "El producto no debe ser nulo"),
                    () -> assertEquals("P001", producto.getCodigo(), "Código incorrecto"),
                    () -> assertEquals("Arroz", producto.getNombre(), "Nombre incorrecto"),
                    () -> assertEquals(2500.0, producto.getPrecio(), "Precio incorrecto"),
                    () -> assertEquals("SIMPLE", producto.obtenerTipo(), "Tipo incorrecto"),
                    () -> assertTrue(producto instanceof ProductoSimple, "Debe ser ProductoSimple")
            );
        }

        @Test
        @DisplayName("ProductoPesoFactory crea productos por peso correctamente")
        void testProductoPesoFactoryCreation() {
            // Arrange
            ProductoFactory factory = new ProductoPesoFactory();

            // Act
            Producto producto = factory.crearProducto("C001", "Carne molida", 18000.0, "CARNES");

            // Assert
            assertAll("Verificar producto por peso creado",
                    () -> assertNotNull(producto, "El producto no debe ser nulo"),
                    () -> assertEquals("C001", producto.getCodigo(), "Código incorrecto"),
                    () -> assertEquals("PESO", producto.obtenerTipo(), "Tipo incorrecto"),
                    () -> assertTrue(producto instanceof ProductoPeso, "Debe ser ProductoPeso"),
                    () -> assertTrue(producto.requiereRefrigeracion(), "Carnes requieren refrigeración")
            );
        }

        @Test
        @DisplayName("Productos simples calculan precio final correctamente")
        void testProductoSimplePrecioFinal() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();
            Producto producto = factory.crearProducto("P002", "Azúcar", 3000.0, "GRANOS");

            // Act
            double precioFinal = producto.calcularPrecioFinal();

            // Assert
            assertEquals(3000.0, precioFinal, 0.01,
                    "El precio final debe ser igual al precio base en productos simples");
        }

        @Test
        @DisplayName("Productos por peso calculan precio final según peso")
        void testProductoPesoPrecioFinal() {
            // Arrange
            ProductoFactory factory = new ProductoPesoFactory();
            Producto producto = factory.crearProducto("C002", "Pollo", 15000.0, "CARNES");
            ProductoPeso productoPeso = (ProductoPeso) producto;
            productoPeso.setPesoPromedio(2.5);

            // Act
            double precioFinal = producto.calcularPrecioFinal();

            // Assert
            assertEquals(37500.0, precioFinal, 0.01,
                    "El precio final debe ser precio por kilo × peso");
        }

        @Test
        @DisplayName("Factory devuelve el tipo correcto de producto")
        void testFactoryTipoProducto() {
            // Arrange & Act & Assert
            ProductoFactory simpleFactory = new ProductoSimpleFactory();
            ProductoFactory pesoFactory = new ProductoPesoFactory();

            assertAll("Verificar tipos de factories",
                    () -> assertEquals("SIMPLE", simpleFactory.getTipoProducto()),
                    () -> assertEquals("PESO", pesoFactory.getTipoProducto())
            );
        }
    }

    @Nested
    @DisplayName("Casos límite y validaciones")
    class EdgeCasesTests {

        @Test
        @DisplayName("Producto con código nulo lanza excepción")
        void testProductoCodigoNulo() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> factory.crearProducto(null, "Producto", 1000.0, "CATEGORIA"),
                    "Debe lanzar excepción con código nulo");
        }

        @Test
        @DisplayName("Producto con código vacío lanza excepción")
        void testProductoCodigoVacio() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> factory.crearProducto("", "Producto", 1000.0, "CATEGORIA"),
                    "Debe lanzar excepción con código vacío");
        }

        @Test
        @DisplayName("Producto con nombre nulo lanza excepción")
        void testProductoNombreNulo() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> factory.crearProducto("P001", null, 1000.0, "CATEGORIA"),
                    "Debe lanzar excepción con nombre nulo");
        }

        @Test
        @DisplayName("Producto con precio negativo lanza excepción")
        void testProductoPrecioNegativo() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> factory.crearProducto("P001", "Producto", -100.0, "CATEGORIA"),
                    "Debe lanzar excepción con precio negativo");
        }

        @Test
        @DisplayName("ProductoSimple con stock negativo lanza excepción")
        void testProductoSimpleStockNegativo() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();
            Producto producto = factory.crearProducto("P001", "Producto", 1000.0, "CATEGORIA");
            ProductoSimple productoSimple = (ProductoSimple) producto;

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> productoSimple.setStock(-10),
                    "Debe lanzar excepción con stock negativo");
        }

        @Test
        @DisplayName("ProductoPeso con peso cero o negativo lanza excepción")
        void testProductoPesoPesoInvalido() {
            // Arrange
            ProductoFactory factory = new ProductoPesoFactory();
            Producto producto = factory.crearProducto("C001", "Carne", 15000.0, "CARNES");
            ProductoPeso productoPeso = (ProductoPeso) producto;

            // Act & Assert
            assertAll("Validar pesos inválidos",
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> productoPeso.setPesoPromedio(0),
                            "Peso cero debe lanzar excepción"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> productoPeso.setPesoPromedio(-1.5),
                            "Peso negativo debe lanzar excepción")
            );
        }

        @Test
        @DisplayName("Modificar precio a negativo lanza excepción")
        void testModificarPrecioNegativo() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();
            Producto producto = factory.crearProducto("P001", "Producto", 1000.0, "CATEGORIA");

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> producto.setPrecio(-500.0),
                    "Modificar precio a negativo debe lanzar excepción");
        }
    }

    @Nested
    @DisplayName("Tests de equals, hashCode y toString")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Productos con mismo código son iguales")
        void testProductosConMismoCodigoSonIguales() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();
            Producto p1 = factory.crearProducto("P001", "Producto A", 1000.0, "CAT1");
            Producto p2 = factory.crearProducto("P001", "Producto B", 2000.0, "CAT2");

            // Act & Assert
            assertEquals(p1, p2, "Productos con mismo código deben ser iguales");
            assertEquals(p1.hashCode(), p2.hashCode(), "HashCode debe ser igual");
        }

        @Test
        @DisplayName("Productos con diferente código son diferentes")
        void testProductosConDiferenteCodigoSonDiferentes() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();
            Producto p1 = factory.crearProducto("P001", "Producto", 1000.0, "CATEGORIA");
            Producto p2 = factory.crearProducto("P002", "Producto", 1000.0, "CATEGORIA");

            // Act & Assert
            assertNotEquals(p1, p2, "Productos con diferente código deben ser diferentes");
        }

        @Test
        @DisplayName("toString contiene información relevante")
        void testToStringContieneInformacion() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();
            Producto producto = factory.crearProducto("P001", "Arroz", 2500.0, "GRANOS");

            // Act
            String toString = producto.toString();

            // Assert
            assertAll("Verificar contenido de toString",
                    () -> assertTrue(toString.contains("P001"), "Debe contener código"),
                    () -> assertTrue(toString.contains("Arroz"), "Debe contener nombre"),
                    () -> assertTrue(toString.contains("2500"), "Debe contener precio")
            );
        }
    }

    @Nested
    @DisplayName("Tests de integración ligera")
    class IntegrationLightTests {

        @Test
        @DisplayName("Flujo completo: crear producto simple y calcular precio")
        void testFlujoCompletoProductoSimple() {
            // Arrange
            ProductoFactory factory = new ProductoSimpleFactory();

            // Act
            Producto producto = factory.crearProducto("P001", "Leche", 3500.0, "LACTEOS");
            ProductoSimple productoSimple = (ProductoSimple) producto;
            productoSimple.setStock(50);
            productoSimple.setRefrigeracion(true);
            double precioFinal = producto.calcularPrecioFinal();

            // Assert
            assertAll("Verificar flujo completo",
                    () -> assertEquals("P001", producto.getCodigo()),
                    () -> assertEquals(50, productoSimple.getStock()),
                    () -> assertTrue(productoSimple.requiereRefrigeracion()),
                    () -> assertEquals(3500.0, precioFinal, 0.01)
            );
        }

        @Test
        @DisplayName("Flujo completo: crear producto por peso y calcular precio")
        void testFlujoCompletoProductoPeso() {
            // Arrange
            ProductoFactory factory = new ProductoPesoFactory();

            // Act
            Producto producto = factory.crearProducto("C001", "Lomo", 25000.0, "CARNES");
            ProductoPeso productoPeso = (ProductoPeso) producto;
            productoPeso.setPesoPromedio(1.8);
            double precioFinal = producto.calcularPrecioFinal();

            // Assert
            assertAll("Verificar flujo completo producto por peso",
                    () -> assertEquals("C001", producto.getCodigo()),
                    () -> assertEquals(1.8, productoPeso.getPesoPromedio(), 0.01),
                    () -> assertTrue(productoPeso.isRequiereBascula()),
                    () -> assertTrue(producto.requiereRefrigeracion()),
                    () -> assertEquals(45000.0, precioFinal, 0.01)
            );
        }

        @Test
        @DisplayName("Diferentes factories crean productos del tipo correcto")
        void testDiferentesFactoriesCreanTiposCorrecto() {
            // Arrange
            ProductoFactory simpleFactory = new ProductoSimpleFactory();
            ProductoFactory pesoFactory = new ProductoPesoFactory();

            // Act
            Producto simple = simpleFactory.crearProducto("P001", "Pan", 2000.0, "PANADERIA");
            Producto peso = pesoFactory.crearProducto("F001", "Manzanas", 4500.0, "FRUTAS");

            // Assert
            assertAll("Verificar tipos correctos",
                    () -> assertInstanceOf(ProductoSimple.class, simple),
                    () -> assertInstanceOf(ProductoPeso.class, peso),
                    () -> assertEquals("SIMPLE", simple.obtenerTipo()),
                    () -> assertEquals("PESO", peso.obtenerTipo())
            );
        }
    }
}
