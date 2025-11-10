package e2e.Proxy;

import Estructurales.Proxy.controlador.CatalogoController;
import Estructurales.Proxy.modelo.*;
import Estructurales.Proxy.vista.ConsolaProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Proxy Pattern - End-to-End Tests")
public class ProxyE2ETest {
    private CatalogoController controller;
    private ConsolaProxy vista;

    @BeforeEach
    public void setUp() {
        vista = new ConsolaProxy();
        controller = new CatalogoController(vista);
    }

    @Test
    @DisplayName("E2E - Usuario navega catálogo de productos")
    public void testUsuarioNavegaCatalogo() {
        System.out.println("\n=== ESCENARIO E2E: NAVEGACIÓN DE CATÁLOGO ===\n");
        
        // Usuario abre la aplicación
        System.out.println("Usuario abre la aplicación de catálogo...");
        
        // Sistema carga 20 productos (lazy loading)
        System.out.println("\nCargando catálogo con 20 productos...");
        for (int i = 1; i <= 20; i++) {
            controller.agregarProducto("PROD" + String.format("%03d", i));
        }
        
        assertEquals(20, controller.getCantidadProductos());
        System.out.println("✓ Catálogo cargado con 20 productos");
        
        // Usuario ve la lista (solo miniaturas, sin cargar imágenes completas)
        System.out.println("\nUsuario visualiza lista de productos...");
        long inicio = System.currentTimeMillis();
        controller.mostrarCatalogo();
        long fin = System.currentTimeMillis();
        
        System.out.println("✓ Lista mostrada en " + (fin - inicio) + "ms (lazy loading)");
        
        // Usuario hace clic en el producto 5 (se carga la imagen completa)
        System.out.println("\n--- Usuario selecciona producto PROD005 ---");
        controller.cargarImagen(4);
        controller.mostrarImagen(4);
        
        // Usuario hace clic en el producto 12
        System.out.println("\n--- Usuario selecciona producto PROD012 ---");
        controller.cargarImagen(11);
        controller.mostrarImagen(11);
        
        // Usuario vuelve al producto 5 (carga instantánea desde caché)
        System.out.println("\n--- Usuario vuelve a producto PROD005 (desde caché) ---");
        controller.mostrarImagen(4);
        
        System.out.println("\n✓ Navegación completada exitosamente");
    }

    @Test
    @DisplayName("E2E - Carga inicial rápida con lazy loading")
    public void testCargaInicialRapida() {
        System.out.println("\n=== ESCENARIO E2E: CARGA INICIAL RÁPIDA ===\n");
        
        System.out.println("Simulando carga de catálogo grande...");
        
        // Cargar 100 productos
        long inicio = System.currentTimeMillis();
        for (int i = 1; i <= 100; i++) {
            controller.agregarProducto("IMG" + String.format("%04d", i));
        }
        long fin = System.currentTimeMillis();
        
        long tiempoCarga = fin - inicio;
        System.out.println("Tiempo de carga de 100 productos: " + tiempoCarga + "ms");
        
        assertEquals(100, controller.getCantidadProductos());
        
        // La carga debe ser rápida (menos de 1 segundo)
        assertTrue(tiempoCarga < 1000, "La carga debe ser rápida con lazy loading");
        
        System.out.println("✓ Carga inicial optimizada con Proxy");
    }

    @Test
    @DisplayName("E2E - Usuario busca producto y lo visualiza")
    public void testUsuarioBuscaYVisualizaProducto() {
        System.out.println("\n=== ESCENARIO E2E: BÚSQUEDA Y VISUALIZACIÓN ===\n");
        
        // Preparar catálogo
        System.out.println("Preparando catálogo...");
        String[] productos = {"Laptop", "Mouse", "Teclado", "Monitor", "Webcam"};
        for (String producto : productos) {
            controller.agregarProducto(producto);
        }
        
        // Usuario busca "Monitor"
        System.out.println("\nUsuario busca 'Monitor'...");
        int indiceMonitor = 3;
        
        ImagenProducto monitor = controller.getProducto(indiceMonitor);
        assertNotNull(monitor);
        
        // Usuario visualiza el monitor (primera vez)
        System.out.println("\n--- Primera visualización (carga desde disco) ---");
        long inicio1 = System.currentTimeMillis();
        controller.mostrarImagen(indiceMonitor);
        long fin1 = System.currentTimeMillis();
        long tiempo1 = fin1 - inicio1;
        
        // Usuario cierra y vuelve a abrir el monitor (desde caché)
        System.out.println("\n--- Segunda visualización (desde caché) ---");
        long inicio2 = System.currentTimeMillis();
        controller.mostrarImagen(indiceMonitor);
        long fin2 = System.currentTimeMillis();
        long tiempo2 = fin2 - inicio2;
        
        System.out.println("\nComparación de tiempos:");
        System.out.println("Primera carga: " + tiempo1 + "ms");
        System.out.println("Segunda carga: " + tiempo2 + "ms");
        
        // La segunda carga debe ser más rápida
        assertTrue(tiempo2 < tiempo1, "Caché debe hacer la segunda carga más rápida");
        
        System.out.println("✓ Caché funcionando correctamente");
    }

