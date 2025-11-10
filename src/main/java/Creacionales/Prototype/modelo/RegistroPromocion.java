package Creacionales.Prototype.modelo;

import java.util.HashMap;
import java.util.Map;

public class RegistroPromocion {
    private final Map<String, Promocion> prototipos;

    public RegistroPromocion() {
        this.prototipos = new HashMap<>();
        inicializarPrototiposBase();
    }

    private void inicializarPrototiposBase() {
        PromocionPorcentaje descuentoBase = new PromocionPorcentaje(
            "BASE_PORCENTAJE", 
            "Descuento Porcentual Base", 
            "Plantilla para descuentos por porcentaje"
        );
        descuentoBase.setPorcentajeDescuento(10.0);
        registrarPrototipo("BASE_PORCENTAJE", descuentoBase);

        PromocionDosXUno dosXUnoBase = new PromocionDosXUno(
            "BASE_2X1",
            "Promoción 2x1 Base",
            "Plantilla para promociones 2x1"
        );
        dosXUnoBase.setCantidadCompra(2);
        dosXUnoBase.setCantidadPaga(1);
        registrarPrototipo("BASE_2X1", dosXUnoBase);

        PromocionCombo comboBase = new PromocionCombo(
            "BASE_COMBO",
            "Combo Base",
            "Plantilla para combos"
        );
        registrarPrototipo("BASE_COMBO", comboBase);
    }

    public void registrarPrototipo(String clave, Promocion prototipo) {
        if (clave == null || clave.trim().isEmpty()) {
            throw new IllegalArgumentException("La clave no puede ser nula o vacía");
        }
        if (prototipo == null) {
            throw new IllegalArgumentException("El prototipo no puede ser nulo");
        }
        prototipos.put(clave, prototipo);
    }

    public Promocion obtenerPrototipo(String clave) {
        Promocion prototipo = prototipos.get(clave);
        if (prototipo == null) {
            return null;
        }
        return prototipo.clone();
    }

    public boolean existePrototipo(String clave) {
        return prototipos.containsKey(clave);
    }

    public void eliminarPrototipo(String clave) {
        prototipos.remove(clave);
    }

    public int cantidadPrototipos() {
        return prototipos.size();
    }
}
