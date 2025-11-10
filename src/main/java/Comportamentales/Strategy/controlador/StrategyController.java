package Comportamentales.Strategy.controlador;

import Comportamentales.Strategy.modelo.*;
import Comportamentales.Strategy.vista.ConsolaStrategy;

public class StrategyController {
    private ConsolaStrategy vista;

    public StrategyController(ConsolaStrategy vista) {
        this.vista = vista;
    }

    public CarritoCompra crearCarrito(double total) {
        return new CarritoCompra(total);
    }

    public DescuentoStrategy crearDescuentoPorcentaje(double porcentaje) {
        return new DescuentoPorcentaje(porcentaje);
    }

    public DescuentoStrategy crearDescuento2x1() {
        return new Descuento2x1();
    }

    public DescuentoStrategy crearDescuento3x2(int cantidadMin, double porcentaje) {
        return new Descuento3x2(cantidadMin, porcentaje);
    }

    public void aplicarEstrategia(CarritoCompra carrito, DescuentoStrategy estrategia) {
        carrito.setEstrategia(estrategia);
        vista.mostrarMensaje("Estrategia de descuento aplicada");
    }

    public double calcularTotalConDescuento(CarritoCompra carrito) {
        return carrito.calcularTotal();
    }
}