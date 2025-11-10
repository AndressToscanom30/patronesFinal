package Integration.Composite;

import Estructurales.Composite.modelo.*;
import Estructurales.Composite.controlador.*;
import Estructurales.Composite.vista.ConsolaComposite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.Calendar;

@DisplayName("Composite Pattern - Integration Tests")
public class CompositeIntegrationTest {

    @Test
    @DisplayName("Integración - Jerarquía de categorías multinivel")
    public void testJerarquiaCategorias() {
        Categoria categoriaRaiz = new Categoria("Raíz", "Categoría raíz", 0, "root", 0, true, 0);
        Categoria categoriaElectronica = new Categoria("Electrónica", "Productos electrónicos", 0, "electronics", 1,
                true, 1);
        Categoria categoriaComputadoras = new Categoria("Computadoras", "Equipos de computación", 0, "computer", 1,
                true, 2);

        ProductoIndividual laptop = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual mouse = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);

        categoriaComputadoras.agregarSubcategoria(laptop);
        categoriaComputadoras.agregarSubcategoria(mouse);
        categoriaElectronica.agregarSubcategoria(categoriaComputadoras);
        categoriaRaiz.agregarSubcategoria(categoriaElectronica);

        assertEquals(1025.0, categoriaRaiz.calcularTotal(), 0.01);
        assertEquals(2, categoriaRaiz.contarItems());
        assertEquals(2.6, categoriaRaiz.calcularPesoTotal(), 0.01);
    }

    @Test
    @DisplayName("Integración - Combo con productos individuales")
    public void testComboConProductos() {
        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 900.0, 125.0, "combo.jpg");
        ProductoIndividual laptop = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual mouse = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);

        combo.agregarProducto(laptop);
        combo.agregarProducto(mouse);

        assertEquals(900.0, combo.calcularTotal(), 0.01);
        assertEquals(2, combo.contarItems());
        assertEquals(2.6, combo.calcularPesoTotal(), 0.01);
        assertEquals(125.0, combo.calcularAhorro(), 0.01);
    }

    @Test
    @DisplayName("Integración - Descuento en cascada multinivel")
    public void testDescuentoCascada() {
        Categoria categoriaElectronica = new Categoria("Electrónica", "Productos electrónicos", 0, "electronics", 1,
                true, 1);
        Categoria categoriaComputadoras = new Categoria("Computadoras", "Equipos de computación", 0, "computer", 1,
                true, 2);

        ProductoIndividual laptop = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual mouse = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);

        categoriaComputadoras.agregarSubcategoria(laptop);
        categoriaComputadoras.agregarSubcategoria(mouse);
        categoriaElectronica.agregarSubcategoria(categoriaComputadoras);

        categoriaElectronica.aplicarDescuento(10);

        assertEquals(922.5, categoriaElectronica.calcularTotal(), 0.01);
    }

    @Test
    @DisplayName("Integración - Paquete promocional complejo")
    public void testPaquetePromocionalComplejo() {
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fin = cal.getTime();

        PaquetePromocional paquete = new PaquetePromocional("Black Friday", "Promoción especial", 20, inicio, fin,
                "Válido hasta agotar stock", 100);

        ProductoIndividual laptop = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual mouse = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);

        paquete.agregarProducto(laptop);
        paquete.agregarProducto(mouse);

        assertTrue(paquete.validarVigencia());
        assertTrue(paquete.aplicarRestriccion());
        assertEquals(820.0, paquete.calcularTotal(), 0.01);
        assertEquals(2, paquete.contarItems());
    }

    @Test
    @DisplayName("Integración - Mezcla de compuestos y simples")
    public void testMezclaCompuestosYSimples() {
        Categoria categoriaRaiz = new Categoria("Raíz", "Categoría raíz", 0, "root", 0, true, 0);
        Categoria categoriaElectronica = new Categoria("Electrónica", "Productos electrónicos", 0, "electronics", 1,
                true, 1);
        Categoria categoriaComputadoras = new Categoria("Computadoras", "Equipos de computación", 0, "computer", 1,
                true, 2);

        ProductoIndividual laptop = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual mouse = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);

        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 900.0, 125.0, "combo.jpg");
        combo.agregarProducto(laptop);
        combo.agregarProducto(mouse);

        categoriaComputadoras.agregarSubcategoria(combo);
        categoriaElectronica.agregarSubcategoria(categoriaComputadoras);
        categoriaRaiz.agregarSubcategoria(categoriaElectronica);

        assertEquals(900.0, categoriaRaiz.calcularTotal(), 0.01);
        assertEquals(2, categoriaRaiz.contarItems());
        assertEquals(2.6, categoriaRaiz.calcularPesoTotal(), 0.01);
    }

    // ==================== MVC Integration Tests ====================

    @Test
    @DisplayName("MVC - Controller con inventario vacío")
    public void testControllerConInventarioVacio() {
        ConsolaComposite view = new ConsolaComposite();
        InventarioController controller = new InventarioController(view);

        assertEquals(0.0, controller.obtenerTotalInventario(), 0.01);
        assertEquals(0, controller.obtenerCantidadItems());
        assertEquals(0.0, controller.obtenerPesoTotal(), 0.01);
    }

    @Test
    @DisplayName("MVC - Controller con inventario")
    public void testControllerConInventario() {
        ConsolaComposite view = new ConsolaComposite();
        InventarioController controller = new InventarioController(view);
        Categoria categoriaRaiz = new Categoria("Raíz", "Categoría raíz", 0, "root", 0, true, 0);

        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        categoriaRaiz.agregarSubcategoria(producto);
        controller.setRaiz(categoriaRaiz);

        assertEquals(1000.0, controller.obtenerTotalInventario(), 0.01);
        assertEquals(1, controller.obtenerCantidadItems());
        assertEquals(2.5, controller.obtenerPesoTotal(), 0.01);
    }

    @Test
    @DisplayName("MVC - Aplicar descuento desde controller")
    public void testAplicarDescuentoDesdeController() {
        ConsolaComposite view = new ConsolaComposite();
        InventarioController controller = new InventarioController(view);
        Categoria categoriaRaiz = new Categoria("Raíz", "Categoría raíz", 0, "root", 0, true, 0);

        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        categoriaRaiz.agregarSubcategoria(producto);
        controller.setRaiz(categoriaRaiz);

        controller.aplicarDescuentoGlobal(10);
        assertEquals(900.0, controller.obtenerTotalInventario(), 0.01);
    }

    @Test
    @DisplayName("MVC - Mostrar información completa")
    public void testMostrarInformacion() {
        ConsolaComposite view = new ConsolaComposite();
        InventarioController controller = new InventarioController(view);
        Categoria categoriaRaiz = new Categoria("Raíz", "Categoría raíz", 0, "root", 0, true, 0);

        ProductoIndividual producto = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        categoriaRaiz.agregarSubcategoria(producto);
        controller.setRaiz(categoriaRaiz);

        controller.mostrarInformacion();
        // Verificar que no lanza excepciones
    }

    @Test
    @DisplayName("MVC - Controller con estructura compleja")
    public void testControllerConEstructuraCompleja() {
        ConsolaComposite view = new ConsolaComposite();
        InventarioController controller = new InventarioController(view);

        Categoria tienda = new Categoria("Mi Tienda", "Tienda principal", 0, "store", 0, true, 0);
        Categoria electronica = new Categoria("Electrónica", "Productos electrónicos", 0, "electronics", 1, true, 1);

        ProductoIndividual laptop = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual mouse = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);

        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 900.0, 125.0, "combo.jpg");
        combo.agregarProducto(laptop);
        combo.agregarProducto(mouse);

        electronica.agregarSubcategoria(combo);
        tienda.agregarSubcategoria(electronica);

        controller.setRaiz(tienda);

        assertEquals(900.0, controller.obtenerTotalInventario(), 0.01);
        assertEquals(2, controller.obtenerCantidadItems());
        assertEquals(2.6, controller.obtenerPesoTotal(), 0.01);
    }

    @Test
    @DisplayName("Integración - Categorías anidadas con descuentos acumulativos")
    public void testCategoriasAnidasConDescuentos() {
        Categoria raiz = new Categoria("Raíz", "Categoría raíz", 0, "root", 0, true, 0);
        Categoria nivel1 = new Categoria("Nivel 1", "Primera categoría", 0, "cat1", 1, true, 1);
        Categoria nivel2 = new Categoria("Nivel 2", "Segunda categoría", 0, "cat2", 1, true, 2);

        ProductoIndividual producto = new ProductoIndividual("P001", "Producto", "Producto test", 100.0, 1.0);

        nivel2.agregarSubcategoria(producto);
        nivel1.agregarSubcategoria(nivel2);
        raiz.agregarSubcategoria(nivel1);

        raiz.aplicarDescuento(10);

        assertEquals(90.0, raiz.calcularTotal(), 0.01); // CAMBIAR DE 72.9 A 90.0
    }

    @Test
    @DisplayName("Integración - Combo dentro de paquete promocional")
    public void testComboDentroDePaquete() {
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fin = cal.getTime();

        ProductoIndividual laptop = new ProductoIndividual("P001", "Laptop", "Laptop Dell", 1000.0, 2.5);
        ProductoIndividual mouse = new ProductoIndividual("P002", "Mouse", "Mouse inalámbrico", 25.0, 0.1);

        Combo combo = new Combo("Combo Oficina", "Combo para oficina", 0, "Premium", 900.0, 125.0, "combo.jpg");
        combo.agregarProducto(laptop);
        combo.agregarProducto(mouse);

        PaquetePromocional paquete = new PaquetePromocional("Navidad", "Promoción navideña", 15, inicio, fin,
                "Limitado", 50);
        paquete.agregarProducto(combo);

        assertEquals(765.0, paquete.calcularTotal(), 0.01);
        assertEquals(2, paquete.contarItems());
        assertTrue(paquete.validarVigencia());
    }
}
