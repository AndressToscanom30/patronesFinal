package Creacionales.AbstractFactory.modelo;

public interface GestorDevolucion {
    boolean permitirDevolucion(String codigoVenta, int diasTranscurridos);
    double calcularReembolso(double montoOriginal, int diasTranscurridos);
    String obtenerPoliticaDevolucion();
    String obtenerTipoGestor();
}