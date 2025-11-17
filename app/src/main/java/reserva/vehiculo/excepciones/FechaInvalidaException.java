package reserva.vehiculo.excepciones;

public class FechaInvalidaException extends RuntimeException {
    public FechaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
