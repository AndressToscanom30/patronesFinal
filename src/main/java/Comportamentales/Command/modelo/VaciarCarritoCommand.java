package Comportamentales.Command.modelo;

import java.util.ArrayList;
import java.util.List;

public class VaciarCarritoCommand implements Command {
    private CarritoCompras carrito;
    private List<ItemCarrito> itemsAnteriores;
    
    public VaciarCarritoCommand(CarritoCompras carrito) {
        this.carrito = carrito;
        this.itemsAnteriores = new ArrayList<>();
    }
    
    @Override
    public void ejecutar() {
        // Guardar estado actual antes de vaciar
        itemsAnteriores = new ArrayList<>(carrito.getItems());
        carrito.vaciar();
    }
    
    @Override
    public void deshacer() {
        // Restaurar items anteriores
        for (ItemCarrito item : itemsAnteriores) {
            carrito.agregarProducto(item.getProducto(), item.getCantidad());
        }
    }
    
    @Override
    public String obtenerDescripcion() {
        return "Vaciar carrito de compras";
    }
}