package reserva.vehiculo.excepciones;

public class MantenimientoNoEncontradoException extends RuntimeException {
    public MantenimientoNoEncontradoException() { super(); }
    public MantenimientoNoEncontradoException(String message) { super(message); }
}

