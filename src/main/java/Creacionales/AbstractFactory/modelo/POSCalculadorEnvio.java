package Creacionales.AbstractFactory.modelo;

import Creacionales.AbstractFactory.modelo.*;

public class POSCalculadorEnvio implements CalculadorEnvio {

    @Override
    public double calcularCostoEnvio(String destino, double peso) {
        return 0.0; // POS no tiene envío, compra presencial
    }

    @Override
    public int calcularTiempoEntrega(String destino) {
        return 0;
    }

    @Override
    public boolean soportaEnvioExpress() {
        return false;
    }

    @Override
    public String obtenerTipoCalculador() {
        return "Calculador POS - Sin Envío (Retiro Inmediato)";
    }
}