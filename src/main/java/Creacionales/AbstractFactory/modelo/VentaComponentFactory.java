package Creacionales.AbstractFactory.modelo;

public interface VentaComponentFactory {
    
    GeneradorFactura crearGeneradorFactura();
   
    ValidadorStock crearValidadorStock();
  
    CalculadorEnvio crearCalculadorEnvio();
  
    GestorDevolucion crearGestorDevolucion();
  
    String getTipoCanal();
}