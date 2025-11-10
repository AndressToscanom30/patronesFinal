package Creacionales.AbstractFactory.modelo;

import Creacionales.AbstractFactory.modelo.*;

public class POSGeneradorFactura implements GeneradorFactura {
    private static int contador = 1000;

    @Override
    public String generarNumeroFactura() {
        return String.format("POS-FAC-%06d", contador++);
    }

    @Override
    public String generarFormatoFactura(DatosVenta datos) {
        StringBuilder factura = new StringBuilder();
        factura.append("========== FACTURA POS ==========\n");
        factura.append("Número: ").append(datos.getNumeroVenta()).append("\n");
        factura.append("Fecha: ").append(datos.getFecha()).append("\n");
        factura.append("Cliente: ").append(datos.getClienteNombre()).append("\n");
        factura.append("===============================\n");
        
        for (ItemVenta item : datos.getItems()) {
            factura.append(String.format("%s x%d - $%.2f\n", 
                item.getNombreProducto(), 
                item.getCantidad(), 
                item.getSubtotal()));
        }
        
        factura.append("===============================\n");
        factura.append(String.format("Subtotal: $%.2f\n", datos.getSubtotal()));
        factura.append(String.format("IVA (19%%): $%.2f\n", datos.getImpuestos()));
        factura.append(String.format("TOTAL: $%.2f\n", datos.getTotal()));
        factura.append("===============================\n");
        factura.append("Gracias por su compra!\n");
        
        return factura.toString();
    }

    @Override
    public boolean requiereFacturaElectronica() {
        return false; // POS puede usar factura física
    }

    @Override
    public String obtenerTipoGenerador() {
        return "Generador POS - Factura Física/Ticket";
    }
}
