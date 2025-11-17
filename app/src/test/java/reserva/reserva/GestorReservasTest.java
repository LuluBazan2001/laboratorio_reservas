package reserva.reserva;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import reserva.cliente.Cliente;
import reserva.cliente.ClienteParticular;
import reserva.modalidad.ModalidadAlquiler;
import reserva.modalidad.PorDia;
import reserva.reserva.excepciones.CancelarReservasException;
import reserva.reserva.excepciones.ConfirmacionReservaException;
import reserva.reserva.excepciones.FinalizarReservasException;
import reserva.reserva.excepciones.ReservaNoEncontradaException;
import reserva.reserva.excepciones.ReservaSolapadaException;
import reserva.vehiculo.EstadoVehiculo;
import reserva.vehiculo.TipoVehiculo;
import reserva.vehiculo.Vehiculo;

public class GestorReservasTest {
    /*************************************************************************************************** */
    /*                                 CREAR Y REGISTRAR RESERVA                                  */
    /*************************************************************************************************** */
    @Test
    public void testRegistrarReserva() {
        GestorReservas gestorReservas = new GestorReservas();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912", "juan@gmail.com", "43224512");
        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("1234", vehic, cliente, LocalDate.now(), LocalDate.now(), modalidad);

        gestorReservas.registrarReserva(reserva);

        assertEquals(1, gestorReservas.getReservas().size());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
    }

    @Test (expected = ReservaSolapadaException.class)
    public void testRegistrarReservaSolapada_LanzaException() {
        GestorReservas gestorReservas = new GestorReservas();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912", "juan@gmail.com", "43224512");
        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("1234", vehic, cliente, LocalDate.now(), LocalDate.now(), modalidad);
        Reserva reservaSolapada = new Reserva("1235", vehic, cliente, LocalDate.now(), LocalDate.now(), modalidad);

        gestorReservas.registrarReserva(reserva);
        gestorReservas.registrarReserva(reservaSolapada); //Deberia lanzar una excepcion, por que ya existe una reserva para el mismo vehiculo.
    }

    /*************************************************************************************************** */
    /*                                 CONFIRMAR RESERVA                                  */
    /*************************************************************************************************** */
    @Test
    public void testConfirmarReserva() {
        GestorReservas gestorReservas = new GestorReservas();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912", "juan@gmail.com", "43224512");
        ModalidadAlquiler modalidad = new PorDia();
        //el plusDays(3) nos permite crear una reserva de 3 dias
        Reserva reserva = new Reserva("1234", vehic, cliente, LocalDate.now().plusDays(3), LocalDate.now(), modalidad);

        gestorReservas.registrarReserva(reserva);
        gestorReservas.confirmarReserva("1234");

        assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());
        assertEquals(EstadoVehiculo.Estado.RENTADO, vehic.getEstado());
    }

    @Test (expected = ConfirmacionReservaException.class)
    public void testConfirmarReserva_LanzaExceptionSiYaConfirmada() {
        GestorReservas gestorReservas = new GestorReservas();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912", "juan@gmail.com", "43224512");
        ModalidadAlquiler modalidad = new PorDia();
        //el plusDays(3) nos permite crear una reserva de 3 dias
        Reserva reserva = new Reserva("1234", vehic, cliente, LocalDate.now().plusDays(2), LocalDate.now(), modalidad);

        gestorReservas.registrarReserva(reserva);
        gestorReservas.confirmarReserva("1234");
        gestorReservas.confirmarReserva("1234"); //Deberia lanzar una excepcion, por que ya esta confirmada.
    }

    @Test(expected = ReservaNoEncontradaException.class)
    public void testConfirmarReserva_NoExiste() {
        GestorReservas gestorReservas = new GestorReservas();
        gestorReservas.confirmarReserva("0000"); // No existe, debería lanzar excepción
    }


    /*************************************************************************************************** */
/*                                 CANCELAR RESERVA                                  */
/*************************************************************************************************** */
@Test
public void testCancelarReserva() {
    GestorReservas gestorReservas = new GestorReservas();
    Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020",
            EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
    Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912",
            "juan@gmail.com", "43224512");
    ModalidadAlquiler modalidad = new PorDia();

    // el plusDays(3) crea una reserva de 3 días
    Reserva reserva = new Reserva("1234", vehic, cliente,
            LocalDate.now(), LocalDate.now().plusDays(3), modalidad);

    gestorReservas.registrarReserva(reserva);
    gestorReservas.confirmarReserva("1234");
    gestorReservas.cancelarReserva("1234");

    assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    assertEquals(EstadoVehiculo.Estado.DISPONIBLE, vehic.getEstado());
}

