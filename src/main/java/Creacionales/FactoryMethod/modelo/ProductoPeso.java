package Creacionales.FactoryMethod.modelo;

public class ProductoPeso extends Producto {

    private double precioPorKilo;
    private double pesoPromedio;
    private boolean requiereBascula;

    public ProductoPeso(String codigo, String nombre, double precioPorKilo, String categoria) {
        super(codigo, nombre, precioPorKilo, categoria);
        this.precioPorKilo = precioPorKilo;
        this.pesoPromedio = 1.0;
        this.requiereBascula = true;
    }

    @Override
    public double calcularPrecioFinal() {
        return precioPorKilo * pesoPromedio;
    }

    @Override
    public boolean requiereRefrigeracion() {
        return categoria != null &&
                (categoria.equalsIgnoreCase("CARNES") ||
                        categoria.equalsIgnoreCase("LACTEOS"));
    }

    @Override
    public String obtenerTipo() {
        return "PESO";
    }

    public double getPrecioPorKilo() {
        return precioPorKilo;
    }

    public double getPesoPromedio() {
        return pesoPromedio;
    }

    public boolean isRequiereBascula() {
        return requiereBascula;
    }

    public void setPesoPromedio(double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser positivo");
        }
        this.pesoPromedio = peso;
    }

}
