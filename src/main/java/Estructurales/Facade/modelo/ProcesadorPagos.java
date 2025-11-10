package Estructurales.Facade.modelo;

public class ProcesadorPagos {
    
    public boolean procesarPago(String metodoPago, double monto) {
        System.out.println("Procesando pago de $" + monto + " con método: " + metodoPago);
        return monto > 0;
    }
    
    public String generarComprobante(String metodoPago, double monto) {
        return "COMPROBANTE-" + System.currentTimeMillis();
    }
    
    public boolean revertirPago(String comprobante) {
        System.out.println("Revirtiendo pago: " + comprobante);
        return true;
    }
}