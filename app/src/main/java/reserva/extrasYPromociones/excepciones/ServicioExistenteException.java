package reserva.extrasYPromociones.excepciones;

public class ServicioExistenteException extends RuntimeException {
    public ServicioExistenteException(String mensaje) {
        super(mensaje);
    }
}
