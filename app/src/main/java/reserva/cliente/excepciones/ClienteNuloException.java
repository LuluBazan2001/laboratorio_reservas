package reserva.cliente.excepciones;

public class ClienteNuloException extends RuntimeException {
    public ClienteNuloException(String mensaje) {
        super(mensaje);
    }
}
