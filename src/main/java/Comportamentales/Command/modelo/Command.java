package Comportamentales.Command.modelo;

public interface Command {
    void ejecutar();
    void deshacer();
    String obtenerDescripcion();
}