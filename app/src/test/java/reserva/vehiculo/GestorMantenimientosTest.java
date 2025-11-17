package reserva.vehiculo;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.Test;

import reserva.vehiculo.excepciones.GestorMantenimientosException;

public class GestorMantenimientosTest {
    @Test
    public void abrirYFinalizarMantenimiento_yCalculoCostos_exitoso() {
        GestorMantenimientos gestor = new GestorMantenimientos();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        

        Mantenimiento m = gestor.abrirMantenimiento(vehic, LocalDate.of(2025,10,1), "Preventivo");

        List<Mantenimiento> abiertos = gestor.getMantenimientosAbiertos();
        assertTrue("Debe contener el mantenimiento abierto", abiertos.stream().anyMatch(x -> x.equals(m)));

        BigDecimal costo = new BigDecimal("3000");
        gestor.cerrarMantenimiento(m, LocalDate.of(2025,10,3), "Reparación frenos", costo);

        assertFalse("No debe aparecer entre abiertos", gestor.getMantenimientosAbiertos().stream().anyMatch(x -> x.equals(m)));
        BigDecimal total = gestor.calcularCostoTotalEntre(LocalDate.of(2025,10,1), LocalDate.of(2025,10,31));
        assertEquals("El total debe considerar el mantenimiento cerrado", costo, total);
    }

    @Test(expected = GestorMantenimientosException.class)
    public void cerrarMantenimiento_nulo_debeFallar() {
        GestorMantenimientos gestor = new GestorMantenimientos();
        gestor.cerrarMantenimiento(null, LocalDate.of(2025,10,3), "t", BigDecimal.ZERO);
    }
}

