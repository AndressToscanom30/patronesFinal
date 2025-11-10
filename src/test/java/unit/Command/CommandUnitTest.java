package unit.Command;

import Comportamentales.Command.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Command Pattern - Unit Tests")
public class CommandUnitTest {
    
    private CarritoCompras carrito;
    private Producto producto1;
    private Producto producto2;
    
    @BeforeEach
    public void setUp() {
        carrito = new CarritoCompras();
        producto1 = new Producto("P001", "Leche", 3500, "Lácteos");
        producto2 = new Producto("P002", "Pan", 2500, "Panadería");
    }
    
    // ==================== Producto Tests ====================
    
    @Test
    @DisplayName("Producto - Crear producto")
    public void testCrearProducto() {
        Producto p = new Producto("P001", "Arroz", 4000, "Granos");
        assertEquals("P001", p.getCodigo());
        assertEquals("Arroz", p.getNombre());
        assertEquals(4000, p.getPrecio(), 0.01);
        assertEquals("Granos", p.getCategoria());
    }
    
    @Test
    @DisplayName("Producto - ToString")
    public void testProductoToString() {
        String resultado = producto1.toString();
        assertTrue(resultado.contains("Leche"));
        assertTrue(resultado.contains("3500"));
    }
    
    // ==================== ItemCarrito Tests ====================
    
    @Test
    @DisplayName("ItemCarrito - Crear item")
    public void testCrearItemCarrito() {
        ItemCarrito item = new ItemCarrito(producto1, 2);
        assertEquals(producto1, item.getProducto());
        assertEquals(2, item.getCantidad());
    }
    
    @Test
    @DisplayName("ItemCarrito - Calcular subtotal")
    public void testCalcularSubtotalItem() {
        ItemCarrito item = new ItemCarrito(producto1, 3);
        assertEquals(10500, item.getSubtotal(), 0.01); // 3500 * 3
    }
    
    @Test
    @DisplayName("ItemCarrito - Modificar cantidad")
    public void testModificarCantidad() {
        ItemCarrito item = new ItemCarrito(producto1, 2);
        item.setCantidad(5);
        assertEquals(5, item.getCantidad());
        assertEquals(17500, item.getSubtotal(), 0.01); // 3500 * 5
    }
    
    // ==================== CarritoCompras Tests ====================
    
    @Test
    @DisplayName("CarritoCompras - Crear carrito vacío")
    public void testCrearCarritoVacio() {
        CarritoCompras carrito = new CarritoCompras();
        assertTrue(carrito.estaVacio());
        assertEquals(0, carrito.getCantidadItems());
    }
    
    @Test
    @DisplayName("CarritoCompras - Agregar producto")
    public void testAgregarProducto() {
        carrito.agregarProducto(producto1, 2);
        assertFalse(carrito.estaVacio());
        assertEquals(1, carrito.getCantidadItems());
    }
    
    @Test
    @DisplayName("CarritoCompras - Agregar mismo producto suma cantidades")
    public void testAgregarMismoProducto() {
        carrito.agregarProducto(producto1, 2);
        carrito.agregarProducto(producto1, 3);
        
        assertEquals(1, carrito.getCantidadItems());
        assertEquals(5, carrito.getItems().get(0).getCantidad());
    }
    
    @Test
    @DisplayName("CarritoCompras - Eliminar producto")
    public void testEliminarProducto() {
        carrito.agregarProducto(producto1, 5);
        carrito.eliminarProducto(producto1, 2);
        
        assertEquals(3, carrito.getItems().get(0).getCantidad());
    }
    
    @Test
    @DisplayName("CarritoCompras - Eliminar producto completamente")
    public void testEliminarProductoCompletamente() {
        carrito.agregarProducto(producto1, 3);
        carrito.eliminarProducto(producto1, 5); // Elimina más de lo que hay
        
        assertTrue(carrito.estaVacio());
    }
    
    @Test
    @DisplayName("CarritoCompras - Calcular subtotal")
    public void testCalcularSubtotal() {
        carrito.agregarProducto(producto1, 2); // 3500 * 2 = 7000
        carrito.agregarProducto(producto2, 1); // 2500 * 1 = 2500
        
        assertEquals(9500, carrito.calcularSubtotal(), 0.01);
    }
    
    @Test
    @DisplayName("CarritoCompras - Aplicar descuento")
    public void testAplicarDescuento() {
        carrito.agregarProducto(producto1, 2);
        carrito.aplicarDescuento(10);
        
        assertEquals(10, carrito.getDescuento(), 0.01);
        assertEquals(700, carrito.calcularDescuento(), 0.01); // 10% de 7000
        assertEquals(6300, carrito.calcularTotal(), 0.01); // 7000 - 700
    }
    
