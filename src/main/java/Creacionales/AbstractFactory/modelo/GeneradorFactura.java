package Creacionales.AbstractFactory.modelo;

public interface GeneradorFactura {
    String generarNumeroFactura();
    String generarFormatoFactura(DatosVenta datos);
    boolean requiereFacturaElectronica();
    String obtenerTipoGenerador();
}
