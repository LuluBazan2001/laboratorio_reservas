package reserva.vehiculo.excepciones;

public class DescripcionVaciaException extends RuntimeException {
    public DescripcionVaciaException(String mensaje) {
        super(mensaje);
    }
}
