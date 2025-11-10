package Estructurales.Decorator.modelo;

import java.util.List;

public interface ProductoVendible {
    double obtenerPrecio();
    String obtenerDescripcion();
    double obtenerPeso();
    boolean requiereRefrigeracion();
    int obtenerTiempoEntrega();
    List<String> obtenerDetalles();
}