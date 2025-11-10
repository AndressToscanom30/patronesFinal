package Comportamentales.TemplateMethod.modelo;

public abstract class ProcesoVenta {

    public final String procesarVenta(double monto) {
        StringBuilder resultado = new StringBuilder();

        resultado.append(iniciarTransaccion());
        resultado.append(validarPago(monto));
        resultado.append(procesarPago(monto));
        resultado.append(generarComprobante());
        resultado.append(finalizarTransaccion());

        if (requiereValidacionAdicional()) {
            resultado.append(validacionAdicional());
        }

        return resultado.toString();
    }

    protected abstract String validarPago(double monto);

    protected abstract String procesarPago(double monto);

    protected abstract String generarComprobante();

    public String iniciarTransaccion() {
        return "Iniciando transacción...\n";
    }

    public String finalizarTransaccion() {
        return "Transacción finalizada con éxito.\n";
    }

    public boolean requiereValidacionAdicional() {
        return false;
    }

    protected String validacionAdicional() {
        return "";
    }
}