package reserva.vehiculo;

import java.util.List;

import reserva.vehiculo.excepciones.VehiculoDuplicadoException;
import reserva.vehiculo.excepciones.VehiculoNoEncontradoException;

import java.util.ArrayList;
import java.util.Collections;

public class Flota {
    //Aqui podriamos tener metodos para gestionar la flota de vehiculos
    //Listar vehiculos, agregar, eliminar, consultar disponibilidad, enlistar vehiculos en mantenimiento, etc.
    private List<Vehiculo> vehiculos = new ArrayList<>();

    public void altaVehiculo(Vehiculo vehiculo) {
        if(vehiculos.stream().anyMatch(vehic -> vehic.getPatente().equalsIgnoreCase(vehiculo.getPatente()))) {
            throw new VehiculoDuplicadoException(
                "El vehiculo con patente " + vehiculo.getPatente() + " ya existe en la flota."
            );
        }
        vehiculos.add(vehiculo);
    }

    public List<Vehiculo> getVehiculos() {
        return Collections.unmodifiableList(vehiculos);
    }

    public void cambiarEstado(String patente, EstadoVehiculo.Estado nuevoEstado) {
        Vehiculo vehiculoEncontrado = null;
            for (Vehiculo vehiculo : vehiculos) {
                if (vehiculo.getPatente().equals(patente)) {
                    vehiculoEncontrado = vehiculo;
                    break;
                }
            }
        
        if(vehiculoEncontrado == null) {
            throw new VehiculoNoEncontradoException("No se encontro el vehiculo con patente " + patente);
        }
        vehiculoEncontrado.setEstado(nuevoEstado);
    }

    public boolean comprobarDuplicado(String patente) {
        return vehiculos.stream().anyMatch(vehiculo -> vehiculo.getPatente().equalsIgnoreCase(patente));
    }

/********************************************************* */
/* BUSQUEDAS */
/********************************************************* */
    public List<Vehiculo> buscarPorPatente(String patente){
        return vehiculos.stream().filter(p -> p.getPatente().equalsIgnoreCase(patente)).toList();
    }

    public List<Vehiculo> consultarDisponibilidadPorTipo(TipoVehiculo tipo) {
        return vehiculos.stream()
                .filter(v -> v.getTipo() == tipo && v.getEstado() == EstadoVehiculo.Estado.DISPONIBLE)
                .toList();
    }

/********************************************************* */
/*LISTAR POR ESTADOS */
/********************************************************* */
    public List<Vehiculo> listarTodosDisponibles(){
        return vehiculos.stream().filter(d -> d.getEstado() == EstadoVehiculo.Estado.DISPONIBLE).toList();
    }
    public List<Vehiculo> listarTodosMantenimiento(){
        return vehiculos.stream().filter(d -> d.getEstado() == EstadoVehiculo.Estado.MANTENIMIENTO).toList();
    }
    public List<Vehiculo> listarTodosRentado(){
        return vehiculos.stream().filter(d -> d.getEstado() == EstadoVehiculo.Estado.RENTADO).toList();
    }


/********************************************************* */
/* AGREGAR, MODIFICAR Y ELIMINAR VEHICULOS */
/********************************************************* */
    public void modificarVehiculo(Vehiculo nuevoVehiculo){
        //Busca el vehiculo con la patente que brinda el parametro que recibe el metodo
        List<Vehiculo> encontrado = buscarPorPatente(nuevoVehiculo.getPatente());

        //Si el vehiculo no existe, no se puede modificar
        if(encontrado.isEmpty()){
            // exception
            throw new VehiculoNoEncontradoException("No se encontro el vehiculo con patente: " + nuevoVehiculo.getPatente());
        }

        Vehiculo vehiculoExistente = encontrado.get(0);

        // Luego verificar el estado
        if (vehiculoExistente.getEstado() == EstadoVehiculo.Estado.RENTADO) {
            throw new IllegalStateException("No se puede modificar un vehículo que está rentado.");
        }

        //Si el vehiculo existe, se modifica
        vehiculos.remove(vehiculoExistente);
        vehiculos.add(nuevoVehiculo);
        
    }

    public void eliminarVehiculo(String patente){
        List<Vehiculo> encontrado = buscarPorPatente(patente);
        
        if(encontrado.isEmpty()){
            throw new VehiculoNoEncontradoException("No se encontro el vehiculo con patente " + patente);
        }
        
        Vehiculo vehiculoAEliminar = encontrado.get(0);

        //verificamos su estado
        if (vehiculoAEliminar.getEstado() == EstadoVehiculo.Estado.RENTADO) {
            throw new IllegalStateException("No se puede eliminar un vehículo que está rentado.");
        }

        
        vehiculos.remove(vehiculoAEliminar);
    }

}
