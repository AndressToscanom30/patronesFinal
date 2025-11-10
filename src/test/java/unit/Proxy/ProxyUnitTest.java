package unit.Proxy;

import Estructurales.Proxy.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Proxy Pattern - Unit Tests")
public class ProxyUnitTest {
    
    // ==================== ImagenProductoReal Tests ====================
    
    @Test
    @DisplayName("ImagenProductoReal - Crear imagen")
    public void testCrearImagenReal() {
        ImagenProductoReal imagen = new ImagenProductoReal("P001", "/images/P001.jpg");
        assertNotNull(imagen);
        assertEquals("P001", imagen.getCodigoProducto());
        assertEquals("/images/P001.jpg", imagen.getRutaArchivo());
    }
    
    @Test
    @DisplayName("ImagenProductoReal - Cargar imagen")
    public void testCargarImagenReal() {
        ImagenProductoReal imagen = new ImagenProductoReal("P001", "/images/P001.jpg");
        assertDoesNotThrow(() -> imagen.cargar());
        assertTrue(imagen.obtenerDimensiones() > 0);
    }
    
    @Test
    @DisplayName("ImagenProductoReal - Mostrar imagen")
    public void testMostrarImagenReal() {
        ImagenProductoReal imagen = new ImagenProductoReal("P001", "/images/P001.jpg");
        assertDoesNotThrow(() -> imagen.mostrar());
    }
    
    @Test
    @DisplayName("ImagenProductoReal - Obtener tamano")
    public void testObtenerTamanioReal() {
        ImagenProductoReal imagen = new ImagenProductoReal("P001", "/images/P001.jpg");
        imagen.cargar();
        byte[] datos = imagen.obtenerTamanio();
        assertNotNull(datos);
        assertTrue(datos.length > 0);
    }
    
    @Test
    @DisplayName("ImagenProductoReal - Obtener dimensiones")
    public void testObtenerDimensionesReal() {
        ImagenProductoReal imagen = new ImagenProductoReal("P001", "/images/P001.jpg");
        imagen.cargar();
        long dimensiones = imagen.obtenerDimensiones();
        assertTrue(dimensiones > 0);
    }
    
    @Test
    @DisplayName("ImagenProductoReal - Redimensionar")
    public void testRedimensionarReal() {
        ImagenProductoReal imagen = new ImagenProductoReal("P001", "/images/P001.jpg");
        assertDoesNotThrow(() -> imagen.redimensionar(800, 600));
    }
    
    @Test
    @DisplayName("ImagenProductoReal - Procesar imagen")
    public void testProcesarImagen() {
        ImagenProductoReal imagen = new ImagenProductoReal("P001", "/images/P001.jpg");
        imagen.cargar();
        assertDoesNotThrow(() -> imagen.procesarImagen());
    }
    
    // ==================== ImagenProductoProxy Tests ====================
    
    @Test
    @DisplayName("ImagenProductoProxy - Crear proxy")
    public void testCrearProxy() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        assertNotNull(proxy);
        assertEquals("P001", proxy.getCodigoProducto());
        assertFalse(proxy.isCargada());
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Lazy loading")
    public void testLazyLoading() {
        // El proxy NO debe cargar la imagen al instanciarse
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        assertFalse(proxy.isCargada());
        
        // La imagen se carga cuando se solicita
        proxy.cargar();
        assertTrue(proxy.isCargada());
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Mostrar sin cargar previamente")
    public void testMostrarSinCargar() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        assertDoesNotThrow(() -> proxy.mostrar());
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Cargar y mostrar")
    public void testCargarYMostrar() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        proxy.cargar();
        assertDoesNotThrow(() -> proxy.mostrar());
        assertTrue(proxy.isCargada());
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Obtener tamano")
    public void testObtenerTamanioProxy() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        byte[] datos = proxy.obtenerTamanio();
        assertNotNull(datos);
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Obtener dimensiones sin cargar")
    public void testObtenerDimensionesSinCargar() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        long dimensiones = proxy.obtenerDimensiones();
        assertEquals(0, dimensiones);
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Redimensionar")
    public void testRedimensionarProxy() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        assertDoesNotThrow(() -> proxy.redimensionar(800, 600));
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Leer desde disco con caché")
    public void testLeerDesdeDisco() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        assertFalse(proxy.leerDesdeDisco());
        
        proxy.cargar();
        assertTrue(proxy.leerDesdeDisco());
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Procesar imagen")
    public void testProcesarImagenProxy() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        assertDoesNotThrow(() -> proxy.procesarImagen());
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Contador de accesos")
    public void testContadorAccesos() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        assertEquals(0, proxy.getContadorAccesos());
        
        proxy.mostrar();
        assertTrue(proxy.getContadorAccesos() > 0);
        
        proxy.cargar();
        assertTrue(proxy.getContadorAccesos() > 1);
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Limpiar caché")
    public void testLimpiarCache() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        proxy.cargar();
        assertTrue(proxy.isCargada());
        
        proxy.limpiarCache();
        assertFalse(proxy.leerDesdeDisco());
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Caché funcional")
    public void testCacheFuncional() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        
        // Primera carga (lenta)
        long inicio1 = System.currentTimeMillis();
        proxy.cargar();
        long fin1 = System.currentTimeMillis();
        long tiempo1 = fin1 - inicio1;
        
        // Segunda carga (rápida desde caché)
        long inicio2 = System.currentTimeMillis();
        proxy.cargar();
        long fin2 = System.currentTimeMillis();
        long tiempo2 = fin2 - inicio2;
        
        // La segunda carga debe ser más rápida
        assertTrue(tiempo2 < tiempo1);
    }
    
    @Test
    @DisplayName("ImagenProductoProxy - Múltiples accesos con caché")
    public void testMultiplesAccesosConCache() {
        ImagenProductoProxy proxy = new ImagenProductoProxy("P001");
        
        // Primer acceso
        proxy.mostrar();
        int accesos1 = proxy.getContadorAccesos();
        
        // Segundo acceso
        proxy.mostrar();
        int accesos2 = proxy.getContadorAccesos();
        
        // Tercer acceso
        proxy.obtenerTamanio();
        int accesos3 = proxy.getContadorAccesos();
        
        assertTrue(accesos2 > accesos1);
        assertTrue(accesos3 > accesos2);
    }
}