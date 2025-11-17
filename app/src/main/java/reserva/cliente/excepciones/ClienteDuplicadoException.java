package reserva.cliente.excepciones;

public class ClienteDuplicadoException extends RuntimeException{
    public ClienteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
