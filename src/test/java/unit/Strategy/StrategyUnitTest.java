package unit.Strategy;

import Comportamentales.Strategy.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Strategy Pattern - Unit Tests")
public class StrategyUnitTest {
    
    // ==================== DescuentoPorcentaje Tests ====================
    
    @Test
    @DisplayName("DescuentoPorcentaje - Crear descuento")
    public void testCrearDescuentoPorcentaje() {
        // Crear descuento del 10%
        DescuentoPorcentaje descuento = new DescuentoPorcentaje(10);
        assertEquals(10, descuento.getPorcentaje(), 0.01);
    }
    
    @Test
    @DisplayName("DescuentoPorcentaje - Aplicar descuento 10%")
    public void testAplicarDescuento10Porciento() {
        // 10% de descuento sobre 1000 = 900
        DescuentoPorcentaje descuento = new DescuentoPorcentaje(10);
        double resultado = descuento.aplicarDescuento(1000);
        assertEquals(900, resultado, 0.01);
    }
    
    @Test
    @DisplayName("DescuentoPorcentaje - Aplicar descuento 25%")
    public void testAplicarDescuento25Porciento() {
        // 25% de descuento sobre 1000 = 750
        DescuentoPorcentaje descuento = new DescuentoPorcentaje(25);
        double resultado = descuento.aplicarDescuento(1000);
        assertEquals(750, resultado, 0.01);
    }
    
    @Test
    @DisplayName("DescuentoPorcentaje - Aplicar descuento 50%")
    public void testAplicarDescuento50Porciento() {
        // 50% de descuento sobre 1000 = 500
        DescuentoPorcentaje descuento = new DescuentoPorcentaje(50);
        double resultado = descuento.aplicarDescuento(1000);
        assertEquals(500, resultado, 0.01);
    }
    
    @Test
    @DisplayName("DescuentoPorcentaje - Modificar porcentaje")
    public void testModificarPorcentaje() {
        DescuentoPorcentaje descuento = new DescuentoPorcentaje(10);
        descuento.setPorcentaje(20);
        assertEquals(20, descuento.getPorcentaje(), 0.01);
    }
    
    // ==================== Descuento2x1 Tests ====================
    
    @Test
    @DisplayName("Descuento2x1 - Crear descuento")
    public void testCrearDescuento2x1() {
        Descuento2x1 descuento = new Descuento2x1();
        assertNotNull(descuento);
    }
    
    @Test
    @DisplayName("Descuento2x1 - Aplicar descuento")
    public void testAplicarDescuento2x1() {
        // 2x1 = 50% de descuento
        Descuento2x1 descuento = new Descuento2x1();
        double resultado = descuento.aplicarDescuento(1000);
        assertEquals(500, resultado, 0.01);
    }
    
    @Test
    @DisplayName("Descuento2x1 - Aplicar con diferentes montos")
    public void testAplicarDescuento2x1Varios() {
        Descuento2x1 descuento = new Descuento2x1();
        
        assertEquals(250, descuento.aplicarDescuento(500), 0.01);
        assertEquals(500, descuento.aplicarDescuento(1000), 0.01);
        assertEquals(1000, descuento.aplicarDescuento(2000), 0.01);
    }
    
    // ==================== Descuento3x2 Tests ====================
    
    @Test
    @DisplayName("Descuento3x2 - Crear descuento")
    public void testCrearDescuento3x2() {
        Descuento3x2 descuento = new Descuento3x2(3, 33.33);
        assertEquals(3, descuento.getCantidadMin());
        assertEquals(33.33, descuento.getPorcentaje(), 0.01);
    }
    
    @Test
    @DisplayName("Descuento3x2 - Aplicar descuento")
    public void testAplicarDescuento3x2() {
        // 3x2 con 33.33% de descuento
        Descuento3x2 descuento = new Descuento3x2(3, 33.33);
        double resultado = descuento.aplicarDescuento(1000);
        assertEquals(666.7, resultado, 0.1);
    }
    
    @Test
    @DisplayName("Descuento3x2 - Modificar cantidad mínima")
    public void testModificarCantidadMinima() {
        Descuento3x2 descuento = new Descuento3x2(3, 33.33);
        descuento.setCantidadMin(5);
        assertEquals(5, descuento.getCantidadMin());
    }
    
    @Test
    @DisplayName("Descuento3x2 - Modificar porcentaje")
    public void testModificarPorcentaje3x2() {
        Descuento3x2 descuento = new Descuento3x2(3, 33.33);
        descuento.setPorcentaje(40);
        assertEquals(40, descuento.getPorcentaje(), 0.01);
    }
    
    // ==================== CarritoCompra Tests ====================
    
    @Test
    @DisplayName("CarritoCompra - Crear carrito")
    public void testCrearCarrito() {
        CarritoCompra carrito = new CarritoCompra(1000);
        assertEquals(1000, carrito.getTotal(), 0.01);
    }
    
    @Test
    @DisplayName("CarritoCompra - Calcular total sin descuento")
    public void testCalcularTotalSinDescuento() {
        CarritoCompra carrito = new CarritoCompra(1000);
        assertEquals(1000, carrito.calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("CarritoCompra - Cambiar estrategia")
    public void testCambiarEstrategia() {
        CarritoCompra carrito = new CarritoCompra(1000);
        
        // Sin descuento
        assertEquals(1000, carrito.calcularTotal(), 0.01);
        
        // Con descuento 10%
        DescuentoStrategy desc10 = new DescuentoPorcentaje(10);
        carrito.setEstrategia(desc10);
        assertEquals(900, carrito.calcularTotal(), 0.01);
        
        // Cambiar a descuento 20%
        DescuentoStrategy desc20 = new DescuentoPorcentaje(20);
        carrito.setEstrategia(desc20);
        assertEquals(800, carrito.calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("CarritoCompra - Aplicar descuento porcentaje")
    public void testAplicarDescuentoPorcentaje() {
        CarritoCompra carrito = new CarritoCompra(1000);
        DescuentoStrategy descuento = new DescuentoPorcentaje(15);
        carrito.setEstrategia(descuento);
        
        assertEquals(850, carrito.calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("CarritoCompra - Aplicar descuento 2x1")
    public void testAplicarDescuento2x1EnCarrito() {
        CarritoCompra carrito = new CarritoCompra(1000);
        DescuentoStrategy descuento = new Descuento2x1();
        carrito.setEstrategia(descuento);
        
        assertEquals(500, carrito.calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("CarritoCompra - Aplicar descuento 3x2")
    public void testAplicarDescuento3x2EnCarrito() {
        CarritoCompra carrito = new CarritoCompra(1000);
        DescuentoStrategy descuento = new Descuento3x2(3, 30);
        carrito.setEstrategia(descuento);
        
        assertEquals(700, carrito.calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("CarritoCompra - Modificar total")
    public void testModificarTotal() {
        CarritoCompra carrito = new CarritoCompra(1000);
        carrito.setTotal(2000);
        assertEquals(2000, carrito.getTotal(), 0.01);
    }
    
    @Test
    @DisplayName("CarritoCompra - Obtener estrategia")
    public void testObtenerEstrategia() {
        CarritoCompra carrito = new CarritoCompra(1000);
        assertNull(carrito.getEstrategia());
        
        DescuentoStrategy descuento = new DescuentoPorcentaje(10);
        carrito.setEstrategia(descuento);
        assertNotNull(carrito.getEstrategia());
    }
}