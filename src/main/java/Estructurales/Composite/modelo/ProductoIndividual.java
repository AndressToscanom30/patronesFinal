package Estructurales.Composite.modelo;

public class ProductoIndividual implements ComponenteInventario {
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private double peso;
    private double descuentoAcumulado;

    public ProductoIndividual(String codigo, String nombre, String descripcion, double precio, double peso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.peso = peso;
        this.descuentoAcumulado = 0.0;
    }

    @Override
    public double calcularTotal() {
        return precio * (1 - descuentoAcumulado / 100);
    }

    @Override
    public double calcularPesoTotal() {
        return peso;
    }

    @Override
    public int contarItems() {
        return 1;
    }

    @Override
    public void aplicarDescuento(double porcentaje) {
        this.descuentoAcumulado += porcentaje;
    }

    public boolean esCompuesto() {
        return false;
    }

    public void ajustarStock(int cantidad) {
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public double getPeso() {
        return peso;
    }

    public double getDescuentoAcumulado() {
        return descuentoAcumulado;
    }

    public void setDescuentoAcumulado(double descuentoAcumulado) {
        this.descuentoAcumulado = descuentoAcumulado;
    }
}
