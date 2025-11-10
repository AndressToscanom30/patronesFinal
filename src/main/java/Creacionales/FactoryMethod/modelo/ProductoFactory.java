package Creacionales.FactoryMethod.modelo;

public interface ProductoFactory {

    Producto crearProducto(String codigo, String nombre, double precio, String categoria);
    String getTipoProducto();

}
