package Estructurales.Decorator.modelo;

import java.util.ArrayList;
import java.util.List;

public class ProductoBase implements ProductoVendible {
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precioBase;
    private double peso;

    public ProductoBase(String codigo, String nombre, String descripcion, double precioBase, double peso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.peso = peso;
    }

    @Override
    public double obtenerPrecio() {
        return precioBase;
    }

    @Override
    public String obtenerDescripcion() {
        return descripcion;
    }

    @Override
    public double obtenerPeso() {
        return peso;
    }

    @Override
    public boolean requiereRefrigeracion() {
        return false;
    }

    @Override
    public int obtenerTiempoEntrega() {
        return 3;
    }

    @Override
    public List<String> obtenerDetalles() {
        List<String> detalles = new ArrayList<>();
        detalles.add("Código: " + codigo);
        detalles.add("Nombre: " + nombre);
        detalles.add("Precio base: $" + String.format("%.2f", precioBase));
        detalles.add("Peso: " + peso + "kg");
        return detalles;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }

    @Override
    public String toString() {
        return String.format("%s - %s ($%.2f)", codigo, nombre, precioBase);
    }
}