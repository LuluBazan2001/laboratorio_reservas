package reserva.modalidad;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import reserva.modalidad.excepciones.VehiculoOTarifaNulaException;
import reserva.vehiculo.Vehiculo;

public class PorDia implements ModalidadAlquiler {
    @Override
    public BigDecimal calcularCosto(Vehiculo vehiculo, LocalDate inicio, LocalDate fin) {
        if (vehiculo == null || vehiculo.getTarifaBase() == null)
            throw new VehiculoOTarifaNulaException("Vehículo o tarifa base nula");

        long dias = ChronoUnit.DAYS.between(inicio, fin) + 1;
        return vehiculo.getTarifaBase().multiply(BigDecimal.valueOf(dias));
    }
}

