package Estructurales.Composite.controlador;

import Estructurales.Composite.modelo.*;
import Estructurales.Composite.vista.ConsolaComposite;

public class InventarioController {
    private ComponenteInventario raiz;
    private ConsolaComposite view;

    public InventarioController(ConsolaComposite view) {
        this.view = view;
    }

    public void setRaiz(ComponenteInventario raiz) {
        this.raiz = raiz;
    }

    public double obtenerTotalInventario() {
        if (raiz == null) {
            return 0.0;
        }
        return raiz.calcularTotal();
    }

    public double obtenerPesoTotal() {
        if (raiz == null) {
            return 0.0;
        }
        return raiz.calcularPesoTotal();
    }

    public int obtenerCantidadItems() {
        if (raiz == null) {
            return 0;
        }
        return raiz.contarItems();
    }

    public void aplicarDescuentoGlobal(double porcentaje) {
        if (raiz != null) {
            raiz.aplicarDescuento(porcentaje);
            view.mostrarMensaje("Descuento del " + porcentaje + "% aplicado correctamente");
        }
    }

    public void mostrarInformacion() {
        if (raiz == null) {
            System.out.println("No hay inventario cargado");
            return;
        }
        view.mostrarTotal(obtenerTotalInventario());
        view.mostrarPesoTotal(obtenerPesoTotal());
        view.mostrarCantidadItems(obtenerCantidadItems());
    }
}
