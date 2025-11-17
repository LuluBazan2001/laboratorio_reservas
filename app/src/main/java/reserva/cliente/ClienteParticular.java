package reserva.cliente;
public class ClienteParticular extends Cliente {
    //se identifica por dni
    private String dni;

    public ClienteParticular(String nombre, String direccion, String telefono, String email, String dni) {
        super(nombre, direccion, telefono, email);
        this.dni = dni;
    }


    public String getDni() {
        return dni;
    }

    @Override
    public String getIdentificacion(){
        return dni;
    }
} 