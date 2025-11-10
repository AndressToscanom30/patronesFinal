package Comportamentales.Strategy.modelo;

public class Descuento3x2 implements DescuentoStrategy {
    private int cantidadMin;
    private double porcentaje;
    
    public Descuento3x2(int cantidadMin, double porcentaje) {
        this.cantidadMin = cantidadMin;
        this.porcentaje = porcentaje;
    }
    
    @Override
    public double aplicarDescuento(double total) {
        double descuento = total * (porcentaje / 100);
        return total - descuento;
    }
    
    public int getCantidadMin() {
        return cantidadMin;
    }
    
    public void setCantidadMin(int cantidadMin) {
        this.cantidadMin = cantidadMin;
    }
    
    public double getPorcentaje() {
        return porcentaje;
    }
    
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}