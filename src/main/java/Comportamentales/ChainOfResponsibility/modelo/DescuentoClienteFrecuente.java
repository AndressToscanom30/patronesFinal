package Comportamentales.ChainOfResponsibility.modelo;

public class DescuentoClienteFrecuente extends ManejadorDescuento {
    private int comprasMinimasRequeridas;
    private double porcentajeDescuento;
    
    public DescuentoClienteFrecuente(int comprasMinimasRequeridas, double porcentajeDescuento) {
        this.comprasMinimasRequeridas = comprasMinimasRequeridas;
        this.porcentajeDescuento = porcentajeDescuento;
    }
    
    @Override
    public ResultadoDescuento procesarSolicitud(SolicitudDescuento solicitud) {
        System.out.println("→ Verificando descuento por cliente frecuente...");
        
        if (solicitud.getNumeroCompras() >= comprasMinimasRequeridas) {
            double descuento = solicitud.getMontoTotal() * (porcentajeDescuento / 100);
            System.out.printf("  ✓ Descuento cliente frecuente aplicado: %.0f%% (%d compras)%n", 
                porcentajeDescuento, solicitud.getNumeroCompras());
            return new ResultadoDescuento(
                true,
                descuento,
                String.format("Descuento cliente frecuente: %.0f%% por tener %d compras", 
                    porcentajeDescuento, solicitud.getNumeroCompras()),
                "CLIENTE_FRECUENTE"
            );
        }
        
        System.out.println("  ✗ No califica (compras mínimas: " + comprasMinimasRequeridas + ")");
        return pasarAlSiguiente(solicitud);
    }
    
    public int getComprasMinimasRequeridas() {
        return comprasMinimasRequeridas;
    }
    
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}