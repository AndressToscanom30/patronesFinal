package Comportamentales.TemplateMethod.modelo;

public class VentaEfectivo extends ProcesoVenta {
    
    @Override
    protected String validarPago(double monto) {
        return "Validando pago en efectivo...\n" +
               "Verificando billetes y monedas...\n" +
               "Monto recibido: $" + monto + "\n";
    }
    
    @Override
    protected String procesarPago(double monto) {
        return "Procesando pago en efectivo...\n" +
               "Abriendo caja registradora...\n" +
               "Guardando dinero...\n" +
               "Calculando cambio...\n";
    }
    
    @Override
    protected String generarComprobante() {
        return "Generando comprobante de pago en efectivo...\n" +
               "Imprimiendo factura física...\n";
    }
    
    @Override
    public boolean requiereValidacionAdicional() {
        return true;
    }
    
    @Override
    protected String validacionAdicional() {
        return "Verificando autenticidad de billetes...\n" +
               "Detector de billetes falsos: OK\n";
    }
}