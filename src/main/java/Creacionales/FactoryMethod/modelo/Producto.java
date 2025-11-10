package Creacionales.FactoryMethod.modelo;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Producto {
    protected String codigo;
    protected String nombre;
    protected double precio;
    protected String categoria;
    protected LocalDate fechaCreacion;

    protected Producto(String codigo, String nombre, double precio, String categoria) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede ser nulo o vacío");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.fechaCreacion = LocalDate.now();
    }

    public abstract double calcularPrecioFinal();

    public abstract boolean requiereRefrigeracion();

    public abstract String obtenerTipo();

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

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(codigo, producto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("%s[codigo=%s, nombre=%s, precio=%.2f]",
                getClass().getSimpleName(), codigo, nombre, precio);
    }
}
