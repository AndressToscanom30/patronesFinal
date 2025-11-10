package Estructurales.Facade.modelo;

public class ValidadorStock {
    
    public boolean validarDisponibilidad(String codigoProducto, int cantidad) {
        System.out.println("Validando stock para producto: " + codigoProducto);
        return cantidad <= 100;
    }
    
    public void reservarStock(String codigoProducto, int cantidad) {
        System.out.println("Reservando " + cantidad + " unidades del producto: " + codigoProducto);
    }
    
    public void liberarStock(String codigoProducto, int cantidad) {
        System.out.println("Liberando " + cantidad + " unidades del producto: " + codigoProducto);
    }
}