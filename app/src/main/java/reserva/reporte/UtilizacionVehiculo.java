package reserva.reporte;

import java.math.BigDecimal;

public class UtilizacionVehiculo {
    private final String patente;
    private final long diasRentados;
    private final long diasPeriodo;
    private final BigDecimal porcentajeUtilizacion; //entre 0 y 100

    public UtilizacionVehiculo(String patente, long diasRentados, long diasPeriodo, BigDecimal porcentajeUtilizacion) {
        this.patente = patente;
        this.diasRentados = diasRentados;
        this.diasPeriodo = diasPeriodo;
        this.porcentajeUtilizacion = porcentajeUtilizacion;
    }

    public String getPatente() { return patente; }
    public long getDiasRentados() { return diasRentados; }
    public long getDiasPeriodo() { return diasPeriodo; }
    public BigDecimal getPorcentajeUtilizacion() { return porcentajeUtilizacion; }
}
