package reserva.reporte;

import java.math.BigDecimal;

public class ResumenEjecutivo {
    private final long totalReservas;
    private final BigDecimal ingresosEstimados;
    private final BigDecimal promedioUtilizacion; //porcentaje (0-100)
    private final BigDecimal costoMantenimientos;

    public ResumenEjecutivo(long totalReservas, BigDecimal ingresosEstimados, BigDecimal promedioUtilizacion, BigDecimal costoMantenimientos) {
        this.totalReservas = totalReservas;
        this.ingresosEstimados = ingresosEstimados;
        this.promedioUtilizacion = promedioUtilizacion;
        this.costoMantenimientos = costoMantenimientos;
    }

    public long getTotalReservas() { return totalReservas; }
    public BigDecimal getIngresosEstimados() { return ingresosEstimados; }
    public BigDecimal getPromedioUtilizacion() { return promedioUtilizacion; }
    public BigDecimal getCostoMantenimientos() { return costoMantenimientos; }
}
