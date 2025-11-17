package reserva.modalidad;

import java.math.BigDecimal;
import java.time.LocalDate;

import reserva.vehiculo.Vehiculo;

//Esta interfaz nos va a permitir definir la regla general del calculo, es decir, cada modalidad sabrá como usar la tarifa del vehículo y las fechas
public interface ModalidadAlquiler {
    BigDecimal calcularCosto(Vehiculo vehiculo, LocalDate inicio, LocalDate fin);
}

