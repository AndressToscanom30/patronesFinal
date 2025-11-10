package Integration.Command;

import Comportamentales.Command.modelo.*;
import Comportamentales.Command.controlador.CommandController;
import Comportamentales.Command.vista.ConsolaCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Command Pattern - Integration Tests")
public class CommandIntegrationTest {
    
    private CommandController controller;
    private ConsolaCommand vista;
    private Producto producto1;
    private Producto producto2;
    private Producto producto3;
    
    @BeforeEach
    public void setUp() {
        vista = new ConsolaCommand();
        controller = new CommandController(vista);
        producto1 = new Producto("P001", "Leche", 3500, "Lácteos");
        producto2 = new Producto("P002", "Pan", 2500, "Panadería");
        producto3 = new Producto("P003", "Huevos", 8000, "Huevos");
    }
    
    // ==================== MVC Integration Tests ====================
    
    @Test
    @DisplayName("MVC - Controller agrega producto")
    public void testControllerAgregaProducto() {
        controller.agregarProducto(producto1, 2);
        
        assertEquals(1, controller.getCarrito().getCantidadItems());
        assertEquals(7000, controller.getCarrito().calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("MVC - Controller elimina producto")
    public void testControllerEliminaProducto() {
        controller.agregarProducto(producto1, 5);
        controller.eliminarProducto(producto1, 2);
        
        assertEquals(3, controller.getCarrito().getItems().get(0).getCantidad());
    }
    
    @Test
    @DisplayName("MVC - Controller aplica descuento")
    public void testControllerAplicaDescuento() {
        controller.agregarProducto(producto1, 2);
        controller.aplicarDescuento(10);
        
        assertEquals(10, controller.getCarrito().getDescuento(), 0.01);
        assertEquals(6300, controller.getCarrito().calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("MVC - Controller vacía carrito")
    public void testControllerVaciaCarrito() {
        controller.agregarProducto(producto1, 2);
        controller.agregarProducto(producto2, 1);
        controller.vaciarCarrito();
        
        assertTrue(controller.getCarrito().estaVacio());
    }
    
    @Test
    @DisplayName("MVC - Controller deshace operación")
    public void testControllerDeshace() {
        controller.agregarProducto(producto1, 2);
        assertTrue(controller.deshacer());
        
        assertTrue(controller.getCarrito().estaVacio());
    }
    
    @Test
    @DisplayName("MVC - Controller rehace operación")
    public void testControllerRehace() {
        controller.agregarProducto(producto1, 2);
        controller.deshacer();
        assertTrue(controller.rehacer());
        
        assertFalse(controller.getCarrito().estaVacio());
    }
    
    // ==================== Integración Comando-Carrito ====================
    
    @Test
    @DisplayName("Integración - Múltiples comandos en secuencia")
    public void testMultiplesComandosSecuencia() {
        // Agregar productos
        controller.agregarProducto(producto1, 2);
        controller.agregarProducto(producto2, 1);
        controller.agregarProducto(producto3, 1);
        
        assertEquals(3, controller.getCarrito().getCantidadItems());
        assertEquals(17500, controller.getCarrito().calcularSubtotal(), 0.01);
        
        // Aplicar descuento
        controller.aplicarDescuento(20);
        assertEquals(14000, controller.getCarrito().calcularTotal(), 0.01);
    }
    
    @Test
    @DisplayName("Integración - Deshacer múltiples operaciones")
    public void testDeshacerMultiplesOperaciones() {
        controller.agregarProducto(producto1, 2);
        controller.agregarProducto(producto2, 1);
        controller.agregarProducto(producto3, 1);
        
        // Deshacer última operación (huevos)
        controller.deshacer();
        assertEquals(2, controller.getCarrito().getCantidadItems());
        
        // Deshacer otra (pan)
        controller.deshacer();
        assertEquals(1, controller.getCarrito().getCantidadItems());
        
        // Deshacer otra (leche)
        controller.deshacer();
        assertTrue(controller.getCarrito().estaVacio());
    }
    
    @Test
    @DisplayName("Integración - Rehacer múltiples operaciones")
    public void testRehacerMultiplesOperaciones() {
        controller.agregarProducto(producto1, 2);
        controller.agregarProducto(producto2, 1);
        
        controller.deshacer();
        controller.deshacer();
        
        assertTrue(controller.getCarrito().estaVacio());
        
        // Rehacer
        controller.rehacer();
        assertEquals(1, controller.getCarrito().getCantidadItems());
        
        controller.rehacer();
        assertEquals(2, controller.getCarrito().getCantidadItems());
    }
    
    @Test
    @DisplayName("Integración - Vaciar y deshacer restaura productos")
    public void testVaciarYDeshacerRestaurar() {
        controller.agregarProducto(producto1, 2);
        controller.agregarProducto(producto2, 1);
        controller.agregarProducto(producto3, 1);
        
        int cantidadAntes = controller.getCarrito().getCantidadItems();
        
        controller.vaciarCarrito();
        assertTrue(controller.getCarrito().estaVacio());
        
        controller.deshacer();
        assertEquals(cantidadAntes, controller.getCarrito().getCantidadItems());
    }
    
    @Test
    @DisplayName("Integración - Historial refleja todas las operaciones")
    public void testHistorialReflejaOperaciones() {
        controller.agregarProducto(producto1, 2);
        controller.agregarProducto(producto2, 1);
        controller.aplicarDescuento(10);
        
        assertEquals(3, controller.obtenerHistorial().size());
    }
    
    // ==================== Escenarios Complejos ====================
    
    @Test
    @DisplayName("Integración - Flujo de compra completo con cambios")
    public void testFlujoCompletoConCambios() {
        // Cliente agrega productos
        controller.agregarProducto(producto1, 2);
        controller.agregarProducto(producto2, 3);
        controller.agregarProducto(producto3, 1);
        
        double total1 = controller.getCarrito().calcularTotal();
        
        // Cliente aplica descuento
        controller.aplicarDescuento(15);
        double total2 = controller.getCarrito().calcularTotal();
        
        assertTrue(total2 < total1);
        
        // Cliente se arrepiente del descuento
        controller.deshacer();
        assertEquals(total1, controller.getCarrito().calcularTotal(), 0.01);
        
        // Cliente quita un producto
        controller.eliminarProducto(producto2, 3);
        assertTrue(controller.getCarrito().calcularTotal() < total1);
    }
    
    @Test
    @DisplayName("Integración - Comando nuevo limpia pila de rehacer")
    public void testComandoNuevoLimpiaPilaRehacer() {
        controller.agregarProducto(producto1, 2);
        controller.agregarProducto(producto2, 1);
        
        controller.deshacer();
        assertTrue(controller.puedeRehacer());
        
        // Nuevo comando limpia pila de rehacer
        controller.agregarProducto(producto3, 1);
        assertFalse(controller.puedeRehacer());
    }
    
    @Test
    @DisplayName("Integración - Múltiples descuentos aplicados")
    public void testMultiplesDescuentos() {
        controller.agregarProducto(producto1, 2);
        
        controller.aplicarDescuento(10);
        double total1 = controller.getCarrito().calcularTotal();
        
        controller.aplicarDescuento(20);
        double total2 = controller.getCarrito().calcularTotal();
        
        assertTrue(total2 < total1);
        
        // Deshacer vuelve al descuento anterior
        controller.deshacer();
        assertEquals(total1, controller.getCarrito().calcularTotal(), 0.01);
    }
}