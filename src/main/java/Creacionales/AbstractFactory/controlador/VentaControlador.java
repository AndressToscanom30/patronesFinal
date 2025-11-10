package Creacionales.AbstractFactory.controlador;

import Creacionales.AbstractFactory.modelo.*;

import java.util.*;

public class VentaControlador {
    private final Map<String, VentaComponentFactory> factories;
    private final List<DatosVenta> ventas;
    private int contadorVentas;

    public VentaControlador() {
        this.factories = new HashMap<>();
        this.ventas = new ArrayList<>();
        this.contadorVentas = 1000;
        inicializarFactories();
    }

    private void inicializarFactories() {
        factories.put("POS", new POSVentaFactory());
        //factories.put("ECOMMERCE", new EcommerceVentaFactory());
        //factories.put("B2B", new B2BVentaFactory()); No implementados para agilizar 
    }

    public VentaComponentFactory obtenerFactory(String tipoCanal) {
        return factories.get(tipoCanal.toUpperCase());
    }

    public DatosVenta crearVenta(String tipoCanal, String clienteId, String clienteNombre) {
        String numeroVenta = generarNumeroVenta(tipoCanal);
        return new DatosVenta(numeroVenta, clienteId, clienteNombre);
    }

    public boolean procesarVenta(String tipoCanal, DatosVenta datos) {
        VentaComponentFactory factory = obtenerFactory(tipoCanal);
        if (factory == null) {
            return false;
        }

        // Validar stock
        ValidadorStock validador = factory.crearValidadorStock();
        for (ItemVenta item : datos.getItems()) {
            if (!validador.validarDisponibilidad(item.getCodigoProducto(), item.getCantidad())) {
                return false;
            }
        }

        // Reservar stock
        for (ItemVenta item : datos.getItems()) {
            validador.reservarStock(item.getCodigoProducto(), item.getCantidad());
        }

        // Generar factura
        GeneradorFactura generador = factory.crearGeneradorFactura();
        String factura = generador.generarFormatoFactura(datos);

        ventas.add(datos);
        return true;
    }

    public double calcularEnvio(String tipoCanal, String destino, double peso) {
        VentaComponentFactory factory = obtenerFactory(tipoCanal);
        if (factory == null) {
            return 0.0;
        }

        CalculadorEnvio calculador = factory.crearCalculadorEnvio();
        return calculador.calcularCostoEnvio(destino, peso);
    }

    public boolean procesarDevolucion(String tipoCanal, String codigoVenta, int diasTranscurridos, double monto) {
        VentaComponentFactory factory = obtenerFactory(tipoCanal);
        if (factory == null) {
            return false;
        }

        GestorDevolucion gestor = factory.crearGestorDevolucion();
        if (!gestor.permitirDevolucion(codigoVenta, diasTranscurridos)) {
            return false;
        }

        double reembolso = gestor.calcularReembolso(monto, diasTranscurridos);
        return reembolso > 0;
    }

    public List<DatosVenta> obtenerVentas() {
        return new ArrayList<>(ventas);
    }

    public List<DatosVenta> obtenerVentasPorCanal(String tipoCanal) {
        return ventas.stream()
            .filter(v -> v.getNumeroVenta().startsWith(tipoCanal))
            .toList();
    }

    public Set<String> obtenerCanalesDisponibles() {
        return factories.keySet();
    }

    private String generarNumeroVenta(String tipoCanal) {
        return String.format("%s-%06d", tipoCanal.toUpperCase(), contadorVentas++);
    }

    public DatosVenta buscarVenta(String numeroVenta) {
        return ventas.stream()
            .filter(v -> v.getNumeroVenta().equals(numeroVenta))
            .findFirst()
            .orElse(null);
    }

    public double calcularTotalVentas() {
        return ventas.stream()
            .mapToDouble(DatosVenta::getTotal)
            .sum();
    }

    public Map<String, Long> obtenerEstadisticasPorCanal() {
        Map<String, Long> stats = new HashMap<>();
        for (String canal : factories.keySet()) {
            long cantidad = obtenerVentasPorCanal(canal).size();
            stats.put(canal, cantidad);
        }
        return stats;
    }

    public String obtenerInformacionFactory(String tipoCanal) {
        VentaComponentFactory factory = obtenerFactory(tipoCanal);
        if (factory == null) {
            return "Canal no encontrado";
        }

        StringBuilder info = new StringBuilder();
        info.append("=== Información del Canal: ").append(factory.getTipoCanal()).append(" ===\n");
        
        GeneradorFactura generador = factory.crearGeneradorFactura();
        info.append("Generador: ").append(generador.obtenerTipoGenerador()).append("\n");
        info.append("Factura electrónica: ").append(generador.requiereFacturaElectronica() ? "Sí" : "No").append("\n");
        
        ValidadorStock validador = factory.crearValidadorStock();
        info.append("Validador: ").append(validador.obtenerTipoValidador()).append("\n");
        
        CalculadorEnvio calculador = factory.crearCalculadorEnvio();
        info.append("Calculador: ").append(calculador.obtenerTipoCalculador()).append("\n");
        info.append("Envío express: ").append(calculador.soportaEnvioExpress() ? "Sí" : "No").append("\n");
        
        GestorDevolucion gestor = factory.crearGestorDevolucion();
        info.append("Gestor: ").append(gestor.obtenerTipoGestor()).append("\n");
        info.append("Política: ").append(gestor.obtenerPoliticaDevolucion()).append("\n");
        
        return info.toString();
    }
}