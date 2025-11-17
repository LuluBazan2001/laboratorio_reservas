package reserva.vehiculo.excepciones;

public class MantenimientoInvalidoException extends RuntimeException {
    public MantenimientoInvalidoException() { super(); }
    public MantenimientoInvalidoException(String message) { super(message); }
    public MantenimientoInvalidoException(String message, Throwable cause) { super(message, cause); }
}

