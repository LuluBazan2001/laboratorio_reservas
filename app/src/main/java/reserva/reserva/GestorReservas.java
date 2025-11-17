package reserva.reserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import reserva.reserva.excepciones.*;
import reserva.vehiculo.EstadoVehiculo;
import reserva.vehiculo.GestorMantenimientos;
import reserva.vehiculo.Vehiculo;

public class GestorReservas {
    //Con esta clase gestionaremos las reservas, haciendo hincapié en el control de los solapes y la disponibilidad de los vehiculos
    private List<Reserva> reservas = new ArrayList<>();
    private GestorMantenimientos gestorMantenimientos = new GestorMantenimientos();


    public void registrarReserva(Reserva reserva) {
        if (reserva.getVehiculo().getEstado() == EstadoVehiculo.Estado.MANTENIMIENTO) {
            throw new ReservaSolapadaException("El vehículo está en mantenimiento y no puede reservarse.");
        }

        //Con este if verificamos que el vehiculo esté disponible para la reserva, consultando la disponibilidad de todos los vehiculos, las fechas de inicio y fin de la reserva
        if(!estaDisponible(reserva.getVehiculo(), reserva.getFechaInicioReserva(), reserva.getFechaFinReserva())) {
            //Si la reserva no se puede realizar porque el vehiculo no está disponible, lanza una excepción
            throw new ReservaSolapadaException("El vehículo " + reserva.getVehiculo().getPatente() + " no está disponible en ese período."); 
        }
        reservas.add(reserva);
    }

    public boolean estaDisponible(Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaFin) {
        //Este metodo verifica si un vehiculo esta disponible entre dos fechas.
        //retorna false si ya tiene reservas activas o pendientes que se solapen en el mismo periodo
        for (Reserva reserva : reservas) {
            //Verifico si la reserva pertenece al mismo vehículo
            if (reserva.getVehiculo().equals(vehiculo)) {
                //Solo consideramos reservas ACTIVAS o PENDIENTES
                if (reserva.getEstado() == EstadoReserva.ACTIVA || 
                    reserva.getEstado() == EstadoReserva.PENDIENTE) {
                    //Verificamos si las fechas se solapan
                    //Con isBefore y isAfter comparamos si la fecha de inicio de la reserva es anterior a la fecha de inicio de la reserva, o si la fecha de fin de la reserva es posterior a la fecha de fin de la reserva
                    boolean solapan = !(fechaFin.isBefore(reserva.getFechaInicioReserva()) || 
                                        fechaInicio.isAfter(reserva.getFechaFinReserva()));

                    if (solapan) {
                        //Si hay solapamiento, el vehículo no está disponible
                        return false;
                    }
                }
            }
        }
        //Si no hay reserva, el vehículo está disponible
        return true;
    }


    public void confirmarReserva(String codigoReserva) {
        Reserva reserva = buscarReserva(codigoReserva);
        reserva.confirmarReserva();
    }

    public void cancelarReserva(String codigoReserva) {
        Reserva reserva = buscarReserva(codigoReserva);
        reserva.cancelarReserva();
    }

    //Aqui crearemos mantenimientos post-uso al finalizar una reserva
    public void finalizarReserva(String codigoReserva, boolean requiereMantenimiento, String descripcion) {
        Reserva reserva = buscarReserva(codigoReserva);
        reserva.finalizarReserva();

        // Política post-uso: si requiere mantenimiento, lo abre automáticamente
        if (requiereMantenimiento) {
            gestorMantenimientos.abrirMantenimiento(reserva.getVehiculo(), LocalDate.now(),descripcion != null ? 
                descripcion : "Mantenimiento preventivo post-uso"
            );
        }
    }

    private Reserva buscarReserva(String codigoReserva) {
        return reservas.stream().filter(res -> res.getCodigoReserva().equals(codigoReserva)).findFirst().orElseThrow(() -> 
            new ReservaNoEncontradaException("No se encontró la reserva con codigo: " + codigoReserva));
    }

    public List<Reserva> getReservas() {
        return Collections.unmodifiableList(reservas);
    }

    public GestorMantenimientos getGestorMantenimientos() {
        return gestorMantenimientos;
    }
}
