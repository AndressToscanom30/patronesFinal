package Creacionales.FactoryMethod.modelo;

public class ProductoSimpleFactory implements ProductoFactory {

    @Override
    public Producto crearProducto(String codigo, String nombre, double precio, String categoria) {
        ProductoSimple producto = new ProductoSimple(codigo, nombre, precio, categoria);
        producto.setStock(0);
        return producto;
    }

    @Override
    public String getTipoProducto() {
        return "SIMPLE";
    }

}
