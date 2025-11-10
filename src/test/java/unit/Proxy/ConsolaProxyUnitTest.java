package unit.Proxy;

import Estructurales.Proxy.vista.ConsolaProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConsolaProxy - Vista Tests (Cobertura)")
public class ConsolaProxyUnitTest {

    @Test
    @DisplayName("mostrarMenu imprime opciones del proxy")
    public void testMostrarMenuPrintsOptions() {
        ConsolaProxy consola = new ConsolaProxy();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            consola.mostrarMenu();
            String salida = outContent.toString();
            assertTrue(salida.contains("PROXY - Sistema de Catálogo"));
            assertTrue(salida.contains("1. Cargar productos al catálogo"));
            assertTrue(salida.contains("8. Salir"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("verListaProductos cuando no hay productos muestra aviso")
    public void testVerListaProductosEmpty() throws Exception {
        ConsolaProxy consola = new ConsolaProxy();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));

            Method verLista = ConsolaProxy.class.getDeclaredMethod("verListaProductos");
            verLista.setAccessible(true);
            verLista.invoke(consola);

            String salida = outContent.toString();
            assertTrue(salida.contains("No hay productos en el catálogo") || salida.contains("Lista mostrada"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("mostrarEstadisticas imprime los datos proporcionados")
    public void testMostrarEstadisticas() {
        ConsolaProxy consola = new ConsolaProxy();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            consola.mostrarEstadisticas(5, true);
            String salida = outContent.toString();
            assertTrue(salida.contains("Número de accesos") || salida.contains("Estadísticas"));
            assertTrue(salida.contains("5") || salida.contains("Sí"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("mostrarMensaje y mostrarError imprimen correctamente")
    public void testMostrarMensajeAndError() {
        ConsolaProxy consola = new ConsolaProxy();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        try {
            System.setOut(new PrintStream(outContent));
            System.setErr(new PrintStream(errContent));

            consola.mostrarMensaje("hola");
            consola.mostrarError("malo");

            assertTrue(outContent.toString().contains("hola"));
            assertTrue(errContent.toString().contains("ERROR: malo"));
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}
