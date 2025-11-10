package Creacionales.Builder.modelo;

public class ItemFactura {
    private String codigoProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double descuentoItem;

    public ItemFactura(String codigoProducto, String nombreProducto, int cantidad, double precioUnitario) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuentoItem = 0.0;
    }

    public double calcularTotal() {
        return (cantidad * precioUnitario) - descuentoItem;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getDescuentoItem() {
        return descuentoItem;
    }

    public void setDescuentoItem(double descuento) {
        if (descuento < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo");
        }
        this.descuentoItem = descuento;
    }
}
