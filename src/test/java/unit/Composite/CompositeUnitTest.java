package unit.Composite;

import Estructurales.Composite.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.Calendar;

@DisplayName("Composite Pattern - Unit Tests")
public class CompositeUnitTest {
   
    @Test
    @DisplayName("ProductoIndividual - Calcular total sin descuento")
    public void testProductoCalcularTotal() {
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        assertEquals(1000.0, producto.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("ProductoIndividual - Calcular peso total")
    public void testProductoCalcularPesoTotal() {
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        assertEquals(2.5, producto.calcularPesoTotal(), 0.01);
    }

    @Test
    @DisplayName("ProductoIndividual - Contar items")
    public void testProductoContarItems() {
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        assertEquals(1, producto.contarItems());
    }

    @Test
    @DisplayName("ProductoIndividual - Aplicar descuento simple")
    public void testProductoAplicarDescuento() {
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        producto.aplicarDescuento(10);
        assertEquals(900.0, producto.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("ProductoIndividual - Aplicar descuento múltiple")
    public void testProductoAplicarDescuentoMultiple() {
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        producto.aplicarDescuento(10);
        producto.aplicarDescuento(5);
        assertEquals(850.0, producto.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("ProductoIndividual - No es compuesto")
    public void testProductoEsCompuesto() {
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        assertFalse(producto.esCompuesto());
    }

    @Test
    @DisplayName("ProductoIndividual - Getters funcionan correctamente")
    public void testProductoGetters() {
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        assertEquals("P001", producto.getCodigo());
        assertEquals("Laptop", producto.getNombre());
        assertEquals("Laptop Dell", producto.getDescripcion());
        assertEquals(1000.0, producto.getPrecio(), 0.01);
        assertEquals(2.5, producto.getPeso(), 0.01);
    }

    @Test
    @DisplayName("Categoria - Agregar subcategoría")
    public void testCategoriaAgregarSubcategoria() {
        Categoria categoria = new Categoria("Electrónica", "Productos electrónicos", 0, "icon-electronics", 1, true, 1);
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        categoria.agregarSubcategoria(producto);
        assertEquals(1, categoria.contarItems());
    }

    @Test
    @DisplayName("Categoria - Calcular total vacía")
    public void testCategoriaCalcularTotalVacia() {
        Categoria categoria = new Categoria("Electrónica", "Productos electrónicos", 0, "icon-electronics", 1, true, 1);
        assertEquals(0.0, categoria.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("Categoria - Calcular total con productos")
    public void testCategoriaCalcularTotalConProductos() {
        Categoria categoria = new Categoria("Electrónica", "Productos electrónicos", 0, "icon-electronics", 1, true, 1);
        ProductoIndividual producto1 = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual producto2 = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);
        categoria.agregarSubcategoria(producto1);
        categoria.agregarSubcategoria(producto2);
        assertEquals(1025.0, categoria.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("Categoria - Calcular peso total")
    public void testCategoriaCalcularPesoTotal() {
        Categoria categoria = new Categoria("Electrónica", "Productos electrónicos", 0, "icon-electronics", 1, true, 1);
        ProductoIndividual producto1 = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual producto2 = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);
        categoria.agregarSubcategoria(producto1);
        categoria.agregarSubcategoria(producto2);
        assertEquals(2.6, categoria.calcularPesoTotal(), 0.01);
    }

    @Test
    @DisplayName("Categoria - Contar items múltiples")
    public void testCategoriaContarItems() {
        Categoria categoria = new Categoria("Electrónica", "Productos electrónicos", 0, "icon-electronics", 1, true, 1);
        ProductoIndividual producto1 = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual producto2 = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);
        categoria.agregarSubcategoria(producto1);
        categoria.agregarSubcategoria(producto2);
        assertEquals(2, categoria.contarItems());
    }

    @Test
    @DisplayName("Categoria - Aplicar descuento en cascada")
    public void testCategoriaAplicarDescuentoACascada() {
        Categoria categoria = new Categoria("Electrónica", "Productos electrónicos", 0, "icon-electronics", 1, true, 1);
        ProductoIndividual producto1 = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual producto2 = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);
        categoria.agregarSubcategoria(producto1);
        categoria.agregarSubcategoria(producto2);
        categoria.aplicarDescuento(10);
        assertEquals(922.5, categoria.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("Categoria - Es compuesto")
    public void testCategoriaEsCompuesto() {
        Categoria categoria = new Categoria("Electrónica", "Productos electrónicos", 0, "icon-electronics", 1, true, 1);
        assertTrue(categoria.esCompuesto());
    }

    @Test
    @DisplayName("Categoria - Obtener hijos")
    public void testCategoriaObtenerHijos() {
        Categoria categoria = new Categoria("Electrónica", "Productos electrónicos", 0, "icon-electronics", 1, true, 1);
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        categoria.agregarSubcategoria(producto);
        assertEquals(1, categoria.obtenerHijos().size());
    }

    // ==================== Combo Tests ====================
    
    @Test
    @DisplayName("Combo - Calcular ahorro")
    public void testComboCalcularAhorro() {
        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 800.0, 200.0, "combo.jpg");
        ProductoIndividual producto1 = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual producto2 = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);
        combo.agregarProducto(producto1);
        combo.agregarProducto(producto2);
        assertEquals(225.0, combo.calcularAhorro(), 0.01);
    }

    @Test
    @DisplayName("Combo - Validar combinación vacía")
    public void testComboValidarCombinacionVacia() {
        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 800.0, 200.0, "combo.jpg");
        assertFalse(combo.validarCombinacion());
    }

    @Test
    @DisplayName("Combo - Validar combinación con productos")
    public void testComboValidarCombinacionConProductos() {
        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 800.0, 200.0, "combo.jpg");
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        combo.agregarProducto(producto);
        assertTrue(combo.validarCombinacion());
    }

    @Test
    @DisplayName("Combo - Calcular total")
    public void testComboCalcularTotal() {
        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 800.0, 200.0, "combo.jpg");
        assertEquals(800.0, combo.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("Combo - Calcular total con descuento")
    public void testComboCalcularTotalConDescuento() {
        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 800.0, 200.0, "combo.jpg");
        combo.aplicarDescuento(10);
        assertEquals(720.0, combo.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("Combo - Calcular peso total")
    public void testComboCalcularPesoTotal() {
        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 800.0, 200.0, "combo.jpg");
        ProductoIndividual producto1 = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual producto2 = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);
        combo.agregarProducto(producto1);
        combo.agregarProducto(producto2);
        assertEquals(2.6, combo.calcularPesoTotal(), 0.01);
    }

    @Test
    @DisplayName("Combo - Contar items")
    public void testComboContarItems() {
        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 800.0, 200.0, "combo.jpg");
        ProductoIndividual producto1 = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual producto2 = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);
        combo.agregarProducto(producto1);
        combo.agregarProducto(producto2);
        assertEquals(2, combo.contarItems());
    }

    @Test
    @DisplayName("Combo - Es compuesto")
    public void testComboEsCompuesto() {
        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 800.0, 200.0, "combo.jpg");
        assertTrue(combo.esCompuesto());
    }

    // ==================== PaquetePromocional Tests ====================
    
    @Test
    @DisplayName("PaquetePromocional - Validar vigencia actual")
    public void testPaqueteValidarVigencia() {
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fin = cal.getTime();
        
        PaquetePromocional paquete = new PaquetePromocional("Black Friday", "Promoción especial", 20, inicio, fin, "Válido hasta agotar stock", 100);
        assertTrue(paquete.validarVigencia());
    }

    @Test
    @DisplayName("PaquetePromocional - Validar vigencia expirada")
    public void testPaqueteValidarVigenciaExpirado() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -60);
        Date inicioViejo = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date finViejo = cal.getTime();
        
