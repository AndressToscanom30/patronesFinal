package Comportamentales.TemplateMethod.modelo;

public class VentaTarjeta extends ProcesoVenta {
    private String tipoCuenta;
    
    public VentaTarjeta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    
    @Override
    public String validarPago(double monto) {
        return "Validando pago con tarjeta...\n" +
               "Tipo de cuenta: " + tipoCuenta + "\n" +
               "Verificando saldo disponible...\n" +
               "Monto a cobrar: $" + monto + "\n";
    }
    
    @Override
    public String procesarPago(double monto) {
        return "Procesando pago con tarjeta...\n" +
               "Conectando con banco...\n" +
               "Solicitando autorización...\n" +
               "Aprobando transacción...\n" +
               "Débito realizado exitosamente.\n";
    }
    
    @Override
    public String generarComprobante() {
        return "Generando comprobante de pago con tarjeta...\n" +
               "Imprimiendo voucher para firma...\n" +
               "Generando comprobante digital...\n";
    }
    
    @Override
    public boolean requiereValidacionAdicional() {
        return true;
    }
    
    @Override
    public String validacionAdicional() {
        return "Validación adicional de seguridad...\n" +
               "Verificando código PIN...\n" +
               "Validación 3D Secure: OK\n";
    }
    
    public String getTipoCuenta() {
        return tipoCuenta;
    }
}