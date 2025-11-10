package Comportamentales.ChainOfResponsibility.modelo;

public class ResultadoDescuento {
    private boolean aprobado;
    private double montoDescuento;
    private String descripcion;
    private String tipoDescuento;
    
    public ResultadoDescuento(boolean aprobado, double montoDescuento, 
                              String descripcion, String tipoDescuento) {
        this.aprobado = aprobado;
        this.montoDescuento = montoDescuento;
        this.descripcion = descripcion;
        this.tipoDescuento = tipoDescuento;
    }
    
    public boolean isAprobado() {
        return aprobado;
    }
    
    public double getMontoDescuento() {
        return montoDescuento;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public String getTipoDescuento() {
        return tipoDescuento;
    }
    
    @Override
    public String toString() {
        if (aprobado) {
            return String.format("Descuento aprobado: $%.2f - %s [%s]", 
                montoDescuento, descripcion, tipoDescuento);
        }
        return "No se aplicó descuento: " + descripcion;
    }
}