package Estructurales.Facade.modelo;

public class ActualizadorInventario {
    
    public void descontarStock(String codigoProducto, int cantidad) {
        System.out.println("Descontando " + cantidad + " unidades del inventario: " + codigoProducto);
    }
    
    public void registrarMovimiento(String codigoProducto, int cantidad, String tipoMovimiento) {
        System.out.println("Registrando movimiento: " + tipoMovimiento + " - Producto: " + codigoProducto + " - Cantidad: " + cantidad);
    }
    
    public boolean verificarStockMinimo(String codigoProducto) {
        System.out.println("Verificando stock mínimo para: " + codigoProducto);
        return true;
    }
}