        PaquetePromocional paqueteExpirado = new PaquetePromocional("Viejo", "Expirado", 20, inicioViejo, finViejo, "Expirado", 100);
        assertFalse(paqueteExpirado.validarVigencia());
    }

    @Test
    @DisplayName("PaquetePromocional - Aplicar restricción válida")
    public void testPaqueteAplicarRestriccion() {
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fin = cal.getTime();
        
        PaquetePromocional paquete = new PaquetePromocional("Black Friday", "Promoción especial", 20, inicio, fin, "Válido hasta agotar stock", 100);
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        paquete.agregarProducto(producto);
        assertTrue(paquete.aplicarRestriccion());
    }

    @Test
    @DisplayName("PaquetePromocional - Calcular total con descuento")
    public void testPaqueteCalcularTotalConDescuento() {
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fin = cal.getTime();
        
        PaquetePromocional paquete = new PaquetePromocional("Black Friday", "Promoción especial", 20, inicio, fin, "Válido hasta agotar stock", 100);
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        paquete.agregarProducto(producto);
        assertEquals(800.0, paquete.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("PaquetePromocional - Contar items")
    public void testPaqueteContarItems() {
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fin = cal.getTime();
        
        PaquetePromocional paquete = new PaquetePromocional("Black Friday", "Promoción especial", 20, inicio, fin, "Válido hasta agotar stock", 100);
        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        paquete.agregarProducto(producto);
        assertEquals(1, paquete.contarItems());
    }

    @Test
    @DisplayName("PaquetePromocional - Es compuesto")
    public void testPaqueteEsCompuesto() {
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fin = cal.getTime();
        
        PaquetePromocional paquete = new PaquetePromocional("Black Friday", "Promoción especial", 20, inicio, fin, "Válido hasta agotar stock", 100);
        assertTrue(paquete.esCompuesto());
    }
}
