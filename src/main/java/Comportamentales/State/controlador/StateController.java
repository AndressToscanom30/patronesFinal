package Comportamentales.State.controlador;

import Comportamentales.State.modelo.*;
import Comportamentales.State.vista.ConsolaState;
import java.util.ArrayList;
import java.util.List;

public class StateController {
    private ConsolaState vista;
    private List<Pedido> pedidos;
    private int contadorPedidos;
    
    public StateController(ConsolaState vista) {
        this.vista = vista;
        this.pedidos = new ArrayList<>();
        this.contadorPedidos = 1000;
    }
    
    public Pedido crearPedido(String cliente) {
        String numeroPedido = "PED-" + (++contadorPedidos);
        Pedido pedido = new Pedido(numeroPedido, cliente);
        pedidos.add(pedido);
        vista.mostrarMensaje("✓ Pedido creado: " + numeroPedido);
        return pedido;
    }
    
    public void agregarItemPedido(Pedido pedido, String producto, int cantidad, double precio) {
        ItemPedido item = new ItemPedido(producto, cantidad, precio);
        pedido.agregarItem(item);
        vista.mostrarMensaje("✓ Item agregado al pedido");
    }
    
    public void procesarPedido(Pedido pedido) {
        pedido.procesar();
    }
    
    public void cancelarPedido(Pedido pedido) {
        pedido.cancelar();
    }
    
    public void confirmarPago(Pedido pedido) {
        pedido.confirmarPago();
    }
    
    public void enviarPedido(Pedido pedido) {
        pedido.enviar();
    }
    
    public void entregarPedido(Pedido pedido) {
        pedido.entregar();
    }
    
    public List<Pedido> obtenerPedidos() {
        return new ArrayList<>(pedidos);
    }
    
    public Pedido buscarPedido(String numeroPedido) {
        for (Pedido pedido : pedidos) {
            if (pedido.getNumeroPedido().equals(numeroPedido)) {
                return pedido;
            }
        }
        return null;
    }
    
    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getEstadoActual().equals(estado)) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }
}