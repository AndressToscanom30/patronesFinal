package Creacionales.Prototype.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Promocion implements Cloneable {
    protected String id;
    protected String nombre;
    protected String descripcion;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    protected double porcentajeDescuento;
    protected boolean activa;
    protected List<String> categoriasAplicables;
    protected int limiteUnidades;

    protected Promocion(String id, String nombre, String descripcion) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activa = false;
        this.categoriasAplicables = new ArrayList<>();
        this.limiteUnidades = 0;
    }

     @Override
    public Promocion clone() {
        try {
            Promocion clonada = (Promocion) super.clone();
            clonada.categoriasAplicables = new ArrayList<>(this.categoriasAplicables);
            return clonada;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Error al clonar promoción", e);
        }
    }

    public abstract double aplicarDescuento(double monto);

   public abstract boolean validar();

    public abstract String obtenerTipo();

    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        return activa && 
               (fechaInicio == null || !hoy.isBefore(fechaInicio)) &&
               (fechaFin == null || !hoy.isAfter(fechaFin));
    }

    public boolean esAplicableACategoria(String categoria) {
            if (categoria == null || categoria.trim().isEmpty()) {
                return false;
            }
        
            // Si es una promoción tipo combo, verificar los productos
            if (this instanceof PromocionCombo combo) {
                // Buscamos productos que contengan la categoría exacta
                return combo.getProductosRequeridos().stream()
                        .anyMatch(prod -> {
                            String[] parts = prod.split(":");
                            // El formato esperado es "codigo:categoria:nombre"
                            return parts.length >= 2 && parts[1].equals(categoria);
                        });
            }
        
         // Si la promoción no tiene categorías específicas, se aplica a todas
         // O si la categoría está en la lista de categorías aplicables
         return categoriasAplicables.isEmpty() || 
             categoriasAplicables.contains(categoria);
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public boolean isActiva() { return activa; }
    public List<String> getCategoriasAplicables() { return new ArrayList<>(categoriasAplicables); }
    public int getLimiteUnidades() { return limiteUnidades; }

    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFechaInicio(LocalDate fecha) { this.fechaInicio = fecha; }
    public void setFechaFin(LocalDate fecha) { this.fechaFin = fecha; }
    public void setPorcentajeDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("Porcentaje debe estar entre 0 y 100");
        }
        this.porcentajeDescuento = porcentaje;
    }
    public void setActiva(boolean activa) { this.activa = activa; }
    public void setLimiteUnidades(int limite) {
        if (limite < 0) {
            throw new IllegalArgumentException("El límite no puede ser negativo");
        }
        this.limiteUnidades = limite;
    }

    public void agregarCategoria(String categoria) {
        if (!categoriasAplicables.contains(categoria)) {
            categoriasAplicables.add(categoria);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Promocion that = (Promocion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, nombre=%s, descuento=%.1f%%, activa=%s]",
                getClass().getSimpleName(), id, nombre, porcentajeDescuento, activa);
    }
}
