package Estructurales.Facade.modelo;

public class SistemaVentasFacade {
    private ValidadorStock validadorStock;
    private ProcesadorPagos procesadorPagos;
    private GeneradorFacturas generadorFacturas;
    private ActualizadorInventario actualizadorInventario;
    private SistemaFidelizacion sistemaFidelizacion;
    private ServicioNotificaciones servicioNotificaciones;
    private GeneradorReportes generadorReportes;
    
    public SistemaVentasFacade() {
        this.validadorStock = new ValidadorStock();
        this.procesadorPagos = new ProcesadorPagos();
        this.generadorFacturas = new GeneradorFacturas();
        this.actualizadorInventario = new ActualizadorInventario();
        this.sistemaFidelizacion = new SistemaFidelizacion();
        this.servicioNotificaciones = new ServicioNotificaciones();
        this.generadorReportes = new GeneradorReportes();
    }
    
    public ResultadoVenta procesarVenta(Venta venta) {
        ResultadoVenta resultado = new ResultadoVenta();
        
        try {
            System.out.println("\n=== INICIANDO PROCESO DE VENTA ===\n");
            
            System.out.println("PASO 1: Validando stock...");
            for (ItemVenta item : venta.getItems()) {
                if (!validadorStock.validarDisponibilidad(item.getCodigoProducto(), item.getCantidad())) {
                    resultado.setExitoso(false);
                    resultado.setMensaje("Stock insuficiente para: " + item.getNombreProducto());
                    return resultado;
                }
                validadorStock.reservarStock(item.getCodigoProducto(), item.getCantidad());
            }
            System.out.println("✓ Stock validado y reservado\n");
            
            System.out.println("PASO 2: Procesando pago...");
            boolean pagoExitoso = procesadorPagos.procesarPago(venta.getMetodoPago(), venta.getTotal());
            if (!pagoExitoso) {
                liberarStockReservado(venta);
                resultado.setExitoso(false);
                resultado.setMensaje("Error al procesar el pago");
                return resultado;
            }
            String comprobante = procesadorPagos.generarComprobante(venta.getMetodoPago(), venta.getTotal());
            venta.setComprobantePago(comprobante);
            System.out.println("✓ Pago procesado exitosamente\n");
            
            System.out.println("PASO 3: Generando factura...");
            String numeroFactura = generadorFacturas.generarFactura(
                venta.getClienteNombre(),
                venta.getSubtotal(),
                venta.getImpuestos()
            );
            venta.setNumeroFactura(numeroFactura);
            generadorFacturas.generarPDF(numeroFactura);
            System.out.println("✓ Factura generada: " + numeroFactura + "\n");
            
            System.out.println("PASO 4: Actualizando inventario...");
            for (ItemVenta item : venta.getItems()) {
                actualizadorInventario.descontarStock(item.getCodigoProducto(), item.getCantidad());
                actualizadorInventario.registrarMovimiento(
                    item.getCodigoProducto(),
                    item.getCantidad(),
                    "VENTA"
                );
                actualizadorInventario.verificarStockMinimo(item.getCodigoProducto());
            }
            System.out.println("✓ Inventario actualizado\n");
            
            System.out.println("PASO 5: Procesando programa de fidelización...");
            int puntos = sistemaFidelizacion.calcularPuntos(venta.getTotal());
            sistemaFidelizacion.acreditarPuntos(venta.getClienteId(), puntos);
            System.out.println("✓ " + puntos + " puntos acreditados\n");
            
            System.out.println("PASO 6: Enviando notificaciones...");
            servicioNotificaciones.enviarEmail(
                venta.getClienteEmail(),
                "Confirmación de compra",
                "Su compra ha sido procesada. Factura: " + numeroFactura
            );
            generadorFacturas.enviarFacturaElectronica(numeroFactura, venta.getClienteEmail());
            servicioNotificaciones.notificarVenta(venta.getClienteId(), numeroFactura);
            System.out.println("✓ Notificaciones enviadas\n");
            
            System.out.println("PASO 7: Generando reportes...");
            generadorReportes.registrarVenta(numeroFactura, venta.getTotal(), venta.getFecha());
            generadorReportes.actualizarEstadisticas("SUCURSAL-001", venta.getTotal());
            System.out.println("✓ Reportes actualizados\n");
            
            resultado.setExitoso(true);
            resultado.setMensaje("Venta procesada exitosamente");
            resultado.setNumeroFactura(numeroFactura);
            resultado.setComprobantePago(comprobante);
            resultado.setPuntosGanados(puntos);
            
            System.out.println("=== VENTA COMPLETADA EXITOSAMENTE ===\n");
            
        } catch (Exception e) {
            resultado.setExitoso(false);
            resultado.setMensaje("Error al procesar venta: " + e.getMessage());
            liberarStockReservado(venta);
            if (venta.getComprobantePago() != null) {
                procesadorPagos.revertirPago(venta.getComprobantePago());
            }
        }
        
        return resultado;
    }
    
    private void liberarStockReservado(Venta venta) {
        System.out.println("Liberando stock reservado...");
        for (ItemVenta item : venta.getItems()) {
            validadorStock.liberarStock(item.getCodigoProducto(), item.getCantidad());
        }
    }
    
    public int consultarPuntosCliente(String clienteId) {
        return sistemaFidelizacion.consultarPuntos(clienteId);
    }
    
    public String generarReporteVentas(java.util.Date fechaInicio, java.util.Date fechaFin) {
        return generadorReportes.generarReporteVentas(fechaInicio, fechaFin);
    }
}