package Creacionales.AbstractFactory.modelo;

public interface CalculadorEnvio {
    double calcularCostoEnvio(String destino, double peso);
    int calcularTiempoEntrega(String destino);
    boolean soportaEnvioExpress();
    String obtenerTipoCalculador();
}
