package e2e.Composite;

import Estructurales.Composite.controlador.InventarioController;
import Estructurales.Composite.modelo.*;
import Estructurales.Composite.vista.ConsolaComposite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import java.util.Date;

@DisplayName("Composite Pattern - End-to-End Tests")
public class CompositeE2ETest {
    private InventarioController controller;
    private ConsolaComposite view;
    private Categoria tienda;

    @BeforeEach
    public void setUp() {
        view = new ConsolaComposite();
        controller = new InventarioController(view);
        tienda = new Categoria("Mi Tienda", "Tienda principal", 0, "store", 0, true, 0);
    }

    @Test
    @DisplayName("E2E - Escenario completo de tienda electrónica")
    public void testEscenarioCompletoTiendaElectronica() {
        // Crear estructura de categorías
        Categoria electronica = new Categoria("Electrónica", "Productos electrónicos", 0, "electronics", 1, true, 1);
        Categoria computadoras = new Categoria("Computadoras", "Equipos de computación", 0, "computer", 1, true, 2);
        Categoria accesorios = new Categoria("Accesorios", "Accesorios de computadora", 0, "accessories", 2, true, 2);
        
        // Crear productos individuales
        ProductoIndividual laptop1 = new ProductoIndividual("P001", "Laptop Dell XPS", "Laptop de alta gama", 1500.0, 2.5);
        ProductoIndividual laptop2 = new ProductoIndividual("P002", "Laptop HP Pavilion", "Laptop económica", 800.0, 2.3);
        ProductoIndividual mouse = new ProductoIndividual("P003", "Mouse Logitech", "Mouse inalámbrico", 25.0, 0.1);
        ProductoIndividual teclado = new ProductoIndividual("P004", "Teclado Mecánico", "Teclado gaming", 120.0, 0.8);
        ProductoIndividual monitor = new ProductoIndividual("P005", "Monitor Samsung", "Monitor 27 pulgadas", 300.0, 5.0);
        
        // Crear combo
        Combo comboOficina = new Combo("Combo Oficina Pro", "Todo para tu oficina", 0, "Premium", 2500.0, 200.0, "combo-oficina.jpg");
        comboOficina.agregarProducto(laptop1);
        comboOficina.agregarProducto(mouse);
        comboOficina.agregarProducto(teclado);
        comboOficina.agregarProducto(monitor);
        
        // Crear paquete promocional
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fin = cal.getTime();
        
        PaquetePromocional blackFriday = new PaquetePromocional("Black Friday 2025", "Ofertas increíbles", 25, inicio, fin, "Hasta agotar stock", 50);
        blackFriday.agregarProducto(laptop2);
        blackFriday.agregarProducto(mouse);
        
        // Armar jerarquía
        accesorios.agregarSubcategoria(new ProductoIndividual("P006", "Cable HDMI", "Cable 2m", 15.0, 0.2));
        accesorios.agregarSubcategoria(new ProductoIndividual("P007", "USB Hub", "Hub 4 puertos", 30.0, 0.3));
        
        computadoras.agregarSubcategoria(comboOficina);
        computadoras.agregarSubcategoria(blackFriday);
        
        electronica.agregarSubcategoria(computadoras);
        electronica.agregarSubcategoria(accesorios);
        
        tienda.agregarSubcategoria(electronica);
        
        controller.setRaiz(tienda);
        
        // Verificaciones iniciales
        System.out.println("\n=== ESCENARIO COMPLETO: TIENDA ELECTRÓNICA ===\n");
        controller.mostrarInformacion();
        
        assertEquals(3163.75, controller.obtenerTotalInventario(), 0.01);
        assertEquals(8, controller.obtenerCantidadItems());
        assertEquals(11.3, controller.obtenerPesoTotal(), 0.01);
        
        // Aplicar descuento global del 15%
        System.out.println("\n--- Aplicando descuento global del 15% ---\n");
        controller.aplicarDescuentoGlobal(15);
        controller.mostrarInformacion();
        
        assertTrue(controller.obtenerTotalInventario() < 3162.5);
        
        // Verificar que el combo mantiene su lógica
        assertTrue(comboOficina.validarCombinacion());
        assertEquals(4, comboOficina.contarItems());
        
        // Verificar que el paquete promocional está vigente
        assertTrue(blackFriday.validarVigencia());
        assertTrue(blackFriday.aplicarRestriccion());
        
        System.out.println("\n--- Resultados Finales ---");
        System.out.println("Total con descuento: $" + String.format("%.2f", controller.obtenerTotalInventario()));
        System.out.println("Items totales: " + controller.obtenerCantidadItems());
        System.out.println("Peso total: " + String.format("%.2f", controller.obtenerPesoTotal()) + " kg");
    }

