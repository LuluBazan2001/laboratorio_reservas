package reserva.extrasYPromociones.excepciones;

public class ServicioNoExistenteException extends RuntimeException {
    public ServicioNoExistenteException(String mensaje) {
        super(mensaje);
    }
}
