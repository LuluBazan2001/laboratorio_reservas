package reserva.reserva.excepciones;

public class CancelarReservasException extends RuntimeException {
   public CancelarReservasException(String mensaje) {
        super(mensaje);
    }
}