    @Test
    @DisplayName("E2E - Escenario multi-categorías con descuentos selectivos")
    public void testEscenarioMultiCategoriasConDescuentos() {
        // Categoría ropa
        Categoria ropa = new Categoria("Ropa", "Prendas de vestir", 0, "clothes", 1, true, 1);
        ProductoIndividual camisa = new ProductoIndividual("R001", "Camisa", "Camisa formal", 50.0, 0.3);
        ProductoIndividual pantalon = new ProductoIndividual("R002", "Pantalón", "Pantalón casual", 70.0, 0.5);
        ropa.agregarSubcategoria(camisa);
        ropa.agregarSubcategoria(pantalon);
        
        // Categoría electrónica
        Categoria electronica = new Categoria("Electrónica", "Dispositivos", 0, "electronics", 2, true, 1);
        ProductoIndividual celular = new ProductoIndividual("E001", "Celular", "Smartphone", 500.0, 0.2);
        ProductoIndividual audifono = new ProductoIndividual("E002", "Audífonos", "Inalámbricos", 80.0, 0.1);
        electronica.agregarSubcategoria(celular);
        electronica.agregarSubcategoria(audifono);
        
        tienda.agregarSubcategoria(ropa);
        tienda.agregarSubcategoria(electronica);
        
        controller.setRaiz(tienda);
        
        System.out.println("\n=== ESCENARIO: MULTI-CATEGORÍAS CON DESCUENTOS ===\n");
        System.out.println("Estado inicial:");
        controller.mostrarInformacion();
        
        double totalInicial = controller.obtenerTotalInventario();
        assertEquals(700.0, totalInicial, 0.01);
        assertEquals(4, controller.obtenerCantidadItems());
        
        // Aplicar descuento solo a ropa
        System.out.println("\n--- Aplicando 20% de descuento solo en Ropa ---");
        ropa.aplicarDescuento(20);
        controller.mostrarInformacion();
        
        // Verificar que el total cambió correctamente
        double totalEsperado = (120.0 * 0.8) + 580.0; // Ropa con 20% desc + Electrónica sin desc
        assertEquals(totalEsperado, controller.obtenerTotalInventario(), 0.01);
        
        // Aplicar descuento global adicional
        System.out.println("\n--- Aplicando 10% de descuento global adicional ---");
        controller.aplicarDescuentoGlobal(10);
        controller.mostrarInformacion();
        
        assertTrue(controller.obtenerTotalInventario() < totalInicial);
        
        System.out.println("\n--- Análisis de Descuentos ---");
        System.out.println("Descuento aplicado a Ropa: 20%");
        System.out.println("Descuento global adicional: 10%");
        System.out.println("Descuento total en Ropa: " + ((1 - (controller.obtenerTotalInventario() / totalInicial)) * 100) + "%");
    }

    @Test
    @DisplayName("E2E - Escenario de combo dentro de promoción")
    public void testEscenarioComboYPromocionesJuntos() {
        // Crear productos base
        ProductoIndividual tv = new ProductoIndividual("TV001", "Smart TV", "TV 55 pulgadas", 1000.0, 15.0);
        ProductoIndividual soundbar = new ProductoIndividual("SB001", "Soundbar", "Barra de sonido", 200.0, 3.0);
        ProductoIndividual soporte = new ProductoIndividual("SP001", "Soporte TV", "Soporte de pared", 50.0, 2.0);
        
        // Combo home theater
        Combo comboTheater = new Combo("Home Theater Completo", "Todo para tu sala", 0, "Premium", 1100.0, 150.0, "theater.jpg");
        comboTheater.agregarProducto(tv);
        comboTheater.agregarProducto(soundbar);
        comboTheater.agregarProducto(soporte);
        
        // Paquete promocional con el combo incluido
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 15);
        Date fin = cal.getTime();
        
        PaquetePromocional promoNavidad = new PaquetePromocional("Navidad 2025", "Especial navideño", 15, inicio, fin, "Compra ahora", 20);
        promoNavidad.agregarProducto(comboTheater);
        
