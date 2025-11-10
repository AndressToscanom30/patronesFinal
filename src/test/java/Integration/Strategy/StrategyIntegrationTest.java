package Integration.Strategy;

import Comportamentales.Strategy.modelo.*;
import Comportamentales.Strategy.controlador.StrategyController;
import Comportamentales.Strategy.vista.ConsolaStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Strategy Pattern - Integration Tests")
public class StrategyIntegrationTest {
    
    // ==================== Integración entre Estrategias y Carrito ====================
    
    @Test
    @DisplayName("Integración - Múltiples estrategias en un carrito")
    public void testMultiplesEstrategias() {
        CarritoCompra carrito = new CarritoCompra(1000);
        
        // Probar con descuento porcentaje 10%
        DescuentoStrategy desc1 = new DescuentoPorcentaje(10);
        carrito.setEstrategia(desc1);
        assertEquals(900, carrito.calcularTotal(), 0.01);
        
        // Cambiar a descuento 2x1
        DescuentoStrategy desc2 = new Descuento2x1();
        carrito.setEstrategia(desc2);
        assertEquals(500, carrito.calcularTotal(), 0.01);
        
        // Cambiar a descuento 3x2
        DescuentoStrategy desc3 = new Descuento3x2(3, 33.33);
        carrito.setEstrategia(desc3);
        assertEquals(666.7, carrito.calcularTotal(), 0.1);
    }
    
    @Test
    @DisplayName("Integración - Comparación de estrategias")
    public void testComparacionEstrategias() {
        double totalBase = 1000;
        
        // Sin descuento
        CarritoCompra carrito1 = new CarritoCompra(totalBase);
        double total1 = carrito1.calcularTotal();
        
        // Con 10%
        CarritoCompra carrito2 = new CarritoCompra(totalBase);
        carrito2.setEstrategia(new DescuentoPorcentaje(10));
        double total2 = carrito2.calcularTotal();
        
        // Con 2x1
        CarritoCompra carrito3 = new CarritoCompra(totalBase);
        carrito3.setEstrategia(new Descuento2x1());
        double total3 = carrito3.calcularTotal();
        
        // Verificar orden de precios
        assertTrue(total3 < total2);  // 2x1 es más barato que 10%
        assertTrue(total2 < total1);  // 10% es más barato que sin descuento
    }
    
    // ==================== MVC Integration Tests ====================
    
    @Test
    @DisplayName("MVC - Controller crea carrito")
    public void testControllerCreaCarrito() {
        ConsolaStrategy vista = new ConsolaStrategy();
        StrategyController controller = new StrategyController(vista);
        
        CarritoCompra carrito = controller.crearCarrito(500);
        assertNotNull(carrito);
        assertEquals(500, carrito.getTotal(), 0.01);
    }
    
    @Test
    @DisplayName("MVC - Controller crea estrategias")
    public void testControllerCreaEstrategias() {
        ConsolaStrategy vista = new ConsolaStrategy();
        StrategyController controller = new StrategyController(vista);
        
        // Crear descuento porcentaje
        DescuentoStrategy desc1 = controller.crearDescuentoPorcentaje(15);
        assertNotNull(desc1);
        assertTrue(desc1 instanceof DescuentoPorcentaje);
        
        // Crear descuento 2x1
        DescuentoStrategy desc2 = controller.crearDescuento2x1();
        assertNotNull(desc2);
        assertTrue(desc2 instanceof Descuento2x1);
        
        // Crear descuento 3x2
        DescuentoStrategy desc3 = controller.crearDescuento3x2(3, 30);
        assertNotNull(desc3);
        assertTrue(desc3 instanceof Descuento3x2);
    }
    
    @Test
    @DisplayName("MVC - Controller aplica estrategia")
    public void testControllerAplicaEstrategia() {
        ConsolaStrategy vista = new ConsolaStrategy();
        StrategyController controller = new StrategyController(vista);
        
        CarritoCompra carrito = controller.crearCarrito(1000);
        DescuentoStrategy estrategia = controller.crearDescuentoPorcentaje(20);
        
        controller.aplicarEstrategia(carrito, estrategia);
        
        assertEquals(800, controller.calcularTotalConDescuento(carrito), 0.01);
    }
    
    @Test
    @DisplayName("MVC - Controller calcula total")
    public void testControllerCalculaTotal() {
        ConsolaStrategy vista = new ConsolaStrategy();
        StrategyController controller = new StrategyController(vista);
        
        CarritoCompra carrito = controller.crearCarrito(1000);
        carrito.setEstrategia(new DescuentoPorcentaje(10));
        
        double total = controller.calcularTotalConDescuento(carrito);
        assertEquals(900, total, 0.01);
    }
    
    // ==================== Escenarios de Negocio ====================
    
    @Test
    @DisplayName("Integración - Cliente VIP con 25% descuento")
    public void testClienteVIP() {
        CarritoCompra carrito = new CarritoCompra(5000);
        DescuentoStrategy descuentoVIP = new DescuentoPorcentaje(25);
        carrito.setEstrategia(descuentoVIP);
        
        assertEquals(3750, carrito.calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("Integración - Promoción Black Friday 50%")
    public void testBlackFriday() {
        CarritoCompra carrito = new CarritoCompra(10000);
        DescuentoStrategy blackFriday = new DescuentoPorcentaje(50);
        carrito.setEstrategia(blackFriday);
        
        assertEquals(5000, carrito.calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("Integración - Promoción 3x2 en productos seleccionados")
    public void testPromocion3x2() {
        CarritoCompra carrito = new CarritoCompra(3000);
        DescuentoStrategy promo3x2 = new Descuento3x2(3, 33.33);
        carrito.setEstrategia(promo3x2);
        
        assertEquals(2000, carrito.calcularTotal(), 1);
    }
    
    @Test
    @DisplayName("Integración - Cambio dinámico de estrategia en runtime")
    public void testCambioDinamicoEstrategia() {
        CarritoCompra carrito = new CarritoCompra(1000);
        
        // Inicialmente sin descuento
        assertEquals(1000, carrito.calcularTotal(), 0.01);
        
        // Cliente agrega cupón de 15%
        carrito.setEstrategia(new DescuentoPorcentaje(15));
        assertEquals(850, carrito.calcularTotal(), 0.01);
        
        // Cliente cambia a promoción 2x1
        carrito.setEstrategia(new Descuento2x1());
        assertEquals(500, carrito.calcularTotal(), 0.01);
        
        // Cliente decide no usar descuento
        carrito.setEstrategia(null);
        assertEquals(1000, carrito.calcularTotal(), 0.01);
    }
}