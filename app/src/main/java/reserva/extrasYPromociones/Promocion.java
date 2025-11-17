package reserva.extrasYPromociones;

import java.math.BigDecimal;
import java.util.Objects;

import reserva.cliente.Cliente;
import reserva.cliente.ClienteEmpresarial;
import reserva.vehiculo.Vehiculo;

//Aqui vamos a definir las promociones que se pueden aplicar a las reservas. Beneficios, descuentos aplicables segun las condiciones que contenga la reserva (vehiculo electrico, cliente empresarial, cantidad de dias, etc.)
public class Promocion {
    private String nombre;
    private BigDecimal porcentajeDescuento; //por ejemplo: 0.10 = 10%
    private String descripcion;
    //Controlamos si puede sumarse con otras promociones
    private boolean combinable;
    private boolean aplicaVehiculoElectrico;
    private boolean aplicaClienteEmpresarial;

    public Promocion(String nombre, BigDecimal porcentajeDescuento, boolean combinable,
                     boolean aplicaVehiculoElectrico, boolean aplicaClienteEmpresarial, String descripcion) {
        this.nombre = nombre;
        this.porcentajeDescuento = porcentajeDescuento;
        this.combinable = combinable;
        this.aplicaVehiculoElectrico = aplicaVehiculoElectrico;
        this.aplicaClienteEmpresarial = aplicaClienteEmpresarial;
        this.descripcion = descripcion;
    }

    /******************************************** */
    /*   LOGICA DE APLICABILIDAD                   */
    /******************************************** */
    public boolean aplicaA(Vehiculo vehiculo, Cliente cliente) {
        boolean aplica = true;

        if (aplicaVehiculoElectrico && !vehiculo.getTipo().isElectrico()) {
            aplica = false;
        }
        if (aplicaClienteEmpresarial && !(cliente instanceof ClienteEmpresarial)) {
            aplica = false;
        }
        return aplica;
    }

    public BigDecimal getPorcentajeDescuento() { return porcentajeDescuento; }
    public boolean isCombinable() { return combinable; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Promocion)) return false;
        Promocion that = (Promocion) o;
        return Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}