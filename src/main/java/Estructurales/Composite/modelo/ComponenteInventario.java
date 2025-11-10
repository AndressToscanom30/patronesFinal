package Estructurales.Composite.modelo;

import java.util.List;

public interface ComponenteInventario {
    double calcularTotal();
    double calcularPesoTotal();
    int contarItems();
    void aplicarDescuento(double porcentaje);
}
