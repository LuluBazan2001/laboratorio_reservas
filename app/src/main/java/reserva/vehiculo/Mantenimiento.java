package reserva.vehiculo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import reserva.vehiculo.excepciones.CostoMantenimientoInvalidoException;
import reserva.vehiculo.excepciones.DescripcionVaciaException;
import reserva.vehiculo.excepciones.FechaInvalidaException;
import reserva.vehiculo.excepciones.MantenimientoAbiertoException;
import reserva.vehiculo.excepciones.MantenimientoCerradoException;
import reserva.vehiculo.excepciones.MantenimientoInvalidoException;

//Esta clase se encargará de registrar el mantenimiento de un vehiculo, con su apertura, cierre, descripcion y costo
public class Mantenimiento {
    private Vehiculo vehiculo;
    private LocalDate fechaApertura;
    private String descripcion;
    private boolean abierto;
    private LocalDate fechaCierre;
    private String trabajoRealizado;
    private BigDecimal costo;


    public Mantenimiento(Vehiculo vehiculo, LocalDate fechaApertura, String descripcion) {
        if (vehiculo == null)
            throw new MantenimientoInvalidoException("El vehículo no puede ser nulo");
        if (fechaApertura == null)
            throw new FechaInvalidaException("La fecha de apertura no puede ser nula");
        if (descripcion == null || descripcion.isBlank())
            throw new DescripcionVaciaException("La descripción no puede estar vacía");

        this.vehiculo = vehiculo;
        this.fechaApertura = fechaApertura;
        this.descripcion = descripcion;
        this.abierto = true;
        this.costo = BigDecimal.ZERO;

        //Bloqueamos el vehículo
        vehiculo.setEstado(EstadoVehiculo.Estado.MANTENIMIENTO);
    }

    public Vehiculo getVehiculo() { return vehiculo; }
    public LocalDate getFechaApertura() { return fechaApertura; }
    public String getDescripcion() { return descripcion; }
    public boolean isAbierto() { return abierto; }
    public boolean isCerrado() { return !abierto; }

    public LocalDate getFechaCierre() {
        if (abierto) throw new MantenimientoAbiertoException("El mantenimiento aún está abierto");
        return fechaCierre;
    }

    public BigDecimal getCosto() {
        if (abierto) throw new MantenimientoAbiertoException("El mantenimiento aún no está cerrado");
        return costo;
    }

    public String getTrabajoRealizado() {
        if (abierto) throw new MantenimientoAbiertoException("El mantenimiento aún no está cerrado");
        return trabajoRealizado;
    }

    public void cerrar(LocalDate fechaCierre, String trabajoRealizado, BigDecimal costo) {
        if (!abierto) throw new MantenimientoCerradoException("El mantenimiento ya está cerrado");
        if (fechaCierre == null) throw new FechaInvalidaException("La fecha de cierre no puede ser nula");
        if (trabajoRealizado == null || trabajoRealizado.isBlank()) throw new IllegalArgumentException("Debe indicar el trabajo realizado");
        if (costo == null || costo.signum() < 0) throw new CostoMantenimientoInvalidoException("El costo del mantenimiento no puede ser negativo");

        this.fechaCierre = fechaCierre;
        this.trabajoRealizado = trabajoRealizado;
        this.costo = costo;
        this.abierto = false;

        //Liberamos el vehículo
        vehiculo.setEstado(EstadoVehiculo.Estado.DISPONIBLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mantenimiento)) return false;
        Mantenimiento that = (Mantenimiento) o;
        return Objects.equals(vehiculo, that.vehiculo) &&
               Objects.equals(fechaApertura, that.fechaApertura);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehiculo, fechaApertura);
    }
}
