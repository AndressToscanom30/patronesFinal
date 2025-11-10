package Estructurales.Facade.modelo;

public class ServicioNotificaciones {
    
    public void enviarEmail(String destinatario, String asunto, String mensaje) {
        System.out.println("Enviando email a " + destinatario + " - Asunto: " + asunto);
    }
    
    public void enviarSMS(String telefono, String mensaje) {
        System.out.println("Enviando SMS a " + telefono + ": " + mensaje);
    }
    
    public void notificarVenta(String clienteId, String numeroFactura) {
        System.out.println("Notificando venta al cliente " + clienteId + " - Factura: " + numeroFactura);
    }
}