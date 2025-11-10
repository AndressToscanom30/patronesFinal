package Comportamentales.Command.modelo;

public class AplicarDescuentoCommand implements Command {
    private CarritoCompras carrito;
    private double porcentaje;
    private double descuentoAnterior;
    
    public AplicarDescuentoCommand(CarritoCompras carrito, double porcentaje) {
        this.carrito = carrito;
        this.porcentaje = porcentaje;
    }
    
    @Override
    public void ejecutar() {
        descuentoAnterior = carrito.getDescuento();
        carrito.aplicarDescuento(porcentaje);
    }
    
    @Override
    public void deshacer() {
        carrito.aplicarDescuento(descuentoAnterior);
    }
    
    @Override
    public String obtenerDescripcion() {
        return String.format("Aplicar descuento del %.2f%%", porcentaje);
    }
    
    public double getPorcentaje() {
        return porcentaje;
    }
}