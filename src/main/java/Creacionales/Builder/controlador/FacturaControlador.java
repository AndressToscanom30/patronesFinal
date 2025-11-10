package Creacionales.Builder.controlador;

import Creacionales.Builder.modelo.*;

import java.time.LocalDate;
import java.util.*;

public class FacturaControlador {
    private final Map<String, Factura> facturas;
    private int contadorFacturas;

    public FacturaControlador() {
        this.facturas = new HashMap<>();
        this.contadorFacturas = 1000;
    }

    public Factura.Builder iniciarFactura() {
        String numeroFactura = generarNumeroFactura();
        return new Factura.Builder(numeroFactura);
    }

    public Factura guardarFactura(Factura factura) {
        if (factura == null) {
            throw new IllegalArgumentException("La factura no puede ser nula");
        }
        facturas.put(factura.getNumeroFactura(), factura);
        return factura;
    }

    public Factura buscarFactura(String numeroFactura) {
        return facturas.get(numeroFactura);
    }

    public List<Factura> obtenerTodasLasFacturas() {
        return new ArrayList<>(facturas.values());
    }

    public List<Factura> buscarPorCliente(String nombreCliente) {
        return facturas.values().stream()
                .filter(f -> f.getCliente() != null &&
                        f.getCliente().getNombre().contains(nombreCliente))
                .toList();
    }

    public List<Factura> buscarPorFecha(LocalDate fecha) {
        return facturas.values().stream()
                .filter(f -> f.getFechaEmision().equals(fecha))
                .toList();
    }

    public double calcularTotalVentas() {
        return facturas.values().stream()
                .mapToDouble(Factura::calcularTotal)
                .sum();
    }

    private String generarNumeroFactura() {
        return String.format("F-%06d", contadorFacturas++);
    }

    public Factura crearFacturaSimple(Cliente cliente, List<ItemFactura> items, String metodoPago) {
        return iniciarFactura()
                .paraCliente(cliente)
                .conMetodoPago(metodoPago)
                .calcularImpuestosAutomaticamente()
                .build();
    }

    private static class BuilderExtendido extends Factura.Builder {
        public BuilderExtendido(String numeroFactura) {
            super(numeroFactura);
        }

        public BuilderExtendido agregarItems(List<ItemFactura> items) {
            for (ItemFactura item : items) {
                agregarItem(item);
            }
            return this;
        }
    }
}
