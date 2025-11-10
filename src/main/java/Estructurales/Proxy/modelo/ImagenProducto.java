package Estructurales.Proxy.modelo;

public interface ImagenProducto {
    void cargar();
    void mostrar();
    byte[] obtenerTamanio();
    long obtenerDimensiones();
    void redimensionar(int ancho, int alto);
    boolean leerDesdeDisco();
    void procesarImagen();
}