    @Test
    @DisplayName("E2E - Control de acceso y auditoría")
    public void testControlAccesoYAuditoria() {
        System.out.println("\n=== ESCENARIO E2E: CONTROL DE ACCESO Y AUDITORÍA ===\n");
        
        // Crear producto con proxy
        controller.agregarProducto("CONFIDENCIAL");
        
        System.out.println("Producto confidencial agregado al sistema...");
        
        // Simular múltiples accesos
        System.out.println("\nSimulando accesos de usuario...");
        
        ImagenProducto imagen = controller.getProducto(0);
        ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
        
        // Acceso 1
        System.out.println("\n--- Acceso #1 ---");
        controller.mostrarImagen(0);
        
        // Acceso 2
        System.out.println("\n--- Acceso #2 ---");
        controller.cargarImagen(0);
        
        // Acceso 3
        System.out.println("\n--- Acceso #3 ---");
        controller.mostrarImagen(0);
        
        // Verificar auditoría
        int totalAccesos = proxy.getContadorAccesos();
        System.out.println("\n--- Reporte de Auditoría ---");
        System.out.println("Total de accesos registrados: " + totalAccesos);
        
        assertTrue(totalAccesos >= 3, "Debe haber al menos 3 accesos registrados");
        
        System.out.println("✓ Sistema de auditoría funcionando correctamente");
    }

    @Test
    @DisplayName("E2E - Gestión de memoria con imágenes grandes")
    public void testGestionMemoriaImagenesGrandes() {
        System.out.println("\n=== ESCENARIO E2E: GESTIÓN DE MEMORIA ===\n");
        
        // Simular catálogo con muchas imágenes
        System.out.println("Creando catálogo con 50 imágenes de alta resolución...");
        
        for (int i = 1; i <= 50; i++) {
            controller.agregarProducto("HD_IMAGE_" + i);
        }
        
        System.out.println("✓ 50 productos creados (sin cargar en memoria)");
        
        // Usuario navega y solo ve algunos productos
        System.out.println("\nUsuario navega y visualiza solo 5 productos:");
        int[] productosVistos = {0, 5, 12, 23, 45};
        
        for (int indice : productosVistos) {
            System.out.println("- Cargando producto #" + (indice + 1));
            controller.cargarImagen(indice);
        }
        
        // Verificar que solo se cargaron 5 imágenes
        int imagenesCargadas = 0;
        for (int i = 0; i < 50; i++) {
            ImagenProducto imagen = controller.getProducto(i);
            if (imagen instanceof ImagenProductoProxy) {
                ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
                if (proxy.isCargada()) {
                    imagenesCargadas++;
                }
            }
        }
        
        System.out.println("\nEstadísticas de memoria:");
        System.out.println("Total de productos: 50");
        System.out.println("Imágenes cargadas en memoria: " + imagenesCargadas);
        System.out.println("Ahorro de memoria: " + ((50 - imagenesCargadas) * 100 / 50) + "%");
        
        assertTrue(imagenesCargadas <= 5, "Solo las imágenes vistas deben estar cargadas");
        
        System.out.println("✓ Gestión de memoria optimizada");
    }

