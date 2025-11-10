package Creacionales.FactoryMethod.modelo;

public class ProductoPesoFactory implements ProductoFactory{

    @Override
    public Producto crearProducto(String codigo, String nombre, double precioPorKilo, String categoria) {
        ProductoPeso producto = new ProductoPeso(codigo, nombre, precioPorKilo, categoria);
        producto.setPesoPromedio(1.0);
        return producto;
    }

    @Override
    public String getTipoProducto() {
        return "PESO";
    }

}
