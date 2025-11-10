package Creacionales.Prototype.modelo;

public class PromocionPorcentaje extends Promocion {
    private double montoMinimo;

    public PromocionPorcentaje(String id, String nombre, String descripcion) {
        super(id, nombre, descripcion);
        this.montoMinimo = 0.0;
    }

    @Override
    public double aplicarDescuento(double monto) {
        if (monto < montoMinimo) {
            return monto;
        }
        return monto - (monto * porcentajeDescuento / 100.0);
    }

    @Override
    public boolean validar() {
        return porcentajeDescuento > 0 && porcentajeDescuento <= 100;
    }

    @Override
    public String obtenerTipo() {
        return "PORCENTAJE";
    }

    @Override
    public PromocionPorcentaje clone() {
        PromocionPorcentaje clonada = (PromocionPorcentaje) super.clone();
        clonada.montoMinimo = this.montoMinimo;
        return clonada;
    }

    public double getMontoMinimo() { return montoMinimo; }
    
    public void setMontoMinimo(double montoMinimo) {
        if (montoMinimo < 0) {
            throw new IllegalArgumentException("El monto mínimo no puede ser negativo");
        }
        this.montoMinimo = montoMinimo;
    }
}
