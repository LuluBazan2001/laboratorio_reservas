package reserva.vehiculo.excepciones;

public class VehiculoNoEncontradoException extends RuntimeException {
    public VehiculoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
}
