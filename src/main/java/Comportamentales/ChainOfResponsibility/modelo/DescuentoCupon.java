package Comportamentales.ChainOfResponsibility.modelo;

import java.util.HashMap;
import java.util.Map;

public class DescuentoCupon extends ManejadorDescuento {
    private Map<String, Double> cuponesValidos;
    
    public DescuentoCupon() {
        this.cuponesValidos = new HashMap<>();
        inicializarCupones();
    }
    
    private void inicializarCupones() {
        cuponesValidos.put("VERANO2024", 15.0);
        cuponesValidos.put("PRIMERACOMPRA", 20.0);
        cuponesValidos.put("BLACKFRIDAY", 50.0);
        cuponesValidos.put("NAVIDAD", 25.0);
        cuponesValidos.put("BIENVENIDO", 10.0);
    }
    
    @Override
    public ResultadoDescuento procesarSolicitud(SolicitudDescuento solicitud) {
        System.out.println("→ Verificando cupón de descuento...");
        
        String codigoCupon = solicitud.getCodigoCupon();
        
        if (codigoCupon != null && !codigoCupon.isEmpty()) {
            if (cuponesValidos.containsKey(codigoCupon)) {
                double porcentaje = cuponesValidos.get(codigoCupon);
                double descuento = solicitud.getMontoTotal() * (porcentaje / 100);
                System.out.printf("  ✓ Cupón '%s' aplicado: %.0f%% de descuento%n", 
                    codigoCupon, porcentaje);
                return new ResultadoDescuento(
                    true,
                    descuento,
                    String.format("Cupón '%s' aplicado: %.0f%% de descuento", 
                        codigoCupon, porcentaje),
                    "CUPON"
                );
            } else {
                System.out.println("  ✗ Cupón inválido o expirado: " + codigoCupon);
            }
        } else {
            System.out.println("  ✗ No se proporcionó cupón");
        }
        
        return pasarAlSiguiente(solicitud);
    }
    
    public void agregarCupon(String codigo, double porcentaje) {
        cuponesValidos.put(codigo, porcentaje);
    }
    
    public Map<String, Double> getCuponesValidos() {
        return new HashMap<>(cuponesValidos);
    }
}