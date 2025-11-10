package Estructurales.Facade.modelo;

public class ResultadoVenta {
    private boolean exitoso;
    private String mensaje;
    private String numeroFactura;
    private String comprobantePago;
    private int puntosGanados;
    
    public boolean isExitoso() {
        return exitoso;
    }
    
    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public String getNumeroFactura() {
        return numeroFactura;
    }
    
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }
    
    public String getComprobantePago() {
        return comprobantePago;
    }
    
    public void setComprobantePago(String comprobantePago) {
        this.comprobantePago = comprobantePago;
    }
    
    public int getPuntosGanados() {
        return puntosGanados;
    }
    
    public void setPuntosGanados(int puntosGanados) {
        this.puntosGanados = puntosGanados;
    }
}