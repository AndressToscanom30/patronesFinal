package unit.Command;

import Comportamentales.Command.vista.ConsolaCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConsolaCommand - Vista Tests (Cobertura)")
public class ConsolaCommandUnitTest {

    @Test
    @DisplayName("mostrarMenu imprime las opciones")
    public void testMostrarMenuPrintsOptions() {
        ConsolaCommand consola = new ConsolaCommand();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            consola.mostrarMenu();
            String salida = outContent.toString();
            assertTrue(salida.contains("COMMAND - Gestión"));
            assertTrue(salida.contains("1. Ver catálogo"));
            assertTrue(salida.contains("11. Salir"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("verCatalogo (método privado) muestra productos del catálogo")
    public void testVerCatalogoPrivateMethod() throws Exception {
        ConsolaCommand consola = new ConsolaCommand();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));

            Method verCatalogo = ConsolaCommand.class.getDeclaredMethod("verCatalogo");
            verCatalogo.setAccessible(true);
            verCatalogo.invoke(consola);

            String salida = outContent.toString();
            assertTrue(salida.contains("CATÁLOGO DE PRODUCTOS"));
            assertTrue(salida.matches("(?s).*Leche.*Pan.*Huevos.*"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("verCarrito cuando está vacío imprime mensaje apropiado")
    public void testVerCarritoEmpty() throws Exception {
        ConsolaCommand consola = new ConsolaCommand();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));

            Method verCarrito = ConsolaCommand.class.getDeclaredMethod("verCarrito");
            verCarrito.setAccessible(true);
            verCarrito.invoke(consola);

            String salida = outContent.toString();
            assertTrue(salida.contains("El carrito está vacío") || salida.contains("Productos:"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("mostrarMensaje y mostrarError imprimen en los streams correctos")
    public void testMostrarMensajeAndError() {
        ConsolaCommand consola = new ConsolaCommand();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        try {
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));

            consola.mostrarMensaje("hola mundo");
            consola.mostrarError("algo malo");

            assertTrue(outContent.toString().contains("hola mundo"));
            assertTrue(errContent.toString().contains("ERROR: algo malo"));
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}
