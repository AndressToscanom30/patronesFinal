package Comportamentales.Command.controlador;

import Comportamentales.Command.modelo.*;
import Comportamentales.Command.vista.ConsolaCommand;
import java.util.List;

public class CommandController {
    private ConsolaCommand vista;
    private CarritoCompras carrito;
    private GestorComandos gestor;
    
    public CommandController(ConsolaCommand vista) {
        this.vista = vista;
        this.carrito = new CarritoCompras();
        this.gestor = new GestorComandos();
    }
    
    public void agregarProducto(Producto producto, int cantidad) {
        Command comando = new AgregarProductoCommand(carrito, producto, cantidad);
        gestor.ejecutarComando(comando);
        vista.mostrarMensaje("✓ Producto agregado al carrito");
    }
    
    public void eliminarProducto(Producto producto, int cantidad) {
        Command comando = new EliminarProductoCommand(carrito, producto, cantidad);
        gestor.ejecutarComando(comando);
        vista.mostrarMensaje("✓ Producto eliminado del carrito");
    }
    
    public void aplicarDescuento(double porcentaje) {
        Command comando = new AplicarDescuentoCommand(carrito, porcentaje);
        gestor.ejecutarComando(comando);
        vista.mostrarMensaje(String.format("✓ Descuento del %.2f%% aplicado", porcentaje));
    }
    
    public void vaciarCarrito() {
        Command comando = new VaciarCarritoCommand(carrito);
        gestor.ejecutarComando(comando);
        vista.mostrarMensaje("✓ Carrito vaciado");
    }
    
    public boolean deshacer() {
        if (gestor.deshacer()) {
            vista.mostrarMensaje("✓ Operación deshecha");
            return true;
        }
        vista.mostrarError("No hay operaciones para deshacer");
        return false;
    }
    
    public boolean rehacer() {
        if (gestor.rehacer()) {
            vista.mostrarMensaje("✓ Operación rehecha");
            return true;
        }
        vista.mostrarError("No hay operaciones para rehacer");
        return false;
    }
    
    public CarritoCompras getCarrito() {
        return carrito;
    }
    
    public List<String> obtenerHistorial() {
        return gestor.obtenerHistorial();
    }
    
    public boolean puedeDeshacer() {
        return gestor.puedeDeshacer();
    }
    
    public boolean puedeRehacer() {
        return gestor.puedeRehacer();
    }
    
    public void limpiarHistorial() {
        gestor.limpiarHistorial();
    }
}