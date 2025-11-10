package Comportamentales.Command.modelo;

public class EliminarProductoCommand implements Command {
    private CarritoCompras carrito;
    private Producto producto;
    private int cantidad;
    
    public EliminarProductoCommand(CarritoCompras carrito, Producto producto, int cantidad) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
    }
    
    @Override
    public void ejecutar() {
        carrito.eliminarProducto(producto, cantidad);
    }
    
    @Override
    public void deshacer() {
        carrito.agregarProducto(producto, cantidad);
    }
    
    @Override
    public String obtenerDescripcion() {
        return String.format("Eliminar %d x %s del carrito", cantidad, producto.getNombre());
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
}