package Comportamentales.Command.modelo;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private String categoria;
    
    public Producto(String codigo, String nombre, double precio, String categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    @Override
    public String toString() {
        return String.format("%s - $%.2f", nombre, precio);
    }
}