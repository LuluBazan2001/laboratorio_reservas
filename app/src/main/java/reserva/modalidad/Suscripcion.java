package reserva.modalidad;

import java.math.BigDecimal;
import java.time.LocalDate;

import reserva.modalidad.excepciones.VehiculoOTarifaNulaException;
import reserva.vehiculo.Vehiculo;


public class Suscripcion implements ModalidadAlquiler {
    @Override
    public BigDecimal calcularCosto(Vehiculo vehiculo, LocalDate inicio, LocalDate fin) {
        if (vehiculo == null || vehiculo.getTarifaBase() == null)
            throw new VehiculoOTarifaNulaException("Vehículo o tarifa base nula");

        return vehiculo.getTarifaBase(); // tarifa fija mensual, por ejemplo
    }
}