    @Test
    @DisplayName("CarritoCompras - Vaciar carrito")
    public void testVaciarCarrito() {
        carrito.agregarProducto(producto1, 2);
        carrito.agregarProducto(producto2, 1);
        carrito.vaciar();
        
        assertTrue(carrito.estaVacio());
        assertEquals(0, carrito.getCantidadItems());
    }
    
    // ==================== AgregarProductoCommand Tests ====================
    
    @Test
    @DisplayName("AgregarProductoCommand - Ejecutar comando")
    public void testEjecutarAgregarProducto() {
        Command comando = new AgregarProductoCommand(carrito, producto1, 2);
        comando.ejecutar();
        
        assertEquals(1, carrito.getCantidadItems());
        assertEquals(2, carrito.getItems().get(0).getCantidad());
    }
    
    @Test
    @DisplayName("AgregarProductoCommand - Deshacer comando")
    public void testDeshacerAgregarProducto() {
        Command comando = new AgregarProductoCommand(carrito, producto1, 2);
        comando.ejecutar();
        comando.deshacer();
        
        assertTrue(carrito.estaVacio());
    }
    
    @Test
    @DisplayName("AgregarProductoCommand - Obtener descripción")
    public void testDescripcionAgregarProducto() {
        Command comando = new AgregarProductoCommand(carrito, producto1, 2);
        String descripcion = comando.obtenerDescripcion();
        
        assertTrue(descripcion.contains("Agregar"));
        assertTrue(descripcion.contains("Leche"));
        assertTrue(descripcion.contains("2"));
    }
    
    // ==================== EliminarProductoCommand Tests ====================
    
    @Test
    @DisplayName("EliminarProductoCommand - Ejecutar comando")
    public void testEjecutarEliminarProducto() {
        carrito.agregarProducto(producto1, 5);
        
        Command comando = new EliminarProductoCommand(carrito, producto1, 2);
        comando.ejecutar();
        
        assertEquals(3, carrito.getItems().get(0).getCantidad());
    }
    
    @Test
    @DisplayName("EliminarProductoCommand - Deshacer comando")
    public void testDeshacerEliminarProducto() {
        carrito.agregarProducto(producto1, 5);
        
        Command comando = new EliminarProductoCommand(carrito, producto1, 2);
        comando.ejecutar();
        comando.deshacer();
        
        assertEquals(5, carrito.getItems().get(0).getCantidad());
    }
    
    @Test
    @DisplayName("EliminarProductoCommand - Obtener descripción")
    public void testDescripcionEliminarProducto() {
        Command comando = new EliminarProductoCommand(carrito, producto1, 2);
        String descripcion = comando.obtenerDescripcion();
        
        assertTrue(descripcion.contains("Eliminar"));
        assertTrue(descripcion.contains("Leche"));
    }
    
    // ==================== AplicarDescuentoCommand Tests ====================
    
    @Test
    @DisplayName("AplicarDescuentoCommand - Ejecutar comando")
    public void testEjecutarAplicarDescuento() {
        Command comando = new AplicarDescuentoCommand(carrito, 15);
        comando.ejecutar();
        
        assertEquals(15, carrito.getDescuento(), 0.01);
    }
    
    @Test
    @DisplayName("AplicarDescuentoCommand - Deshacer comando")
    public void testDeshacerAplicarDescuento() {
        carrito.aplicarDescuento(10);
        
        Command comando = new AplicarDescuentoCommand(carrito, 20);
        comando.ejecutar();
        assertEquals(20, carrito.getDescuento(), 0.01);
        
        comando.deshacer();
        assertEquals(10, carrito.getDescuento(), 0.01);
    }
    
    @Test
    @DisplayName("AplicarDescuentoCommand - Obtener descripción")
    public void testDescripcionAplicarDescuento() {
        Command comando = new AplicarDescuentoCommand(carrito, 25);
        String descripcion = comando.obtenerDescripcion();
        
        assertTrue(descripcion.contains("descuento"));
        assertTrue(descripcion.contains("25"));
    }
    
    // ==================== VaciarCarritoCommand Tests ====================
    
    @Test
    @DisplayName("VaciarCarritoCommand - Ejecutar comando")
    public void testEjecutarVaciarCarrito() {
        carrito.agregarProducto(producto1, 2);
        carrito.agregarProducto(producto2, 1);
        
        Command comando = new VaciarCarritoCommand(carrito);
        comando.ejecutar();
        
        assertTrue(carrito.estaVacio());
    }
    
