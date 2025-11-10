package Estructurales.Decorator.modelo;

import java.util.ArrayList;
import java.util.List;

public class Personalizacion extends ProductoDecorator {
    private double costoPersonalizacion;
    private String textoPersonalizado;
    private String tipoPersona;
    private String posicion;

    public Personalizacion(ProductoVendible producto, String textoPersonalizado, 
                          String tipoPersona, String posicion) {
        super(producto);
        this.textoPersonalizado = textoPersonalizado;
        this.tipoPersona = tipoPersona;
        this.posicion = posicion;
        this.costoPersonalizacion = calcularCostoPersonalizacion(tipoPersona, textoPersonalizado);
    }

    private double calcularCostoPersonalizacion(String tipo, String texto) {
        double costo = switch (tipo.toUpperCase()) {
            case "GRABADO" -> 5000;
            case "BORDADO" -> 8000;
            case "IMPRESION" -> 3000;
            default -> 3000;
        };

        if (texto.length() > 20) {
            costo += 2000;
        }

        return costo;
    }

    @Override
    public double obtenerPrecio() {
        return productoBase.obtenerPrecio() + costoPersonalizacion;
    }

    @Override
    public String obtenerDescripcion() {
        return productoBase.obtenerDescripcion() + " + Personalización " + tipoPersona;
    }

    @Override
    public int obtenerTiempoEntrega() {
        return productoBase.obtenerTiempoEntrega() + 2;
    }

    @Override
    public List<String> obtenerDetalles() {
        List<String> detalles = new ArrayList<>(productoBase.obtenerDetalles());
        detalles.add("--- Personalización ---");
        detalles.add("Tipo: " + tipoPersona);
        detalles.add("Texto: " + textoPersonalizado);
        detalles.add("Posición: " + posicion);
        detalles.add("Costo: $" + String.format("%.2f", costoPersonalizacion));
        detalles.add("Tiempo adicional: +2 días");
        return detalles;
    }

    public boolean validarTexto() {
        if (textoPersonalizado == null || textoPersonalizado.trim().isEmpty()) {
            return false;
        }
        
        if (textoPersonalizado.length() > 50) {
            return false;
        }
        
        return textoPersonalizado.matches("[a-zA-Z0-9 áéíóúñÁÉÍÓÚÑ.,!-]*");
    }

    public double getCostoPersonalizacion() { return costoPersonalizacion; }
    public String getTextoPersonalizado() { return textoPersonalizado; }
    public String getTipoPersona() { return tipoPersona; }
    public String getPosicion() { return posicion; }
}