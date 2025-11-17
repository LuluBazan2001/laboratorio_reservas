package reserva.vehiculo.excepciones;

public class PatenteVaciaException extends RuntimeException {
    public PatenteVaciaException(String mensaje) {
        super(mensaje);
    }
}
