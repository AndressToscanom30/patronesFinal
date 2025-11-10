package Estructurales.Facade.controlador;

import Estructurales.Facade.modelo.*;
import Estructurales.Facade.vista.ConsolaFacade;

public class VentasController {
    private SistemaVentasFacade facade;
    private ConsolaFacade vista;
    
    public VentasController(ConsolaFacade vista) {
        this.facade = new SistemaVentasFacade();
        this.vista = vista;
    }
    
    public void procesarVenta(Venta venta) {
        ResultadoVenta resultado = facade.procesarVenta(venta);
        vista.mostrarResultadoVenta(resultado);
    }
    
    public void consultarPuntos(String clienteId) {
        int puntos = facade.consultarPuntosCliente(clienteId);
        vista.mostrarPuntosCliente(clienteId, puntos);
    }
    
    public void generarReporte(java.util.Date fechaInicio, java.util.Date fechaFin) {
        String reporte = facade.generarReporteVentas(fechaInicio, fechaFin);
        vista.mostrarReporte(reporte);
    }
}