        Categoria entretenimiento = new Categoria("Entretenimiento", "Audio y video", 0, "entertainment", 1, true, 1);
        entretenimiento.agregarSubcategoria(promoNavidad);
        
        tienda.agregarSubcategoria(entretenimiento);
        controller.setRaiz(tienda);
        
        System.out.println("\n=== ESCENARIO: COMBO DENTRO DE PROMOCIÓN ===\n");
        controller.mostrarInformacion();
        
        // El combo cuesta 1100, con 15% de descuento del paquete = 935
        assertEquals(935.0, controller.obtenerTotalInventario(), 0.01);
        assertEquals(3, controller.obtenerCantidadItems());
        assertEquals(20.0, controller.obtenerPesoTotal(), 0.01);
        
        // Verificar ahorro del combo
        double ahorroCombo = comboTheater.calcularAhorro();
        assertTrue(ahorroCombo > 0);
        
        // Verificar vigencia de promoción
        assertTrue(promoNavidad.validarVigencia());
        
        System.out.println("\n--- Análisis de Ahorros ---");
        System.out.println("Precio individual de productos: $" + (tv.getPrecio() + soundbar.getPrecio() + soporte.getPrecio()));
        System.out.println("Precio del combo: $" + comboTheater.getPrecioEspecial());
        System.out.println("Ahorro del combo: $" + String.format("%.2f", ahorroCombo));
        System.out.println("Descuento adicional por promoción: 15%");
        System.out.println("Precio final: $" + String.format("%.2f", controller.obtenerTotalInventario()));
        System.out.println("Ahorro total: $" + String.format("%.2f", ahorroCombo + (1100.0 * 0.15)));
    }

    @Test
    @DisplayName("E2E - Escenario de tienda completa con operaciones complejas")
    public void testEscenarioTiendaCompletaOperacionesComplejas() {
        // Crear estructura compleja de tienda
        Categoria electronica = new Categoria("Electrónica", "Productos electrónicos", 0, "electronics", 1, true, 1);
        Categoria hogar = new Categoria("Hogar", "Artículos del hogar", 0, "home", 2, true, 1);
        Categoria deportes = new Categoria("Deportes", "Artículos deportivos", 0, "sports", 3, true, 1);
        
        // Productos de electrónica
        ProductoIndividual laptop = new ProductoIndividual("E001", "Laptop", "Laptop gaming", 1500.0, 2.5);
        ProductoIndividual tablet = new ProductoIndividual("E002", "Tablet", "Tablet 10 pulgadas", 300.0, 0.5);
        electronica.agregarSubcategoria(laptop);
        electronica.agregarSubcategoria(tablet);
        
        // Productos de hogar
        ProductoIndividual licuadora = new ProductoIndividual("H001", "Licuadora", "Licuadora 600W", 80.0, 2.0);
        ProductoIndividual cafetera = new ProductoIndividual("H002", "Cafetera", "Cafetera automática", 150.0, 3.0);
        
        Combo comboHogar = new Combo("Combo Cocina", "Electrodomésticos básicos", 0, "Standard", 200.0, 30.0, "cocina.jpg");
        comboHogar.agregarProducto(licuadora);
        comboHogar.agregarProducto(cafetera);
        hogar.agregarSubcategoria(comboHogar);
        
        // Productos de deportes
        ProductoIndividual bicicleta = new ProductoIndividual("D001", "Bicicleta", "Bicicleta montaña", 500.0, 15.0);
        ProductoIndividual casco = new ProductoIndividual("D002", "Casco", "Casco protector", 40.0, 0.5);
        deportes.agregarSubcategoria(bicicleta);
        deportes.agregarSubcategoria(casco);
        
        // Crear promoción especial
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 45);
        Date fin = cal.getTime();
        
        PaquetePromocional megaPromo = new PaquetePromocional("Mega Oferta", "Aprovecha ahora", 30, inicio, fin, "Solo online", 100);
        megaPromo.agregarProducto(laptop);
        megaPromo.agregarProducto(tablet);
        
        Categoria promociones = new Categoria("Promociones", "Ofertas especiales", 0, "promos", 4, true, 1);
        promociones.agregarSubcategoria(megaPromo);
        
        // Armar tienda completa
        tienda.agregarSubcategoria(electronica);
        tienda.agregarSubcategoria(hogar);
        tienda.agregarSubcategoria(deportes);
        tienda.agregarSubcategoria(promociones);
        
        controller.setRaiz(tienda);
        
        System.out.println("\n=== ESCENARIO: TIENDA COMPLETA CON OPERACIONES COMPLEJAS ===\n");
        System.out.println("=== Estado Inicial ===");
        controller.mostrarInformacion();
        
        double totalInicial = controller.obtenerTotalInventario();
        int itemsIniciales = controller.obtenerCantidadItems();
        
        assertTrue(totalInicial > 0);
        assertTrue(itemsIniciales > 0);
        
        // Operación 1: Descuento en categoría específica
        System.out.println("\n=== Operación 1: 25% descuento en Deportes ===");
        deportes.aplicarDescuento(25);
        controller.mostrarInformacion();
        
        double totalDespuesOp1 = controller.obtenerTotalInventario();
        assertTrue(totalDespuesOp1 < totalInicial);
        
        // Operación 2: Descuento global
        System.out.println("\n=== Operación 2: 10% descuento global ===");
        controller.aplicarDescuentoGlobal(10);
        controller.mostrarInformacion();
        
        double totalDespuesOp2 = controller.obtenerTotalInventario();
        assertTrue(totalDespuesOp2 < totalDespuesOp1);
        
        // Verificaciones finales
        System.out.println("\n=== Verificaciones Finales ===");
        System.out.println("Items totales: " + controller.obtenerCantidadItems());
        System.out.println("Peso total: " + String.format("%.2f", controller.obtenerPesoTotal()) + " kg");
        System.out.println("Descuento total aplicado: " + String.format("%.2f", ((1 - (totalDespuesOp2 / totalInicial)) * 100)) + "%");
        
        assertEquals(itemsIniciales, controller.obtenerCantidadItems());
        assertTrue(megaPromo.validarVigencia());
        assertTrue(comboHogar.validarCombinacion());
        
        System.out.println("\n¡Todas las operaciones completadas exitosamente!");
    }

    @Test
    @DisplayName("E2E - Escenario de Black Friday con múltiples promociones")
    public void testEscenarioBlackFridayMultiplesPromociones() {
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date fin = cal.getTime();
        
        // Crear productos
        ProductoIndividual tv4k = new ProductoIndividual("TV001", "TV 4K", "Smart TV 65 pulgadas", 2000.0, 25.0);
        ProductoIndividual consola = new ProductoIndividual("CON001", "Consola", "Consola gaming", 500.0, 3.0);
        ProductoIndividual juego1 = new ProductoIndividual("JUE001", "Juego AAA", "Juego de acción", 60.0, 0.1);
        ProductoIndividual juego2 = new ProductoIndividual("JUE002", "Juego Aventura", "Juego de aventura", 60.0, 0.1);
        
        // Combo gaming
        Combo comboGaming = new Combo("Pack Gamer", "Todo para gamers", 0, "Ultimate", 550.0, 70.0, "gaming.jpg");
        comboGaming.agregarProducto(consola);
        comboGaming.agregarProducto(juego1);
        comboGaming.agregarProducto(juego2);
        
        // Paquete Black Friday
        PaquetePromocional blackFriday = new PaquetePromocional("Black Friday", "Ofertas imperdibles", 40, inicio, fin, "Stock limitado", 30);
        blackFriday.agregarProducto(tv4k);
        blackFriday.agregarProducto(comboGaming);
        
        tienda.agregarSubcategoria(blackFriday);
        controller.setRaiz(tienda);
        
        System.out.println("\n=== ESCENARIO: BLACK FRIDAY ===\n");
        controller.mostrarInformacion();
        
        // Precio TV: 2000, Combo: 550, Total: 2550
        // Con 40% descuento: 1530
        assertEquals(1530.0, controller.obtenerTotalInventario(), 0.01);
        assertEquals(4, controller.obtenerCantidadItems());
        
        System.out.println("\n--- Detalles de la Promoción ---");
        System.out.println("Precio sin descuento: $2550");
        System.out.println("Descuento Black Friday: 40%");
        System.out.println("Precio final: $" + String.format("%.2f", controller.obtenerTotalInventario()));
        System.out.println("Ahorro total: $" + String.format("%.2f", 2550.0 - controller.obtenerTotalInventario()));
        
        assertTrue(blackFriday.validarVigencia());
        assertTrue(blackFriday.aplicarRestriccion());
    }
}