    @Test
    @DisplayName("VaciarCarritoCommand - Deshacer comando")
    public void testDeshacerVaciarCarrito() {
        carrito.agregarProducto(producto1, 2);
        carrito.agregarProducto(producto2, 1);
        
        Command comando = new VaciarCarritoCommand(carrito);
        comando.ejecutar();
        comando.deshacer();
        
        assertEquals(2, carrito.getCantidadItems());
    }
    
    @Test
    @DisplayName("VaciarCarritoCommand - Obtener descripción")
    public void testDescripcionVaciarCarrito() {
        Command comando = new VaciarCarritoCommand(carrito);
        String descripcion = comando.obtenerDescripcion();
        
        assertTrue(descripcion.contains("Vaciar"));
        assertTrue(descripcion.contains("carrito"));
    }
    
    // ==================== GestorComandos Tests ====================
    
    @Test
    @DisplayName("GestorComandos - Crear gestor vacío")
    public void testCrearGestorVacio() {
        GestorComandos gestor = new GestorComandos();
        assertFalse(gestor.puedeDeshacer());
        assertFalse(gestor.puedeRehacer());
        assertEquals(0, gestor.getTamanoHistorial());
    }
    
    @Test
    @DisplayName("GestorComandos - Ejecutar comando")
    public void testEjecutarComando() {
        GestorComandos gestor = new GestorComandos();
        Command comando = new AgregarProductoCommand(carrito, producto1, 2);
        
        gestor.ejecutarComando(comando);
        
        assertTrue(gestor.puedeDeshacer());
        assertEquals(1, gestor.getTamanoHistorial());
    }
    
    @Test
    @DisplayName("GestorComandos - Deshacer comando")
    public void testDeshacerComando() {
        GestorComandos gestor = new GestorComandos();
        Command comando = new AgregarProductoCommand(carrito, producto1, 2);
        
        gestor.ejecutarComando(comando);
        assertTrue(gestor.deshacer());
        
        assertTrue(carrito.estaVacio());
        assertTrue(gestor.puedeRehacer());
    }
    
    @Test
    @DisplayName("GestorComandos - Rehacer comando")
    public void testRehacerComando() {
        GestorComandos gestor = new GestorComandos();
        Command comando = new AgregarProductoCommand(carrito, producto1, 2);
        
        gestor.ejecutarComando(comando);
        gestor.deshacer();
        assertTrue(gestor.rehacer());
        
        assertFalse(carrito.estaVacio());
    }
    
    @Test
    @DisplayName("GestorComandos - No puede deshacer sin comandos")
    public void testNoPuedeDeshacerSinComandos() {
        GestorComandos gestor = new GestorComandos();
        assertFalse(gestor.deshacer());
    }
    
    @Test
    @DisplayName("GestorComandos - No puede rehacer sin deshacer")
    public void testNoPuedeRehacerSinDeshacer() {
        GestorComandos gestor = new GestorComandos();
        Command comando = new AgregarProductoCommand(carrito, producto1, 2);
        
        gestor.ejecutarComando(comando);
        assertFalse(gestor.rehacer());
    }
    
    @Test
    @DisplayName("GestorComandos - Limpiar historial después de nuevo comando")
    public void testLimpiarHistorialDespuesNuevoComando() {
        GestorComandos gestor = new GestorComandos();
        
        // Ejecutar y deshacer
        gestor.ejecutarComando(new AgregarProductoCommand(carrito, producto1, 2));
        gestor.deshacer();
        assertTrue(gestor.puedeRehacer());
        
        // Ejecutar nuevo comando limpia pila de rehacer
        gestor.ejecutarComando(new AgregarProductoCommand(carrito, producto2, 1));
        assertFalse(gestor.puedeRehacer());
    }
    
    @Test
    @DisplayName("GestorComandos - Obtener último comando")
    public void testObtenerUltimoComando() {
        GestorComandos gestor = new GestorComandos();
        Command comando = new AgregarProductoCommand(carrito, producto1, 2);
        
        gestor.ejecutarComando(comando);
        String descripcion = gestor.obtenerUltimoComando();
        
        assertTrue(descripcion.contains("Agregar"));
    }
    
    @Test
    @DisplayName("GestorComandos - Limpiar historial")
    public void testLimpiarHistorial() {
        GestorComandos gestor = new GestorComandos();
        gestor.ejecutarComando(new AgregarProductoCommand(carrito, producto1, 2));
        gestor.ejecutarComando(new AgregarProductoCommand(carrito, producto2, 1));
        
        gestor.limpiarHistorial();
        
        assertEquals(0, gestor.getTamanoHistorial());
        assertFalse(gestor.puedeDeshacer());
    }
}