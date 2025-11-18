package reserva.reporte;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MantenimientoReporte {
    private final String patente;
    private final LocalDate fechaApertura;
    private final LocalDate fechaCierre; //puede ser null si abierto
    private final String descripcion;
    private final boolean abierto;
    private final BigDecimal costo; //puede ser 0 si abierto o no informado

    public MantenimientoReporte(String patente, LocalDate fechaApertura, LocalDate fechaCierre, String descripcion, boolean abierto, BigDecimal costo) {
        this.patente = patente;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.descripcion = descripcion;
        this.abierto = abierto;
        this.costo = costo;
    }

    public String getPatente() { return patente; }
    public LocalDate getFechaApertura() { return fechaApertura; }
    public LocalDate getFechaCierre() { return fechaCierre; }
    public String getDescripcion() { return descripcion; }
    public boolean isAbierto() { return abierto; }
    public BigDecimal getCosto() { return costo; }
}
