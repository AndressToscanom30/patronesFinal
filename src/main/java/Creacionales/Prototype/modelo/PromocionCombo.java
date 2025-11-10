package Creacionales.Prototype.modelo;

import java.util.ArrayList;
import java.util.List;

public class PromocionCombo extends Promocion {
    private List<String> productosRequeridos;
    private double precioCombo;

    public PromocionCombo(String id, String nombre, String descripcion) {
        super(id, nombre, descripcion);
        this.productosRequeridos = new ArrayList<>();
        this.precioCombo = 0.0;
    }

    @Override
    public double aplicarDescuento(double monto) {
        // El descuento se calcula comparando precio combo vs suma de productos
        return precioCombo;
    }

    public double calcularAhorro(double sumaPreciosIndividuales) {
        return sumaPreciosIndividuales - precioCombo;
    }

    @Override
    public boolean validar() {
        return !productosRequeridos.isEmpty() && precioCombo > 0;
    }

    @Override
    public String obtenerTipo() {
        return "COMBO";
    }

    @Override
    public PromocionCombo clone() {
        PromocionCombo clonada = (PromocionCombo) super.clone();
        clonada.productosRequeridos = new ArrayList<>(this.productosRequeridos);
        clonada.precioCombo = this.precioCombo;
        return clonada;
    }

    public List<String> getProductosRequeridos() {
        return new ArrayList<>(productosRequeridos);
    }

    public double getPrecioCombo() { return precioCombo; }

    public void setPrecioCombo(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio del combo no puede ser negativo");
        }
        this.precioCombo = precio;
    }

    public void agregarProducto(String codigoProducto) {
        if (!productosRequeridos.contains(codigoProducto)) {
            productosRequeridos.add(codigoProducto);
        }
    }

    public boolean contieneProducto(String codigoProducto) {
        return productosRequeridos.contains(codigoProducto);
    }
}
