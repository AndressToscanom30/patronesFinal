package Estructurales.Decorator.modelo;

import java.util.ArrayList;
import java.util.List;

public class GarantiaExtendida extends ProductoDecorator {
    private double costoGarantia;
    private int mesesGarantia;
    private String cobertura;

    public GarantiaExtendida(ProductoVendible producto, int mesesGarantia, String cobertura) {
        super(producto);
        this.mesesGarantia = mesesGarantia;
        this.cobertura = cobertura;
        this.costoGarantia = calcularCostoGarantia(mesesGarantia, cobertura, producto);
    }

    private double calcularCostoGarantia(int meses, String cobertura, ProductoVendible producto) {
        // Obtener el precio base del producto (sin decoradores)
        double precioBase = obtenerPrecioBase(producto);
        
        double porcentaje = switch (meses) {
            case 12 -> 0.10;
            case 24 -> 0.18;
            case 36 -> 0.25;
            default -> 0.10;
        };

        if (cobertura.equalsIgnoreCase("COMPLETA")) {
            porcentaje += 0.05;
        }

        return precioBase * porcentaje;
    }
    
    // Método para obtener el precio base sin decoradores
    private double obtenerPrecioBase(ProductoVendible producto) {
        if (producto instanceof ProductoDecorator) {
            return obtenerPrecioBase(((ProductoDecorator) producto).productoBase);
        }
        return producto.obtenerPrecio();
    }

    @Override
    public double obtenerPrecio() {
        return productoBase.obtenerPrecio() + costoGarantia;
    }

    @Override
    public String obtenerDescripcion() {
        return productoBase.obtenerDescripcion() + " + Garantía Extendida " + mesesGarantia + " meses";
    }

    @Override
    public int obtenerTiempoEntrega() {
        return productoBase.obtenerTiempoEntrega() + 2; // AGREGAR ESTE MÉTODO
    }

    @Override
    public List<String> obtenerDetalles() {
        List<String> detalles = new ArrayList<>(productoBase.obtenerDetalles());
        detalles.add("--- Garantía Extendida ---");
        detalles.add("Duración: " + mesesGarantia + " meses");
        detalles.add("Cobertura: " + cobertura);
        detalles.add("Costo garantía: $" + String.format("%.2f", costoGarantia));
        detalles.add("Tiempo adicional: +2 días"); // AGREGAR ESTA LÍNEA
        return detalles;
    }

    public boolean validarAplicabilidad() {
        return obtenerPrecioBase(productoBase) >= 50000;
    }

    public double getCostoGarantia() { return costoGarantia; }
    public int getMesesGarantia() { return mesesGarantia; }
    public String getCobertura() { return cobertura; }
}