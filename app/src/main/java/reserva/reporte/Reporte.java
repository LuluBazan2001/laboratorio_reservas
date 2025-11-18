package reserva.reporte;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import reserva.reserva.GestorReservas;
import reserva.reserva.Reserva;
import reserva.vehiculo.Flota;
import reserva.vehiculo.GestorMantenimientos;
import reserva.vehiculo.Mantenimiento;
import reserva.vehiculo.Vehiculo;

public class Reporte {
    private final GestorReservas gestorReservas;
    private final GestorMantenimientos gestorMantenimientos;
    private final Flota flota;

    public Reporte(GestorReservas gestorReservas, GestorMantenimientos gestorMantenimientos, Flota flota) {
        this.gestorReservas = gestorReservas;
        this.gestorMantenimientos = gestorMantenimientos;
        this.flota = flota;
    }

    /********************************************************* */
    /*            RESERVAS POR PERIODO                         */
    /********************************************************* */
    public List<ReservaReporte> reservasPorPeriodo(LocalDate desde, LocalDate hasta, Set<String> estadosIncluidos) {
        List<ReservaReporte> resultado = new ArrayList<>();
        List<Reserva> totalidadReservas = gestorReservas.getReservas();

        for (Reserva r : totalidadReservas) {
            //con .name() obtenemos el nombre del estado
            String nombreEstado = r.getEstado().name();
            //si el estado no está en el set de estados a incluir, no lo consideramos
            if (!estadosIncluidos.contains(nombreEstado)) continue;

            //si el periodo en cuestion no se solapa con el periodo solicitado, no lo consideramos
            if (!intervalosSolapan(desde, hasta, r.getFechaInicioReserva(), r.getFechaFinReserva())) continue;

            //si el cliente no es nulo, obtenemos su identificación
            String cliente = (r.getCliente() != null) ? r.getCliente().toString() : "N/A";
            ReservaReporte reservaReporte = new ReservaReporte(
                    r.getCodigoReserva(),
                    r.getVehiculo().getPatente(),
                    cliente,
                    r.getFechaInicioReserva(),
                    r.getFechaFinReserva(),
                    nombreEstado,
                    r.getClass().getSimpleName(),
                    r.getCostoTotal()
            );
            //agregamos el objeto a la lista de resultados
            resultado.add(reservaReporte);
        }
        //devolvemos la lista de resultados
        return resultado;
    }



    /********************************************************* */
    /*            UTILIZACIÓN DE FLOTA                        */
    /********************************************************* */
    public List<UtilizacionVehiculo> utilizacionFlota(LocalDate desde, LocalDate hasta) {
        //Obtenemos la lista de vehiculos
        List<UtilizacionVehiculo> result = new ArrayList<>();
        List<Vehiculo> vehiculos = flota.getVehiculos();
        //Calculamos el periodo de dias
        long periodoDias = ChronoUnit.DAYS.between(desde, hasta) + 1;
        //Si el periodo es negativo, lo fijamos a cero
        if (periodoDias <= 0) periodoDias = 0;

        List<Reserva> todas = gestorReservas.getReservas();

        for (Vehiculo v : vehiculos) {
            //Calculamos el número de días que se rentan
            //0L es el valor inicial de diasRentados
            long diasRentados = 0L;
            for (Reserva r : todas) {
                //consideramos solo reservas del vehículo y ACTIVAS que solapan con el periodo
                //Si la reserva no es del mismo vehículo, lo saltamos
                if (!r.getVehiculo().equals(v)) continue;
                //Si la reserva no es activa, lo saltamos
                if (r.getEstado() != reserva.reserva.EstadoReserva.ACTIVA) continue;
                //Si la reserva no solapa con el periodo solicitado, lo saltamos
                if (!intervalosSolapan(desde, hasta, r.getFechaInicioReserva(), r.getFechaFinReserva())) continue;

                //Calculamos el número de días que se rentan
                LocalDate inicio = r.getFechaInicioReserva().isBefore(desde) ? desde : r.getFechaInicioReserva();
                LocalDate fin = r.getFechaFinReserva().isAfter(hasta) ? hasta : r.getFechaFinReserva();
                long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;
                //Sumamos los días rentados
                diasRentados += dias;
            }

            //Calculamos el porcentaje de utilización
            BigDecimal porcentaje;
            //Si el periodo es cero, el porcentaje es cero
            if (periodoDias == 0) {
                porcentaje = BigDecimal.ZERO;
            } else {
                //Calculamos el porcentaje
                porcentaje = BigDecimal.valueOf(diasRentados)
                        //Multiplicamos por 100 para obtener un porcentaje
                        .multiply(BigDecimal.valueOf(100))
                        //Dividimos por el periodo para obtener el porcentaje
                        .divide(BigDecimal.valueOf(periodoDias), 
                        //Precision a dos decimales
                        2, 
                        //El resultado debe ser redondeado hacia arriba
                        RoundingMode.HALF_UP);
            }
            //Agregamos el resultado a la lista de resultados
            result.add(new UtilizacionVehiculo(v.getPatente(), diasRentados, periodoDias, porcentaje));
        }
        return result;
    }



