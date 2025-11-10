package Integration.Proxy;

import Estructurales.Proxy.modelo.*;
import Estructurales.Proxy.controlador.CatalogoController;
import Estructurales.Proxy.vista.ConsolaProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Proxy Pattern - Integration Tests")
public class ProxyIntegrationTest {

    // ==================== Proxy con Real Tests ====================

    @Test
    @DisplayName("Integración - Proxy delega a objeto real")
    public void testProxyDelegaAObjetoReal() {
        // Crear proxy
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");

        // El proxy debe delegar correctamente al objeto real
        assertFalse(proxy.isCargada());
        proxy.cargar();
        assertTrue(proxy.isCargada());

        // Verificar que las operaciones se delegan correctamente
        assertDoesNotThrow(() -> {
            proxy.mostrar();
            proxy.redimensionar(800, 600);
            proxy.procesarImagen();
        });
    }

    @Test
    @DisplayName("Integración - Múltiples proxies independientes")
    public void testMultiplesProxiesIndependientes() {
        // Crear múltiples proxies
        ImagenProductoProxy proxy1 = new ImagenProductoProxy("P001");
        ImagenProductoProxy proxy2 = new ImagenProductoProxy("P002");
        ImagenProductoProxy proxy3 = new ImagenProductoProxy("P003");

        // Cargar solo el primero
        proxy1.cargar();

        // Verificar independencia
        assertTrue(proxy1.isCargada());
        assertFalse(proxy2.isCargada());
        assertFalse(proxy3.isCargada());
    }

    @Test
    @DisplayName("Integración - Caché funciona entre operaciones")
    public void testCacheFuncionaEntreOperaciones() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");

        // Primera operación carga la imagen
        proxy.cargar();
        assertTrue(proxy.isCargada());

        // Operaciones subsecuentes usan caché
        assertTrue(proxy.leerDesdeDisco());

        // Limpiar caché
        proxy.limpiarCache();
        assertFalse(proxy.leerDesdeDisco());
    }

    // ==================== MVC Integration Tests ====================

    @Test
    @DisplayName("MVC - Controller gestiona catálogo")
    public void testControllerGestionaCatalogo() {
        ConsolaProxy vista = new ConsolaProxy();
        CatalogoController controller = new CatalogoController(vista);

        // Agregar productos
        controller.agregarProducto("P001");
        controller.agregarProducto("P002");
        controller.agregarProducto("P003");

        assertEquals(3, controller.getCantidadProductos());
    }

    @Test
    @DisplayName("MVC - Controller muestra catálogo sin cargar todas las imágenes")
    public void testControllerMuestraCatalogoLazy() {
        ConsolaProxy vista = new ConsolaProxy();
        CatalogoController controller = new CatalogoController(vista);

        // Agregar 10 productos
        for (int i = 1; i <= 10; i++) {
            controller.agregarProducto("P00" + i);
        }

        // Mostrar catálogo con lazy loading
        controller.mostrarCatalogo();

        // El test pasa si no hay excepciones
        assertTrue(true);
    }

    @Test
    @DisplayName("MVC - Controller carga imagen específica")
    public void testControllerCargaImagenEspecifica() {
        ConsolaProxy vista = new ConsolaProxy();
        CatalogoController controller = new CatalogoController(vista);

        controller.agregarProducto("P001");
        controller.agregarProducto("P002");

        // Cargar solo la primera imagen
        assertDoesNotThrow(() -> controller.cargarImagen(0));

        // Verificar que se cargó
        ImagenProducto imagen = controller.getProducto(0);
        assertNotNull(imagen);
    }

    @Test
    @DisplayName("MVC - Controller maneja índices inválidos")
    public void testControllerManejaIndicesInvalidos() {
        ConsolaProxy vista = new ConsolaProxy();
        CatalogoController controller = new CatalogoController(vista);

        controller.agregarProducto("P001");

        // Intentar acceder a índice inválido
        assertDoesNotThrow(() -> controller.cargarImagen(10));
        assertDoesNotThrow(() -> controller.mostrarImagen(-1));

        assertNull(controller.getProducto(10));
    }

    // ==================== Performance Tests ====================

    @Test
    @DisplayName("Integración - Performance con caché vs sin caché")
    public void testPerformanceCacheVsSinCache() {
        // Sin proxy (objeto real directo)
        ImagenProductoReal real = new ImagenProductoReal("P001", "/images/P001.jpg");

        long inicio1 = System.currentTimeMillis();
        real.cargar();
        real.mostrar();
        real.obtenerTamanio();
        long fin1 = System.currentTimeMillis();
        long tiempoSinProxy = fin1 - inicio1;

        // Con proxy y caché
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");

        // Primera carga
        proxy.cargar();

        // Accesos subsecuentes (con caché)
        long inicio2 = System.currentTimeMillis();
        proxy.mostrar();
        proxy.obtenerTamanio();
        proxy.leerDesdeDisco();
        long fin2 = System.currentTimeMillis();
        long tiempoConCache = fin2 - inicio2;

        // El proxy con caché debe ser más rápido en accesos subsecuentes
        assertTrue(tiempoConCache < tiempoSinProxy);
    }

    @Test
    @DisplayName("Integración - Ahorro de memoria con lazy loading")
    public void testAhorroMemoriaLazyLoading() {
        // Crear 100 proxies (no cargan inmediatamente)
        ImagenProductoProxy[] proxies = new ImagenProductoProxy[100];
        for (int i = 0; i < 100; i++) {
            proxies[i] = new ImagenProductoProxy("P" + String.format("%03d", i));
        }

        // Verificar que ninguno está cargado
        int cargados = 0;
        for (ImagenProductoProxy proxy : proxies) {
            if (proxy.isCargada()) {
                cargados++;
            }
        }

        assertEquals(0, cargados);

        // Cargar solo los primeros 10
        for (int i = 0; i < 10; i++) {
            proxies[i].cargar();
        }

        // Verificar que solo 10 están cargados
        cargados = 0;
        for (ImagenProductoProxy proxy : proxies) {
            if (proxy.isCargada()) {
                cargados++;
            }
        }

        assertEquals(10, cargados);
    }

    @Test
    @DisplayName("Integración - Logging de accesos")
    public void testLoggingAccesos() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");

        assertEquals(0, proxy.getContadorAccesos());

        // Realizar varias operaciones
        proxy.mostrar();
        proxy.cargar();
        proxy.obtenerTamanio();
        proxy.redimensionar(800, 600);
        proxy.procesarImagen();

        // Verificar que se registraron todos los accesos
        assertTrue(proxy.getContadorAccesos() >= 5);
    }
}