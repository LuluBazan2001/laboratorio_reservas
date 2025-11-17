package reserva.vehiculo;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.Test;
import reserva.vehiculo.excepciones.MantenimientoInvalidoException;

public class MantenimientoTest {
    @Test
    public void abrirYCerrarMantenimiento_exitoso() {
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Mantenimiento mant = new Mantenimiento(vehic, LocalDate.now(), "Chequeo post-uso");

        //al crear, el vehículo debe quedar en estado MANTENIMIENTO
        assertTrue("El mantenimiento debe estar abierto inicialmente", mant.isAbierto());
        assertEquals("Vehículo debe quedar en mantenimiento", EstadoVehiculo.Estado.MANTENIMIENTO, vehic.getEstado());

        //cerramos el mantenimiento
        BigDecimal costo = new BigDecimal("1500");
        mant.cerrar(LocalDate.now().plusDays(3), "Cambio de pastillas", costo);

        assertFalse("Ahora debe estar cerrado", mant.isAbierto());
        assertEquals(costo, mant.getCosto());
        assertEquals("Cambio de pastillas", mant.getTrabajoRealizado());
        assertEquals("Vehículo liberado a DISPONIBLE", EstadoVehiculo.Estado.DISPONIBLE, vehic.getEstado());
    }

    @Test(expected = MantenimientoInvalidoException.class)
    public void abrirMantenimiento_conVehiculoNulo_debeFallar() {
        new Mantenimiento(null, LocalDate.now(), "desc");
    }
}


