package Creacionales.AbstractFactory.modelo;

public interface ValidadorStock {
    boolean validarDisponibilidad(String codigoProducto, int cantidad);
    int obtenerStockDisponible(String codigoProducto);
    boolean reservarStock(String codigoProducto, int cantidad);
    String obtenerTipoValidador();
}
