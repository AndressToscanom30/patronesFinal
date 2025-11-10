package Comportamentales.TemplateMethod.modelo;

public class VentaTransferencia extends ProcesoVenta {
    private String banco;
    
    public VentaTransferencia(String banco) {
        this.banco = banco;
    }
    
    @Override
    protected String validarPago(double monto) {
        return "Validando pago por transferencia...\n" +
               "Banco: " + banco + "\n" +
               "Verificando datos bancarios...\n" +
               "Monto a transferir: $" + monto + "\n";
    }
    
    @Override
    protected String procesarPago(double monto) {
        return "Procesando transferencia bancaria...\n" +
               "Generando código de referencia...\n" +
               "Esperando confirmación del banco...\n" +
               "Transferencia confirmada.\n";
    }
    
    @Override
    protected String generarComprobante() {
        return "Generando comprobante de transferencia...\n" +
               "Número de referencia generado.\n" +
               "Enviando comprobante por email...\n";
    }
    
    public String getBanco() {
        return banco;
    }
}