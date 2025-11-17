package reserva.extrasYPromociones.excepciones;

public class PromocionNoExistenteException extends RuntimeException {
    public PromocionNoExistenteException(String mensaje) {
        super(mensaje);
    }
}
