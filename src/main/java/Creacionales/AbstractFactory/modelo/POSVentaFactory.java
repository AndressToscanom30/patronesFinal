package Creacionales.AbstractFactory.modelo;


import Creacionales.AbstractFactory.modelo.*;

public class POSVentaFactory implements VentaComponentFactory {

    @Override
    public GeneradorFactura crearGeneradorFactura() {
        return new POSGeneradorFactura();
    }

    @Override
    public ValidadorStock crearValidadorStock() {
        return new POSValidadorStock();
    }

    @Override
    public CalculadorEnvio crearCalculadorEnvio() {
        return new POSCalculadorEnvio();
    }

    @Override
    public GestorDevolucion crearGestorDevolucion() {
        return new POSGestorDevolucion();
    }

    @Override
    public String getTipoCanal() {
        return "POS";
    }
}