package reserva.vehiculo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import reserva.vehiculo.excepciones.GestorMantenimientosException;
import reserva.vehiculo.excepciones.MantenimientoNoEncontradoException;

//Aqui vamos a gestionar todos los mantenimientos abiertos y cerrados, reportes y bloqueos
public class GestorMantenimientos {

    private List<Mantenimiento> mantenimientos = new ArrayList<>();

    public Mantenimiento abrirMantenimiento(Vehiculo vehiculo, LocalDate fecha, String descripcion) {
        if (vehiculo.getEstado() == EstadoVehiculo.Estado.RENTADO) throw new MantenimientoNoEncontradoException("No se puede realizar mantenimiento a un vehículo rentado");

        Mantenimiento m = new Mantenimiento(vehiculo, fecha, descripcion);
        mantenimientos.add(m);
        return m;
    }

    public void cerrarMantenimiento(Mantenimiento mantenimiento, LocalDate fechaCierre, String trabajo, BigDecimal costo) {
        if (mantenimiento == null) throw new GestorMantenimientosException("El mantenimiento no puede ser nulo");
        mantenimiento.cerrar(fechaCierre, trabajo, costo);
    }

    public List<Mantenimiento> getMantenimientos() {
        return Collections.unmodifiableList(mantenimientos);
    }

    public List<Mantenimiento> getMantenimientosAbiertos() {
        //Filtramos los mantenimientos que están abiertos
        //Mantenimiento::isAbierto devuelve true si el mantenimiento está abierto
        //toList devuelve una lista con todos los elementos de la colección
        return mantenimientos.stream().filter(Mantenimiento::isAbierto).collect(Collectors.toList());
    }

    public List<Mantenimiento> getMantenimientosDeVehiculo(Vehiculo vehiculo) {
        return mantenimientos.stream().filter(m -> m.getVehiculo().equals(vehiculo)).collect(Collectors.toList());
    }

    public BigDecimal calcularCostoTotalEntre(LocalDate desde, LocalDate hasta) {
        return mantenimientos.stream().filter(m -> m.isCerrado() && !m.getFechaCierre().isBefore(desde) && !m.getFechaCierre().isAfter(hasta))
        //este map devuelve el costo del mantenimiento
        .map(Mantenimiento::getCosto)
        //reduce tiene como parámetros dos métodos de acumulación, uno que devuelve el resultado inicial y otro que devuelve el resultado de la acumulación
        //en este caso, el resultado inicial es el costo inicial (cero) y el resultado de la acumulación es el costo del mantenimiento
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    //Este metodo nos permite consultar si un vehículo con mantenimiento programado NO figura como disponible en ese periodo
    //verifica si existe algún mantenimiento cuyo periodo (apertura..cierre) solape con el periodo consultado
    //esto considera tanto mantenimientos abiertos como cerrados y comprueba el solape correctamente
    public boolean esVehiculoNoDisponible(Vehiculo vehiculo, LocalDate desde, LocalDate hasta) {
    return mantenimientos.stream()
            .filter(m -> m.getVehiculo().equals(vehiculo))
            .anyMatch(m -> {
                LocalDate inicioM = m.getFechaApertura();
                LocalDate finM = m.isCerrado() ? m.getFechaCierre() : hasta;
                // comprobamos solapamiento entre [inicioM, finM] y [desde, hasta] (inclusivo)
                boolean solapan = !(finM.isBefore(desde) || inicioM.isAfter(hasta));
                return solapan;
            });
    }

}

