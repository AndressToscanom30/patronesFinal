package Comportamentales.ChainOfResponsibility.modelo;

public class DescuentoPorMonto extends ManejadorDescuento {
    private double montoMinimo;
    private double porcentajeDescuento;
    
    public DescuentoPorMonto(double montoMinimo, double porcentajeDescuento) {
        this.montoMinimo = montoMinimo;
        this.porcentajeDescuento = porcentajeDescuento;
    }
    
    @Override
    public ResultadoDescuento procesarSolicitud(SolicitudDescuento solicitud) {
        System.out.println("→ Verificando descuento por monto...");
        
        if (solicitud.getMontoTotal() >= montoMinimo) {
            double descuento = solicitud.getMontoTotal() * (porcentajeDescuento / 100);
            System.out.printf("  ✓ Descuento por monto aplicado: %.0f%% (compra >= $%.0f)%n", 
                porcentajeDescuento, montoMinimo);
            return new ResultadoDescuento(
                true,
                descuento,
                String.format("Descuento por monto: %.0f%% por compras sobre $%.0f", 
                    porcentajeDescuento, montoMinimo),
                "MONTO"
            );
        }
        
        System.out.printf("  ✗ No califica (monto mínimo: $%.0f)%n", montoMinimo);
        return pasarAlSiguiente(solicitud);
    }
    
    public double getMontoMinimo() {
        return montoMinimo;
    }
    
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}