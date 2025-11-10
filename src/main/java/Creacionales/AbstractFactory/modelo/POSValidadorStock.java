package Creacionales.AbstractFactory.modelo;

import Creacionales.AbstractFactory.modelo.*;
import java.util.HashMap;
import java.util.Map;

public class POSValidadorStock implements ValidadorStock {
    private final Map<String, Integer> stockLocal;

    public POSValidadorStock() {
        this.stockLocal = new HashMap<>();
        inicializarStockDemo();
    }

    private void inicializarStockDemo() {
        stockLocal.put("PROD-001", 50);
        stockLocal.put("PROD-002", 30);
        stockLocal.put("PROD-003", 100);
        stockLocal.put("PROD-004", 25);
        stockLocal.put("PROD-005", 75);
    }

    @Override
    public boolean validarDisponibilidad(String codigoProducto, int cantidad) {
        int disponible = stockLocal.getOrDefault(codigoProducto, 0);
        return disponible >= cantidad;
    }

    @Override
    public int obtenerStockDisponible(String codigoProducto) {
        return stockLocal.getOrDefault(codigoProducto, 0);
    }

    @Override
    public boolean reservarStock(String codigoProducto, int cantidad) {
        if (!validarDisponibilidad(codigoProducto, cantidad)) {
            return false;
        }
        
        int stockActual = stockLocal.get(codigoProducto);
        stockLocal.put(codigoProducto, stockActual - cantidad);
        return true;
    }

    @Override
    public String obtenerTipoValidador() {
        return "Validador POS - Stock Local en Tiempo Real";
    }
}
