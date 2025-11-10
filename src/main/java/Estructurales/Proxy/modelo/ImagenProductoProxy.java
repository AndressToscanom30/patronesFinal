package Estructurales.Proxy.modelo;

import java.util.HashMap;
import java.util.Map;

public class ImagenProductoProxy implements ImagenProducto {
    private String codigoProducto;
    private ImagenProductoReal imagenReal;
    private boolean cargada;
    private boolean cargadaCache;
    private Map<String, byte[]> cache;
    private int contadorAccesos;
    
    public ImagenProductoProxy(String codigoProducto) {
        this.codigoProducto = codigoProducto;
        this.cargada = false;
        this.cargadaCache = false;
        this.cache = new HashMap<>();
        this.contadorAccesos = 0;
    }
    
    @Override
    public void cargar() {
        registrarAcceso();
        
        if (cargadaCache) {
            System.out.println("ImagenProductoProxy: Imagen ya en caché, carga instantánea");
            return;
        }
        
        System.out.println("ImagenProductoProxy: Verificando permisos de acceso...");
        System.out.println("ImagenProductoProxy: Iniciando carga lazy de imagen...");
        
        if (imagenReal == null) {
            String rutaArchivo = "/images/productos/" + codigoProducto + ".jpg";
            imagenReal = new ImagenProductoReal(codigoProducto, rutaArchivo);
        }
        
        imagenReal.cargar();
        guardarEnCache();
        cargada = true;
        cargadaCache = true;
    }
    
    @Override
    public void mostrar() {
        registrarAcceso();
        
        System.out.println("ImagenProductoProxy: Solicitando mostrar imagen...");
        
        if (leerDesdeDisco()) {
            System.out.println("ImagenProductoProxy: Imagen obtenida desde caché");
            System.out.println("ImagenProductoProxy: Mostrando miniatura desde proxy");
            return;
        }
        
        if (imagenReal == null) {
            cargar();
        }
        
        imagenReal.mostrar();
    }
    
    @Override
    public byte[] obtenerTamanio() {
        registrarAcceso();
        
        if (cargadaCache && cache.containsKey(codigoProducto)) {
            System.out.println("ImagenProductoProxy: Obteniendo tamano desde caché");
            return cache.get(codigoProducto);
        }
        
        if (imagenReal == null) {
            cargar();
        }
        
        return imagenReal.obtenerTamanio();
    }
    
    @Override
    public long obtenerDimensiones() {
        registrarAcceso();
        
        if (imagenReal == null) {
            System.out.println("ImagenProductoProxy: Retornando dimensiones sin cargar imagen completa");
            return 0;
        }
        
        return imagenReal.obtenerDimensiones();
    }
    
    @Override
    public void redimensionar(int ancho, int alto) {
        registrarAcceso();
        System.out.println("ImagenProductoProxy: Redireccionando operación de redimensionar...");
        
        if (imagenReal == null) {
            cargar();
        }
        
        imagenReal.redimensionar(ancho, alto);
        guardarEnCache();
    }
    
    @Override
    public boolean leerDesdeDisco() {
        if (cargadaCache) {
            System.out.println("ImagenProductoProxy: Lectura desde caché en memoria");
            return true;
        }
        return false;
    }
    
    @Override
    public void procesarImagen() {
        registrarAcceso();
        System.out.println("ImagenProductoProxy: Procesamiento delegado a imagen real");
        
        if (imagenReal == null) {
            cargar();
        }
        
        imagenReal.procesarImagen();
    }
    
    private void guardarEnCache() {
        if (imagenReal != null) {
            System.out.println("ImagenProductoProxy: Guardando imagen en caché...");
            cache.put(codigoProducto, imagenReal.obtenerTamanio());
        }
    }
    
    private void registrarAcceso() {
        contadorAccesos++;
        System.out.println("ImagenProductoProxy: Acceso #" + contadorAccesos + " registrado");
    }
    
    public void limpiarCache() {
        System.out.println("ImagenProductoProxy: Limpiando caché...");
        cache.clear();
        cargadaCache = false;
    }
    
    public boolean isCargada() {
        return cargada;
    }
    
    public int getContadorAccesos() {
        return contadorAccesos;
    }
    
    public String getCodigoProducto() {
        return codigoProducto;
    }
}