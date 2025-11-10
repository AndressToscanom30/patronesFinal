package Estructurales.Facade.modelo;

public class SistemaFidelizacion {
    
    public int calcularPuntos(double montoCompra) {
        return (int) (montoCompra / 10);
    }
    
    public void acreditarPuntos(String clienteId, int puntos) {
        System.out.println("Acreditando " + puntos + " puntos al cliente: " + clienteId);
    }
    
    public int consultarPuntos(String clienteId) {
        return 150;
    }
}