@Test(expected = CancelarReservasException.class)
public void testCancelarReserva_LanzaExceptionSiFinalizada() {
    GestorReservas gestorReservas = new GestorReservas();
    Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020",
            EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
    Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912",
            "juan@gmail.com", "43224512");
    ModalidadAlquiler modalidad = new PorDia();

    Reserva reserva = new Reserva("1234", vehic, cliente,
            LocalDate.now(), LocalDate.now().plusDays(3), modalidad);

    gestorReservas.registrarReserva(reserva);
    gestorReservas.confirmarReserva("1234");

    // ahora usamos la versión extendida de finalizarReserva
    gestorReservas.finalizarReserva("1234", false, null);

    // debería lanzar excepción al intentar cancelar una reserva finalizada
    gestorReservas.cancelarReserva("1234");
}

@Test(expected = ReservaNoEncontradaException.class)
public void testCancelarReserva_NoExiste() {
    GestorReservas gestorReservas = new GestorReservas();
    gestorReservas.cancelarReserva("0000"); // No existe, debería lanzar excepción
}

/*************************************************************************************************** */
/*                                 FINALIZAR RESERVA                                  */
/*************************************************************************************************** */
@Test
public void testFinalizarReserva() {
    GestorReservas gestorReservas = new GestorReservas();
    Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020",
            EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
    Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912",
            "juan@gmail.com", "43224512");
    ModalidadAlquiler modalidad = new PorDia();

    Reserva reserva = new Reserva("1234", vehic, cliente,
            LocalDate.now(), LocalDate.now().plusDays(3), modalidad);

    gestorReservas.registrarReserva(reserva);
    gestorReservas.confirmarReserva("1234");

    // Usamos la nueva firma con mantenimiento requerido
    gestorReservas.finalizarReserva("1234", true, "Chequeo general post uso");

    assertEquals(EstadoReserva.FINALIZADA, reserva.getEstado());
    assertEquals(EstadoVehiculo.Estado.MANTENIMIENTO, vehic.getEstado());
}

@Test(expected = FinalizarReservasException.class)
public void testFinalizarReserva_LanzaExceptionSiNoConfirmada() {
    GestorReservas gestorReservas = new GestorReservas();
    Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020",
            EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
    Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912",
            "juan@gmail.com", "43224512");
    ModalidadAlquiler modalidad = new PorDia();

    Reserva reserva = new Reserva("1234", vehic, cliente,
            LocalDate.now(), LocalDate.now().plusDays(3), modalidad);

    gestorReservas.registrarReserva(reserva);

    // Debería lanzar excepción porque no está confirmada (ni activa)
    gestorReservas.finalizarReserva("1234", false, "Sin revisión");
}

@Test(expected = ReservaNoEncontradaException.class)
public void testFinalizarReserva_NoExiste() {
    GestorReservas gestorReservas = new GestorReservas();
    gestorReservas.finalizarReserva("0000", false, "No existe"); // No existe, debería lanzar excepción
}

    
    /*************************************************************************************************** */
    /*                                 BUSCAR/DISPONIBILIDAD RESERVA                                  */
    /*************************************************************************************************** */
    @Test
    public void testDisponibilidadVehiculo_SinSolape() {
        GestorReservas gestorReservas = new GestorReservas();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912", "juan@gmail.com", "43224512");
        ModalidadAlquiler modalidad = new PorDia();
        //el plusDays(3) nos permite crear una reserva de 3 dias
        Reserva reserva = new Reserva("1234", vehic, cliente, LocalDate.of(2025,10,1), LocalDate.of(2025,10,5), modalidad);

        gestorReservas.registrarReserva(reserva);

        boolean disponible = gestorReservas.estaDisponible(vehic, LocalDate.of(2025,10,6), LocalDate.of(2025,10,8));

        assertTrue("El vehiculo deberia estar disponible fuera del rango reservado", disponible);
    }

    @Test
    public void testDisponibilidadVehiculo_ConSolape() {
        GestorReservas gestorReservas = new GestorReservas();
        Vehiculo vehic = new Vehiculo("FGT458", "Toyota", "Corolla 2020", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Juan", "Cordoba 333", "3834934912", "juan@gmail.com", "43224512");
        ModalidadAlquiler modalidad = new PorDia();
        //el plusDays(3) nos permite crear una reserva de 3 dias
        Reserva reserva = new Reserva("1234", vehic, cliente, LocalDate.of(2025,10,1), LocalDate.of(2025,10,5), modalidad);

        gestorReservas.registrarReserva(reserva);

        boolean disponible = gestorReservas.estaDisponible(vehic, LocalDate.of(2025,10,3), LocalDate.of(2025,10,4));

        assertFalse("El vehiculo no deberia estar disponible dentro del rango reservado", disponible);
    }//NO DEBERIA GENERAR UNA EXCEPCION?

    /*************************************************************************************************** */
    /*                     TEST BUSCAR / RESERVA NO ENCONTRADA                                           */
    /*************************************************************************************************** */
    @Test(expected = ReservaNoEncontradaException.class)
    public void testBuscarReserva_NoExiste() {
        GestorReservas gestorReservas = new GestorReservas();
        gestorReservas.confirmarReserva("9999"); // No existe, debería lanzar excepción
    }

}
