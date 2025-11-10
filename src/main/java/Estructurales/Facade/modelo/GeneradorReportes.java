package Estructurales.Facade.modelo;

import java.util.Date;

public class GeneradorReportes {
    
    public void registrarVenta(String numeroFactura, double monto, Date fecha) {
        System.out.println("Registrando venta en reportes: " + numeroFactura + " - Monto: $" + monto);
    }
    
    public void actualizarEstadisticas(String sucursal, double monto) {
        System.out.println("Actualizando estadísticas de sucursal: " + sucursal);
    }
    
    public String generarReporteVentas(Date fechaInicio, Date fechaFin) {
        return "REPORTE-VENTAS-" + System.currentTimeMillis();
    }
}