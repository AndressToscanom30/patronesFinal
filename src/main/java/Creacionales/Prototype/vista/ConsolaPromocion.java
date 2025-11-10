package Creacionales.Prototype.vista;

import Creacionales.Prototype.controlador.PromocionControlador;
import Creacionales.Prototype.modelo.*;
import java.time.LocalDate;
import java.util.Scanner;


public class ConsolaPromocion {
    private final PromocionControlador controlador;
    private final Scanner scanner;

    public ConsolaPromocion() {
        this.controlador = new PromocionControlador();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== PROTOTYPE - Gestión de Promociones ===");
        System.out.println("1. Crear promoción desde prototipo");
        System.out.println("2. Clonar promoción existente");
        System.out.println("3. Listar promociones vigentes");
        System.out.println("4. Crear prototipo personalizado");
        System.out.println("5. Aplicar promoción (simular)");
        System.out.println("6. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1 -> crearDesdePrototipo();
                case 2 -> clonarPromocion();
                case 3 -> listarPromocionesVigentes();
                case 4 -> crearPrototipoPersonalizado();
                case 5 -> simularAplicacion();
                case 6 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }
        
        System.out.println("¡Hasta luego!");
    }

    private void crearDesdePrototipo() {
        System.out.println("\n--- Crear desde Prototipo ---");
        System.out.println("Prototipos disponibles:");
        System.out.println("1. BASE_PORCENTAJE");
        System.out.println("2. BASE_2X1");
        System.out.println("3. BASE_COMBO");
        System.out.print("Seleccione: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        String clave = switch (opcion) {
            case 1 -> "BASE_PORCENTAJE";
            case 2 -> "BASE_2X1";
            case 3 -> "BASE_COMBO";
            default -> null;
        };
        
        if (clave == null) {
            System.out.println("Opción inválida");
            return;
        }
        
        System.out.print("Nombre de la promoción: ");
        String nombre = scanner.nextLine();
        
        Promocion promocion = controlador.crearDesdePrototipo(clave, nombre);
        
        if (promocion != null) {
            configurarPromocion(promocion);
            controlador.guardarPromocion(promocion);
            System.out.println("✓ Promoción creada: " + promocion);
        } else {
            System.out.println("✗ Error al crear promoción");
        }
    }

    private void configurarPromocion(Promocion promocion) {
        System.out.print("Fecha inicio (yyyy-MM-dd) o ENTER para hoy: ");
        String fechaInicioStr = scanner.nextLine();
        LocalDate fechaInicio = fechaInicioStr.isEmpty() ? 
            LocalDate.now() : LocalDate.parse(fechaInicioStr);
        
        System.out.print("Fecha fin (yyyy-MM-dd): ");
        String fechaFinStr = scanner.nextLine();
        LocalDate fechaFin = LocalDate.parse(fechaFinStr);
        
        promocion.setFechaInicio(fechaInicio);
        promocion.setFechaFin(fechaFin);
        promocion.setActiva(true);
        
        if (promocion instanceof PromocionPorcentaje pp) {
            System.out.print("Porcentaje de descuento: ");
            double porcentaje = scanner.nextDouble();
            scanner.nextLine();
            pp.setPorcentajeDescuento(porcentaje);
            
            System.out.print("Monto mínimo: ");
            double montoMin = scanner.nextDouble();
            scanner.nextLine();
            pp.setMontoMinimo(montoMin);
        }
    }

    private void clonarPromocion() {
        System.out.println("\n--- Clonar Promoción ---");
        var promociones = controlador.obtenerTodasLasPromociones();
        
        if (promociones.isEmpty()) {
            System.out.println("No hay promociones para clonar");
            return;
        }
        
        System.out.println("Promociones disponibles:");
        for (int i = 0; i < promociones.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, promociones.get(i));
        }
        
        System.out.print("Seleccione promoción a clonar: ");
        int indice = scanner.nextInt() - 1;
        scanner.nextLine();
        
        if (indice < 0 || indice >= promociones.size()) {
            System.out.println("Índice inválido");
            return;
        }
        
        Promocion original = promociones.get(indice);
        
        System.out.print("Nombre para la copia: ");
        String nuevoNombre = scanner.nextLine();
        
        Promocion clonada = controlador.clonarPromocion(original.getId(), nuevoNombre);
        
        if (clonada != null) {
            controlador.guardarPromocion(clonada);
            System.out.println("✓ Promoción clonada: " + clonada);
        } else {
            System.out.println("✗ Error al clonar");
        }
    }

    private void listarPromocionesVigentes() {
        System.out.println("\n--- Promociones Vigentes ---");
        var vigentes = controlador.buscarPromocionesVigentes();
        
        if (vigentes.isEmpty()) {
            System.out.println("No hay promociones vigentes");
        } else {
            vigentes.forEach(p -> {
                System.out.printf("- %s | Tipo: %s | Vigente hasta: %s%n",
                    p.getNombre(), p.obtenerTipo(), p.getFechaFin());
            });
        }
    }

    private void crearPrototipoPersonalizado() {
        System.out.println("\n--- Crear Prototipo Personalizado ---");
        System.out.print("Clave del prototipo: ");
        String clave = scanner.nextLine();
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.println("Tipo: 1.Porcentaje 2.2x1 3.Combo");
        int tipo = scanner.nextInt();
        scanner.nextLine();
        
        Promocion prototipo = switch (tipo) {
            case 1 -> new PromocionPorcentaje(clave, nombre, "Prototipo personalizado");
            case 2 -> new PromocionDosXUno(clave, nombre, "Prototipo personalizado");
            case 3 -> new PromocionCombo(clave, nombre, "Prototipo personalizado");
            default -> null;
        };
        
        if (prototipo != null) {
            controlador.registrarPrototipoPersonalizado(clave, prototipo);
            System.out.println("✓ Prototipo registrado: " + clave);
        }
    }

    private void simularAplicacion() {
        System.out.println("\n--- Simular Aplicación de Promoción ---");
        var vigentes = controlador.buscarPromocionesVigentes();
        
        if (vigentes.isEmpty()) {
            System.out.println("No hay promociones vigentes");
            return;
        }
        
        System.out.println("Seleccione promoción:");
        for (int i = 0; i < vigentes.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, vigentes.get(i).getNombre());
        }
        
        int indice = scanner.nextInt() - 1;
        scanner.nextLine();
        
        if (indice < 0 || indice >= vigentes.size()) {
            System.out.println("Índice inválido");
            return;
        }
        
        Promocion promo = vigentes.get(indice);
        
        System.out.print("Monto original: ");
        double monto = scanner.nextDouble();
        scanner.nextLine();
        
        double montoFinal = promo.aplicarDescuento(monto);
        double ahorro = monto - montoFinal;
        
        System.out.printf("Monto original: $%.2f%n", monto);
        System.out.printf("Ahorro: $%.2f%n", ahorro);
        System.out.printf("Total a pagar: $%.2f%n", montoFinal);
    }

    public static void main(String[] args) {
        ConsolaPromocion vista = new ConsolaPromocion();
        vista.ejecutar();
    }
}