    @Test
    @DisplayName("E2E - Limpieza de caché y recarga")
    public void testLimpiezaCacheYRecarga() {
        System.out.println("\n=== ESCENARIO E2E: LIMPIEZA DE CACHÉ ===\n");
        
        // Crear y cargar productos
        System.out.println("Cargando productos iniciales...");
        controller.agregarProducto("IMG001");
        controller.agregarProducto("IMG002");
        controller.agregarProducto("IMG003");
        
        // Cargar todas las imágenes
        System.out.println("\nCargando todas las imágenes en caché...");
        for (int i = 0; i < 3; i++) {
            controller.cargarImagen(i);
        }
        
        // Verificar que están en caché
        System.out.println("Verificando caché...");
        for (int i = 0; i < 3; i++) {
            ImagenProducto imagen = controller.getProducto(i);
            if (imagen instanceof ImagenProductoProxy) {
                ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
                assertTrue(proxy.leerDesdeDisco(), "Imagen debe estar en caché");
            }
        }
        System.out.println("✓ Todas las imágenes en caché");
        
        // Limpiar caché
        System.out.println("\nLimpiando caché...");
        for (int i = 0; i < 3; i++) {
            ImagenProducto imagen = controller.getProducto(i);
            if (imagen instanceof ImagenProductoProxy) {
                ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
                proxy.limpiarCache();
            }
        }
        
        // Verificar que el caché fue limpiado
        System.out.println("Verificando limpieza...");
        for (int i = 0; i < 3; i++) {
            ImagenProducto imagen = controller.getProducto(i);
            if (imagen instanceof ImagenProductoProxy) {
                ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
                assertFalse(proxy.leerDesdeDisco(), "Caché debe estar vacío");
            }
        }
        System.out.println("✓ Caché limpiado exitosamente");
        
        // Recargar una imagen
        System.out.println("\nRecargando imagen...");
        controller.cargarImagen(0);
        
        ImagenProducto imagen = controller.getProducto(0);
        if (imagen instanceof ImagenProductoProxy) {
            ImagenProductoProxy proxy = (ImagenProductoProxy) imagen;
            assertTrue(proxy.leerDesdeDisco(), "Imagen recargada debe estar en caché");
        }
        
        System.out.println("✓ Imagen recargada correctamente");
    }

    @Test
    @DisplayName("E2E - Flujo completo de comercio electrónico")
    public void testFlujoCompletoEcommerce() {
        System.out.println("\n=== ESCENARIO E2E: FLUJO COMPLETO E-COMMERCE ===\n");
        
        // 1. Usuario entra a la tienda online
        System.out.println("PASO 1: Usuario accede a tienda online");
        System.out.println("Cargando catálogo principal...");
        
        String[] categorias = {"Electrónica", "Ropa", "Hogar", "Deportes"};
        for (int i = 0; i < 20; i++) {
            String categoria = categorias[i % categorias.length];
            controller.agregarProducto(categoria + "_PROD_" + (i + 1));
        }
        
        System.out.println("✓ Catálogo cargado: " + controller.getCantidadProductos() + " productos");
        
        // 2. Usuario navega categoría Electrónica
        System.out.println("\nPASO 2: Usuario filtra categoría 'Electrónica'");
        System.out.println("Mostrando productos de Electrónica...");
        for (int i = 0; i < 5; i++) {
            controller.mostrarImagen(i * 4);
        }
        
        // 3. Usuario ve detalles de un producto
        System.out.println("\nPASO 3: Usuario ve detalles de producto");
        System.out.println("Cargando imagen en alta resolución...");
        controller.cargarImagen(0);
        controller.mostrarImagen(0);
        
        ImagenProducto producto = controller.getProducto(0);
        assertNotNull(producto);
        
        // 4. Usuario compara productos (varias vistas)
        System.out.println("\nPASO 4: Usuario compara productos");
        int[] productosComparar = {0, 4, 8};
        for (int indice : productosComparar) {
            System.out.println("Comparando producto #" + (indice + 1));
            controller.mostrarImagen(indice);
        }
        
        // 5. Usuario vuelve al primer producto (caché)
        System.out.println("\nPASO 5: Usuario vuelve al primer producto");
        System.out.println("(Carga instantánea desde caché)");
        long inicio = System.currentTimeMillis();
        controller.mostrarImagen(0);
        long fin = System.currentTimeMillis();
        
        System.out.println("Tiempo de carga: " + (fin - inicio) + "ms");
        
        // 6. Verificar eficiencia del sistema
        System.out.println("\n--- ESTADÍSTICAS FINALES ---");
        System.out.println("Total productos en catálogo: " + controller.getCantidadProductos());
        
        int cargados = 0;
        for (int i = 0; i < controller.getCantidadProductos(); i++) {
            ImagenProducto img = controller.getProducto(i);
            if (img instanceof ImagenProductoProxy) {
                ImagenProductoProxy proxy = (ImagenProductoProxy) img;
                if (proxy.isCargada()) {
                    cargados++;
                }
            }
        }
        
        System.out.println("Productos cargados en memoria: " + cargados);
        System.out.println("Eficiencia de memoria: " + ((controller.getCantidadProductos() - cargados) * 100 / controller.getCantidadProductos()) + "% de ahorro");
        
        assertTrue(cargados < controller.getCantidadProductos(), 
            "No todos los productos deben estar cargados en memoria");
        
        System.out.println("\n✓ Flujo E-Commerce completado exitosamente");
    }
}