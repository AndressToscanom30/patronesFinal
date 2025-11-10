package Comportamentales.Strategy.modelo;

public class Descuento2x1 implements DescuentoStrategy {
    
    @Override
    public double aplicarDescuento(double total) {
        return total / 2;
    }
}