package Creacionales.AbstractFactory.vista;

import Creacionales.AbstractFactory.controlador.VentaControlador;
import Creacionales.AbstractFactory.modelo.*;
import java.util.Map;
import java.util.Scanner;

public class ConsolaVenta {
    private final VentaControlador controlador;
    private final Scanner scanner;

    public ConsolaVenta() {
        this.controlador = new VentaControlador();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        System.out.println("\n=== ABSTRACT FACTORY - Gestión de Ventas Multi-Canal ===");
        System.out.println("1. Crear venta POS (Punto de Venta)");
        System.out.println("2. Crear venta E-commerce (Online)");
        System.out.println("3. Crear venta B2B (Corporativa)");
        System.out.println("4. Consultar información de canal");
        System.out.println("5. Procesar devolución");
        System.out.println("6. Calcular envío");
        System.out.println("7. Ver estadísticas");
        System.out.println("8. Listar ventas");
        System.out.println("9. Salir");
        System.out.print("Opción: ");
    }

    public void ejecutar() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1 -> crearVentaPOS();
                case 2 -> crearVentaEcommerce();
                case 3 -> crearVentaB2B();
                case 4 -> consultarInfoCanal();
                case 5 -> procesarDevolucion();
                case 6 -> calcularEnvio();
                case 7 -> verEstadisticas();
                case 8 -> listarVentas();
                case 9 -> continuar = false;
                default -> System.out.println("Opción inválida");
            }
        }
        
        System.out.println("¡Hasta luego!");
    }

    private void crearVentaPOS() {
        System.out.println("\n--- Venta en Punto de Venta (POS) ---");
        
        System.out.print("ID Cliente: ");
        String clienteId = scanner.nextLine();
        
        System.out.print("Nombre Cliente: ");
        String clienteNombre = scanner.nextLine();
        
        DatosVenta venta = controlador.crearVenta("POS", clienteId, clienteNombre);
        
        System.out.println("Agregar productos (escriba 'fin' para terminar):");
        while (true) {
            System.out.print("Código producto (o 'fin'): ");
            String codigo = scanner.nextLine();
            if (codigo.equalsIgnoreCase("fin")) break;
            
            System.out.print("Nombre producto: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            
            System.out.print("Precio unitario: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();
            
            venta.agregarItem(new ItemVenta(codigo, nombre, cantidad, precio));
            System.out.println("✓ Producto agregado");
        }
        
        System.out.print("Método de pago: ");
        venta.setMetodoPago(scanner.nextLine());
        
        boolean exito = controlador.procesarVenta("POS", venta);
        
        if (exito) {
            System.out.println("\n✓ Venta procesada exitosamente");
            System.out.println("Número de venta: " + venta.getNumeroVenta());
            System.out.printf("Total: $%.2f%n", venta.getTotal());
            System.out.println("\nCaracterísticas POS:");
            System.out.println("- Validación de stock en tiempo real");
            System.out.println("- Sin costo de envío");
            System.out.println("- Devolución: 15 días");
        } else {
            System.out.println("✗ Error al procesar venta (stock insuficiente)");
        }
    }

    private void crearVentaEcommerce() {
        System.out.println("\n--- Venta E-commerce (Online) ---");
        
        System.out.print("ID Cliente: ");
        String clienteId = scanner.nextLine();
        
        System.out.print("Nombre Cliente: ");
        String clienteNombre = scanner.nextLine();
        
        DatosVenta venta = controlador.crearVenta("ECOMMERCE", clienteId, clienteNombre);
        
        System.out.println("Agregar productos (escriba 'fin' para terminar):");
        while (true) {
            System.out.print("Código producto (o 'fin'): ");
            String codigo = scanner.nextLine();
            if (codigo.equalsIgnoreCase("fin")) break;
            
            System.out.print("Nombre producto: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            
            System.out.print("Precio unitario: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();
            
            venta.agregarItem(new ItemVenta(codigo, nombre, cantidad, precio));
            System.out.println("✓ Producto agregado");
        }
        
        System.out.print("Dirección de envío: ");
        venta.setDireccionEnvio(scanner.nextLine());
        
        System.out.print("Método de pago: ");
        venta.setMetodoPago(scanner.nextLine());
        
        // Calcular envío
        double costoEnvio = controlador.calcularEnvio("ECOMMERCE", venta.getDireccionEnvio(), 5.0);
        
        boolean exito = controlador.procesarVenta("ECOMMERCE", venta);
        
        if (exito) {
            System.out.println("\n✓ Venta procesada exitosamente");
            System.out.println("Número de venta: " + venta.getNumeroVenta());
            System.out.printf("Subtotal: $%.2f%n", venta.getTotal());
            System.out.printf("Envío: $%.2f%n", costoEnvio);
            System.out.printf("Total con envío: $%.2f%n", venta.getTotal() + costoEnvio);
            System.out.println("\nCaracterísticas E-commerce:");
            System.out.println("- Validación de inventario general");
            System.out.println("- Envío a domicilio incluido");
            System.out.println("- Devolución: 30 días");
        } else {
            System.out.println("✗ Error al procesar venta");
        }
    }

    private void crearVentaB2B() {
        System.out.println("\n--- Venta B2B (Corporativa) ---");
        
        System.out.print("ID Empresa: ");
        String empresaId = scanner.nextLine();
        
        System.out.print("Nombre Empresa: ");
        String empresaNombre = scanner.nextLine();
        
        DatosVenta venta = controlador.crearVenta("B2B", empresaId, empresaNombre);
        
        System.out.println("Agregar productos (escriba 'fin' para terminar):");
        while (true) {
            System.out.print("Código producto (o 'fin'): ");
            String codigo = scanner.nextLine();
            if (codigo.equalsIgnoreCase("fin")) break;
            
            System.out.print("Nombre producto: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            
            System.out.print("Precio unitario: ");
            double precio = scanner.nextDouble();
            scanner.nextLine();
            
            venta.agregarItem(new ItemVenta(codigo, nombre, cantidad, precio));
            System.out.println("✓ Producto agregado");
        }
        
        System.out.print("Centro de distribución: ");
        venta.setDireccionEnvio(scanner.nextLine());
        
        venta.setMetodoPago("CREDITO_30_DIAS");
        
        boolean exito = controlador.procesarVenta("B2B", venta);
        
        if (exito) {
            System.out.println("\n✓ Venta corporativa procesada");
            System.out.println("Número de venta: " + venta.getNumeroVenta());
            System.out.printf("Total: $%.2f%n", venta.getTotal());
            System.out.println("Método de pago: Crédito 30 días");
            System.out.println("\nCaracterísticas B2B:");
            System.out.println("- Validación contra órdenes de compra");
            System.out.println("- Envío logístico especializado");
            System.out.println("- Devolución: 60 días");
        } else {
            System.out.println("✗ Error al procesar venta corporativa");
        }
    }

    private void consultarInfoCanal() {
        System.out.println("\n--- Información de Canal ---");
        System.out.println("Canales disponibles: " + controlador.obtenerCanalesDisponibles());
        System.out.print("Ingrese canal (POS/ECOMMERCE/B2B): ");
        String canal = scanner.nextLine();
        
        String info = controlador.obtenerInformacionFactory(canal);
        System.out.println("\n" + info);
    }

    private void procesarDevolucion() {
        System.out.println("\n--- Procesar Devolución ---");
        
        System.out.print("Número de venta: ");
        String numeroVenta = scanner.nextLine();
        
        DatosVenta venta = controlador.buscarVenta(numeroVenta);
        if (venta == null) {
            System.out.println("✗ Venta no encontrada");
            return;
        }
        
        String tipoCanal = numeroVenta.substring(0, numeroVenta.indexOf('-'));
        
        System.out.print("Días transcurridos desde la compra: ");
        int dias = scanner.nextInt();
        scanner.nextLine();
        
        boolean permitida = controlador.procesarDevolucion(
            tipoCanal, 
            numeroVenta, 
            dias, 
            venta.getTotal()
        );
        
        if (permitida) {
            VentaComponentFactory factory = controlador.obtenerFactory(tipoCanal);
            GestorDevolucion gestor = factory.crearGestorDevolucion();
            double reembolso = gestor.calcularReembolso(venta.getTotal(), dias);
            
            System.out.println("✓ Devolución aprobada");
            System.out.printf("Monto original: $%.2f%n", venta.getTotal());
            System.out.printf("Reembolso: $%.2f%n", reembolso);
        } else {
            System.out.println("✗ Devolución no permitida");
            System.out.println("Verifique la política de devoluciones del canal");
        }
    }

    private void calcularEnvio() {
        System.out.println("\n--- Calcular Costo de Envío ---");
        
        System.out.println("Canal: 1.E-commerce 2.B2B");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        String canal = opcion == 1 ? "ECOMMERCE" : "B2B";
        
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        
        System.out.print("Peso (kg): ");
        double peso = scanner.nextDouble();
        scanner.nextLine();
        
        double costo = controlador.calcularEnvio(canal, destino, peso);
        
        VentaComponentFactory factory = controlador.obtenerFactory(canal);
        CalculadorEnvio calculador = factory.crearCalculadorEnvio();
        int tiempoEntrega = calculador.calcularTiempoEntrega(destino);
        
        System.out.printf("\nCosto de envío: $%.2f%n", costo);
        System.out.printf("Tiempo estimado: %d días%n", tiempoEntrega);
        System.out.println("Envío express disponible: " + 
            (calculador.soportaEnvioExpress() ? "Sí" : "No"));
    }

    private void verEstadisticas() {
        System.out.println("\n--- Estadísticas de Ventas ---");
        
        Map<String, Long> stats = controlador.obtenerEstadisticasPorCanal();
        
        System.out.println("\nVentas por canal:");
        stats.forEach((canal, cantidad) -> 
            System.out.printf("- %s: %d ventas%n", canal, cantidad)
        );
        
        System.out.printf("\nTotal ventas: %d%n", controlador.obtenerVentas().size());
        System.out.printf("Monto total: $%.2f%n", controlador.calcularTotalVentas());
    }

    private void listarVentas() {
        System.out.println("\n--- Lista de Ventas ---");
        
        var ventas = controlador.obtenerVentas();
        
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas");
        } else {
            ventas.forEach(v -> {
                System.out.printf("%s | Cliente: %s | Total: $%.2f | Items: %d%n",
                    v.getNumeroVenta(),
                    v.getClienteNombre(),
                    v.getTotal(),
                    v.getItems().size());
            });
        }
    }

    public static void main(String[] args) {
        ConsolaVenta vista = new ConsolaVenta();
        vista.ejecutar();
    }
}
