package reserva.reserva.excepciones;

public class ReservaNoEncontradaException extends RuntimeException {
    public ReservaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
