package reserva.vehiculo;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.Test;

import reserva.cliente.*;
import reserva.modalidad.*;
import reserva.reserva.*;

public class GestorReservasMantenimientoIntegrationTest {
    @Test
    public void finalizarReserva_abreMantenimiento_exitoso() {
        GestorReservas gestorReservas = new GestorReservas();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912", "juan@gmail.com", "43224512");
        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("abc123", vehic, cliente, LocalDate.now(), LocalDate.now().plusDays(2), modalidad);

        gestorReservas.registrarReserva(reserva);
        gestorReservas.confirmarReserva("abc123");

        gestorReservas.finalizarReserva("abc123", true, "Revisión post-uso");

        //obtenemos el gestor de mantenimientos
        GestorMantenimientos gm = gestorReservas.getGestorMantenimientos();
        assertNotNull("Debe existir el gestor de mantenimientos", gm);

        //Debe existir al menos un mantenimiento asociado al vehículo
        assertFalse("Debe haber mantenimientos registrados", gm.getMantenimientosDeVehiculo(vehic).isEmpty());

        Mantenimiento m = gm.getMantenimientosDeVehiculo(vehic).get(0);
        assertTrue("El mantenimiento debe estar abierto tras finalizar reserva", m.isAbierto());
        assertEquals("El vehículo debe estar en estado MANTENIMIENTO", EstadoVehiculo.Estado.MANTENIMIENTO, vehic.getEstado());
    }
}

