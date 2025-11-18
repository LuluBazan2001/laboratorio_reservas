package reserva.reporte;

import org.junit.Test;
import reserva.vehiculo.*;
import reserva.reserva.*;
import reserva.cliente.*;
import reserva.modalidad.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ReportesTest {

    @Test
    public void testReservasPorPeriodo_simple() {
        GestorReservas gestorReservas = new GestorReservas();
        GestorMantenimientos gestorMantenimientos = new GestorMantenimientos();
        Flota flota = new Flota();
        gestorReservas.setGestorMantenimientos(gestorMantenimientos);
        Reporte reporte = new Reporte(gestorReservas, gestorMantenimientos, flota);
        Vehiculo vehiculo = new Vehiculo("FGZ123", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("1000"));
        flota.altaVehiculo(vehiculo);

        Reserva reserva = new Reserva("1234", vehiculo, new ClienteParticular("Lourdes", "Calle 123", "381111111", "lourdes@gmail.com", "40111222"),LocalDate.of(2025,1,1), LocalDate.of(2025,1,3), new PorDia());
        gestorReservas.registrarReserva(reserva);
        gestorReservas.confirmarReserva(reserva.getCodigoReserva());

        List<ReservaReporte> lista = reporte.reservasPorPeriodo(LocalDate.of(2025,1,1), LocalDate.of(2025,1,31), Set.of("ACTIVA","FINALIZADA","PENDIENTE"));
        assertNotNull(lista);
        assertEquals(1, lista.size());
        ReservaReporte rep = lista.get(0);
        assertEquals("1234", rep.getCodigoReserva());
        assertEquals("FGZ123", rep.getPatenteVehiculo());
    }

    @Test
    public void utilizacionFlota_calculo() {
        GestorReservas gestorReservas = new GestorReservas();
        GestorMantenimientos gestorMantenimientos = new GestorMantenimientos();
        Flota flota = new Flota();
        gestorReservas.setGestorMantenimientos(gestorMantenimientos);
        Reporte reporte = new Reporte(gestorReservas, gestorMantenimientos, flota);
        Vehiculo vehiculo = new Vehiculo("FGZ123", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("1000"));
        flota.altaVehiculo(vehiculo);

        //periodo: 2025-02-01 .. 2025-02-10 (10 dias)
        LocalDate desde = LocalDate.of(2025,2,1);
        LocalDate hasta = LocalDate.of(2025,2,10);

        //reserva activa 3 dias dentro del periodo
        Reserva reserva = new Reserva("1234", vehiculo, new ClienteParticular("Lourdes", "Calle 123", "381111111", "lourdes@gmail.com", "40111222"), LocalDate.of(2025,2,3), LocalDate.of(2025,2,5), new PorDia());
        gestorReservas.registrarReserva(reserva);
        gestorReservas.confirmarReserva(reserva.getCodigoReserva());

        List<UtilizacionVehiculo> util = reporte.utilizacionFlota(desde, hasta);
        assertNotNull(util);
        assertEquals(1, util.size());
        UtilizacionVehiculo u = util.get(0);
        assertEquals("FGZ123", u.getPatente());
        //3 dias / 10 dias = 30.00%
        assertEquals(new BigDecimal("30.00"), u.getPorcentajeUtilizacion());
        assertEquals(3L, u.getDiasRentados());
        assertEquals(10L, u.getDiasPeriodo());
    }

    @Test
    public void mantenimientosEntre_y_pendientes() {
        GestorReservas gestorReservas = new GestorReservas();
        GestorMantenimientos gestorMantenimientos = new GestorMantenimientos();
        Flota flota = new Flota();
        gestorReservas.setGestorMantenimientos(gestorMantenimientos);
        Reporte reporte = new Reporte(gestorReservas, gestorMantenimientos, flota);
        Vehiculo vehiculo = new Vehiculo("FGZ123", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("1000"));
        flota.altaVehiculo(vehiculo);

        LocalDate inicio = LocalDate.of(2025,3,1);
        Mantenimiento mantenimiento = gestorMantenimientos.abrirMantenimiento(vehiculo, inicio, "reparacion");
        //lo cerramos el 5/3
        gestorMantenimientos.cerrarMantenimiento(mantenimiento, LocalDate.of(2025,3,5), "ok", new BigDecimal("500"));

        List<MantenimientoReporte> rep = reporte.mantenimientosEntre(LocalDate.of(2025,3,1), LocalDate.of(2025,3,31));
        assertNotNull(rep);
        assertFalse(rep.isEmpty());
        boolean encontrado = false;
        for (MantenimientoReporte r : rep) {
            if ("FGZ123".equals(r.getPatente()) && r.getFechaCierre() != null) {
                encontrado = true;
                break;
            }
        }
        assertTrue(encontrado);

        //abrimos uno nuevo y debería aparecer en pendientes
        Mantenimiento m2 = gestorMantenimientos.abrirMantenimiento(vehiculo, LocalDate.of(2025,4,1), "reparacion 2");
        List<MantenimientoReporte> pendientes = reporte.mantenimientosPendientes();
        assertTrue(pendientes.stream().anyMatch(p -> p.getPatente().equals("FGZ123")));
    }

    @Test
    public void resumenEjecutivo_basico() {
        GestorReservas gestorReservas = new GestorReservas();
        GestorMantenimientos gestorMantenimientos = new GestorMantenimientos();
        Flota flota = new Flota();
        gestorReservas.setGestorMantenimientos(gestorMantenimientos);
        Reporte reporte = new Reporte(gestorReservas, gestorMantenimientos, flota);

        Vehiculo vehiculo = new Vehiculo("FGZ123", "Ford", "Focus", EstadoVehiculo.Estado.DISPONIBLE, TipoVehiculo.AUTO, new BigDecimal("1000"));
        flota.altaVehiculo(vehiculo);

        // una reserva y un mantenimiento
        Reserva reserva = new Reserva("R3", vehiculo, new ClienteParticular("C3","d","t","e", "dni3"),
                LocalDate.of(2025,5,1), LocalDate.of(2025,5,2), new PorDia());
        gestorReservas.registrarReserva(reserva);
        gestorReservas.confirmarReserva(reserva.getCodigoReserva());
        gestorReservas.finalizarReserva(reserva.getCodigoReserva(), true, "post-uso"); //abre mantenimiento

        ResumenEjecutivo resumen = reporte.resumenEjecutivo(LocalDate.of(2025,5,1), LocalDate.of(2025,5,31));
        assertNotNull(resumen);
        assertTrue(resumen.getTotalReservas() >= 1);
        assertNotNull(resumen.getIngresosEstimados());
        assertNotNull(resumen.getPromedioUtilizacion());
        assertNotNull(resumen.getCostoMantenimientos());
    }
}
