package Estructurales.Facade.modelo;

import java.util.Date;

public class GeneradorFacturas {
    
    public String generarFactura(String cliente, double subtotal, double impuestos) {
        System.out.println("Generando factura para cliente: " + cliente);
        String numeroFactura = "FAC-" + System.currentTimeMillis();
        return numeroFactura;
    }
    
    public void enviarFacturaElectronica(String numeroFactura, String email) {
        System.out.println("Enviando factura " + numeroFactura + " a: " + email);
    }
    
    public byte[] generarPDF(String numeroFactura) {
        System.out.println("Generando PDF de factura: " + numeroFactura);
        return new byte[0];
    }
}