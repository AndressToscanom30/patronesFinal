package Creacionales.AbstractFactory.modelo;

import Creacionales.AbstractFactory.modelo.*;

public class POSGestorDevolucion implements GestorDevolucion {
    private static final int DIAS_DEVOLUCION = 15;
    private static final double PENALIZACION_POR_DIA = 0.02; // 2% por día

    @Override
    public boolean permitirDevolucion(String codigoVenta, int diasTranscurridos) {
        return diasTranscurridos <= DIAS_DEVOLUCION;
    }

    @Override
    public double calcularReembolso(double montoOriginal, int diasTranscurridos) {
        if (diasTranscurridos > DIAS_DEVOLUCION) {
            return 0.0;
        }
        
        if (diasTranscurridos <= 7) {
            return montoOriginal; // Reembolso completo primera semana
        }
        
        // Después de 7 días, penalización
        double penalizacion = montoOriginal * PENALIZACION_POR_DIA * (diasTranscurridos - 7);
        return Math.max(0, montoOriginal - penalizacion);
    }

    @Override
    public String obtenerPoliticaDevolucion() {
        return "POS: 15 días, reembolso completo primeros 7 días, " +
               "penalización 2% por día después del día 7";
    }

    @Override
    public String obtenerTipoGestor() {
        return "Gestor POS - Devolución con Ticket Físico";
    }
}
