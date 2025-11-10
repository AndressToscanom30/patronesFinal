package Comportamentales.Strategy.modelo;

public class CarritoCompra {
    private double total;
    private DescuentoStrategy estrategia;
    
    public CarritoCompra(double total) {
        this.total = total;
    }
    
    public void setEstrategia(DescuentoStrategy estrategia) {
        this.estrategia = estrategia;
    }
    
    public double calcularTotal() {
        if (estrategia != null) {
            return estrategia.aplicarDescuento(total);
        }
        return total;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public DescuentoStrategy getEstrategia() {
        return estrategia;
    }
}