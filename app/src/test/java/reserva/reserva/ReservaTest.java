package reserva.reserva;
import org.junit.Test;

import reserva.vehiculo.*;
import reserva.cliente.*;
import reserva.extrasYPromociones.Promocion;
import reserva.extrasYPromociones.ServicioExtra;
import reserva.modalidad.ModalidadAlquiler;
import reserva.modalidad.PorDia;
import reserva.reserva.excepciones.*;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservaTest {

    /*************************************************************************************************** */
    /*                                 CONFIRMAR RESERVA                                                 */
    /*************************************************************************************************** */
    @Test
    public void testConfirmarReserva_Exitosa() {
        Vehiculo vehic = new Vehiculo("ABC123", "Toyota", "Corolla", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Juan", "Calle 123", "381111111", "juan@gmail.com", "40111222");
        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("R001", vehic, cliente, LocalDate.now(), LocalDate.now().plusDays(3), modalidad);

        reserva.confirmarReserva();

        assertEquals("El estado de la reserva debe cambiar a ACTIVA", EstadoReserva.ACTIVA, reserva.getEstado());
        assertEquals("El vehículo debe quedar RENTADO al confirmar la reserva", EstadoVehiculo.Estado.RENTADO, vehic.getEstado());
    }

    @Test(expected = ConfirmacionReservaException.class)
    public void testConfirmarReserva_LanzaExceptionSiNoPendiente() {
        Vehiculo vehic = new Vehiculo("DEF456", "Renault", "Clio", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Ana", "Mitre 456", "381222222", "ana@gmail.com", "40555111");
        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("R002", vehic, cliente, LocalDate.now(), LocalDate.now().plusDays(2), modalidad);

        // Primero se confirma correctamente
        reserva.confirmarReserva();
        // Luego intenta confirmarse otra vez (ya no está pendiente)
        reserva.confirmarReserva(); // Debe lanzar ConfirmacionReservaException
    }

    /*************************************************************************************************** */
    /*                                 CANCELAR RESERVA                                                  */
    /*************************************************************************************************** */
    @Test
    public void testCancelarReserva_Exitosa() {
        Vehiculo vehic = new Vehiculo("GHI789", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Pedro", "Belgrano 300", "381333333", "pedro@gmail.com", "40666333");
        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("R003", vehic, cliente, LocalDate.now(), LocalDate.now().plusDays(4), modalidad);

        reserva.cancelarReserva();

        assertEquals("El estado de la reserva debe ser CANCELADA", EstadoReserva.CANCELADA, reserva.getEstado());
        assertEquals("El vehículo debe quedar DISPONIBLE al cancelar la reserva", EstadoVehiculo.Estado.DISPONIBLE, vehic.getEstado());
    }

    @Test(expected = CancelarReservasException.class)
    public void testCancelarReserva_LanzaExceptionSiFinalizada() {
        Vehiculo vehic = new Vehiculo("JKL321", "Peugeot", "208", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Sofía", "Catamarca 12", "381444444", "sofia@gmail.com", "40999111");
        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("R004", vehic, cliente, LocalDate.now(), LocalDate.now().plusDays(1), modalidad);

        reserva.confirmarReserva();
        reserva.finalizarReserva(); // Ahora está finalizada
        reserva.cancelarReserva();  // Debe lanzar excepción
    }

    /*************************************************************************************************** */
    /*                                 FINALIZAR RESERVA                                                 */
    /*************************************************************************************************** */
    @Test
    public void testFinalizarReserva_Exitosa() {
        Vehiculo vehic = new Vehiculo("MNO654", "Chevrolet", "Onix", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("María", "Buenos Aires 100", "381555555", "maria@gmail.com", "41111000");
        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("R005", vehic, cliente, LocalDate.now(), LocalDate.now().plusDays(5), modalidad);

        reserva.confirmarReserva();  // Debe estar activa
        reserva.finalizarReserva();

        assertEquals("El estado de la reserva debe ser FINALIZADA", EstadoReserva.FINALIZADA, reserva.getEstado());
        assertEquals("El vehículo debe volver a estar DISPONIBLE al finalizar la reserva", EstadoVehiculo.Estado.DISPONIBLE, vehic.getEstado());
    }

    @Test(expected = FinalizarReservasException.class)
    public void testFinalizarReserva_LanzaExceptionSiNoActiva() {
        Vehiculo vehic = new Vehiculo("PQR987", "Honda", "Civic", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Diego", "San Martín 77", "381666666", "diego@gmail.com", "42222333");
        ModalidadAlquiler modalidad = new PorDia();
        Reserva reserva = new Reserva("R006", vehic, cliente, LocalDate.now(), LocalDate.now().plusDays(2), modalidad);

        // No se confirma antes
        reserva.finalizarReserva(); // Debe lanzar FinalizarReservasException
    }

    @Test
    public void finalizarReserva_generaMantenimiento_siCumplePolitica() {
        // Arrange
        GestorReservas gestorReservas = new GestorReservas();

        Vehiculo vehic = new Vehiculo("PQR987", "Honda", "Civic", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("María", "Buenos Aires 100", "381555555", "maria@gmail.com", "41111000");

        LocalDate inicio = LocalDate.of(2025, 9, 1);
        LocalDate fin = LocalDate.of(2025, 9, 3);

        Reserva reserva = new Reserva("abc123", vehic, cliente, inicio, fin, new PorDia());
        gestorReservas.registrarReserva(reserva);
        gestorReservas.confirmarReserva(reserva.getCodigoReserva());

        //finalizar reserva (esto debería disparar la política que genera mantenimiento)
        gestorReservas.finalizarReserva(reserva.getCodigoReserva(), true, "Chequeo general post uso");

        //el vehículo pasó a estado MANTENIMIENTO
        assertEquals("El vehículo debería haber quedado en estado MANTENIMIENTO", EstadoVehiculo.Estado.MANTENIMIENTO, vehic.getEstado());
    }

    /*************************************************************************************************** */
    /*                                 COSTO TOTAL (comportamiento base)                                 */
    /*************************************************************************************************** */
    @Test
    public void testCalcularCostoTotal_SinPromosNiExtras() {
        Vehiculo vehic = new Vehiculo("PQR987", "Honda", "Civic", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("9000"));
        Cliente cliente = new ClienteParticular("Diego", "San Martín 77", "381666666", "diego@gmail.com", "42222333");

        ModalidadAlquiler modalidad = new PorDia();

        Reserva reserva = new Reserva("R006", vehic, cliente, LocalDate.now(), LocalDate.now().plusDays(2), modalidad);

        BigDecimal costoTotal = reserva.getCostoTotal();

        assertEquals(new BigDecimal("27000"), costoTotal);
    }
    


    @Test
    public void calculoCosto_desgloseYTotal() {
        Vehiculo vehiculo = new Vehiculo("AAA111", "Marca", "Modelo", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("8000"));
        ClienteParticular cliente = new ClienteParticular("Juan Gomez", "Calle 123", "381111111", "juan@gmail.com", "40111222");
        LocalDate inicio = LocalDate.of(2025, 11, 1);
        LocalDate fin = inicio.plusDays(2); // PorDia usa +1 -> 3 días

        PorDia modalidad = new PorDia();
        Reserva reserva = new Reserva("1234", vehiculo, cliente, inicio, fin, modalidad);

        ServicioExtra s1 = new ServicioExtra("GPS", new BigDecimal("200.00"), true);
        ServicioExtra s2 = new ServicioExtra("SillaBebe", new BigDecimal("150.00"), true);
        reserva.agregarServicioExtra(s1);
        reserva.agregarServicioExtra(s2);

        Promocion p1 = new Promocion("Promocion 1", new BigDecimal("0.10"), true, false, false, "10% genérico");
        Promocion p2 = new Promocion("Promocion 2", new BigDecimal("0.20"), true, false, false, "20% genérico");
        reserva.agregarPromocion(p1);
        reserva.agregarPromocion(p2);

        BigDecimal total = reserva.getCostoTotal();

        BigDecimal costoBaseEsperado = modalidad.calcularCosto(vehiculo, inicio, fin); // 8000 * 3 = 24000
        BigDecimal extrasEsperado = s1.getCostoServicio().add(s2.getCostoServicio()); // 350
        BigDecimal subtotalEsperado = costoBaseEsperado.add(extrasEsperado); // 24350

        //dado que ambas promos son combinables, se suman los porcentajes: 10% + 20% = 30%
        BigDecimal totalDescuentoEsperado = subtotalEsperado.multiply(new BigDecimal("0.30")); // 7305
        BigDecimal totalEsperado = subtotalEsperado.subtract(totalDescuentoEsperado); // 17045

        assertTrue("Total esperado = " + totalEsperado + " pero fue: " + total, total.compareTo(totalEsperado) == 0);
    }

    //Creamos dos reservas sobre mismo vehículo y fechas solapadas; intentamos confirmar la segunda reserva y nos arroja la exception ReservaSolapadaException
    @Test(expected = ReservaSolapadaException.class)
    public void reservaSolapada_noPermiteConfirmar() {
        GestorReservas gestor = new GestorReservas();
        Vehiculo vehiculo = new Vehiculo("abc123", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("7000"));
        ClienteParticular cliente = new ClienteParticular("Juan Gomez", "Calle 123", "381111111", "juan@gmail.com", "40111222");
        ClienteParticular cliente2 = new ClienteParticular("Juan Gomez", "Calle 123", "381111111", "XXXXXXXXXXXXXX", "40111223");

        LocalDate inicio1 = LocalDate.of(2025, 12, 1);
        LocalDate fin1 = LocalDate.of(2025, 12, 5);
        LocalDate inicio2 = LocalDate.of(2025, 12, 4); // solapa con la reserva1
        LocalDate fin2 = LocalDate.of(2025, 12, 8);

        PorDia modalidad = new PorDia();
        Reserva reserva = new Reserva("R1", vehiculo, cliente, inicio1, fin1, modalidad);
        Reserva reserva2 = new Reserva("R2", vehiculo, cliente2, inicio2, fin2, modalidad);

        gestor.registrarReserva(reserva);
        gestor.confirmarReserva("R1"); //confirma la primera

        gestor.registrarReserva(reserva2);
        //Al intentar confirmar la segunda debe lanzar ReservaSolapadaException
        gestor.confirmarReserva("R2");
    }

}
