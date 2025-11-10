package Creacionales.Prototype.controlador;

import Creacionales.Prototype.modelo.*;
import java.time.LocalDate;
import java.util.*;

public class PromocionControlador {
    private final RegistroPromocion registro;
    private final Map<String, Promocion> promocionesActivas;
    private int contadorPromociones;

    public PromocionControlador() {
        this.registro = new RegistroPromocion();
        this.promocionesActivas = new HashMap<>();
        this.contadorPromociones = 1000;
    }

    public Promocion crearDesdePrototipo(String clavePrototipo, String nuevoNombre) {
        Promocion promocion = registro.obtenerPrototipo(clavePrototipo);
        if (promocion == null) {
            return null;
        }
        
        String nuevoId = generarIdPromocion();
        promocion.setId(nuevoId);
        promocion.setNombre(nuevoNombre);
        
        return promocion;
    }

    public void guardarPromocion(Promocion promocion) {
        if (promocion == null) {
            throw new IllegalArgumentException("La promoción no puede ser nula");
        }
        promocionesActivas.put(promocion.getId(), promocion);
    }

    public Promocion buscarPromocion(String id) {
        return promocionesActivas.get(id);
    }

    public List<Promocion> obtenerPromocionesActivas() {
        return promocionesActivas.values().stream()
            .filter(Promocion::estaVigente)
            .toList();
    }

    public List<Promocion> obtenerTodasLasPromociones() {
        return new ArrayList<>(promocionesActivas.values());
    }

    public Promocion clonarPromocion(String idOriginal, String nuevoNombre) {
        Promocion original = promocionesActivas.get(idOriginal);
        if (original == null) {
            return null;
        }
        
        Promocion clonada = original.clone();
        String nuevoId = generarIdPromocion();
        clonada.setId(nuevoId);
        clonada.setNombre(nuevoNombre);
        
        return clonada;
    }

    public void registrarPrototipoPersonalizado(String clave, Promocion prototipo) {
        registro.registrarPrototipo(clave, prototipo);
    }

    public Promocion crearPromocionPorcentaje(String nombre, double porcentaje, 
                                               LocalDate inicio, LocalDate fin) {
        Promocion promocion = crearDesdePrototipo("BASE_PORCENTAJE", nombre);
        if (promocion != null) {
            promocion.setPorcentajeDescuento(porcentaje);
            promocion.setFechaInicio(inicio);
            promocion.setFechaFin(fin);
            promocion.setActiva(true);
        }
        return promocion;
    }

    public Promocion crearPromocion2x1(String nombre, LocalDate inicio, LocalDate fin) {
        Promocion promocion = crearDesdePrototipo("BASE_2X1", nombre);
        if (promocion != null) {
            promocion.setFechaInicio(inicio);
            promocion.setFechaFin(fin);
            promocion.setActiva(true);
        }
        return promocion;
    }

    public List<Promocion> buscarPromocionesVigentes() {
        return promocionesActivas.values().stream()
            .filter(Promocion::estaVigente)
            .toList();
    }

    public List<Promocion> buscarPorCategoria(String categoria) {
        // Retornar promociones activas que sean aplicables a la categoría.
        // Nota: no filtramos por vigencia aquí porque se desea listar las promociones
        // ya configuradas para una categoría aunque su fecha aún no esté en rango.
        return promocionesActivas.values().stream()
            .filter(p -> p.isActiva())
            .filter(p -> p.esAplicableACategoria(categoria))
            .toList();
    }

    private String generarIdPromocion() {
        return String.format("PROMO-%04d", contadorPromociones++);
    }

    public RegistroPromocion getRegistro() {
        return registro;
    }
}
