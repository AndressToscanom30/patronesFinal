package Comportamentales.Strategy.modelo;

public class DescuentoPorcentaje implements DescuentoStrategy {
    private double porcentaje;
    
    public DescuentoPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    @Override
    public double aplicarDescuento(double total) {
        double descuento = total * (porcentaje / 100);
        return total - descuento;
    }
    
    public double getPorcentaje() {
        return porcentaje;
    }
    
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}