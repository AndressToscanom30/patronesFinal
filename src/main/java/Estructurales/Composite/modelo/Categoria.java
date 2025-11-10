package Estructurales.Composite.modelo;

import java.util.ArrayList;
import java.util.List;

public class Categoria extends ProductoIndividual {
    private String icono;
    private int orden;
    private boolean visible;
    private int nivel;
    private List<ComponenteInventario> hijos;

    public Categoria(String nombre, String descripcion, double descuentoAcumulado, String icono, int orden, boolean visible, int nivel) {
        super("CAT-" + nombre, nombre, descripcion, 0.0, 0.0);
        this.icono = icono;
        this.orden = orden;
        this.visible = visible;
        this.nivel = nivel;
        this.hijos = new ArrayList<>();
        if (descuentoAcumulado > 0) {
            setDescuentoAcumulado(descuentoAcumulado);
        }
    }

    public void agregarSubcategoria(ComponenteInventario componente) {
        hijos.add(componente);
    }

    public String obtenerRuta() {
        return getNombre();
    }

    @Override
    public double calcularTotal() {
        double total = 0;
        for (ComponenteInventario hijo : hijos) {
            total += hijo.calcularTotal();
        }
        // Aplicar el descuento de la categoría
        return total * (1 - getDescuentoAcumulado() / 100);
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
    public void aplicarDescuento(double porcentaje) {
        // Solo propagar el descuento a los hijos, NO acumularlo en la categoría
        for (ComponenteInventario hijo : hijos) {
            hijo.aplicarDescuento(porcentaje);
        }
    }

    @Override
    public boolean esCompuesto() {
        return true;
    }

    public List<ComponenteInventario> obtenerHijos() {
        return new ArrayList<>(hijos);
    }

    public String getIcono() {
        return icono;
    }

    public int getOrden() {
        return orden;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getNivel() {
        return nivel;
    }
}