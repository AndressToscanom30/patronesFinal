package unit.Facade;

import Estructurales.Facade.modelo.ResultadoVenta;
import Estructurales.Facade.vista.ConsolaFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsolaFacadeUnitTest {
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
        ConsolaFacade consola = new ConsolaFacade();
        consola.mostrarMenu();
        String out = outContent.toString();
        assertTrue(out.contains("FACADE - Sistema de Ventas"));
        assertTrue(out.contains("Venta express (demo rápida)") || out.contains("Venta express"));
    }

    @Test
    void mostrarResultadoVenta_printsSuccessAndFailure() {
        ConsolaFacade consola = new ConsolaFacade();

        ResultadoVenta exito = new ResultadoVenta();
        exito.setExitoso(true);
        exito.setMensaje("OK");
        exito.setComprobantePago("COMPROBANTE-1");
        exito.setPuntosGanados(10);

        consola.mostrarResultadoVenta(exito);
        String out = outContent.toString();
        assertTrue(out.contains("RESULTADO DE LA VENTA"));
        assertTrue(out.contains("EXITOSA") || out.contains("Comprobante") || out.contains("COMPROBANTE-1"));

        // clear and test failure
        outContent.reset();
        ResultadoVenta fail = new ResultadoVenta();
        fail.setExitoso(false);
        fail.setMensaje("Saldo insuficiente");
        consola.mostrarResultadoVenta(fail);
        String out2 = outContent.toString();
        assertTrue(out2.contains("FALLIDA") || out2.contains("Motivo") || out2.contains("Saldo insuficiente"));
    }

    @Test
    void mostrarPuntosCliente_and_mostrarReporte_printsExpected() {
        ConsolaFacade consola = new ConsolaFacade();
        consola.mostrarPuntosCliente("C-123", 42);
        consola.mostrarReporte("ReporteDemo");

        String out = outContent.toString();
        assertTrue(out.contains("C-123") && out.contains("42"));
        assertTrue(out.contains("Reporte generado: ReporteDemo"));
    }
}