     /********************************************************* */
    /*          MANTENIMIENTOS REALIZADOS Y REPORTES             */
    /********************************************************* */
    public List<MantenimientoReporte> mantenimientosEntre(LocalDate desde, LocalDate hasta) {
        List<MantenimientoReporte> salida = new ArrayList<>();
        List<Mantenimiento> mantenimientos = gestorMantenimientos.getMantenimientos();

        for (Mantenimiento m : mantenimientos) {
            LocalDate inicio = m.getFechaApertura();
            LocalDate fin = m.isCerrado() ? m.getFechaCierre() : hasta;
            if (!intervalosSolapan(desde, hasta, inicio, fin)) continue;

            BigDecimal costo = m.isCerrado() ? m.getCosto() : BigDecimal.ZERO;
            LocalDate fechaCierre = m.isCerrado() ? m.getFechaCierre() : null;

            salida.add(new MantenimientoReporte(
                    m.getVehiculo().getPatente(),
                    m.getFechaApertura(),
                    fechaCierre,
                    m.getDescripcion(),
                    m.isAbierto(),
                    costo
            ));
        }

        return salida;
    }

    public List<MantenimientoReporte> mantenimientosPendientes() {
        List<MantenimientoReporte> salida = new ArrayList<>();
        List<Mantenimiento> abiertos = gestorMantenimientos.getMantenimientosAbiertos();

        for (Mantenimiento m : abiertos) {
            salida.add(new MantenimientoReporte(
                    m.getVehiculo().getPatente(),
                    m.getFechaApertura(),
                    null,
                    m.getDescripcion(),
                    true,
                    BigDecimal.ZERO
            ));
        }
        return salida;
    }

    public BigDecimal costoMantenimientosEntre(LocalDate desde, LocalDate hasta) {
        BigDecimal total = BigDecimal.ZERO;
        List<Mantenimiento> mantenimientos = gestorMantenimientos.getMantenimientos();

        for (Mantenimiento m : mantenimientos) {
            if (!m.isCerrado()) continue;
            LocalDate cierre = m.getFechaCierre();
            if (cierre.isBefore(desde) || cierre.isAfter(hasta)) continue;
            total = total.add(m.getCosto());
        }

        return total;
    }


    /********************************************************* */
    /*                   RESUMEN EJECUTIVO                     */
    /********************************************************* */
    public ResumenEjecutivo resumenEjecutivo(LocalDate desde, LocalDate hasta) {
        //total de reservas (en cualquier estado) que solapan el periodo
        long totalReservas = 0L;
        List<Reserva> todas = gestorReservas.getReservas();
        for (Reserva r : todas) {
            if (intervalosSolapan(desde, hasta, r.getFechaInicioReserva(), r.getFechaFinReserva())) {
                totalReservas++;
            }
        }

        //ingresos estimados: sumamos getCostoTotal() de reservas ACTIVAS o FINALIZADAS dentro del periodo
        BigDecimal ingresos = BigDecimal.ZERO;
        for (Reserva r : todas) {
            if (r.getEstado() == reserva.reserva.EstadoReserva.ACTIVA || r.getEstado() == reserva.reserva.EstadoReserva.FINALIZADA) {
                if (intervalosSolapan(desde, hasta, r.getFechaInicioReserva(), r.getFechaFinReserva())) {
                    ingresos = ingresos.add(r.getCostoTotal());
                }
            }
        }

        //promedio de utilizacion: seria el promedio de porcentajes por vehículo
        List<UtilizacionVehiculo> util = utilizacionFlota(desde, hasta);
        BigDecimal promedioUtil = BigDecimal.ZERO;
        if (!util.isEmpty()) {
            BigDecimal suma = BigDecimal.ZERO;
            for (UtilizacionVehiculo u : util) {
                suma = suma.add(u.getPorcentajeUtilizacion());
            }
            promedioUtil = suma.divide(BigDecimal.valueOf(util.size()), 2, RoundingMode.HALF_UP);
        }
        //costo de mantenimientos entre el periodo solicitado
        //si no hay mantenimientos, devolvemos cero
        BigDecimal costoMant = costoMantenimientosEntre(desde, hasta);
        //devolvemos el resumen ejecutivo, con los datos calculados
        return new ResumenEjecutivo(totalReservas, ingresos, promedioUtil, costoMant);
    }
    
    //Con este metodo comparamos si dos intervalos solapan
    private boolean intervalosSolapan(LocalDate aInicio, LocalDate aFin, LocalDate bInicio, LocalDate bFin) {
        return !(aFin.isBefore(bInicio) || aInicio.isAfter(bFin));
    }
}
