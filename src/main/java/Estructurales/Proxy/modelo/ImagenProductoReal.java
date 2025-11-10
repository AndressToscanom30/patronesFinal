package Estructurales.Proxy.modelo;

public class ImagenProductoReal implements ImagenProducto {
    private String codigoProducto;
    private String rutaArchivo;
    private byte[] datosImagen;
    private long size;
    private int ancho;
    private int alto;
    
    public ImagenProductoReal(String codigoProducto, String rutaArchivo) {
        this.codigoProducto = codigoProducto;
        this.rutaArchivo = rutaArchivo;
        this.datosImagen = new byte[0];
    }
    
    @Override
    public void cargar() {
        System.out.println("ImagenProductoReal: Cargando imagen de alta resolución desde disco...");
        System.out.println("Ruta: " + rutaArchivo);
        leerDesdeDisco();
        procesarImagen();
        System.out.println("Imagen cargada: " + size + " bytes");
    }
    
    @Override
    public void mostrar() {
        if (datosImagen.length == 0) {
            cargar();
        }
        System.out.println("ImagenProductoReal: Mostrando imagen del producto " + codigoProducto);
        System.out.println("Dimensiones: " + ancho + "x" + alto);
    }
    
    @Override
    public byte[] obtenerTamanio() {
        return datosImagen;
    }
    
    @Override
    public long obtenerDimensiones() {
        return size;
    }
    
    @Override
    public void redimensionar(int ancho, int alto) {
        System.out.println("ImagenProductoReal: Redimensionando imagen a " + ancho + "x" + alto);
        this.ancho = ancho;
        this.alto = alto;
        procesarImagen();
    }
    
    @Override
    public boolean leerDesdeDisco() {
        System.out.println("ImagenProductoReal: Leyendo archivo desde disco...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        this.datosImagen = new byte[1024 * 1024 * 5];
        this.size = datosImagen.length;
        this.ancho = 1920;
        this.alto = 1080;
        return true;
    }
    
    @Override
    public void procesarImagen() {
        System.out.println("ImagenProductoReal: Procesando imagen...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public String getCodigoProducto() {
        return codigoProducto;
    }
    
    public String getRutaArchivo() {
        return rutaArchivo;
    }
}