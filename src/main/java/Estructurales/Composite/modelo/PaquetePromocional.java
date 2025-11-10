package Estructurales.Composite.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaquetePromocional extends ProductoIndividual {
    private Date fechaInicio;
    private Date fechaFin;
    private String condiciones;
    private int limiteUnidades;
    private List<ComponenteInventario> hijos;

    public PaquetePromocional(String nombre, String descripcion, double descuentoAcumulado, Date fechaInicio, Date fechaFin, String condiciones, int limiteUnidades) {
        super("PROMO-" + nombre, nombre, descripcion, 0.0, 0.0);
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.condiciones = condiciones;
        this.limiteUnidades = limiteUnidades;
        this.hijos = new ArrayList<>();
        // Establecer descuento inicial
        if (descuentoAcumulado > 0) {
            setDescuentoAcumulado(descuentoAcumulado);
        }
    }

    public boolean validarVigencia() {
        Date ahora = new Date();
        // Usar compareTo o agregar equals para incluir el mismo día
        return !ahora.before(fechaInicio) && !ahora.after(fechaFin);
    }

    public boolean aplicarRestriccion() {
        return validarVigencia() && contarItems() <= limiteUnidades;
    }

    public void agregarProducto(ComponenteInventario componente) {
        hijos.add(componente);
    }

    @Override
    public double calcularTotal() {
        double totalSinDescuento = 0;
        for (ComponenteInventario hijo : hijos) {
            totalSinDescuento += hijo.calcularTotal(); // Obtiene el total de los hijos con sus propios descuentos
        }
        // Si el paquete está vigente, aplicar el descuento del paquete promocional
        if (validarVigencia()) {
            return totalSinDescuento * (1 - getDescuentoAcumulado() / 100);
        }
        return totalSinDescuento;
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
        // En un paquete promocional, los descuentos se combinan multiplicativamente
        double descuentoActual = getDescuentoAcumulado();
        double descuentoMultiplicativo = (1 - porcentaje/100.0) * (1 - descuentoActual/100.0);
        setDescuentoAcumulado((1 - descuentoMultiplicativo) * 100.0);
        // No propagar el descuento a los hijos
    }

    @Override
    public boolean esCompuesto() {
        return true;
    }

    public List<ComponenteInventario> obtenerHijos() {
        return new ArrayList<>(hijos);
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public int getLimiteUnidades() {
        return limiteUnidades;
    }
}