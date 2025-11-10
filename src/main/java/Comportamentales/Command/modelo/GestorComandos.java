package Comportamentales.Command.modelo;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


public class GestorComandos {
    private Stack<Command> historial;
    private Stack<Command> deshecho;
    
    public GestorComandos() {
        this.historial = new Stack<>();
        this.deshecho = new Stack<>();
    }
    
    public void ejecutarComando(Command comando) {
        comando.ejecutar();
        historial.push(comando);
        deshecho.clear(); // Limpiar pila de deshechos al ejecutar nuevo comando
    }
    
    public boolean deshacer() {
        if (historial.isEmpty()) {
            return false;
        }
        
        Command comando = historial.pop();
        comando.deshacer();
        deshecho.push(comando);
        return true;
    }
    
    public boolean rehacer() {
        if (deshecho.isEmpty()) {
            return false;
        }
        
        Command comando = deshecho.pop();
        comando.ejecutar();
        historial.push(comando);
        return true;
    }
    
    public boolean puedeDeshacer() {
        return !historial.isEmpty();
    }
    
    public boolean puedeRehacer() {
        return !deshecho.isEmpty();
    }
    
    public String obtenerUltimoComando() {
        if (historial.isEmpty()) {
            return "No hay comandos";
        }
        return historial.peek().obtenerDescripcion();
    }
    
    public List<String> obtenerHistorial() {
        List<String> listaHistorial = new ArrayList<>();
        for (Command comando : historial) {
            listaHistorial.add(comando.obtenerDescripcion());
        }
        return listaHistorial;
    }
    
    public void limpiarHistorial() {
        historial.clear();
        deshecho.clear();
    }
    
    public int getTamanoHistorial() {
        return historial.size();
    }
}