package Estructurales.Proxy.controlador;

import Estructurales.Proxy.modelo.ImagenProducto;
import Estructurales.Proxy.modelo.ImagenProductoProxy;
import Estructurales.Proxy.vista.ConsolaProxy;

import java.util.ArrayList;
import java.util.List;

public class CatalogoController {
    private List<ImagenProducto> catalogo;
    private ConsolaProxy vista;
    
    public CatalogoController(ConsolaProxy vista) {
        this.catalogo = new ArrayList<>();
        this.vista = vista;
    }
    
    public void agregarProducto(String codigoProducto) {
        ImagenProducto proxy = new ImagenProductoProxy(codigoProducto);
        catalogo.add(proxy);
        vista.mostrarMensaje("Producto agregado al catálogo: " + codigoProducto);
    }
    
    public void mostrarCatalogo() {
        vista.mostrarMensaje("\n=== Mostrando catálogo ===");
        for (ImagenProducto imagen : catalogo) {
            imagen.mostrar();
        }
    }
    
    public void cargarImagen(int indice) {
        if (indice >= 0 && indice < catalogo.size()) {
            catalogo.get(indice).cargar();
        } else {
            vista.mostrarError("Índice inválido");
        }
    }
    
    public void mostrarImagen(int indice) {
        if (indice >= 0 && indice < catalogo.size()) {
            catalogo.get(indice).mostrar();
        } else {
            vista.mostrarError("Índice inválido");
        }
    }
    
    public int getCantidadProductos() {
        return catalogo.size();
    }
    
    public ImagenProducto getProducto(int indice) {
        if (indice >= 0 && indice < catalogo.size()) {
            return catalogo.get(indice);
        }
        return null;
    }
}