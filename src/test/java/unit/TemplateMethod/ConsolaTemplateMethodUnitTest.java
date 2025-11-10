package unit.TemplateMethod;

import Comportamentales.TemplateMethod.vista.ConsolaTemplateMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsolaTemplateMethodUnitTest {
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void mostrarMenu_printsHeader() {
        ConsolaTemplateMethod consola = new ConsolaTemplateMethod();
        consola.mostrarMenu();
        String out = outContent.toString();
        assertTrue(out.contains("TEMPLATE METHOD - Procesos de Venta") || out.contains("SISTEMA DE VENTAS - TEMPLATE METHOD"));
    }

    @Test
    void compararProcesos_printsImplementations() throws Exception {
        ConsolaTemplateMethod consola = new ConsolaTemplateMethod();
        Method m = ConsolaTemplateMethod.class.getDeclaredMethod("compararProcesos");
        m.setAccessible(true);
        m.invoke(consola);

        String out = outContent.toString();
        assertTrue(out.contains("PROCESO: EFECTIVO") || out.contains("PROCESO: TARJETA") || out.contains("PROCESO: TRANSFERENCIA"));
        assertTrue(out.contains("CONCLUSIÓN") || out.contains("Template"));
    }

    @Test
    void mensajeAndError_printedToCorrectStreams() {
        ConsolaTemplateMethod consola = new ConsolaTemplateMethod();
        consola.mostrarMensaje("hola");
        consola.mostrarError("ups");

        String out = outContent.toString();
        String err = errContent.toString();
        assertTrue(out.contains("hola"));
        assertTrue(err.contains("ERROR: ups") || err.contains("ups"));
    }
}
