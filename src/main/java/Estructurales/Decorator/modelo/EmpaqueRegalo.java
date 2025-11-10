package Estructurales.Decorator.modelo;

import java.util.ArrayList;
import java.util.List;

public class EmpaqueRegalo extends ProductoDecorator {
    private double costoEmpaque;
    private String tipoEmpaque;
    private String incluyeTarjeta;

    public EmpaqueRegalo(ProductoVendible producto, String tipoEmpaque, boolean incluyeTarjeta) {
        super(producto);
        this.tipoEmpaque = tipoEmpaque;
        this.incluyeTarjeta = incluyeTarjeta ? "Sí" : "No";
        this.costoEmpaque = calcularCostoEmpaque(tipoEmpaque, incluyeTarjeta);
    }

    private double calcularCostoEmpaque(String tipo, boolean tarjeta) {
        double costo = switch (tipo.toUpperCase()) {
            case "BASICO" -> 3000;
            case "PREMIUM" -> 8000;
            case "LUJO" -> 15000;
            default -> 3000;
        };
        
        if (tarjeta) {
            costo += 2000;
        }
        
        return costo;
    }

    @Override
    public double obtenerPrecio() {
        return productoBase.obtenerPrecio() + costoEmpaque;
    }

    @Override
    public String obtenerDescripcion() {
        return productoBase.obtenerDescripcion() + " + Empaque Regalo " + tipoEmpaque;
    }

    @Override
    public double obtenerPeso() {
        return productoBase.obtenerPeso() + 0.2;
    }

    @Override
    public int obtenerTiempoEntrega() {
        return productoBase.obtenerTiempoEntrega() + 3; // AGREGAR ESTE MÉTODO
    }

    @Override
    public List<String> obtenerDetalles() {
        List<String> detalles = new ArrayList<>(productoBase.obtenerDetalles());
        detalles.add("--- Empaque Regalo ---");
        detalles.add("Tipo: " + tipoEmpaque);
        detalles.add("Costo empaque: $" + String.format("%.2f", costoEmpaque));
        detalles.add("Incluye tarjeta: " + incluyeTarjeta);
        detalles.add("Peso adicional: 0.2kg");
        detalles.add("Tiempo adicional: +3 días"); // AGREGAR ESTA LÍNEA
        return detalles;
    }

    public double getCostoEmpaque() { return costoEmpaque; }
    public String getTipoEmpaque() { return tipoEmpaque; }
    public String getIncluyeTarjeta() { return incluyeTarjeta; }
}