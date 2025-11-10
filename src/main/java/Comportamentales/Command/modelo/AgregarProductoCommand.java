package Comportamentales.Command.modelo;

public class AgregarProductoCommand implements Command {
    private CarritoCompras carrito;
    private Producto producto;
    private int cantidad;
    
    public AgregarProductoCommand(CarritoCompras carrito, Producto producto, int cantidad) {
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
    }
    
    @Override
    public void ejecutar() {
        carrito.agregarProducto(producto, cantidad);
    }
    
    @Override
    public void deshacer() {
        carrito.eliminarProducto(producto, cantidad);
    }
    
    @Override
    public String obtenerDescripcion() {
        return String.format("Agregar %d x %s al carrito", cantidad, producto.getNombre());
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
}