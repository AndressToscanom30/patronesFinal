package Comportamentales.ChainOfResponsibility.modelo;

public abstract class ManejadorDescuento {
    protected ManejadorDescuento siguiente;
    
    public void setSiguiente(ManejadorDescuento siguiente) {
        this.siguiente = siguiente;
    }
    
    public abstract ResultadoDescuento procesarSolicitud(SolicitudDescuento solicitud);
    
    protected ResultadoDescuento pasarAlSiguiente(SolicitudDescuento solicitud) {
        if (siguiente != null) {
            return siguiente.procesarSolicitud(solicitud);
        }
        return new ResultadoDescuento(false, 0, "No hay más manejadores en la cadena", "NINGUNO");
    }
}