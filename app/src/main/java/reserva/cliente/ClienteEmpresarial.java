package reserva.cliente;

public class ClienteEmpresarial extends Cliente {
    //se identifica por cuit
    private String cuit;
    private String razonSocial;

    public ClienteEmpresarial(String nombre, String direccion, String telefono, String email, String cuit, String razonSocial) {
        super(nombre, direccion, telefono, email);
        this.cuit = cuit;
        this.razonSocial = razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    @Override
    public String getIdentificacion(){
        return cuit;
    }
    
}

