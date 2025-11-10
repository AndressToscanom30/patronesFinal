package Creacionales.Builder.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Factura {
    private final String numeroFactura;
    private final LocalDate fechaEmision;
    private final Cliente cliente;
    private final List<ItemFactura> items;
    private final double subtotal;
    private final double impuestos;
    private final double descuentos;
    private final double propina;
    private final String metodoPago;
    private final String observaciones;
    private final boolean requiereEnvio;
    private final String direccionEnvio;

    private Factura(Builder builder) {
        this.numeroFactura = builder.numeroFactura;
        this.fechaEmision = builder.fechaEmision;
        this.cliente = builder.cliente;
        this.items = Collections.unmodifiableList(new ArrayList<>(builder.items));
        this.subtotal = builder.subtotal;
        this.impuestos = builder.impuestos;
        this.descuentos = builder.descuentos;
        this.propina = builder.propina;
        this.metodoPago = builder.metodoPago;
        this.observaciones = builder.observaciones;
        this.requiereEnvio = builder.requiereEnvio;
        this.direccionEnvio = builder.direccionEnvio;
    }

    public double calcularTotal() {
        return subtotal + impuestos - descuentos + propina;
    }

    public String generarPDF() {
        return String.format("PDF generado para factura %s", numeroFactura);
    }

    public boolean enviarPorCorreo() {
        if (cliente == null || cliente.getEmail() == null) {
            return false;
        }
        return true;
    }

    // Getters
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public double getDescuentos() {
        return descuentos;
    }

    public double getPropina() {
        return propina;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public boolean isRequiereEnvio() {
        return requiereEnvio;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    @Override
    public String toString() {
        return String.format("Factura[%s, Cliente=%s, Total=%.2f]",
                numeroFactura, cliente != null ? cliente.getNombre() : "N/A", calcularTotal());
    }

    public static class Builder {
        private String numeroFactura;
        private LocalDate fechaEmision;
        private Cliente cliente;
        private List<ItemFactura> items;
        private double subtotal;
        private double impuestos;
        private double descuentos;
        private double propina;
        private String metodoPago;
        private String observaciones;
        private boolean requiereEnvio;
        private String direccionEnvio;

        public Builder(String numeroFactura) {
            if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
                throw new IllegalArgumentException("El número de factura es obligatorio");
            }
            this.numeroFactura = numeroFactura;
            this.fechaEmision = LocalDate.now();
            this.items = new ArrayList<>();
            this.subtotal = 0.0;
            this.impuestos = 0.0;
            this.descuentos = 0.0;
            this.propina = 0.0;
            this.requiereEnvio = false;
        }

        public Builder conFechaEmision(LocalDate fecha) {
            this.fechaEmision = fecha;
            return this;
        }

        public Builder paraCliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        public Builder agregarItem(ItemFactura item) {
            if (item == null) {
                throw new IllegalArgumentException("El item no puede ser nulo");
            }
            this.items.add(item);
            this.subtotal += item.calcularTotal();
            return this;
        }

        public Builder conImpuestos(double impuestos) {
            if (impuestos < 0) {
                throw new IllegalArgumentException("Los impuestos no pueden ser negativos");
            }
            this.impuestos = impuestos;
            return this;
        }

        public Builder conDescuentos(double descuentos) {
            if (descuentos < 0) {
                throw new IllegalArgumentException("Los descuentos no pueden ser negativos");
            }
            this.descuentos = descuentos;
            return this;
        }

        public Builder conPropina(double propina) {
            if (propina < 0) {
                throw new IllegalArgumentException("La propina no puede ser negativa");
            }
            this.propina = propina;
            return this;
        }

        public Builder conMetodoPago(String metodoPago) {
            this.metodoPago = metodoPago;
            return this;
        }

        public Builder conObservaciones(String observaciones) {
            this.observaciones = observaciones;
            return this;
        }

        public Builder requiereEnvio(boolean requiere) {
            this.requiereEnvio = requiere;
            return this;
        }

        public Builder conDireccionEnvio(String direccion) {
            this.direccionEnvio = direccion;
            this.requiereEnvio = true;
            return this;
        }

        public Builder calcularImpuestosAutomaticamente() {
            this.impuestos = this.subtotal * 0.19;
            return this;
        }

        public Factura build() {
            validar();
            return new Factura(this);
        }

        private void validar() {
            if (items.isEmpty()) {
                throw new IllegalStateException("La factura debe tener al menos un item");
            }
            if (metodoPago == null || metodoPago.trim().isEmpty()) {
                throw new IllegalStateException("El método de pago es obligatorio");
            }
            if (requiereEnvio && (direccionEnvio == null || direccionEnvio.trim().isEmpty())) {
                throw new IllegalStateException("La dirección de envío es obligatoria cuando requiere envío");
            }
        }
    }
}
