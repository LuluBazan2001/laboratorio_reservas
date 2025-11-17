package reserva.vehiculo.excepciones;

public class CostoMantenimientoInvalidoException extends RuntimeException {
    public CostoMantenimientoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
