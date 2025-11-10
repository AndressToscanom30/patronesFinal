package Comportamentales.TemplateMethod.controlador;

import Comportamentales.TemplateMethod.modelo.*;
import Comportamentales.TemplateMethod.vista.ConsolaTemplateMethod;

public class TemplateMethodController {
    private ConsolaTemplateMethod vista;
    
    public TemplateMethodController(ConsolaTemplateMethod vista) {
        this.vista = vista;
    }
    
    public String procesarVentaEfectivo(double monto) {
        ProcesoVenta venta = new VentaEfectivo();
        return venta.procesarVenta(monto);
    }
    
    public String procesarVentaTarjeta(double monto, String tipoCuenta) {
        ProcesoVenta venta = new VentaTarjeta(tipoCuenta);
        return venta.procesarVenta(monto);
    }
    
    public String procesarVentaTransferencia(double monto, String banco) {
        ProcesoVenta venta = new VentaTransferencia(banco);
        return venta.procesarVenta(monto);
    }
    
    public ProcesoVenta crearProcesoVenta(String tipo, String... parametros) {
        switch (tipo.toUpperCase()) {
            case "EFECTIVO":
                return new VentaEfectivo();
            case "TARJETA":
                String tipoCuenta = parametros.length > 0 ? parametros[0] : "CREDITO";
                return new VentaTarjeta(tipoCuenta);
            case "TRANSFERENCIA":
                String banco = parametros.length > 0 ? parametros[0] : "Banco Nacional";
                return new VentaTransferencia(banco);
            default:
                vista.mostrarError("Tipo de venta no válido");
                return null;
        }
    }
}