package Comportamentales.ChainOfResponsibility.controlador;

import Comportamentales.ChainOfResponsibility.modelo.*;
import Comportamentales.ChainOfResponsibility.vista.ConsolaChainOfResponsibility;

public class ChainOfResponsibilityController {
    private ConsolaChainOfResponsibility vista;
    private ManejadorDescuento cadenaDescuentos;
    
    public ChainOfResponsibilityController(ConsolaChainOfResponsibility vista) {
        this.vista = vista;
        inicializarCadena();
    }
    
    private void inicializarCadena() {
        // Crear manejadores
        ManejadorDescuento descuentoCupon = new DescuentoCupon();
        ManejadorDescuento descuentoClienteFrecuente = new DescuentoClienteFrecuente(5, 15);
        ManejadorDescuento descuentoPorMonto = new DescuentoPorMonto(100000, 10);
        ManejadorDescuento descuentoPorCantidad = new DescuentoPorCantidad(10, 5);
        
        // Configurar cadena
        descuentoCupon.setSiguiente(descuentoClienteFrecuente);
        descuentoClienteFrecuente.setSiguiente(descuentoPorMonto);
        descuentoPorMonto.setSiguiente(descuentoPorCantidad);
        
        cadenaDescuentos = descuentoCupon;
    }
    
    public ResultadoDescuento procesarSolicitud(SolicitudDescuento solicitud) {
        vista.mostrarMensaje("\n═══ PROCESANDO SOLICITUD DE DESCUENTO ═══");
        vista.mostrarMensaje("Cliente: " + solicitud.getClienteId());
        vista.mostrarMensaje(String.format("Monto: $%.2f", solicitud.getMontoTotal()));
        vista.mostrarMensaje("Cantidad: " + solicitud.getCantidad());
        vista.mostrarMensaje("\nIniciando evaluación en la cadena...\n");
        
        ResultadoDescuento resultado = cadenaDescuentos.procesarSolicitud(solicitud);
        
        vista.mostrarMensaje("\n═══ RESULTADO ═══");
        if (resultado.isAprobado()) {
            vista.mostrarMensaje("✓ " + resultado.getDescripcion());
            vista.mostrarMensaje(String.format("Descuento: $%.2f", resultado.getMontoDescuento()));
        } else {
            vista.mostrarMensaje("✗ " + resultado.getDescripcion());
        }
        
        return resultado;
    }
    
    public void configurarCadenaPersonalizada(ManejadorDescuento nuevaCadena) {
        this.cadenaDescuentos = nuevaCadena;
        vista.mostrarMensaje("✓ Cadena de descuentos personalizada configurada");
    }
    
    public ManejadorDescuento getCadenaDescuentos() {
        return cadenaDescuentos;
    }
    
    public SolicitudDescuento crearSolicitud(String clienteId, double monto, int cantidad, 
                                            int numeroCompras, String cupon, String categoria) {
        return new SolicitudDescuento(clienteId, monto, cantidad, numeroCompras, cupon, categoria);
    }
}