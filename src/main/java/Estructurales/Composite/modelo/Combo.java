package Estructurales.Composite.modelo;

import java.util.ArrayList;
import java.util.List;

public class Combo extends ProductoIndividual {
    private String tipo;
    private double precioEspecial;
    private double ahorro;
    private String imagen;
    private List<ComponenteInventario> hijos;

    public Combo(String nombre, String descripcion, double descuentoAcumulado, String tipo, double precioEspecial, double ahorro, String imagen) {
        super("COMBO-" + nombre, nombre, descripcion, precioEspecial, 0.0);
        this.tipo = tipo;
        this.precioEspecial = precioEspecial;
        this.ahorro = ahorro;
        this.imagen = imagen;
        this.hijos = new ArrayList<>();
    }

    public double calcularAhorro() {
        double precioRegular = 0;
        // Calculamos el total sin descuentos
        for (ComponenteInventario hijo : hijos) {
            if (hijo instanceof ProductoIndividual) {
                precioRegular += ((ProductoIndividual) hijo).getPrecio();
            } else {
                precioRegular += hijo.calcularTotal();
            }
        }
        return precioRegular - precioEspecial;
    }

    public boolean validarCombinacion() {
        return !hijos.isEmpty();
    }

    public void agregarProducto(ComponenteInventario componente) {
        hijos.add(componente);
    }

    @Override
    public double calcularTotal() {
        // En un combo, los descuentos son independientes del precio especial
        double precioConDescuento = precioEspecial;
        if (getDescuentoAcumulado() > 0) {
            precioConDescuento *= (1 - getDescuentoAcumulado() / 100);
        }
        return precioConDescuento;
    }

    @Override
    public double calcularPesoTotal() {
        double pesoTotal = 0;
        for (ComponenteInventario hijo : hijos) {
            pesoTotal += hijo.calcularPesoTotal();
        }
        return pesoTotal;
    }

    @Override
    public int contarItems() {
        int count = 0;
        for (ComponenteInventario hijo : hijos) {
            count += hijo.contarItems();
        }
        return count;
    }

    @Override
    public boolean esCompuesto() {
        return true;
    }

    public List<ComponenteInventario> obtenerHijos() {
        return new ArrayList<>(hijos);
    }

    public String getTipo() {
        return tipo;
    }

    public double getPrecioEspecial() {
        return precioEspecial;
    }

    public double getAhorro() {
        return ahorro;
    }

    public String getImagen() {
        return imagen;
    }
}