package reserva.reserva;

public enum EstadoReserva {
    PENDIENTE, //Registrada a la espera de ser confirmada la reservacion
    ACTIVA, //En curso
    FINALIZADA, //Concluida
    CANCELADA //Anulada antes del inicio
}
