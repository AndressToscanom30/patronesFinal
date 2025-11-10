package Creacionales.Prototype.modelo;

public class PromocionDosXUno extends Promocion {
    private int cantidadCompra;
    private int cantidadPaga;

    public PromocionDosXUno(String id, String nombre, String descripcion) {
        super(id, nombre, descripcion);
        this.cantidadCompra = 2;
        this.cantidadPaga = 1;
    }

    @Override
    public double aplicarDescuento(double monto) {
        // El descuento se calcula en base a unidades, no monto
        // Este método retorna el monto original
        return monto;
    }

    public double aplicarDescuentoConUnidades(double precioUnitario, int unidades) {
        if (unidades < cantidadCompra) {
            return precioUnitario * unidades;
        }
        
        int conjuntos = unidades / cantidadCompra;
        int restantes = unidades % cantidadCompra;
        
        double totalConjuntos = conjuntos * cantidadPaga * precioUnitario;
        double totalRestantes = restantes * precioUnitario;
        
        return totalConjuntos + totalRestantes;
    }

    @Override
    public boolean validar() {
        return cantidadCompra > cantidadPaga && cantidadPaga > 0;
    }

    @Override
    public String obtenerTipo() {
        return String.format("%dX%d", cantidadCompra, cantidadPaga);
    }

    @Override
    public PromocionDosXUno clone() {
        PromocionDosXUno clonada = (PromocionDosXUno) super.clone();
        clonada.cantidadCompra = this.cantidadCompra;
        clonada.cantidadPaga = this.cantidadPaga;
        return clonada;
    }

    public int getCantidadCompra() { return cantidadCompra; }
    public int getCantidadPaga() { return cantidadPaga; }

    public void setCantidadCompra(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de compra debe ser positiva");
        }
        this.cantidadCompra = cantidad;
    }

    public void setCantidadPaga(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a pagar debe ser positiva");
        }
        this.cantidadPaga = cantidad;
    }
}
