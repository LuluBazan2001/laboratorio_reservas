package reserva.extrasYPromociones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import reserva.extrasYPromociones.excepciones.*;

public class GestorServicioExtra {
    private List<ServicioExtra> servicios = new ArrayList<>();

    public void agregarServicio(ServicioExtra servicio) {
        if (servicios.contains(servicio)) {
            throw new ServicioExistenteException("El servicio ya existe: " + servicio.getNombreServicio());
        }
        servicios.add(servicio);
    }

    public void eliminarServicio(ServicioExtra servicio) {
        if(!servicios.contains(servicio)){
            throw new ServicioNoExistenteException("El servicio no existe: " + servicio.getNombreServicio());
        }
        servicios.remove(servicio);
    }

    public void modificarServicio(ServicioExtra nuevo) {
        if(!servicios.contains(nuevo)){
            throw new ServicioNoExistenteException("El servicio no existe: " + nuevo.getNombreServicio());
        }
        eliminarServicio(nuevo);
        agregarServicio(nuevo);
    }

    public List<ServicioExtra> listarServicios() {
        return Collections.unmodifiableList(servicios);
    }

    public Optional<ServicioExtra> buscarPorNombre(String nombre) {
        return servicios.stream()
                .filter(s -> s.getNombreServicio().equalsIgnoreCase(nombre))
                .findFirst();
    }
}