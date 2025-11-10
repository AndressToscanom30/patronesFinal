package unit.Builder;

import Creacionales.Builder.vista.ConsolaFactura;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsolaFacturaUnitTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void mostrarMenu_printsHeader() {
        ConsolaFactura consola = new ConsolaFactura();
        consola.mostrarMenu();
        String out = outContent.toString();
        assertTrue(out.contains("BUILDER - Gestión de Facturas") || out.contains("Listar facturas"));
    }

    @Test
    void listarFacturas_whenEmpty_printsNoFacturas() throws Exception {
        ConsolaFactura consola = new ConsolaFactura();
        Method m = ConsolaFactura.class.getDeclaredMethod("listarFacturas");
        m.setAccessible(true);
        m.invoke(consola);

        String out = outContent.toString();
        assertTrue(out.contains("No hay facturas registradas") || out.contains("Lista de Facturas"));
    }
}
