package Creacionales.Builder.vista;

import Creacionales.Builder.controlador.FacturaControlador;
import Creacionales.Builder.modelo.*;
import java.util.Scanner;

public class ConsolaFactura {
    private final FacturaControlador controlador;
    private final Scanner scanner;

    public ConsolaFactura() {
        this.controlador = new FacturaControlador();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== BUILDER - Gestión de Facturas ===");
        System.out.println("1. Crear factura simple");
        System.out.println("2. Crear factura completa");
        System.out.println("3. Crear factura con envío");
        System.out.println("4. Listar facturas");
        System.out.println("5. Buscar factura");
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
                case 1 -> crearFacturaSimple();
                case 2 -> crearFacturaCompleta();
                case 3 -> crearFacturaConEnvio();
                case 4 -> listarFacturas();
                case 5 -> buscarFactura();
                case 6 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }

        System.out.println("¡Hasta luego!");
    }

    private void crearFacturaSimple() {
        System.out.println("\n--- Crear Factura Simple ---");

        Cliente cliente = crearCliente();
        ItemFactura item1 = new ItemFactura("P001", "Arroz", 2, 2500);
        ItemFactura item2 = new ItemFactura("P002", "Azúcar", 1, 3000);

        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(item1)
                .agregarItem(item2)
                .conMetodoPago("EFECTIVO")
                .calcularImpuestosAutomaticamente()
                .build();

        controlador.guardarFactura(factura);

        System.out.println("✓ Factura creada: " + factura);
        System.out.println("Total: $" + factura.calcularTotal());
    }

    private void crearFacturaCompleta() {
        System.out.println("\n--- Crear Factura Completa ---");

        Cliente cliente = crearCliente();

        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("P001", "Leche", 3, 3500))
                .agregarItem(new ItemFactura("P002", "Pan", 5, 2000))
                .agregarItem(new ItemFactura("P003", "Queso", 1, 15000))
                .conMetodoPago("TARJETA")
                .calcularImpuestosAutomaticamente()
                .conDescuentos(2000)
                .conPropina(5000)
                .conObservaciones("Cliente frecuente - Descuento aplicado")
                .build();

        controlador.guardarFactura(factura);

        System.out.println("✓ Factura completa creada: " + factura);
        System.out.printf("Subtotal: $%.2f%n", factura.getSubtotal());
        System.out.printf("Impuestos: $%.2f%n", factura.getImpuestos());
        System.out.printf("Descuentos: $%.2f%n", factura.getDescuentos());
        System.out.printf("Propina: $%.2f%n", factura.getPropina());
        System.out.printf("TOTAL: $%.2f%n", factura.calcularTotal());
    }

    private void crearFacturaConEnvio() {
        System.out.println("\n--- Crear Factura con Envío ---");

        Cliente cliente = crearCliente();
        cliente.setDireccion("Calle 123 #45-67");
        cliente.setEmail("cliente@email.com");

        Factura factura = controlador.iniciarFactura()
                .paraCliente(cliente)
                .agregarItem(new ItemFactura("E001", "Licuadora", 1, 150000))
                .agregarItem(new ItemFactura("E002", "Batidora", 1, 80000))
                .conMetodoPago("TRANSFERENCIA")
                .calcularImpuestosAutomaticamente()
                .conDireccionEnvio(cliente.getDireccion())
                .conObservaciones("Envío programado para mañana")
                .build();

        controlador.guardarFactura(factura);

        System.out.println("✓ Factura con envío creada: " + factura);
        System.out.println("Dirección de envío: " + factura.getDireccionEnvio());
        System.out.println("Total con envío: $" + factura.calcularTotal());
    }

    private void listarFacturas() {
        System.out.println("\n--- Lista de Facturas ---");
        var facturas = controlador.obtenerTodasLasFacturas();

        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas");
        } else {
            facturas.forEach(f -> {
                System.out.printf("%s | Cliente: %s | Total: $%.2f | Fecha: %s%n",
                        f.getNumeroFactura(),
                        f.getCliente() != null ? f.getCliente().getNombre() : "N/A",
                        f.calcularTotal(),
                        f.getFechaEmision());
            });
            System.out.printf("\nTotal ventas: $%.2f%n", controlador.calcularTotalVentas());
        }
    }

    private void buscarFactura() {
        System.out.print("\nNúmero de factura: ");
        String numero = scanner.nextLine();

        Factura factura = controlador.buscarFactura(numero);

        if (factura != null) {
            System.out.println("Encontrada: " + factura);
            System.out.println("Items: " + factura.getItems().size());
            System.out.println("Método de pago: " + factura.getMetodoPago());
            if (factura.getObservaciones() != null) {
                System.out.println("Observaciones: " + factura.getObservaciones());
            }
        } else {
            System.out.println("Factura no encontrada");
        }
    }

    private Cliente crearCliente() {
        System.out.print("Nombre cliente: ");
        String nombre = scanner.nextLine();
        return new Cliente("C" + System.currentTimeMillis(), nombre, "1234567890");
    }

    public static void main(String[] args) {
        ConsolaFactura vista = new ConsolaFactura();
        vista.ejecutar();
    }
}
