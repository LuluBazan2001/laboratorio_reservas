package reserva.vehiculo.excepciones;

public class MantenimientoCerradoException extends RuntimeException {
    public MantenimientoCerradoException(String mensaje) {
        super(mensaje);
    }
}
