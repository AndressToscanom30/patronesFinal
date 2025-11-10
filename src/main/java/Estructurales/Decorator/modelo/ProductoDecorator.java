package Estructurales.Decorator.modelo;

import java.util.List;

public abstract class ProductoDecorator implements ProductoVendible {
    protected ProductoVendible productoBase;

    public ProductoDecorator(ProductoVendible producto) {
        this.productoBase = producto;
    }

    @Override
    public double obtenerPrecio() {
        return productoBase.obtenerPrecio();
    }

    @Override
    public String obtenerDescripcion() {
        return productoBase.obtenerDescripcion();
    }

    @Override
    public double obtenerPeso() {
        return productoBase.obtenerPeso();
    }

    @Override
    public boolean requiereRefrigeracion() {
        return productoBase.requiereRefrigeracion();
    }

    @Override
    public int obtenerTiempoEntrega() {
        return productoBase.obtenerTiempoEntrega();
    }

    @Override
    public List<String> obtenerDetalles() {
        return productoBase.obtenerDetalles();
    }
}