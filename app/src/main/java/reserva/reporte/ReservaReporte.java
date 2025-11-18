package reserva.reporte;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservaReporte {
    private final String codigoReserva;
    private final String patenteVehiculo;
    private final String cliente;
    private final LocalDate inicio;
    private final LocalDate fin;
    private final String estado;
    private final String modalidad;
    private final BigDecimal costoTotal;

    public ReservaReporte(String codigoReserva, String patenteVehiculo, String cliente, LocalDate inicio, LocalDate fin, String estado, String modalidad, BigDecimal costoTotal) {
        this.codigoReserva = codigoReserva;
        this.patenteVehiculo = patenteVehiculo;
        this.cliente = cliente;
        this.inicio = inicio;
        this.fin = fin;
        this.estado = estado;
        this.modalidad = modalidad;
        this.costoTotal = costoTotal;
    }

    public String getCodigoReserva() { return codigoReserva; }
    public String getPatenteVehiculo() { return patenteVehiculo; }
    public String getCliente() { return cliente; }
    public LocalDate getInicio() { return inicio; }
    public LocalDate getFin() { return fin; }
    public String getEstado() { return estado; }
    public String getModalidad() { return modalidad; }
    public BigDecimal getCostoTotal() { return costoTotal; }
}
