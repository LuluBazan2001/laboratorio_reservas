package reserva.vehiculo.excepciones;

public class VehiculoDuplicadoException extends RuntimeException {
    public VehiculoDuplicadoException(String mensaje) {
        super(mensaje);
    }
    
}
