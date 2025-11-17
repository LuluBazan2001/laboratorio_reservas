package reserva.extrasYPromociones.excepciones;

public class PromocionExistenteException extends RuntimeException {
    public PromocionExistenteException(String mensaje) {
        super(mensaje);
    }
}
