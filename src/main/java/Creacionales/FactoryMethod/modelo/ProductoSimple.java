package Creacionales.FactoryMethod.modelo;

public class ProductoSimple extends Producto {
    private int stock;
    private boolean refrigeracion;

    public ProductoSimple(String codigo, String nombre, double precio, String categoria) {
        super(codigo, nombre, precio, categoria);
        this.stock = 0;
        this.refrigeracion = false;
    }

    @Override
    public double calcularPrecioFinal() {
        return precio;
    }

    @Override
    public boolean requiereRefrigeracion() {
        return refrigeracion;
    }

    @Override
    public String obtenerTipo() {
        return "SIMPLE";
    }

    public int getStock() { return stock; }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    public void setRefrigeracion(boolean refrigeracion) {
        this.refrigeracion = refrigeracion;
    }
}
