package Comportamentales.ChainOfResponsibility.modelo;

public class DescuentoPorCantidad extends ManejadorDescuento {
    private int cantidadMinima;
    private double porcentajeDescuento;
    
    public DescuentoPorCantidad(int cantidadMinima, double porcentajeDescuento) {
        this.cantidadMinima = cantidadMinima;
        this.porcentajeDescuento = porcentajeDescuento;
    }
    
    @Override
    public ResultadoDescuento procesarSolicitud(SolicitudDescuento solicitud) {
        System.out.println("→ Verificando descuento por cantidad...");
        
        if (solicitud.getCantidad() >= cantidadMinima) {
            double descuento = solicitud.getMontoTotal() * (porcentajeDescuento / 100);
            System.out.printf("  ✓ Descuento por cantidad aplicado: %.0f%% (compra >= %d unidades)%n", 
                porcentajeDescuento, cantidadMinima);
            return new ResultadoDescuento(
                true,
                descuento,
                String.format("Descuento por cantidad: %.0f%% por comprar %d o más unidades", 
                    porcentajeDescuento, cantidadMinima),
                "CANTIDAD"
            );
        }
        
        System.out.println("  ✗ No califica (cantidad mínima: " + cantidadMinima + ")");
        return pasarAlSiguiente(solicitud);
    }
    
    public int getCantidadMinima() {
        return cantidadMinima;
    }
    
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}