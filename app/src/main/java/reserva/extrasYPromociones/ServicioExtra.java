package reserva.extrasYPromociones;

import java.math.BigDecimal;
import java.util.Objects;

//Aqui, quedara plasmado el servicio extra que se aplica a las reservas, como por ejemplo, GPS, silla para niños, cobertura de seguro, etc.
public class ServicioExtra {
    private String nombreServicio;
    private BigDecimal costoServicio;
    //con combinable indicamos si el servicio es combinable con otros servicios o no
    private boolean combinable;

    public ServicioExtra(String nombreServicio, BigDecimal costoServicio, boolean combinable) {
        this.nombreServicio = nombreServicio;
        this.costoServicio = costoServicio;
        this.combinable = combinable;
    }

    public String getNombreServicio() { return nombreServicio; }
    public BigDecimal getCostoServicio() { return costoServicio; }
    public boolean isCombinable() { return combinable; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicioExtra)) return false;
        ServicioExtra that = (ServicioExtra) o;
        return Objects.equals(nombreServicio, that.nombreServicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombreServicio);
    }
}
