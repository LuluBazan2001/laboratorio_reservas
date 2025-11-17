package reserva.vehiculo.excepciones;

public class MantenimientoAbiertoException extends RuntimeException {
    public MantenimientoAbiertoException(String mensaje) {
        super(mensaje);
    }
}
